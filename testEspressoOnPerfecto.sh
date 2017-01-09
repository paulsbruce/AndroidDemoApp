#!/bin/bash

WORKSPACE="$1" # the build workspace directory of the Android project
BUILD_VARIANT="$2" # the filename prefix (i.e. 'app-debug') of the app and test APK binaries
TEST_PACKAGE="$3" # the Java package name of the tests (e.g. 'com.example.perfecto.tipcalculator.test')
PERFECTO_CLOUD="$4" # the domain name of the Perfecto cloud being used to run the Espresso tests
PERFECTO_USERNAME="$5" # the username of an account with rights to execute the Espresso tests
PERFECTO_PASSWORD="$6" # the password of an account with rights to execute the Espresso tests
REPOSITORY_PATH="$7"

APP_NAME="$BUILD_VARIANT.apk"
APP_FILEPATH="$WORKSPACE/app/build/outputs/apk/$APP_NAME"
TEST_NAME="$BUILD_VARIANT-androidTest.apk"
TEST_FILEPATH="$WORKSPACE/app/build/outputs/apk/$TEST_NAME"
API_BASE_URL="https://$PERFECTO_CLOUD/services/"

EXIT_CODE=5500 # unhandled

function closeUpShop {
  ## close the device
  echo "Closing device..."
  curl -s -N "$API_BASE_URL/executions/$EXECUTION_ID?operation=command&user=$PERFECTO_USERNAME&password=$PERFECTO_PASSWORD&command=device&subcommand=close&param.deviceId=$HANDSET_ID" > /tmp/pResp
  CLOSE_STATUS=$(python -c 'import sys, json; print json.load(open("/tmp/pResp"))["flowEndCode"]')

  ## end execution
  echo "Finalizing cloud execution session..."
  curl -s -N "$API_BASE_URL/executions/$EXECUTION_ID?operation=end&user=$PERFECTO_USERNAME&password=$PERFECTO_PASSWORD" > /tmp/pResp

  rm /tmp/pResp
}
function err_handler() {
  if [[ $1 -ge 4 ]]
  then
    closeUpShop
  fi
  exit $1
}


if [[ $PERFECTO_PASSWORD == "." ]]
then
  read -s -p "Enter your Perfecto password: " PERFECTO_PASSWORD
fi
clear

# verify that the app APK exists
if [ ! -f $APP_FILEPATH ]
then
  echo "Could not find app binary: $APP_FILEPATH" >&2
  EXIT_CODE=1
fi
# verify that the Espresso test APK exists
if [ ! -f $TEST_FILEPATH ]
then
  echo "Could not find test binary: $TEST_FILEPATH" >&2
  EXIT_CODE=2
fi

echo "Beginning main workflow..."

# select device
curl -s -N "$API_BASE_URL/handsets?operation=list&user=$PERFECTO_USERNAME&password=$PERFECTO_PASSWORD&status=Connected&inUse=false&os=Android&responseFormat=xml" > /tmp/pResp
while [[ "$(fuser /tmp/pResp)" ]]; do sleep 1; done
HANDSET_ID=$(python -c 'import sys; import xml.etree.ElementTree as ET; print ET.parse("/tmp/pResp").getroot().findall("handset")[0].find("deviceId").text')
if [[ ${#HANDSET_ID} == 0 ]]
then
  echo "Failed to find a suitable device."
  cat /tmp/pResp
  err_handler 3
fi
echo "Found device $HANDSET_ID"

## obtain a new execution
curl -s -N "$API_BASE_URL/executions?operation=start&user=$PERFECTO_USERNAME&password=$PERFECTO_PASSWORD&responseFormat=json" > /tmp/pResp
while [[ "$(fuser /tmp/pResp)" ]]; do sleep 1; done
RESP=$(cat /tmp/pResp)
if [[ $RESP != *"executionId"* ]]
then
  echo "Failed to obtain a new execution."
  echo "$RESP"
  err_handler 4
fi
EXECUTION_ID=$(python -c 'import sys, json; print json.load(open("/tmp/pResp"))["executionId"]')
REPORT_KEY=$(python -c 'import sys, json; print json.load(open("/tmp/pResp"))["reportKey"]')
REPORT_URL=$(python -c 'import sys, json; print json.load(open("/tmp/pResp"))["singleTestReportUrl"]')
echo "Report Key: "$REPORT_KEY
echo "{ 'reportUrl' : '$REPORT_URL' }"


## open the device
echo "Allocating device $HANDSET_ID..."
curl -s -N "$API_BASE_URL/executions/$EXECUTION_ID?operation=command&user=$PERFECTO_USERNAME&password=$PERFECTO_PASSWORD&command=device&subcommand=open&param.deviceId=$HANDSET_ID" > /tmp/pResp
OPEN_STATUS=$(python -c 'import sys, json; print json.load(open("/tmp/pResp"))["flowEndCode"]')
echo $OPEN_STATUS

# upload to Perfecto cloud through API
echo "Uploading artifacts to cloud repository..."
RESP=$(curl -s -N -X PUT "$API_BASE_URL/repositories/media/$APP_NAME?operation=upload&user=$PERFECTO_USERNAME&password=$PERFECTO_PASSWORD&overwrite=true&responseFormat=xml" --data-binary @$APP_FILEPATH)
if [[ $RESP != *"Success"* ]]
then
  echo "Failed to upload $APP_FILEPATH to Perfecto repository."
  echo "$RESP"
  err_handler 5
fi
echo "Uploaded $APP_NAME to Perfecto repository."

RESP=$(curl -s -N -X PUT "$API_BASE_URL/repositories/media/$TEST_NAME?operation=upload&user=$PERFECTO_USERNAME&password=$PERFECTO_PASSWORD&overwrite=true&responseFormat=xml" --data-binary @$TEST_FILEPATH)
if [[ $RESP != *"Success"* ]]
then
  echo "Failed to upload $TEST_FILEPATH to Perfecto repository."
  echo "$RESP"
  err_handler 6
fi
echo "Uploaded $TEST_NAME to Perfecto repository."

# run Espresso test

## execute commands
echo "Executing Espresso tests..."
curl -s -N "$API_BASE_URL/executions/$EXECUTION_ID?operation=command&user=$PERFECTO_USERNAME&password=$PERFECTO_PASSWORD&command=espresso&subcommand=execute&param.handsetId=$HANDSET_ID&param.testPackage=$TEST_PACKAGE&param.debugApp=$APP_NAME&param.testApp=$TEST_NAME&param.failOnError=True&param.reportFormat=Raw&responseFormat=json" > /tmp/pResp
while [[ "$(fuser /tmp/pResp)" ]]; do sleep 1; done
RESULT_CODE=$(python -c 'import sys, json; print json.load(open("/tmp/pResp"))["flowEndCode"]')
RESULT_DESCRIPTION=$(python -c 'import sys, json; print json.load(open("/tmp/pResp"))["description"]')
if [[ $RESULT_CODE == "SUCCEEDED" ]]
then
  echo "Espresso tests passed perfect[o]ly!"
  EXIT_CODE=0
else
  echo "Failed to execute Espresso tests."
  echo "$RESULT_DESCRIPTION"
  EXIT_CODE=6
fi

sleep 5

echo "Obtaining execution details for stdout..."
curl -s -N "$API_BASE_URL/services/reports/$REPORT_KEY?operation=download&user=$PERFECTO_USERNAME&password=$PERFECTO_PASSWORD" > /tmp/pResp
while [[ "$(fuser /tmp/pResp)" ]]; do sleep 1; done
FIRST_ACTUAL_DATA=$(python -c 'import sys; import xml.etree.ElementTree as ET; print ET.parse("/tmp/pResp").find(".//dataItem[@label=\"actual\"]")[0].text')
if [[ ${#FIRST_ACTUAL_DATA} != 0 ]]
then
  echo $FIRST_ACTUAL_DATA
fi
cat /tmp/pResp

closeUpShop

exit $EXIT_CODE

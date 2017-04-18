#!/bin/bash
GIT_NAME=$(git --no-pager show -s --format='%an' $GIT_COMMIT)
GIT_EMAIL=$(git --no-pager show -s --format='%ae' $GIT_COMMIT)
echo "CONTRIBUTOR: $GIT_NAME $GIT_EMAIL ($GIT_COMMIT)"
cd ${WORKSPACE}
curl -s https://raw.githubusercontent.com/paulsbruce/AndroidDemoApp/master/Jenkins/runEspressoOnTests.sh > ./Perfecto_API.sh
chmod u+x ./Perfecto_API.sh
bash Perfecto_API.sh "${WORKSPACE}" "app-debug" "com.example.perfecto.tipcalculator.test" "${PERFECTO_CLOUD}" "${PERFECTO_USERNAME}" "${PERFECTO_PASSWORD}" "/" "2" "$GIT_EMAIL" "${PERFECTO_REPORTING_KEY}"
exit $?
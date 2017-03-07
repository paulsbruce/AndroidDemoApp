FROM jenkinsci/jenkins
USER root

## create writable jenkins directories for the initial install
RUN mkdir /var/log/jenkins
RUN mkdir /var/cache/jenkins
RUN chown -R jenkins:jenkins /var/log/jenkins
RUN chown -R jenkins:jenkins /var/cache/jenkins

## install basic distro tools used by post-build automation scripts
RUN apt-get update
RUN apt-get install -y apt-transport-https
RUN apt-get -q -y install lsof

## get latest versions at https://developer.android.com/studio/index.html
RUN wget https://dl.google.com/android/repository/tools_r25.2.3-linux.zip -O /opt/android-sdk.zip
RUN mkdir /opt/android-sdk-linux
RUN chmod -R 755 /opt/android-sdk-linux
RUN unzip /opt/android-sdk.zip -d /opt/android-sdk-linux
RUN rm /opt/android-sdk.zip

#RUN wget https://dl.google.com/android/repository/android-ndk-r14-linux-x86_64.zip -O /opt/android-ndk.zip
#RUN mkdir /opt/android-sdk-linux/android-ndk
#RUN chmod -R 755 /opt/android-sdk-linux/android-ndk
#RUN unzip /opt/android-ndk.zip -d /opt/android-sdk-linux/android-ndk
#RUN rm /opt/android-ndk.zip

## configure profile and environment variables
RUN >/etc/profile.d/android.sh
RUN sed -i '$ a\export ANDROID_HOME="/opt/android-sdk-linux"' /etc/profile.d/android.sh
RUN sed -i '$ a\export PATH="$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$PATH"' /etc/profile.d/android.sh
RUN . /etc/profile

## install system-wide git tools
RUN apt-get install git-core

## install Android SDK, auto-accepting terms of service
RUN ( sleep 5 && while [ 1 ]; do sleep 1; echo y; done ) | /opt/android-sdk-linux/tools/android update sdk --no-ui --all --filter platform-tools,android-25,build-tools-25.0.2,android-24,build-tools-24.0.1,android-23,build-tools-23.0.3,android-21,build-tools-21.1.2,tools,extra-android-support,extra-android-m2repository

RUN dpkg --add-architecture i386
USER jenkins
ENV JAVA_OPTS="-Xmx8192m"

# RUN npm install -g appium
# RUN npm install wd
# RUN appium

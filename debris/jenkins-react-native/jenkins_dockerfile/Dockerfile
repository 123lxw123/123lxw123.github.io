FROM jenkins/jenkins:2.73.3
MAINTAINER Ji ke <jike@kezaihui.com>

USER root

# JAVA
RUN apt update
RUN apt-get install openjdk-8-jre -y
RUN ls /usr/lib/jvm/
ENV JAVA_HOME /usr/lib/jvm/java-1.8.0-openjdk-amd64

# automtically accept sdk licences
RUN dpkg --add-architecture i386 && apt-get update && apt-get install -y expect git wget libc6-i386 lib32stdc++6 lib32gcc1 lib32ncurses5 lib32z1 unzip && apt-get clean

COPY scripts /opt/scripts

# Install Android tools
ENV SDK_VERSION "r25.2.5"
RUN mkdir /opt/android-sdk-linux && cd /opt/android-sdk-linux && wget --output-document=tools-sdk.zip --quiet https://dl.google.com/android/repository/tools_${SDK_VERSION}-linux.zip && unzip tools-sdk.zip && rm -f tools-sdk.zip && chmod +x /opt/scripts/android-accept-licenses.sh && chown -R jenkins.jenkins /opt

# Setup environment
ENV ANDROID_HOME /opt/android-sdk-linux
ENV PATH ${PATH}:${ANDROID_HOME}/tools/bin:${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools

ENV BUILD_TOOLS_VERSION 26.0.1
ENV ANDROID_VERSION 26
RUN /opt/scripts/android-accept-licenses.sh "sdkmanager platform-tools"
RUN /opt/scripts/android-accept-licenses.sh "sdkmanager build-tools;${BUILD_TOOLS_VERSION}"
RUN /opt/scripts/android-accept-licenses.sh "sdkmanager platforms;android-${ANDROID_VERSION}"
RUN /opt/scripts/android-accept-licenses.sh "sdkmanager add-ons;addon-google_apis-google-24"
RUN /opt/scripts/android-accept-licenses.sh "sdkmanager extras;android;m2repository"
RUN /opt/scripts/android-accept-licenses.sh "sdkmanager extras;m2repository;com;android;support;constraint;constraint-layout;1.0.2"
RUN /opt/scripts/android-accept-licenses.sh "sdkmanager extras;m2repository;com;android;support;constraint;constraint-layout-solver;1.0.2"
RUN /opt/scripts/android-accept-licenses.sh "sdkmanager system-images;android-${ANDROID_VERSION};google_apis;armeabi-v7a"
RUN /opt/scripts/android-accept-licenses.sh "sdkmanager ndk-bundle"
# RUN /opt/scripts/android-accept-licenses.sh "sdkmanager platform-tools \"build-tools;${BUILD_TOOLS_VERSION}\" \"platforms;android-${ANDROID_VERSION}\" \"add-ons;addon-google_apis-google-24\" \"extras;android;m2repository\" \"extras;m2repository;com;android;support;constraint;constraint-layout;1.0.2\" \"extras;m2repository;com;android;support;constraint;constraint-layout-solver;1.0.2\" \"system-images;android-${ANDROID_VERSION};google_apis;armeabi-v7a\" ndk-bundle"
RUN /opt/scripts/android-accept-licenses.sh "sdkmanager system-images;android-${ANDROID_VERSION};google_apis;armeabi-v7a"

ENV BUILD_TOOLS_VERSION 25.0.1
ENV ANDROID_VERSION 25
RUN /opt/scripts/android-accept-licenses.sh "sdkmanager build-tools;${BUILD_TOOLS_VERSION}"
RUN /opt/scripts/android-accept-licenses.sh "sdkmanager platforms;android-${ANDROID_VERSION}"
# RUN /opt/scripts/android-accept-licenses.sh "sdkmanager platform-tools \"build-tools;${BUILD_TOOLS_VERSION}\" \"platforms;android-${ANDROID_VERSION}\" \"add-ons;addon-google_apis-google-24\" \"extras;android;m2repository\" \"extras;m2repository;com;android;support;constraint;constraint-layout;1.0.2\" \"extras;m2repository;com;android;support;constraint;constraint-layout-solver;1.0.2\" \"system-images;android-${ANDROID_VERSION};google_apis;armeabi-v7a\" ndk-bundle"
RUN /opt/scripts/android-accept-licenses.sh "sdkmanager system-images;android-${ANDROID_VERSION};google_apis;armeabi-v7a"

ENV BUILD_TOOLS_VERSION 25.0.3
RUN /opt/scripts/android-accept-licenses.sh "sdkmanager build-tools;${BUILD_TOOLS_VERSION}"
# RUN /opt/scripts/android-accept-licenses.sh "sdkmanager platform-tools \"build-tools;${BUILD_TOOLS_VERSION}\" \"platforms;android-${ANDROID_VERSION}\" \"add-ons;addon-google_apis-google-24\" \"extras;android;m2repository\" \"extras;m2repository;com;android;support;constraint;constraint-layout;1.0.2\" \"extras;m2repository;com;android;support;constraint;constraint-layout-solver;1.0.2\" \"system-images;android-${ANDROID_VERSION};google_apis;armeabi-v7a\" ndk-bundle"


RUN which adb
RUN which android

RUN echo ANDROID_HOME="$ANDROID_HOME" >> /etc/environment

# 使用淘宝镜像安装Node.js v8.9.2
RUN wget https://npm.taobao.org/mirrors/node/v8.9.2/node-v8.9.2-linux-x64.tar.gz && \
    tar -C /usr/local --strip-components 1 -xzf node-v8.9.2-linux-x64.tar.gz && \
    rm node-v8.9.2-linux-x64.tar.gz
# 使用淘宝的npm镜像
RUN npm install --production -d --registry=https://registry.npm.taobao.org
RUN npm install -g yarn react-native-cli
RUN npm install -g npm-cli-login

# drop back to the regular jenkins user - good practice
USER jenkins

FROM openjdk:12-alpine

MAINTAINER kaybee

ENV SPRING_OUTPUT_ANSI_ENABLE=ALWAYS \
    JAVA_OPT="" \
    PORT=8401 \
    CONFIG_PORT=8888 \
    AUTH_PORT=8301 \
    PROFILES="default"

ADD /build/libs/*.jar /mmall-account.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPT -jar /mmall-account.jar --spring.profiles.active=$PROFILES"]

EXPOSE $PORT

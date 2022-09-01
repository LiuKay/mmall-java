FROM openjdk:12-alpine

MAINTAINER kaybee

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="" \
    PORT=8301 \
    CONFIG_PORT=8888 \
    PROFILES="default"

ADD /build/libs/*.jar /mmall-security.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPT -jar /mmall-security.jar --spring.profiles.active=$PROFILES"]

EXPOSE $PORT
FROM openjdk:12-alpine

MAINTAINER kaybee

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="" \
    PORT=8080 \
    CONFIG_PORT=8888 \
    PROFILES="default"

ADD /build/libs/*.jar /mmall-gateway.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /mmall-gateway.jar --spring.profiles.active=$PROFILES"]

EXPOSE $PORT
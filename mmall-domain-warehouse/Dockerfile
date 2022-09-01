FROM openjdk:12-alpine

MAINTAINER kaybee

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="" \
    PORT=8501 \
    CONFIG_PORT=8888 \
    AUTH_PORT=8301 \
    PROFILES="default"

ADD /build/libs/*.jar /mmall-warehouse.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPT -jar /mmall-warehouse.jar --spring.profiles.active=$PROFILES"]

EXPOSE $PORT
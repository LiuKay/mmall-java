FROM openjdk:12-alpine

MAINTAINER kaybee

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="" \
    PORT=8888 \
    PROFILES="native"

ADD /build/libs/*.jar /mmall-config.jar

RUN apk update && apk add curl && rm -rf /var/cache/apk/*

HEALTHCHECK --interval=5s --timeout=30s CMD curl -f http://localhost:$PORT/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPT -jar /mmall-config.jar --spring.profiles.active=$PROFILES"]

EXPOSE $PORT
# /bin/sh

./gradlew clean
./gradlew assemble

#./gradlew :mmall-domain-account:bootJar
#./gradlew :mmall-domain-payment:bootJar
#./gradlew :mmall-domain-warehouse:bootJar
#./gradlew :mmall-domain-security:bootJar
#./gradlew :mmall-platform-configuration:bootJar
#./gradlew :mmall-platform-gateway:bootJar
#./gradlew :mmall-platform-registry:bootJar

docker-compose -f ./docker-compose.dev.yml up -d




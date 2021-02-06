FROM openjdk:jdk-alpine
# CMD ["java -version"]
# CMD ["./gradlew build"]
# RUN ["ls -a"]
# RUN ["./gradlew","build"]
COPY kosign-push-api/build/libs/kosign-push-api-0.0.1.jar app.jar 
ENTRYPOINT [ "java","-jar","app.jar" ]
 

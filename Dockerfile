FROM eclipse-temurin:21-jdk-jammy
RUN useradd cpuser
COPY target/cp_app.jar /cp_app.jar
RUN chmod a+x /cp_app.jar
USER cpuser
ENTRYPOINT [ "java", "-jar", "/cp_app.jar" ]
EXPOSE 9002
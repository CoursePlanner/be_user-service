FROM eclipse-temurin:21-jdk-jammy
RUN useradd -m cpuser
RUN chown -R cpuser:cpuser /home/cpuser
COPY target/cp_app.jar /home/cpuser/cp_app.jar
USER cpuser
ENV MONGODB_HOST=localhost
ENV MONGODB_PORT=27017
ENV MONGODB_USERNAME=cp_mongo_user
ENV MONGODB_PASSWORD=hoyiAMud5UaZr2y
ENV MONGODB_DB_NAME=cp_db
ENV MONGODB_AUTH_DB_NAME=admin
ENTRYPOINT [ "java", "-jar", "/home/cpuser/cp_app.jar" ]
EXPOSE 9002
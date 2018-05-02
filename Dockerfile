FROM java:8
COPY target/nuvolamagica-0.0.1-SNAPSHOT.jar /usr/share/nuvola-magica/nuvola-magica.jar
EXPOSE 8080
HEALTHCHECK CMD curl http://127.0.0.1:8080/api/ping
CMD ["java", "-jar", "/usr/share/nuvola-magica/nuvola-magica.jar"]

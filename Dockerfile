FROM java:8
COPY target/nuvolamagica-0.0.1-SNAPSHOT.jar /usr/share/nuvola-magica/
EXPOSE 8080
CMD ["java", "-jar", "/usr/share/nuvola-magica/nuvolamagica.jar"]
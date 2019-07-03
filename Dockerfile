FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY dependency/BOOT-INF/lib /app/lib
COPY dependency/META-INF  /app/META-INF
COPY dependency/BOOT-INF/classes /app
EXPOSE 80
CMD ["java", "-cp", "app:app/lib/*", "it.uniroma2.dicii.sdcc.teaching_material_management.TeachingMaterialManagementApplication"]

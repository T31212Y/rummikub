FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y \
    libxext6 libxrender1 libxtst6 libxi6 libgl1-mesa-glx libfreetype6 fontconfig fonts-dejavu-core \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /rummikub

COPY target/scala-3.6.4/Rummikub-assembly-0.1.0-SNAPSHOT.jar rummikub.jar

CMD ["java", "-jar", "rummikub.jar"]
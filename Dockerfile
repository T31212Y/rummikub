FROM debian:bookworm-slim

RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    curl \
    gnupg2 \
    libxext6 libxrender1 libxtst6 libxi6 libgl1-mesa-glx \
    && rm -rf /var/lib/apt/lists/*

RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" > /etc/apt/sources.list.d/sbt.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x99e82a75642ac823" | apt-key add - && \
    apt-get update && apt-get install -y sbt && rm -rf /var/lib/apt/lists/*

WORKDIR /rummikub

ADD . /rummikub

CMD ["sbt", "run"]
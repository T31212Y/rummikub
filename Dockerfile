FROM hseeberger/scala-sbt:graalvm-ce-21.3.0-java17_1.6.2_3.1.1
WORKDIR /rummikub
ADD . /rummikub
CMD ["sbt", "run"]
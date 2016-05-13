FROM    java:openjdk-8-jre

RUN apt-get update --quiet --quiet \
    && apt-get install --quiet --quiet --no-install-recommends lsof \
    && rm -rf /var/lib/apt/lists/*

EXPOSE 8080 7474

ADD ./core/target/maven*jar /test/release-test.jar
WORKDIR /test

RUN mkdir /data
RUN ln -s /data /test/data
VOLUME /data

CMD [ "java", "-jar" , "release-test.jar" ]

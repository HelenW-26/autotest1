# Use the selenium standalone chrome image you were running
#FROM selenium/standalone-chrome:114.0-20251101
FROM selenium/node-chrome:114.0-20251101
# Switch to root to install packages
USER root

# Install OpenJDK 17 and Maven, remove Java 21
RUN apt-get update && \
    apt-get remove -y --purge openjdk-21-* || true && \
    apt-get install -y openjdk-17-jdk maven && \
    rm -rf /var/lib/apt/lists/*

USER seluser

WORKDIR /projects/autotest
COPY pom.xml ./pom.xml

# Download all dependencies defined in pom.xml
RUN mvn -B dependency:go-offline

#CMD ["bash"]
ENTRYPOINT ["/opt/bin/entry_point.sh"]

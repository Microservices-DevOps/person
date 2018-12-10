# Set the base image to java8
FROM openjdk:8-alpine

# File Author
MAINTAINER Amritendu De

# Define default environment variables
ENV PERSON_HOME=/opt
ENV PERSON_BINARIES=/opt/bin

# Create directory
RUN mkdir -p $PERSON_BINARIES

# Set default directory
WORKDIR $PERSON_HOME

# Copy notepad jar file
COPY build/libs/*.jar $PERSON_HOME/person.jar

# Add initialization script
ADD entrypoint.sh $PERSON_BINARIES/entrypoint.sh

# Give permissions
RUN chmod 755 $PERSON_BINARIES/entrypoint.sh

# Give permissions
RUN chmod 755 $PERSON_HOME/person.jar

# Expose default servlet container port
EXPOSE 9090

VOLUME bin

# Main command
ENTRYPOINT ["/bin/sh", "/opt/bin/entrypoint.sh"]

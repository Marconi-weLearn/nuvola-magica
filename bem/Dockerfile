FROM rastasheep/ubuntu-sshd:16.04
COPY script/* /usr/bin/
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get clean && \
    mkdir /var/nuvola-magica
EXPOSE 22
 

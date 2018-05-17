FROM rastasheep/ubuntu-sshd:16.04
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get clean && \
    mkdir /var/nuvola-magica
RUN echo "#!/bin/bash" > /usr/bin/mkfile && \ 
    echo 'mkdir -p "$(dirname "$1")"' >> /usr/bin/mkfile && \
    echo 'touch "$1"' >> /usr/bin/mkfile && \
    chmod +x /usr/bin/mkfile
EXPOSE 22
 

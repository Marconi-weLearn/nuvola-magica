# Nuvola-magica
Nuvola-magica is a software that build and execute projects provided.
It provides some API REST which can be used to perform some operations like uploading a file to a workspace, building the files, executing a project and pulling the stdout/stdin.
**WARNING**: It's not recommended to put it in production because it is not ready yet. There are many security and functionality issues.
## Requirements
It requires ```docker``` with a virtual network for the BEMs.
## Configuration of the project
There is a configuration file called ```application.properties``` in ```src/main/resources/```. It should be configured accordingly your needs. The passwords should be changed.
## Setup (on docker)
```bash
git clone https://github.com/marconi-welearn/nuvola-magica.git
cd nuvola-magica
docker build -t nuvola-magica:latest .
cd bem
docker build -t nuvola-magica-bem:latest .
docker network create nuvola-magica-bem-network
docker run -dp 8081:8080 --name nuvola-magica nuvola-magica:latest
docker network connect nuvola-magica-bem-network nuvola-magica
```

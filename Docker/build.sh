docker run -p 8080:8080 -p 50000:50000 --name=jenkins-master --volumes-from=jenkins-data -d jenkins2

## or if local directory mounted for jenkins config
# docker run -p 8080:8080 -p 50000:50000 --name=jenkins-master -v ~/docker/jenkins2/var/jenkins_home:/var/jenkins_home -d jenkins2

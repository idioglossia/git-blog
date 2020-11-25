From openjdk:8-alpine
RUN mkdir -p /opt/gitblog && mkdir /opt/repository && mkdir /opt/ssh 
RUN apk update && apk add wget unzip
RUN cd /opt && wget -nc https://github.com/idioglossia/git-blog/releases/download/v0.2.2/release.zip && unzip release.zip && rm release.zip
WORKDIR /opt/gitblog/
CMD ["sh","-c", "cp /opt/gitblog.jar /opt/gitblog && java -jar /opt/gitblog/gitblog.jar"]
EXPOSE 8083
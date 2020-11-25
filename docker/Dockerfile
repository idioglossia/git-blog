From openjdk:8-alpine
RUN mkdir -p /opt/gitblog && mkdir /opt/repository && mkdir /opt/ssh 
RUN apk update && apk add wget zip unzip
RUN cd /opt && wget -nc https://github.com/idioglossia/git-blog/releases/download/v0.1-beta.2/gitblog.zip && unzip gitblog.zip && rm gitblog.zip
RUN chmod +x /opt/gitblog.jar
WORKDIR /opt/gitblog/
CMD ["sh","-c", "cp /opt/gitblog.jar /opt/gitblog && java -jar /opt/gitblog/gitblog.jar"]
Expose 8083
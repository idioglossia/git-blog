FROM openjdk:8-alpine
RUN mkdir -p /opt/gitblog && mkdir /opt/repository && mkdir /opt/ssh 
RUN apk update && apk add wget unzip
RUN cd /opt && wget -nc https://github.com/idioglossia/git-blog/releases/download/v0.4.0/release.zip && unzip release.zip && rm release.zip
WORKDIR /opt/gitblog/

# Set user and group
ENV USER=gitblog
ENV GROUP=gitblog
ENV UID=1000
ENV GID=1000
RUN addgroup -g "$GID" "$GROUP"
RUN adduser \
    --disabled-password \
    --gecos "" \
    --ingroup "$USER" \
    --ingroup "$GROUP" \
    --uid "$UID" \
    "$USER"
RUN chown -R $USER:$GROUP /opt/gitblog
RUN chown $USER:$GROUP /opt/gitblog.jar
USER $USER:$GROUP

CMD ["sh","-c", "cp /opt/gitblog.jar /opt/gitblog && java -jar /opt/gitblog/gitblog.jar"]
EXPOSE 8083
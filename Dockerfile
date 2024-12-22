FROM ubuntu:latest
LABEL authors="naono"

ENTRYPOINT ["top", "-b"]
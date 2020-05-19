FROM phusion/baseimage:0.11

RUN apt-get updete && apt-get install 
COPY src/setup-02.sh /tmp/
RUN bash /tmp/setup-02.sh
COPY src/setup-03.sh src/setup-saxon.sh /tmp/
RUN bash /tmp/setup-03.sh
COPY src/* /usr/local/setup/
RUN bash /usr/local/setup/setup-04.sh

CMD ["/sbin/my_init"]

JAVA_HOME=`/usr/libexec/java_home`
SYSTEM= $(shell uname | awk '{print tolower($$0)}')
JAVAH=javah
CC=gcc
CFLAGS=-fPIC
CFLAGS+=-I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/$(SYSTEM)
LDFLAGS=-shared


all: libserialport.so

libserialport.so: net_sensnet_node_natives_SerialPort.c
	$(JAVAH) -classpath ../../bin/ -jni net.sensnet.node.natives.SerialPort && \
	$(CC) $(CFLAGS) $(LDFLAGS) -o libserialport.so net_sensnet_node_natives_SerialPort.c

clean:
	rm -f *.so
	rm -f *.h

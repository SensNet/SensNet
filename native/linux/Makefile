JAVA_HOME=/usr/lib/jvm/default-java
SYSTEM= $(shell uname | awk '{print tolower($$0)}')
JAVAH=javah
CFLAGS=-fPIC
CFLAGS+=-I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/$(SYSTEM)
LDFLAGS=-shared


all: libserialport-$(SUFFIX).so libsystemtime-$(SUFFIX).so

libserialport-$(SUFFIX).so: net_sensnet_node_natives_SerialPort.c
	$(JAVAH) -classpath ../../bin/ -jni net.sensnet.node.natives.SerialPort && \
	$(CC) $(CFLAGS) $(LDFLAGS) -o libserialport-$(SUFFIX).so net_sensnet_node_natives_SerialPort.c

libsystemtime-$(SUFFIX).so: net_sensnet_node_natives_SystemTime.c
	$(JAVAH) -classpath ../../bin/ -jni net.sensnet.node.natives.SystemTime && \
	$(CC) $(CFLAGS) $(LDFLAGS) -o libsystemtime-$(SUFFIX).so net_sensnet_node_natives_SystemTime.c

clean:
	rm -f *.so
	rm -f *.h

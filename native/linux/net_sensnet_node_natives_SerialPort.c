#include <jni.h>
#include <fcntl.h>
#include <termios.h>
#include <errno.h>
#include <stdio.h>
#include "net_sensnet_node_natives_SerialPort.h"
#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT int JNICALL Java_net_sensnet_node_natives_SerialPort_setBaudRate0 (JNIEnv *env, jclass clazz, jstring port, jint baud) {
    printf("CALL");
    int fd;
    const char *p = (*env)->GetStringUTFChars(env, port, (jboolean*)0);
    //const char *p = "/dev/ttyUSB1";
    printf("Setting baudrate of %s...\n", p);
    speed_t bd = B9600;
    
    if(baud <= 0)
        bd = B0;
    else if(baud <= 50)
        bd  = B50;
    else if(baud <= 75)
        bd = B75;
    else if(baud <= 110)
        bd = B110;
    else if(baud <= 134)
        bd = B134;
    else if(baud <= 150)
        bd = B150;
    else if(baud <= 200)
        bd = B200;
    else if(baud <= 300)
        bd = B300;
    else if(baud <= 600)
        bd = B600;
    else if(baud <= 1200)
        bd = B1200;
    else if(baud <= 1800)
        bd = B1800;
    else if(baud <= 2400)
        bd = B2400;
    else if(baud <= 4800)
        bd = B4800;
    else if(baud <= 9600)
        bd = B9600;
    else if(baud <= 19200)
        bd = B19200;
    else if(baud <= 38400)
        bd = B38400;
    else if(baud <= 57600)
        bd = B57600;
    else if(baud <= 115200)
        bd = B115200;
    else if(baud <= 230400)
        bd = B230400;
    else if(baud <= 460800)
        bd = B460800;
    else if(baud <= 500000)
        bd = B500000;
    else if(baud <= 576000)
        bd = B576000;
    else if(baud <= 921600)
        bd = B921600;
    else if(baud <= 1000000) 
        bd = B1000000;
    else if(baud <= 1152000)
        bd = B1152000;
    else if(baud <= 1500000)
        bd = B1500000;
    else if(baud <= 2000000)
        bd = B2000000;
    else if(baud <= 2500000)
        bd = B2500000;
    else if(baud <= 3000000)
        bd = B3000000;
    else if(baud <= 3500000)
        bd = B3500000;
    else if(baud <= 4000000 || baud > 4000000)
        bd = B4000000;
    
    fd = open(p, O_RDWR);
    int *resfd = &fd;
    struct termios settings;
    tcgetattr(fd, &settings);
    tcsetattr(fd, TCSANOW, &settings);
    tcflush(fd, TCOFLUSH);
    close(fd);
    printf("Set baudrate of %s to %d", p, baud);
    (*env)->ReleaseStringUTFChars(env, port, p);
    return fd;
}

#ifdef __cplusplus
}
#endif

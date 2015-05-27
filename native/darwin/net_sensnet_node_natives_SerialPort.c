#include <jni.h>
#include <fcntl.h>
#include <termios.h>
#include "net_sensnet_node_natives_SerialPort.h"
#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT void JNICALL Java_net_sensnet_node_natives_SerialPort_setBaudRate0 (JNIEnv *env, jclass clazz, jstring port, jint baud) {
    int fd;
    const char *p = (*env)->GetStringUTFChars(env, port, 0);
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
    else if(baud <= 230400 || baud > 230400)
        bd = B230400;
    
    fd = open(p, O_RDWR);
    struct termios settings;
    tcgetattr(fd, &settings);
    cfsetospeed(&settings, bd);
    tcsetattr(fd, TCSANOW, &settings);
    tcflush(fd, TCOFLUSH);
    close(fd);
    (*env)->ReleaseStringUTFChars(env, port, p);
}

#ifdef __cplusplus
}
#endif

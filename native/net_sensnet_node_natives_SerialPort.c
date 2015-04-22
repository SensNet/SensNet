#include <jni.h>
#include <fcntl.h>
#include <termios.h>

#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT void JNICALL Java_net_sensnet_node_natives_SerialPort_setBoudRate0 (JNIEnv *env, jstring port, jint boud) {
    int fd;
    const char *p = (*env)->GetStringUTFChars(env, port, 0);
    speed_t bd = B9600;
    
    if(boud <= 0)
        bd = B0;
    else if(boud <= 50)
        bd  = B50;
    else if(boud <= 75)
        bd = B75;
    else if(boud <= 110)
        bd = B110;
    else if(boud <= 134)
        bd = B134;
    else if(boud <= 150)
        bd = B150;
    else if(boud <= 200)
        bd = B200;
    else if(boud <= 300)
        bd = B300;
    else if(boud <= 600)
        bd = B600;
    else if(boud <= 1200)
        bd = B1200;
    else if(boud <= 1800)
        bd = B1800;
    else if(boud <= 2400)
        bd = B2400;
    else if(boud <= 4800)
        bd = B4800;
    else if(boud <= 9600)
        bd = B9600;
    else if(boud <= 19200)
        bd = B19200;
    else if(boud <= 38400)
        bd = B38400;
    else if(boud <= 57600)
        bd = B57600;
    else if(boud <= 115200)
        bd = B115200;
    else if(boud <= 230400)
        bd = B230400;
    else if(boud <= 460800)
        bd = B460800;
    else if(boud <= 500000)
        bd = B500000;
    else if(boud <= 576000)
        bd = B576000;
    else if(boud <= 921600)
        bd = B921600;
    else if(boud <= 1000000) 
        bd = B1000000;
    else if(boud <= 1152000)
        bd = B1152000;
    else if(boud <= 1500000)
        bd = B1500000;
    else if(boud <= 2000000)
        bd = B2000000;
    else if(boud <= 2500000)
        bd = B2500000;
    else if(boud <= 3000000)
        bd = B3000000;
    else if(boud <= 3500000)
        bd = B3500000;
    else if(boud <= 4000000 || boud > 4000000)
        bd = B4000000;
    
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

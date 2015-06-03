#include <jni.h>
#include <fcntl.h>
#include <time.h>
#include "net_sensnet_node_natives_SystemTime.h"
#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT void JNICALL Java_net_sensnet_node_natives_SystemTime_setSystemTime0 (JNIEnv *env, jclass clazz, jint time) {
    time_t t;
    t = time;
	printf("System time set to %d. Return value: %d\n", time, stime(&t)); 
}

#ifdef __cplusplus
}
#endif

#include <string>
#include <sstream>
#include <stdio.h>
#include <yarp/os/Bottle.h>
#include <yarp/os/BufferedPort.h>
#include <yarp/os/Time.h>
#include <yarp/os/impl/NameConfig.h>
#include <android/log.h>
#include <cerrno>
#include <cstdlib>
#include "com_alecive_yarpdroid_demoCTPFragment.h"
#include "handle.h"

#define LOG_TAG "com.alecive.yarpdroid.demoCTPFragment C++"

// cached refs for later callbacks
JavaVM   *jeyvm;


using namespace yarp::os;

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_register (JNIEnv *env, jobject obj)
{
    bool returnValue = true;
    env->GetJavaVM(&jeyvm);
    return (jboolean)returnValue;
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_createBufferedPort
  (JNIEnv *env, jobject obj)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm creating the buffered port");
    if (putenv("YARP_CONF=/data/data/com.alecive.yarpdroid/files/yarpconf"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Putenv failed %d", errno);
    }

    BufferedPort<Bottle> *demoCTPPort;
    demoCTPPort = new BufferedPort<Bottle>;

    if(!demoCTPPort->open("/yarpdroid/demoCTP:o"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Error in opening port!");
        delete demoCTPPort;
        demoCTPPort = 0;
        return (jboolean)false;
    }

    setHandle(env, obj, demoCTPPort, "demoCTPPortHandle");
    return (jboolean)true;
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_sendAction
  (JNIEnv *env, jobject obj, jint actionToSend)
{
    BufferedPort<Bottle>  *demoCTPPort = getHandle<BufferedPort<Bottle>  >(env, obj, "demoCTPPortHandle");
    Bottle& demoCTPBottle = demoCTPPort->prepare();
    demoCTPBottle.clear();
    demoCTPBottle.addInt(actionToSend);
    demoCTPPort -> write();

    return (jboolean)true;
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_destroyBufferedPort
  (JNIEnv *env, jobject obj)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm destroying the buffered port");
    BufferedPort<Bottle>  *demoCTPPort = getHandle<BufferedPort<Bottle>  >(env, obj, "demoCTPPortHandle");
    demoCTPPort->close();
    delete demoCTPPort;
    demoCTPPort = 0;
    return (jboolean)true;
}
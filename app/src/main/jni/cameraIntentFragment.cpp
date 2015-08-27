#include <string>
#include <sstream>
#include <stdio.h>
#include <yarp/os/Bottle.h>
#include <yarp/os/BufferedPort.h>
#include <yarp/os/Time.h>
#include <yarp/os/ManagedBytes.h>
#include <yarp/os/impl/NameConfig.h>
#include <android/log.h>
#include <cerrno>
#include <cstdlib>
#include "com_alecive_yarpdroid_cameraIntentFragment.h"
#include "handle.h"

#define LOG_TAG "com.alecive.yarpdroid.cameraIntentFragment C++"

using namespace yarp::os;

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_cameraIntentFragment_createBufferedPort
  (JNIEnv *env, jobject obj, jstring _applicationName)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm creating the buffered port");
    if (putenv("YARP_CONF=/data/data/com.alecive.yarpdroid/files/yarpconf"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Putenv failed %d", errno);
    }

    BufferedPort<ManagedBytes> *cameraPort;
    cameraPort = new BufferedPort<ManagedBytes>;
    std::string portName=env->GetStringUTFChars(_applicationName, 0);
    portName = portName + "/camera:o";

    if(!cameraPort->open(portName.c_str()))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Error in opening port!");
        delete cameraPort;
        cameraPort = 0;
        return (jboolean)false;
    }

    setHandle(env, obj, cameraPort, "cameraIntentHandle");
    return (jboolean)true;
}

JNIEXPORT void JNICALL Java_com_alecive_yarpdroid_cameraIntentFragment_writeOntoBufferedPort
  (JNIEnv *env, jobject obj, jbyteArray arr)
{
    BufferedPort<ManagedBytes>  *cameraPort = getHandle<BufferedPort<ManagedBytes>  >(env, obj, "cameraIntentHandle");
    ManagedBytes& cameraManagedBytes = cameraPort->prepare();
    size_t len = env->GetArrayLength (arr);
    cameraManagedBytes = ManagedBytes(Bytes(reinterpret_cast<char*>(as_unsigned_char_array(env,arr)),len));
    cameraPort -> write();
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_cameraIntentFragment_destroyBufferedPort
  (JNIEnv *env, jobject obj)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm destroying the buffered port");
    BufferedPort<ManagedBytes>  *cameraPort = getHandle<BufferedPort<ManagedBytes>  >(env, obj, "cameraIntentHandle");
    cameraPort->close();
    delete cameraPort;
    cameraPort = 0;
    return (jboolean)true;
}
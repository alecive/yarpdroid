#include <cerrno>
#include <string>
#include <cstdlib>
#include <sstream>
#include <android/log.h>
#include <yarp/os/Bottle.h>
#include <yarp/os/BufferedPort.h>
#include <yarp/os/Network.h>

#include "com_alecive_yarpdroid_MainActivity.h"
#include "handle.h"

#define LOG_TAG "com.alecive.yarpdroid.MainActivity C++"

using namespace yarp::os;

JavaVM   *javavm;

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_MainActivity_initNetwork
  (JNIEnv *env, jobject obj, jstring _sn, jstring _h, jint _p)
{
    if (putenv("YARP_CONF=/data/data/com.alecive.yarpdroid/files/yarpconf"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "[CheckNetwork()] Putenv failed %d", errno);
    }

    std::string s="Network configuration: ";

    Network yarp;

    ConstString serverName = env->GetStringUTFChars(_sn, 0);
    ConstString host       = env->GetStringUTFChars(_h, 0);
    int port=_p;
    Contact server = Contact::byName(serverName);
    server.setHost(host);
    server.setPort(port);

    if (!Network::setNameServerContact(server))
    {
        __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "setNameServerContact returned false!");
    }

    std::string net = Network::getNameServerName() + " " + host + ":" + int2string(port);
    std::string result = "Checking network: " + net;
    s = s + net;
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, result.c_str());
    if (!yarp.checkNetwork())
    {
        s += " FALSE";
        __android_log_write(ANDROID_LOG_WARN, LOG_TAG, "CheckNetwork() was false");
        return (jboolean)false;
    }
    else
    {
        __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "CheckNetwork() was true!");
        return (jboolean)true;
    }
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_MainActivity_register (JNIEnv *env, jobject obj)
{
    bool returnValue = true;
    env->GetJavaVM(&javavm);
    return (jboolean)returnValue;
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_MainActivity_createBufferedPort
        (JNIEnv *env, jobject obj)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm creating the buffered port");
    if (putenv("YARP_CONF=/data/data/com.alecive.yarpdroid/files/yarpconf"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Putenv failed %d", errno);
    }
    
    BufferedPort<Bottle> *stopPort;
    stopPort = new BufferedPort<Bottle>;
    
    if(!stopPort->open("/yarpdroid/motor_stop:o"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Error in opening port!");
        delete stopPort;
        stopPort = 0;
        return (jboolean)false;
    }

    setHandle(env, obj, stopPort, "stopPortHandle");
    return (jboolean)true;
}


JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_MainActivity_connectStopPort
        (JNIEnv *env, jobject obj, jstring str)
{
    std::string port = env->GetStringUTFChars(str, 0);
    std::string txt  = "I'm going to send an emergency command on " + port;
    __android_log_print(ANDROID_LOG_WARN, LOG_TAG, txt.c_str());
    
    if(!Network::connect("/yarpdroid/motor_stop:o", port.c_str()))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Error in connecting to the remote!");
        return (jboolean)false;
    }
    
    return (jboolean)true;
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_MainActivity_writeStopMsg
(JNIEnv *env, jobject obj)
{    
    BufferedPort<Bottle>  *stopPort = getHandle<BufferedPort<Bottle>  >(env, obj, "stopPortHandle");
    Bottle& stopBottle = stopPort->prepare();
    stopBottle.clear();
    stopBottle.addString("icub-stop-now");
    stopBottle.addDouble(0.9);
    stopPort -> write();
    
    return (jboolean)true;
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_MainActivity_destroyBufferedPort
        (JNIEnv *env, jobject obj)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm destroying the buffered port");
    BufferedPort<Bottle>  *stopPort = getHandle<BufferedPort<Bottle>  >(env, obj, "stopPortHandle");
    stopPort->close();
    delete stopPort;
    stopPort = 0;
    return (jboolean)true;
}

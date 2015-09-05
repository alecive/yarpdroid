#include <string>
#include <sstream>
#include <stdio.h>
#include <yarp/os/Bottle.h>
#include <yarp/os/BufferedPort.h>
#include <yarp/os/Time.h>
#include <yarp/os/impl/NameConfig.h>
#include <yarp/os/RpcClient.h>
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
  (JNIEnv *env, jobject obj, jstring _applicationName)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm creating the buffered port");
    if (putenv("YARP_CONF=/data/data/com.alecive.yarpdroid/files/yarpconf"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Putenv failed %d", errno);
    }

    BufferedPort<Bottle> *demoCTPPort;
    demoCTPPort = new BufferedPort<Bottle>;

    std::string portName=env->GetStringUTFChars(_applicationName, 0);
    portName = portName + "/demoCTP:o";
    if(!demoCTPPort->open(portName.c_str()))
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

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_createRPCPort
        (JNIEnv *env, jobject obj, jstring _applicationName)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm creating the buffered port");
    if (putenv("YARP_CONF=/data/data/com.alecive.yarpdroid/files/yarpconf"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Putenv failed %d", errno);
    }

    RpcClient *demoCTPRPC;
    demoCTPRPC = new RpcClient;

    std::string portName=env->GetStringUTFChars(_applicationName, 0);
    portName = portName + "/rpc:o";
    if(!demoCTPRPC->open(portName.c_str()))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Error in opening rpc port!");
        delete demoCTPRPC;
        demoCTPRPC = 0;
        return (jboolean)false;
    }

    setHandle(env, obj, demoCTPRPC, "demoCTPRPCHandle");
    return (jboolean)true;
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_sendRPCAction
        (JNIEnv *env, jobject obj, jstring msg)
{
    RpcClient *demoCTPRPC = getHandle<RpcClient>(env, obj, "demoCTPRPCHandle");
    Bottle cmd;  cmd.clear();
    Bottle resp; resp.clear();

    cmd.addString(env->GetStringUTFChars(msg, 0));

    demoCTPRPC->write(cmd, resp);
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Received %s", resp.toString().c_str());

    return true;
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_destroyRPCPort
        (JNIEnv *env, jobject obj)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm destroying the rpc port");
    RpcClient *demoCTPRPC = getHandle<RpcClient>(env, obj, "demoCTPRPCHandle");
    demoCTPRPC->close();
    delete demoCTPRPC;
    demoCTPRPC = 0;
    return (jboolean)true;
}
JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_createBufferedMobileSensorDataPort
  (JNIEnv *env, jobject obj, jstring _applicationName)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm creating the sensor port");
    if (putenv("YARP_CONF=/data/data/com.alecive.yarpdroid/files/yarpconf"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Putenv failed %d", errno);
    }

    BufferedPort<Bottle> *MobileSensorPort;
    MobileSensorPort = new BufferedPort<Bottle>;

    std::string portName=env->GetStringUTFChars(_applicationName, 0);
    portName = portName + "/mobilesensor:o";
    if(!MobileSensorPort->open(portName.c_str()))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Error in opening port!");
        delete MobileSensorPort;
        MobileSensorPort = 0;
        return (jboolean)false;
    }

    setHandle(env, obj, MobileSensorPort, "mobileSensorPortHandle");
    return (jboolean)true;
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_destroyBufferedMobileSensorDataPort
  (JNIEnv *env, jobject obj)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm destroying the buffered port");
    BufferedPort<Bottle>  *MobileSensorPort = getHandle<BufferedPort<Bottle>  >(env, obj, "mobileSensorPortHandle");
    MobileSensorPort->close();
    delete MobileSensorPort;
    MobileSensorPort = 0;
    return (jboolean)true;
}

JNIEXPORT void JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_writeOntoBufferedMobilePort
  (JNIEnv *env, jobject obj, jdouble accelerometer1, jdouble accelerometer2, jdouble accelerometer3, jdouble gyroscope1, jdouble gyroscope2, jdouble gyroscope3,
  jdouble orientation1, jdouble orientation2, jdouble orientation3, jstring movementType)
{
    BufferedPort<Bottle>  *MobileSensorPort = getHandle<BufferedPort<Bottle>  >(env, obj, "mobileSensorPortHandle");
    Bottle& MobileBottle = MobileSensorPort->prepare();
    MobileBottle.clear();
    MobileBottle.addDouble((double)accelerometer1);
    MobileBottle.addDouble((double)accelerometer2);
    MobileBottle.addDouble((double)accelerometer3);
    MobileBottle.addDouble((double)gyroscope1);
    MobileBottle.addDouble((double)gyroscope2);
    MobileBottle.addDouble((double)gyroscope3);
    MobileBottle.addDouble((double)orientation1);
    MobileBottle.addDouble((double)orientation2);
    MobileBottle.addDouble((double)orientation3);
    MobileBottle.addString(env->GetStringUTFChars(movementType, 0));

//    std::stringstream ss;
//    ss << accelerometer1 << " " << accelerometer2 << " " << accelerometer3 << " " << gyroscope1 << " " << gyroscope2 << " " << gyroscope3 << std::endl;
//    __android_log_write(ANDROID_LOG_DEBUG, "yarpviewFragment C++", ss.str().c_str());

    MobileSensorPort -> write();
}

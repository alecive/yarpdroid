#include <string>
#include <sstream>
#include <stdio.h>
#include <yarp/os/Bottle.h>
#include <yarp/os/BufferedPort.h>
#include <yarp/os/Network.h>
#include <yarp/os/Time.h>
#include <yarp/os/impl/NameConfig.h>
#include <android/log.h>
#include <cerrno>
#include <cstdlib>
#include "com_alecive_yarpdroid_STTFragment.h"
#include "handle.h"

#define LOG_TAG "com.alecive.yarpdroid.STTFragment C++"

// cached refs for later callbacks
JavaVM   *jvm;
jobject   g_obj;
jmethodID g_mid;


using namespace yarp::os;

class DataProcessor : public TypedReaderCallback<Bottle> {
    JNIEnv *env;
    jobject obj;

    virtual void onRead(Bottle& b) {
        __android_log_print(ANDROID_LOG_WARN, LOG_TAG, "DATA RECEIVED!");

        JNIEnv *myNewEnv;
        JavaVMAttachArgs args;
        args.version = JNI_VERSION_1_6; // choose your JNI version
        args.name = NULL; // you might want to give the java thread a name
        args.group = NULL; // you might want to assign the java thread to a ThreadGroup
        jvm->AttachCurrentThread(&myNewEnv, &args);
        jstring jstr = myNewEnv->NewStringUTF(b.toString().c_str());
        jvm->DetachCurrentThread();

        Java_com_alecive_yarpdroid_STTFragment_getDataReceivedonPort(env,obj,jstr);
    }

    public:
        DataProcessor(JNIEnv *_env, jobject _obj): env(_env), obj(_obj) {};
};

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_STTFragment_register (JNIEnv *env, jobject obj)
{
    bool returnValue = true;
    env->GetJavaVM(&jvm);
    return (jboolean)returnValue;
}

JNIEXPORT void JNICALL Java_com_alecive_yarpdroid_STTFragment_getDataReceivedonPort (JNIEnv *env, jobject obj, jstring jstr)
{
    JavaVMAttachArgs args;
    args.version = JNI_VERSION_1_6; // choose your JNI version
    args.name = NULL; // you might want to give the java thread a name
    args.group = NULL; // you might want to assign the java thread to a ThreadGroup
    jvm->AttachCurrentThread(&env, &args);

    jclass cls=env->GetObjectClass(obj);
    jmethodID method=env->GetMethodID(cls, "nonStaticTestMethod", "(Ljava/lang/String;)V");

    if(env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return;
    }

    // jstring jstr = env->NewStringUTF("Hello from C NONSTATIC");
    env->CallVoidMethod(obj, method, jstr);
    jvm->DetachCurrentThread();
}

JNIEXPORT void JNICALL Java_com_alecive_yarpdroid_STTFragment_testCallbackStatic (JNIEnv *env, jclass jcls)
{
    jclass cls = env->FindClass("com/alecive/yarpdroid/STTFragment");
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return;
    }

    jmethodID methodid;

    methodid = env->GetStaticMethodID(cls, "staticTestMethod", "(Ljava/lang/String;)V");
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return;
    }

    jstring jstr = env->NewStringUTF("Hello from C STATIC");
    env->CallStaticVoidMethod(cls, methodid, jstr);
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_STTFragment_createBufferedPort
  (JNIEnv *env, jobject obj, jstring _applicationName)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm creating the buffered port");
    if (putenv("YARP_CONF=/data/data/com.alecive.yarpdroid/files/yarpconf"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Putenv failed %d", errno);
    }

    DataProcessor* processor = new DataProcessor(env, obj);
    BufferedPort<Bottle> *STTPort;
    STTPort = new BufferedPort<Bottle>;
    STTPort->useCallback(*processor);

    std::string portName=env->GetStringUTFChars(_applicationName, 0);
    portName = portName + "/STT:o";
    if(!STTPort->open(portName.c_str()))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Error in opening port named %s",portName.c_str());
        delete STTPort;
        STTPort = 0;
        return (jboolean)false;
    }

    if(!Network::connect("/yarpdroid/STT:o", "/IOL/speechRecog"))
    {
        __android_log_print(ANDROID_LOG_WARN, LOG_TAG, "Error in connecting STT to the remote");
    }

    setHandle(env, obj, STTPort, "STTPortHandle");
    return (jboolean)true;
}

JNIEXPORT void JNICALL Java_com_alecive_yarpdroid_STTFragment_writeOntoBufferedPort
  (JNIEnv *env, jobject obj, jstring textToSpeak)
{
    BufferedPort<Bottle>  *STTPort = getHandle<BufferedPort<Bottle>  >(env, obj, "STTPortHandle");
    Bottle& STTBottle = STTPort->prepare();
    STTBottle.clear();
    STTBottle.addString(env->GetStringUTFChars(textToSpeak, 0));
    STTPort -> write();
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_STTFragment_destroyBufferedPort
  (JNIEnv *env, jobject obj)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm destroying the buffered port");
    BufferedPort<Bottle>  *STTPort = getHandle<BufferedPort<Bottle>  >(env, obj, "STTPortHandle");
    STTPort->close();
    delete STTPort;
    STTPort = 0;
    return (jboolean)true;
}
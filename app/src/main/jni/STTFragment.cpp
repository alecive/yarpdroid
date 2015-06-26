#include <string>
#include <sstream>
#include <stdio.h>
#include <yarp/os/Bottle.h>
#include <yarp/os/BufferedPort.h>
#include <yarp/os/Time.h>
#include <yarp/os/impl/NameConfig.h>
#include <android/log.h>
#include <cerrno>
#include "com_alecive_yarpdroid_STTFragment.h"
#include "handle.h"

#define LOG_TAG "com.alecive.yarpdroid.STTFragment C++"

using namespace yarp::os;

void Java_com_alecive_yarpdroid_STTFragment_testCallbackNonStatic(JNIEnv *env, jobject obj)
{
    jclass cls=env->GetObjectClass(obj);
    jmethodID method=env->GetMethodID(cls, "nonStaticTestMethod", "(Ljava/lang/String;)V");

    if(env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return;
    }

    jstring jstr = env->NewStringUTF("Hello from C NONSTATIC");
    env->CallVoidMethod(obj, method, jstr);
}

void Java_com_alecive_yarpdroid_STTFragment_testCallbackStatic(JNIEnv *env, jobject obj)
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

JNIEXPORT void JNICALL Java_com_alecive_yarpdroid_STTFragment_createBufferedPort
  (JNIEnv *env, jobject obj)
{
    __android_log_print(ANDROID_LOG_WARN, LOG_TAG, "I'm creating the buffered port");

    BufferedPort<Bottle> *STTPort;
    STTPort = new BufferedPort<Bottle>;
    if(!STTPort->open("/yarpdroid/STT"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Error in opening port!");
    }

    setHandle(env, obj, STTPort, "nativeHandle");
}

JNIEXPORT void JNICALL Java_com_alecive_yarpdroid_STTFragment_writeOntoBufferedPort
  (JNIEnv *env, jobject obj, jstring textToSpeak)
{
    BufferedPort<Bottle> *STTPort = getHandle<BufferedPort<Bottle> >(env, obj, "nativeHandle");
    Bottle& STTBottle = STTPort->prepare();
    STTBottle.addString(env->GetStringUTFChars(textToSpeak, 0));
    STTPort -> write();
}

JNIEXPORT void JNICALL Java_com_alecive_yarpdroid_STTFragment_destroyBufferedPort
  (JNIEnv *env, jobject obj)
{
    __android_log_print(ANDROID_LOG_WARN, LOG_TAG, "I'm destroying the buffered port");
    BufferedPort<Bottle> *STTPort = getHandle<BufferedPort<Bottle> >(env, obj, "nativeHandle");
    delete STTPort;
    STTPort = 0;
}
#include <jni.h>
#include <string>
#include <sstream>
#include <stdio.h>
#include <yarp/os/Network.h>
#include <yarp/os/Time.h>
#include <yarp/os/impl/NameConfig.h>
#include <android/log.h>
#include <cerrno>
#include <stdlib.h>
#include "com_alecive_yarpdroid_MainActivity.h"

using namespace yarp::os;

JNIEXPORT jstring JNICALL Java_com_alecive_yarpdroid_MainActivity_yarpdroid (JNIEnv *env, jobject obj) {

    if (putenv("YARP_CONF=/data/data/com.alecive.yarpdroid/files/yarpconf"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Putenv failed %d", errno);
    }

    std::string s="Network configuration: ";

    Network yarp;

    s += Network::getNameServerName();

    Contact getContact = Network::getNameServerContact();
    s += getContact.getHost();
    s += getContact.getPort();
//    return env->NewStringUTF(s.c_str());

//    Network::init();
    ConstString serverName = "/icub04";
    ConstString host ="10.255.10.133";
    int port =10000;
    Contact server = Contact::byName(serverName);
    server.setHost(host);
    server.setPort(port);


    if (!Network::setNameServerContact(server))
    {
        __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "setNameServerContact returned false!");
    }

    if (!yarp.checkNetwork())
    {
        s += " FALSE";
        __android_log_write(ANDROID_LOG_WARN, LOG_TAG, "CheckNetwork was false");
    }
    else
    {
        s += " TRUE";
    }

    return env->NewStringUTF(s.c_str());
}

void Java_com_alecive_yarpdroid_MainActivity_testCallback(JNIEnv *env, jobject obj)
{
    jclass cls = env->FindClass("com/alecive/yarpdroid/yarpSTTFragment");
    if (env->ExceptionCheck()) {
        __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "ERRORACCIO CLS");
        return;
    }
    jmethodID methodid;

//    try {
        methodid = env->GetStaticMethodID(cls, "staticTestMethod", "([Ljava/lang/String;)V");
//    }
//    catch (NoSuchMethodError* nsme) {
//        __android_log_write(ANDROID_LOG_WARN, LOG_TAG, "nsme");
//    }
//    catch (ExceptionInInitializerError* eiie) {
//        __android_log_write(ANDROID_LOG_WARN, LOG_TAG, "eiie");
//    }
//    catch (OutOfMemoryError* oome) {
//        __android_log_write(ANDROID_LOG_WARN, LOG_TAG, "oome");
//    }


    if(!methodid) {
        __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "ERRORACCIO MTHD");
        return;
    }
    jstring jstr = env->NewStringUTF("Hello from C");

    yarp::os::Time::delay(4.0);

    env->CallStaticVoidMethod(cls, methodid, jstr);
}

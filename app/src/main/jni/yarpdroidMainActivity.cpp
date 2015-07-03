#include <cerrno>
#include <string>
#include <cstdlib>
#include <sstream>
#include <android/log.h>
#include <yarp/os/Network.h>

#include "com_alecive_yarpdroid_MainActivity.h"
#include "handle.h"

#define LOG_TAG "com.alecive.yarpdroid.MainActivity C++"

using namespace yarp::os;

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

    s = Network::getNameServerName() + " " + host + ":" + int2string(port);
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "Checking network..");
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

//    return env->NewStringUTF(s.c_str());

}
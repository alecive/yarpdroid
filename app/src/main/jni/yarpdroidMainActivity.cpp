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

JNIEXPORT jstring JNICALL Java_com_alecive_yarpdroid_MainActivity_initNetwork
  (JNIEnv *env, jobject obj)
{
    if (putenv("YARP_CONF=/data/data/com.alecive.yarpdroid/files/yarpconf"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Putenv failed %d", errno);
    }

    std::string s="Network configuration: ";

    Network yarp;

    ConstString serverName = "/icub04";
//    ConstString host="192.168.1.5";
    ConstString host="10.255.10.133";
    int port=10000;
    Contact server = Contact::byName(serverName);
    server.setHost(host);
    server.setPort(port);

    if (!Network::setNameServerContact(server))
    {
        __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "setNameServerContact returned false!");
    }

    s = Network::getNameServerName() + " " + host + ":" + int2string(port);

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
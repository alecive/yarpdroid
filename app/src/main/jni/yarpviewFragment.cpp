#include <string>
#include <sstream>
#include <fstream>
#include <iomanip>
#include <stdio.h>
#include <stdlib.h>
#include <yarp/os/Bottle.h>
#include <yarp/os/BufferedPort.h>
#include <yarp/os/Time.h>
#include <yarp/os/impl/NameConfig.h>
#include <yarp/sig/Image.h>
#include <android/log.h>
#include <cerrno>
#include <cstdlib>
#include "com_alecive_yarpdroid_yarpviewFragment.h"
#include "handle.h"

#define LOG_TAG "yarpviewFragment C++"

using namespace yarp::sig;
using namespace yarp::os;

JavaVM   *gvm=NULL;

static jclass  javaCls;
static jobject javaObj;

jmethodID     method_setImgLeft;

class DataProcessorImg : public TypedReaderCallback<ImageOf<PixelRgb> > {
    JNIEnv *env;
    jobject obj;
    int dataReceived;

    virtual void onRead(ImageOf<PixelRgb>& img) {
        if(dataReceived%1==0)
        {
            __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "test");
            JavaVMAttachArgs args;
            args.version = JNI_VERSION_1_6; // choose your JNI version
            args.name = NULL; // you might want to give the java thread a name
            args.group = NULL; // you might want to assign the java thread to a ThreadGroup
            gvm->AttachCurrentThread(&env, &args);

            unsigned char *imgRaw = img.getRawImage();
            // std::string imstr(reinterpret_cast<char*>(imgRaw));
            // jstring imgStr = env->NewStringUTF(imstr.c_str());

            jbyteArray imgByte=as_byte_array(env,imgRaw,img.getRawImageSize());
            std::stringstream s;
            s << "DATA RECEIVED! Size of the imgRaw: " << sizeof(imgRaw) << " " << img.getRawImageSize()
              << " Img size:" << img.width() << "x" << img.height();
            __android_log_print(ANDROID_LOG_INFO, LOG_TAG, s.str().c_str());

            // std::stringstream ss;
            // ss << std::hex << std::setfill('0');
            // for (int i=0;i<img.getRawImageSize();i++)
            // {
            //     ss << std::setw(2) << static_cast<unsigned>(imgRaw[i]);
            // }
            // __android_log_print(ANDROID_LOG_WARN, LOG_TAG, ss.str().c_str());

            // std::ofstream out;
            // out.open ("/sdcard/CiveTest/imgOrig.ppm", std::ios::out | std::ios::binary);
            // out<< "P6\n" << img.width() << " " << img.height() <<"\n255\n";
            // out.write (reinterpret_cast<char*>(imgRaw), img.getRawImageSize());
            // out.close ();

            getImgReceivedonPort(imgByte);
            // env->DeleteLocalRef(imgByte);
            gvm->DetachCurrentThread();
        }
        dataReceived++;
    }

    void getImgReceivedonPort (jbyteArray img)
    {
        JavaVMAttachArgs args;
        args.version = JNI_VERSION_1_6; // choose your JNI version
        args.name = NULL; // you might want to give the java thread a name
        args.group = NULL; // you might want to assign the java thread to a ThreadGroup
        gvm->AttachCurrentThread(&env, &args);

        // jmethodID method=env->GetMethodID(javaCls, "setImgLeft", "([B)V");

        if(env->ExceptionCheck()) {
            env->ExceptionDescribe();
            env->ExceptionClear();
            return;
        }

        env->CallVoidMethod(obj, method_setImgLeft, img);
        gvm->DetachCurrentThread();
        __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "getImgReceivedonPort finished");
    }

    public:
        DataProcessorImg(JNIEnv *_env, jobject _obj): env(_env), obj(_obj), dataReceived(0) {};
};

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_yarpviewFragment_register
  (JNIEnv *env, jobject obj)
{
    env->GetJavaVM(&gvm);
    javaObj = env->NewGlobalRef(obj);
    jclass tmp = env->GetObjectClass(javaObj);
    javaCls = (jclass)env->NewGlobalRef(tmp);

    method_setImgLeft=env->GetMethodID(javaCls, "setImgLeft", "([B)V");

    return (jboolean)true;
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_yarpviewFragment_sendTouchEventsonMonoIPort
  (JNIEnv *env, jobject obj, jstring cam, jint u, jint v, jdouble z)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "TEST");
    BufferedPort<Bottle> *MonoPortO= getHandle<BufferedPort<Bottle>  >(env, javaObj, "monoLeftHandle");
    Bottle& MonoPortBottle = MonoPortO->prepare();
    MonoPortBottle.clear();
    MonoPortBottle.addString(env->GetStringUTFChars(cam, 0));
    MonoPortBottle.addInt(u);
    MonoPortBottle.addInt(v);
    MonoPortBottle.addDouble(z);
    MonoPortO -> write();
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_yarpviewFragment_createBufferedImgPortL
  (JNIEnv *env, jobject obj, jstring _applicationName)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm creating the image port");
    if (putenv("YARP_CONF=/data/data/com.alecive.yarpdroid/files/yarpconf"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Putenv failed %d", errno);
    }

    DataProcessorImg* processor = new DataProcessorImg(env, javaObj);
    BufferedPort<ImageOf<PixelRgb> > *ImgPortL;
    ImgPortL = new BufferedPort<ImageOf<PixelRgb> >;
    ImgPortL->useCallback(*processor);

    std::string portName=env->GetStringUTFChars(_applicationName, 0);
    portName = portName + "/cam/left:i";
    if(!ImgPortL->open(portName.c_str()))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Error in opening image port!");
        delete ImgPortL;
        ImgPortL = 0;
        return (jboolean)false;
    }

    setHandle(env, javaObj, ImgPortL, "viewLeftHandle");
    return (jboolean)true;
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_yarpviewFragment_destroyBufferedImgPortL
  (JNIEnv *env, jobject obj)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm destroying the image port");
    BufferedPort<ImageOf<PixelRgb> >  *ImgPortL = getHandle<BufferedPort<ImageOf<PixelRgb> >  >(env, javaObj, "viewLeftHandle");
    ImgPortL->close();
    delete ImgPortL;
    ImgPortL = 0;
    return (jboolean)true;
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_yarpviewFragment_createBufferedMonoIPort
  (JNIEnv *env, jobject obj, jstring _applicationName)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm creating the mono port");
    if (putenv("YARP_CONF=/data/data/com.alecive.yarpdroid/files/yarpconf"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Putenv failed %d", errno);
    }

    BufferedPort<Bottle> *MonoPortO = new BufferedPort<Bottle>;

    std::string portName=env->GetStringUTFChars(_applicationName, 0);
    portName = portName + "/iKinGazeCtrl/mono:o";
    if(!MonoPortO->open(portName.c_str()))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Error in opening mono port!");
        delete MonoPortO;
        MonoPortO = 0;
        return (jboolean)false;
    }

    setHandle(env, javaObj, MonoPortO, "monoLeftHandle");
    return (jboolean)true;
}

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_yarpviewFragment_destroyBufferedMonoIPort
  (JNIEnv *env, jobject obj)
{
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "I'm destroying the mono port");
    BufferedPort<Bottle>  *MonoPortO= getHandle<BufferedPort<Bottle>  >(env, obj, "monoLeftHandle");
    MonoPortO->close();
    delete MonoPortO;
    MonoPortO = 0;
    return (jboolean)true;
}
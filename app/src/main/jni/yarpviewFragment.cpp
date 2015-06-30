#include <string>
#include <sstream>
#include <stdio.h>
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

JavaVM   *gvm;

class DataProcessorImg : public TypedReaderCallback<ImageOf<PixelRgb> > {
    JNIEnv *env;
    jobject obj;

    virtual void onRead(ImageOf<PixelRgb>& img) {

        JavaVMAttachArgs args;
        args.version = JNI_VERSION_1_6; // choose your JNI version
        args.name = NULL; // you might want to give the java thread a name
        args.group = NULL; // you might want to assign the java thread to a ThreadGroup
        gvm->AttachCurrentThread(&env, &args);

        unsigned char *imgRaw = img.getRawImage();
        jbyteArray imgByte=as_byte_array(env,imgRaw,img.getRawImageSize());
            std::stringstream s;
            s << "DATA RECEIVED! Size of the imgRaw: " << sizeof(imgRaw) << " " << img.getRawImageSize()
              << " Img size:" << img.width() << "x" << img.height();
            __android_log_print(ANDROID_LOG_WARN, LOG_TAG, s.str().c_str());
        Java_com_alecive_yarpdroid_yarpviewFragment_getImgReceivedonPort(env, obj, imgByte);

        gvm->DetachCurrentThread();
    }

    public:
        DataProcessorImg(JNIEnv *_env, jobject _obj): env(_env), obj(_obj) {};
};

JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_yarpviewFragment_register
  (JNIEnv *env, jobject obj)
{
    bool returnValue = true;
    env->GetJavaVM(&gvm);
    return (jboolean)returnValue;
}

JNIEXPORT void JNICALL Java_com_alecive_yarpdroid_yarpviewFragment_getImgReceivedonPort
  (JNIEnv *env, jobject obj, jbyteArray img)
{
    JavaVMAttachArgs args;
    args.version = JNI_VERSION_1_6; // choose your JNI version
    args.name = NULL; // you might want to give the java thread a name
    args.group = NULL; // you might want to assign the java thread to a ThreadGroup
    gvm->AttachCurrentThread(&env, &args);

    jclass cls=env->GetObjectClass(obj);
    jmethodID method=env->GetMethodID(cls, "setImgLeft", "([B)V");

    if(env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return;
    }

    env->CallVoidMethod(obj, method, img);
    gvm->DetachCurrentThread();
}

JNIEXPORT void JNICALL Java_com_alecive_yarpdroid_yarpviewFragment_createBufferedImgPortL
  (JNIEnv *env, jobject obj)
{
    __android_log_print(ANDROID_LOG_WARN, LOG_TAG, "I'm creating the image port");
    if (putenv("YARP_CONF=/data/data/com.alecive.yarpdroid/files/yarpconf"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Putenv failed %d", errno);
    }
    
    DataProcessorImg* processor = new DataProcessorImg(env, obj);
    BufferedPort<ImageOf<PixelRgb> > *ImgPortL;
    ImgPortL = new BufferedPort<ImageOf<PixelRgb> >;
    ImgPortL->useCallback(*processor);
    if(!ImgPortL->open("/yarpdroid/imgL"))
    {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Error in opening image port!");
    }

    setHandle(env, obj, ImgPortL, "viewLeftHandle");
}

JNIEXPORT void JNICALL Java_com_alecive_yarpdroid_yarpviewFragment_destroyBufferedImgPortL
  (JNIEnv *env, jobject obj)
{
    __android_log_print(ANDROID_LOG_WARN, LOG_TAG, "I'm destroying the image port");
    BufferedPort<ImageOf<PixelRgb> >  *ImgPortL = getHandle<BufferedPort<ImageOf<PixelRgb> >  >(env, obj, "viewLeftHandle");
    delete ImgPortL;
    ImgPortL = 0;
}

#ifndef _HANDLE_H_INCLUDED_
#define _HANDLE_H_INCLUDED_

#include <string>
#include <sstream>
#include <jni.h>

jfieldID getHandleField(JNIEnv *env, jobject obj, std::string str);

template <typename T>
T *getHandle(JNIEnv *env, jobject obj, std::string str)
{
    jlong handle = env->GetLongField(obj, getHandleField(env, obj, str));
    return reinterpret_cast<T *>(handle);
}

template <typename T>
void setHandle(JNIEnv *env, jobject obj, T *t, std::string str)
{
    jlong handle = reinterpret_cast<jlong>(t);
    env->SetLongField(obj, getHandleField(env, obj, str), handle);
}

std::string int2string ( int num );

jbyteArray     as_byte_array(JNIEnv *env, unsigned char* buf, int len);
unsigned char* as_unsigned_char_array(JNIEnv *env, jbyteArray array);

#endif
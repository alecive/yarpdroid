#include "handle.h"

jfieldID getHandleField(JNIEnv *env, jobject obj, std::string str)
{
    jclass c = env->GetObjectClass(obj);
    // J is the type signature for long:
    return env->GetFieldID(c, str.c_str(), "J");
}

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

std::string int2string ( int num )
{
	std::stringstream ss;
	ss << num;
	return ss.str();
}
#include "handle.h"

jfieldID getHandleField(JNIEnv *env, jobject obj, std::string str)
{
    jclass c = env->GetObjectClass(obj);
    // J is the type signature for long:
    return env->GetFieldID(c, str.c_str(), "J");
}

std::string int2string ( int num )
{
	std::stringstream ss;
	ss << num;
	return ss.str();
}

jbyteArray as_byte_array(JNIEnv *env, unsigned char* buf, int len) {
    jbyteArray array = env->NewByteArray (len);
    env->SetByteArrayRegion (array, 0, len, reinterpret_cast<jbyte*>(buf));
    return array;
}
/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_alecive_yarpdroid_demoCTPFragment */

#ifndef _Included_com_alecive_yarpdroid_demoCTPFragment
#define _Included_com_alecive_yarpdroid_demoCTPFragment
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_alecive_yarpdroid_demoCTPFragment
 * Method:    register
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_register
  (JNIEnv *, jobject);

/*
 * Class:     com_alecive_yarpdroid_demoCTPFragment
 * Method:    createBufferedPort
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_createBufferedPort
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_alecive_yarpdroid_demoCTPFragment
 * Method:    sendAction
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_sendAction
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_alecive_yarpdroid_demoCTPFragment
 * Method:    destroyBufferedPort
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_destroyBufferedPort
  (JNIEnv *, jobject);

/*
 * Class:     com_alecive_yarpdroid_demoCTPFragment
 * Method:    createRPCPort
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_createRPCPort
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_alecive_yarpdroid_demoCTPFragment
 * Method:    sendRPCAction
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_sendRPCAction
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_alecive_yarpdroid_demoCTPFragment
 * Method:    destroyRPCPort
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_alecive_yarpdroid_demoCTPFragment_destroyRPCPort
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif

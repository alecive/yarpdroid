LOCAL_PATH := $(call my-dir)

### include libyarpos as a prebuilt lib ###

include $(CLEAR_VARS)

LOCAL_MODULE            := libYARP_OS
LOCAL_SRC_FILES         := libs/$(TARGET_ARCH_ABI)/libYARP_OS.a
LOCAL_EXPORT_C_INCLUDES := /home/alecive/Programmazione/yarp/src/libYARP_OS/include \
                           /home/alecive/Programmazione/yarp/src/libYARP_OS/include/yarp/os \
                           /home/alecive/Programmazione/yarp/build-arm/generated_include

include $(PREBUILT_STATIC_LIBRARY)

### include libyarpinit as a prebuilt lib ###

include $(CLEAR_VARS)

LOCAL_MODULE            := libYARP_init
LOCAL_SRC_FILES         := libs/$(TARGET_ARCH_ABI)/libYARP_init.a

include $(PREBUILT_STATIC_LIBRARY)

### build your ndk lib ###

include $(CLEAR_VARS)
LOCAL_MODULE := yarpdroid
LOCAL_SRC_FILES := STTFragment.cpp yarpdroidMainActivity.cpp
LOCAL_STATIC_LIBRARIES := libYARP_OS libYARP_init
LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog
include $(BUILD_SHARED_LIBRARY)

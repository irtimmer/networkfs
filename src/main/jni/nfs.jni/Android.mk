LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../external/libnfs/include
LOCAL_SRC_FILES := nfs-jni.c
LOCAL_MODULE := nfs-jni
LOCAL_SHARED_LIBRARIES := nfs

include $(BUILD_SHARED_LIBRARY)

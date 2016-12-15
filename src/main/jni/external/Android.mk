LOCAL_PATH := $(call my-dir)/libnfs

include $(CLEAR_VARS)

LOCAL_MODULE := nfs
LOCAL_CFLAGS := -std=gnu99 -DHAVE_CONFIG_H

LOCAL_C_INCLUDES := $(LOCAL_PATH)/.. \
                    $(LOCAL_PATH)/include \
                    $(LOCAL_PATH)/include/nfsc \
                    $(LOCAL_PATH)/mount \
                    $(LOCAL_PATH)/nfs \
                    $(LOCAL_PATH)/portmap

LOCAL_SRC_FILES := $(LOCAL_PATH)/lib/init.c \
                   $(LOCAL_PATH)/lib/libnfs.c \
                   $(LOCAL_PATH)/lib/libnfs-sync.c \
                   $(LOCAL_PATH)/lib/libnfs-zdr.c \
                   $(LOCAL_PATH)/lib/pdu.c \
                   $(LOCAL_PATH)/lib/socket.c

LOCAL_SRC_FILES += $(LOCAL_PATH)/nfs/nfs.c \
                   $(LOCAL_PATH)/nfs/nfsacl.c \
                   $(LOCAL_PATH)/nfs/libnfs-raw-nfs.c

LOCAL_SRC_FILES += $(LOCAL_PATH)/portmap/portmap.c \
                   $(LOCAL_PATH)/portmap/libnfs-raw-portmap.c

LOCAL_SRC_FILES += $(LOCAL_PATH)/mount/mount.c \
                   $(LOCAL_PATH)/mount/libnfs-raw-mount.c

include $(BUILD_SHARED_LIBRARY)

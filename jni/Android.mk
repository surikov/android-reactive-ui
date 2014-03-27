LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := ReactiveUI
LOCAL_SRC_FILES := ReactiveUI.cpp
LOCAL_LDLIBS := -llog
LOCAL_LDLIBS += -ljnigraphics

include $(BUILD_SHARED_LIBRARY)

APP_OPTIM := debug
LOCAL_CFLAGS := -g


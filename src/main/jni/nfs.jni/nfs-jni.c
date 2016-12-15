/*
 * This file is part of NetworkFS.
 *
 * Copyright (C) 2016 Iwan Timmer
 *
 * NetworkFS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * NetworkFS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NetworkFS; if not, see <http://www.gnu.org/licenses/>.
 */

#include <nfsc/libnfs.h>
#include <jni.h>

#include <stdio.h>
#include <asm/fcntl.h>

JNIEXPORT jlong JNICALL Java_nl_itimmer_networkfs_nfs_Nfs_init(JNIEnv *env, jobject this) {
    return (jlong) nfs_init_context();
}

JNIEXPORT void JNICALL Java_nl_itimmer_networkfs_nfs_Nfs_destroy(JNIEnv *env, jobject this, jlong ctx) {
    nfs_destroy_context((struct nfs_context*) ctx);
}

JNIEXPORT jint JNICALL Java_nl_itimmer_networkfs_nfs_Nfs_mount(JNIEnv *env, jobject this, jlong ctx, jstring jni_server, jstring jni_path) {
    const char* server = (*env)->GetStringUTFChars(env, jni_server, 0);
    const char* path = (*env)->GetStringUTFChars(env, jni_path, 0);

    int ret = nfs_mount((struct nfs_context*) ctx, server, path);

    (*env)->ReleaseStringUTFChars(env, jni_server, server);
    (*env)->ReleaseStringUTFChars(env, jni_path, path);
    return ret;
}

JNIEXPORT jlong JNICALL Java_nl_itimmer_networkfs_nfs_Nfs_opendir(JNIEnv *env, jobject this, jlong ctx, jstring jni_path) {
    const char* path = (*env)->GetStringUTFChars(env, jni_path, 0);

    struct nfsdir *dir;
    int ret = nfs_opendir((struct nfs_context*) ctx, path, &dir);

    (*env)->ReleaseStringUTFChars(env, jni_path, path);

    return ret < 0 ? 0 : (jlong) dir;
}

JNIEXPORT jboolean JNICALL Java_nl_itimmer_networkfs_nfs_Nfs_readdir(JNIEnv *env, jobject this, jlong ctx, jlong dir, jobject file) {
    struct nfsdirent* entry;
    if ((entry = nfs_readdir((struct nfs_context*) ctx, (struct nfsdir*) dir)) == NULL)
        return JNI_FALSE;

    jclass file_class = (*env)->GetObjectClass(env, file);
    jfieldID file_field = (*env)->GetFieldID(env, file_class, "name", "Ljava/lang/String;");
    jfieldID size_field = (*env)->GetFieldID(env, file_class, "size", "J");

    jstring name = (*env)->NewStringUTF(env, entry->name);
    (*env)->SetObjectField(env, file, file_field, name);
    (*env)->SetLongField(env, file, size_field, entry->size);

    return JNI_TRUE;
}

JNIEXPORT jlong JNICALL Java_nl_itimmer_networkfs_nfs_Nfs_open(JNIEnv *env, jobject this, jlong ctx, jstring jni_filename) {
    const char* filename = (*env)->GetStringUTFChars(env, jni_filename, 0);

    struct nfsfh *fd;
    int ret = nfs_open((struct nfs_context*) ctx, filename, O_RDONLY, &fd);

    (*env)->ReleaseStringUTFChars(env, jni_filename, filename);

    return ret < 0 ? 0 : (jlong) fd;
}

JNIEXPORT jint JNICALL Java_nl_itimmer_networkfs_nfs_Nfs_read(JNIEnv *env, jobject this, jlong ctx, jlong fd, jbyteArray jni_buffer, jint offset, jint length) {
    jboolean is_copy;
    jbyte* buffer = (*env)->GetByteArrayElements(env, jni_buffer, &is_copy);

    int ret = nfs_read((struct nfs_context*) ctx, (struct nfsfh*) fd, length, buffer + offset);

    (*env)->ReleaseByteArrayElements(env, jni_buffer, buffer, 0);

    return ret;
}

JNIEXPORT jint JNICALL Java_nl_itimmer_networkfs_nfs_Nfs_pread(JNIEnv *env, jobject this, jlong jni_ctx, jlong jni_fd, jlong position, jbyteArray jni_buffer, jint offset, jint length) {
    jboolean isCopy;
    jbyte* buffer = (*env)->GetByteArrayElements(env, jni_buffer, &isCopy);

    int ret = nfs_pread((struct nfs_context*) jni_ctx, (struct nfsfh*) jni_fd, position, length, buffer + offset);

    (*env)->ReleaseByteArrayElements(env, jni_buffer, buffer, 0);

    return ret;
}

JNIEXPORT jlong JNICALL Java_nl_itimmer_networkfs_nfs_Nfs_lseek(JNIEnv *env, jobject this, jlong ctx, jlong fd, jlong offset, jint whence) {
    jlong currentOffset;
    int ret = nfs_lseek((struct nfs_context*) ctx, (struct nfsfh*) fd, offset, whence, &currentOffset);

    return ret < 0 ? ret : currentOffset;
}

JNIEXPORT void JNICALL Java_nl_itimmer_networkfs_nfs_Nfs_close(JNIEnv *env, jobject this, jlong ctx, jlong fd) {
    nfs_close((struct nfs_context*) ctx, (struct nfsfh*) fd);
}

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

package nl.itimmer.networkfs.nfs;

public final class Nfs {

    static {
        System.loadLibrary("nfs-jni");
    }

    protected static native long init();

    protected static native int mount(long ctx, String server, String path);

    protected static native long opendir(long ctx, String dir);

    protected static native int stat(long ctx, String path, NfsFile file);

    protected static native boolean readdir(long ctx, long dir, NfsFile file);

    protected static native long open(long ctx, String filename);

    protected static native int read(long ctx, long fd, byte[] data, int offset, int length);

    protected static native long lseek(long ctx, long fd, long offset, int whence);

    protected static native int pread(long ctx, long fd, long position, byte[] data, int offset, int length);

    protected static native void close(long ctx, long fd);
}

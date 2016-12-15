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

import java.io.IOException;
import java.io.InputStream;

public class NfsFileInputStream extends InputStream {

    private int SEEK_SET = 0, SEEK_CUR = 1, SEEK_END = 2;

    private long fileContext;
    private String filename;

    private NfsContext ctx;

    public NfsFileInputStream(NfsFile file) throws IOException {
        this(file.getContext(), file.getName());
    }

    public NfsFileInputStream(NfsContext ctx, String filename) throws IOException {
        this.ctx = ctx;
        this.filename = filename;
        synchronized (ctx) {
            fileContext = Nfs.open(ctx.getContext(), filename);
        }
        if (fileContext == 0)
            throw new IOException("Can't open file " + filename);
    }

    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int ret;
        synchronized (ctx) {
            ret = Nfs.read(ctx.getContext(), fileContext, b, off, len);
        }
        if (ret < 0)
            throw new IOException("Couldn't read from file");

        return ret == 0 ? -1 : ret;
    }

    public long seek(long offset) throws IOException {
        long ret;
        synchronized (ctx) {
            ret = Nfs.lseek(ctx.getContext(), fileContext, offset, SEEK_SET);
        }
        if (ret < 0)
            throw new IOException("Can't reset offset");

        return ret;
    }

    public int readAt(long position, byte[] b, int off, int len) throws IOException {
        int ret;
        synchronized (ctx) {
            ret = Nfs.pread(ctx.getContext(), fileContext, position, b, off, len);
        }
        if (ret < 0)
            throw new IOException("Couldn't read from file");

        return ret == 0 ? -1 : ret;
    }

    @Override
    public void close() throws IOException {
        if (fileContext != 0)
            Nfs.close(ctx.getContext(), fileContext);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
    }
}

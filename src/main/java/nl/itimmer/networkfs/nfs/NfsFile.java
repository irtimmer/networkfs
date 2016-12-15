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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NfsFile {

    private String path;
    private String name;
    private long size;
    private boolean isFile;

    private NfsContext ctx;

    public NfsFile(NfsContext ctx, String path) {
        this.ctx = ctx;

        if (path != null) {
            int lastSeperator = path.lastIndexOf(File.separatorChar);
            if (lastSeperator >= 0) {
                this.path = path;
                this.name = path.substring(lastSeperator + 1);
            } else {
                this.name = path;
                this.path = File.separator + path;
            }
        }
    }

    public NfsFile[] listFiles() throws IOException {
        synchronized (ctx) {
            long dirContext = Nfs.opendir(ctx.getContext(), getPath());
            if (dirContext == 0)
                throw new IOException("Can't open NFS directory: " + getPath());

            List<NfsFile> files = new ArrayList<>();
            NfsFile file = new NfsFile(ctx, null);
            while (Nfs.readdir(ctx.getContext(), dirContext, file)) {
                file.path = this.path + File.separator + file.name;
                files.add(file);
                file = new NfsFile(ctx, null);
            }

            return files.toArray(new NfsFile[]{});
        }
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public boolean isFile() {
        return isFile;
    }

    public NfsContext getContext() {
        return ctx;
    }
}

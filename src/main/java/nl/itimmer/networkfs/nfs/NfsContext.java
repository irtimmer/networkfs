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

public class NfsContext {

    private long context;
    private boolean mounted;

    public NfsContext() throws IOException {
        context = Nfs.init();
        if (context == 0)
            throw new IOException("Can't initialize NFS context");
    }

    public void mount(String server, String path) throws IOException {
        if (!mounted) {
            if (Nfs.mount(context, server, path) == 0)
                mounted = true;
            else
                throw new IOException("Can't mount NFS server: " + server + ", path: " + path);
        } else
            throw new IOException("Context is already mounted");
    }

    protected long getContext() {
        return context;
    }

}

/*
 * Copyright 2004-2013 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.test.store;

import java.nio.ByteBuffer;
import org.h2.mvstore.DataUtils;
import org.h2.mvstore.type.DataType;

/**
 * A row type.
 */
public class RowDataType implements DataType {

    static final String PREFIX = "org.h2.test.store.row";

    private final DataType[] types;

    RowDataType(DataType[] types) {
        this.types = types;
    }

    public int compare(Object a, Object b) {
        if (a == b) {
            return 0;
        }
        Object[] ax = (Object[]) a;
        Object[] bx = (Object[]) b;
        int al = ax.length;
        int bl = bx.length;
        int len = Math.min(al, bl);
        for (int i = 0; i < len; i++) {
            int comp = types[i].compare(ax[i], bx[i]);
            if (comp != 0) {
                return comp;
            }
        }
        if (len < al) {
            return -1;
        } else if (len < bl) {
            return 1;
        }
        return 0;
    }

    public int getMemory(Object obj) {
        Object[] x = (Object[]) obj;
        int len = x.length;
        int memory = 0;
        for (int i = 0; i < len; i++) {
            memory += types[i].getMemory(x[i]);
        }
        return memory;
    }

    public Object[] read(ByteBuffer buff) {
        int len = DataUtils.readVarInt(buff);
        Object[] x = new Object[len];
        for (int i = 0; i < len; i++) {
            x[i] = types[i].read(buff);
        }
        return x;
    }

    public ByteBuffer write(ByteBuffer buff, Object obj) {
        Object[] x = (Object[]) obj;
        int len = x.length;
        DataUtils.writeVarInt(buff, len);
        for (int i = 0; i < len; i++) {
            buff = DataUtils.ensureCapacity(buff, 0);
            buff = types[i].write(buff, x[i]);
        }
        return buff;
    }

}

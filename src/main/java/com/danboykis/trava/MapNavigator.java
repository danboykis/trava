package com.danboykis.trava;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum MapNavigator {
    INSTANCE;

    public static Object get(Object m, Object k) {
        if (m == null) {
            return null;
        } else if (m instanceof Map) {
            return ((Map) m).get(k);
        } else {
            return null;
        }
    }

    public static Object getIn(Object data, List<?> path) {
        Object temp = data;
        for (Object p : path) {
            temp = get(temp, p);
            if (temp == null) {
                break;
            }
        }
        return temp;
    }

    public static Object assoc(Object data, Object key, Object value, Object... kvs) {
        if (kvs.length % 2 != 0) throw new IllegalArgumentException("must have an even number of key-vals");

        Object temp = assoc(data, key, value);

        for (int i = 0; i < kvs.length; i += 2) {
            Object k = kvs[i];
            Object v = kvs[i + 1];
            temp = assoc(temp, k, v);
        }
        return temp;
    }

    public static Object assoc(Object data, Object key, Object value) {
        Map m = new HashMap();
        if (data == null) {
            m.put(key, value);
        } else if (data instanceof Map) {
            m = (Map) data;
            m.put(key, value);
        } else { //Overwrite whatever was in before
            m.put(key, value);
        }
        return m;
    }

    public static Object assocIn(Object data, final List<?> path, final Object value, Object... kvs) {
        if (kvs.length % 2 != 0) throw new IllegalArgumentException("must have an even number of key-vals");

        Object temp = assocIn(data, path, value);
        for (int i = 0; i < kvs.length; i += 2) {
            Object k = kvs[i];
            Object v = kvs[i + 1];
            temp = assocIn(temp, (List) k, v);
        }
        return temp;
    }

    public static Object assocIn(Object data, final List<?> path, final Object value) {
        data = (data == null) ? new HashMap() : data;

        if (path.isEmpty()) {
            return data;
        } else if (path.size() == 1) {
            return assoc(data, path.get(0), value);
        } else {
            return assoc(data,
                    path.get(0),
                    assocIn(get(data, path.get(0)),
                            path.subList(1, path.size()),
                            value));
        }
    }

    public static Object update(Object data, Object key, Function updateFn) {
        return assoc(data, key, updateFn.apply(get(data, key)));
    }

    public static Object update(Object data, Object key, BiFunction updateFn, Object x) {
        return assoc(data, key, updateFn.apply(get(data, key), x));
    }
}

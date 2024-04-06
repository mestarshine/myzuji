package com.myzuji.sadk.org.bouncycastle.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CollectionStore implements Store {
    private Collection _local;

    public CollectionStore(Collection collection) {
        this._local = new ArrayList(collection);
    }

    public Collection getMatches(Selector selector) {
        if (selector == null) {
            return new ArrayList(this._local);
        } else {
            List col = new ArrayList();
            Iterator iter = this._local.iterator();

            while (iter.hasNext()) {
                Object obj = iter.next();
                if (selector.match(obj)) {
                    col.add(obj);
                }
            }

            return col;
        }
    }
}

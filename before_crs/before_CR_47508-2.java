/*Fix AbstractCollection.toArray.

ConcurrentHashMap.values().toArray() could throw if the map was being modified.
WeakHashMap had a workaround, but AbstractCollection is supposed to just do the
right thing.

Bug:http://code.google.com/p/android/issues/detail?id=36519Bug:https://issues.apache.org/jira/browse/HARMONY-6681Change-Id:I749fe615edbea6f8285bfe6804c972e8bdcf2530*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/AbstractCollection.java b/luni/src/main/java/java/util/AbstractCollection.java
//Synthetic comment -- index 7cbdcc0..c9b4f8f 100644

//Synthetic comment -- @@ -336,29 +336,20 @@
public abstract int size();

public Object[] toArray() {
        int size = size(), index = 0;
        Iterator<?> it = iterator();
        Object[] array = new Object[size];
        while (index < size) {
            array[index++] = it.next();
        }
        return array;
}

@SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] contents) {
        int size = size(), index = 0;
        if (size > contents.length) {
            Class<?> ct = contents.getClass().getComponentType();
            contents = (T[]) Array.newInstance(ct, size);
        }
for (E entry : this) {
            contents[index++] = (T) entry;
}
        if (index < contents.length) {
            contents[index] = null;
        }
        return contents;
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/WeakHashMap.java b/luni/src/main/java/java/util/WeakHashMap.java
//Synthetic comment -- index 2c07d28..0d9653a 100644

//Synthetic comment -- @@ -384,26 +384,6 @@
}
});
}

                @Override
                public Object[] toArray() {
                    Collection<K> coll = new ArrayList<K>(size());

                    for (Iterator<K> iter = iterator(); iter.hasNext();) {
                        coll.add(iter.next());
                    }
                    return coll.toArray();
                }

                @Override
                public <T> T[] toArray(T[] contents) {
                    Collection<K> coll = new ArrayList<K>(size());

                    for (Iterator<K> iter = iterator(); iter.hasNext();) {
                        coll.add(iter.next());
                    }
                    return coll.toArray(contents);
                }
};
}
return keySet;








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/AbstractCollectionTest.java b/luni/src/test/java/libcore/java/util/AbstractCollectionTest.java
new file mode 100644
//Synthetic comment -- index 0000000..2e6905b

//Synthetic comment -- @@ -0,0 +1,60 @@








/*Fix AbstractCollection.toArray.

ConcurrentHashMap.values().toArray() could throw if the map was being modified.
WeakHashMap had a workaround, but AbstractCollection is supposed to just do the
right thing.

Bug:http://code.google.com/p/android/issues/detail?id=36519Bug:https://issues.apache.org/jira/browse/HARMONY-6681Change-Id:I749fe615edbea6f8285bfe6804c972e8bdcf2530*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/AbstractCollection.java b/luni/src/main/java/java/util/AbstractCollection.java
//Synthetic comment -- index 7cbdcc0..c25db81 100644

//Synthetic comment -- @@ -336,29 +336,20 @@
public abstract int size();

public Object[] toArray() {
        List<Object> list = new ArrayList<Object>(size());
        for (E entry : this) {
            list.add(entry);
}
        return list.toArray();
}

@SuppressWarnings("unchecked")
public <T> T[] toArray(T[] contents) {
        List<T> list = new ArrayList<T>(size());
for (E entry : this) {
            list.add((T) entry);
}
        return list.toArray(contents);
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/WeakHashMap.java b/luni/src/main/java/java/util/WeakHashMap.java
//Synthetic comment -- index 2c07d28..0d9653a 100644

//Synthetic comment -- @@ -384,26 +384,6 @@
}
});
}
};
}
return keySet;








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/AbstractCollectionTest.java b/luni/src/test/java/libcore/java/util/AbstractCollectionTest.java
new file mode 100644
//Synthetic comment -- index 0000000..1c98fd9

//Synthetic comment -- @@ -0,0 +1,59 @@
/*
 * Copyright (C) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package libcore.java.util;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ConcurrentHashMap;
import junit.framework.TestCase;

public final class AbstractCollectionTest extends TestCase {
  // http://code.google.com/p/android/issues/detail?id=36519
  public void test_toArray() throws Exception {
    final ConcurrentHashMap<Integer, Integer> m = new ConcurrentHashMap<Integer, Integer>();
    assertTrue(m.values() instanceof AbstractCollection);

    final AtomicBoolean finished = new AtomicBoolean(false);

    Thread reader = new Thread(new Runnable() {
      @Override public void run() {
        while (!finished.get()) {
          m.values().toArray();
        }
      }
    });

    Thread mutator = new Thread(new Runnable() {
      @Override public void run() {
        for (int i = 0; i < 100; ++i) {
          m.put(-i, -i);
        }
        for (int i = 0; i < 4096; ++i) {
          m.put(i, i);
          m.remove(i);
        }
        finished.set(true);
      }
    });

    reader.start();
    mutator.start();
    reader.join();
    mutator.join();
  }
}








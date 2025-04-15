/*Fix flaky finalization tests.

Bug: 6973520
Change-Id:I091b5ed7f47cc6f75107b8cd68013c00e165037c*/
//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/archive/tests/java/util/zip/ZipFileTest.java b/luni/src/test/java/org/apache/harmony/archive/tests/java/util/zip/ZipFileTest.java
//Synthetic comment -- index 17d251b..a423f22 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipFileTest extends junit.framework.TestCase {

//Synthetic comment -- @@ -150,11 +151,8 @@
* entry1); entry1 = null; zip = null;
*/

        assertNotNull("Did not find entry",
                test_finalize1(test_finalize2(file)));
        System.gc();
        System.gc();
        System.runFinalization();
file.delete();
assertTrue("Zip should not exist", !file.exists());
}








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/lang/ref/PhantomReferenceTest.java b/luni/src/test/java/tests/api/java/lang/ref/PhantomReferenceTest.java
//Synthetic comment -- index 06221c9..6470579 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

//TODO: write a test to verify that the referent's finalize() happens
//      before the PhantomReference is enqueued.
//Synthetic comment -- @@ -81,8 +82,8 @@
Thread t = new TestThread();
t.start();
t.join();
            System.gc();
            System.runFinalization();

assertNull("get() should return null.", tprs[0].get());
assertNull("get() should return null.", tprs[1].get());








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/lang/ref/ReferenceQueueTest.java b/luni/src/test/java/tests/api/java/lang/ref/ReferenceQueueTest.java
//Synthetic comment -- index dc7e738..cad61b3 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceQueueTest extends junit.framework.TestCase {
static Boolean b;
//Synthetic comment -- @@ -97,8 +98,7 @@
sr.enqueue();
wr.enqueue();

        System.gc();
        System.runFinalization();

assertNull(rq.poll());
}








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/lang/ref/ReferenceTest.java b/luni/src/test/java/tests/api/java/lang/ref/ReferenceTest.java
//Synthetic comment -- index a1a7a8c..7461b47 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import junit.framework.AssertionFailedError;

public class ReferenceTest extends junit.framework.TestCase {
Object tmpA, tmpB, tmpC, obj;
//Synthetic comment -- @@ -146,16 +147,14 @@
ReferenceQueue<Object> queue = new ReferenceQueue<Object>();

r = newWeakReference(queue);
        System.gc();
        System.runFinalization();
Reference ref = queue.remove();
assertNotNull("Object not enqueued.", ref);
assertSame("Unexpected ref1", ref, r);
assertNull("Object could not be reclaimed1.", r.get());

r = newWeakReference(queue);
        System.gc();
        System.runFinalization();

// wait for the reference queue thread to enqueue the newly-finalized object
Thread.yield();
//Synthetic comment -- @@ -213,8 +212,7 @@
Thread t = new TestThread();
t.start();
t.join();
            System.gc();
            System.runFinalization();
ref = rq.remove(5000L);    // Give up after five seconds.

assertNotNull("Object not garbage collected.", ref);
//Synthetic comment -- @@ -238,8 +236,7 @@
public void test_get() {
WeakReference ref = newWeakReference(null);

        System.gc();
        System.runFinalization();
assertNull("get() doesn't return null after gc for WeakReference", ref.get());

obj = new Object();
//Synthetic comment -- @@ -322,8 +319,7 @@
Thread t = new TestThread();
t.start();
t.join();
            System.gc();
            System.runFinalization();
Thread.sleep(1000);
if (error != null) {
throw error;








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/lang/ref/SoftReferenceTest.java b/luni/src/test/java/tests/api/java/lang/ref/SoftReferenceTest.java
//Synthetic comment -- index 77c6536..197d829 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Vector;

public class SoftReferenceTest extends junit.framework.TestCase {
static Boolean bool;
//Synthetic comment -- @@ -124,8 +125,7 @@
TestThread t = new TestThread();
t.start();
t.join();
            System.gc();
            System.runFinalization();
ref = rq.poll();
assertNotNull("Object not garbage collected.", ref);
assertNull("Object is not null.", ref.get());








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/util/WeakHashMapTest.java b/luni/src/test/java/tests/api/java/util/WeakHashMapTest.java
//Synthetic comment -- index 0e43bf6..d1a43e5 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import tests.support.Support_MapTest2;

//Synthetic comment -- @@ -208,7 +209,7 @@
do {
System.gc();
System.gc();
            Runtime.getRuntime().runFinalization();
count++;
} while (count <= 5 && entrySet.size() == 100);

//Synthetic comment -- @@ -240,7 +241,8 @@
WeakHashMap map = new WeakHashMap();
map.put(null, "value"); // add null key
System.gc();
        System.runFinalization();
map.remove("nothing"); // Cause objects in queue to be removed
assertEquals("null key was removed", 1, map.size());
}
//Synthetic comment -- @@ -315,7 +317,7 @@
do {
System.gc();
System.gc();
            Runtime.getRuntime().runFinalization();
count++;
} while (count <= 5 && keySet.size() == 100);

//Synthetic comment -- @@ -352,7 +354,7 @@
do {
System.gc();
System.gc();
            Runtime.getRuntime().runFinalization();
count++;
} while (count <= 5 && valuesCollection.size() == 100);









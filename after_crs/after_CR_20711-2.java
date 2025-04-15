/*android.os.Message: respect sPoolSize

Also rename some static members from mFoo to sFoo.

Bug:http://code.google.com/p/android/issues/detail?id=13866Change-Id:I5c5075eb6f529d1534c7aa72b6881873cd08676a*/




//Synthetic comment -- diff --git a/core/java/android/os/Message.java b/core/java/android/os/Message.java
//Synthetic comment -- index 49b72fe..0b5dbdd 100644

//Synthetic comment -- @@ -85,9 +85,9 @@
// sometimes we store linked lists of these things
/*package*/ Message next;

    private static Object sPoolSync = new Object();
    private static Message sPool;
    private static int sPoolSize = 0;

private static final int MAX_POOL_SIZE = 10;

//Synthetic comment -- @@ -96,11 +96,12 @@
* avoid allocating new objects in many cases.
*/
public static Message obtain() {
        synchronized (sPoolSync) {
            if (sPool != null) {
                Message m = sPool;
                sPool = m.next;
m.next = null;
                sPoolSize--;
return m;
}
}
//Synthetic comment -- @@ -237,12 +238,12 @@
* freed.
*/
public void recycle() {
        synchronized (sPoolSync) {
            if (sPoolSize < MAX_POOL_SIZE) {
clearForRecycle();
                next = sPool;
                sPool = this;
                sPoolSize++;
}
}
}
//Synthetic comment -- @@ -453,4 +454,3 @@
replyTo = Messenger.readMessengerOrNullFromParcel(source);
}
}








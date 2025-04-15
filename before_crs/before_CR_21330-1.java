/*Using proper key for removing from map.

Window object was improperly used as a key for removing record from
map. This was silenly ignored, because remove() method accepts Object.
However, the ID should be used there which is type of String, i.e.
the same ID which was used for lookup.

Change-Id:I81fc2f90926a34414bf9042ddf4a2edff4c1fda1*/
//Synthetic comment -- diff --git a/core/java/android/app/LocalActivityManager.java b/core/java/android/app/LocalActivityManager.java
//Synthetic comment -- index a24fcae..7845e03 100644

//Synthetic comment -- @@ -380,7 +380,7 @@
if (r != null) {
win = performDestroy(r, finish);
if (finish) {
                mActivities.remove(r);
}
}
return win;








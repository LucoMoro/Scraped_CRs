/*Don't allow invalid Uris to be added as observers.

If a null segment is added, it will cause problems traversing the list
at a later point.

Change-Id:I5aa97b969cac7231e214168af7d3263b1c16f0a0*/




//Synthetic comment -- diff --git a/core/java/android/content/ContentService.java b/core/java/android/content/ContentService.java
//Synthetic comment -- index 974a667..89e504e 100644

//Synthetic comment -- @@ -496,6 +496,9 @@

// Look to see if the proper child already exists
String segment = getUriSegment(uri, index);
            if (segment == null) {
                throw new IllegalArgumentException("Invalid Uri (" + uri + ") used for observer");
            }
int N = mChildren.size();
for (int i = 0; i < N; i++) {
ObserverNode node = mChildren.get(i);








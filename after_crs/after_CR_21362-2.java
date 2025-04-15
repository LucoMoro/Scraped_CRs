/*Using robust equality check.

Change-Id:Ie30684c472bfa38d0432f855f7075c34709958d5*/




//Synthetic comment -- diff --git a/core/java/android/text/Layout.java b/core/java/android/text/Layout.java
//Synthetic comment -- index 4e197cd..b62aa40 100755

//Synthetic comment -- @@ -1154,7 +1154,7 @@
if (h2 < 0.5f)
h2 = 0.5f;

        if (Float.compare(h1, h2) == 0) {
dest.moveTo(h1, top);
dest.lineTo(h1, bottom);
} else {








/*Can't delete Thai tone mark while writing message

Bug: 7481533

Signed-off-by: Seungju Choi <seungju24.choi@lge.com>

Change-Id:Ibb25fd7032837c0895d6ec691bc6d97d5fa5162f*/
//Synthetic comment -- diff --git a/core/java/android/text/Layout.java b/core/java/android/text/Layout.java
//Synthetic comment -- index 123acca..8b00cc8 100644

//Synthetic comment -- @@ -1094,7 +1094,7 @@

float dist = Math.abs(getPrimaryHorizontal(max) - horiz);

        if (dist < bestdist) {
bestdist = dist;
best = max;
}








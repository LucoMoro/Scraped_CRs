/*Return CONFIG_SMALLEST_SCREEN_SIZE from Configuration.updateFrom()

To be symmetric with Configuration.diff(), return
ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE from
Configuration.updateFrom() if smallestScreenWidthDp was set.

Change-Id:I7faf8106d7453c18fde5a6743e1b8896955fa459*/




//Synthetic comment -- diff --git a/core/java/android/content/res/Configuration.java b/core/java/android/content/res/Configuration.java
//Synthetic comment -- index 423b9af..0e30d0e 100644

//Synthetic comment -- @@ -707,6 +707,7 @@
screenHeightDp = delta.screenHeightDp;
}
if (delta.smallestScreenWidthDp != SMALLEST_SCREEN_WIDTH_DP_UNDEFINED) {
            changed |= ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE;
smallestScreenWidthDp = delta.smallestScreenWidthDp;
}
if (delta.compatScreenWidthDp != SCREEN_WIDTH_DP_UNDEFINED) {








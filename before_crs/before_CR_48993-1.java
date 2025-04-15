/*Correcting typo in doc of MEDIA_XXX intents

Correcting typo in documents of MEDIA_MOUNTED,
MEDIA_UNMOUNTED,MEDIA_UNMOUNTABLE intents, which
may be confusing to APP developers.

Change-Id:Id909b7bfad98e305b8054978f054f4fb9b705311*/
//Synthetic comment -- diff --git a/core/java/android/content/Intent.java b/core/java/android/content/Intent.java
//Synthetic comment -- index cf0603e..89b1bbd 100644

//Synthetic comment -- @@ -1950,7 +1950,7 @@

/**
* Broadcast Action:  External media is present, but not mounted at its mount point.
     * The path to the mount point for the removed media is contained in the Intent.mData field.
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
public static final String ACTION_MEDIA_UNMOUNTED = "android.intent.action.MEDIA_UNMOUNTED";
//Synthetic comment -- @@ -1971,7 +1971,7 @@

/**
* Broadcast Action:  External media is present and mounted at its mount point.
     * The path to the mount point for the removed media is contained in the Intent.mData field.
* The Intent contains an extra with name "read-only" and Boolean value to indicate if the
* media was mounted read only.
*/
//Synthetic comment -- @@ -2002,7 +2002,7 @@

/**
* Broadcast Action:  External media is present but cannot be mounted.
     * The path to the mount point for the removed media is contained in the Intent.mData field.
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
public static final String ACTION_MEDIA_UNMOUNTABLE = "android.intent.action.MEDIA_UNMOUNTABLE";








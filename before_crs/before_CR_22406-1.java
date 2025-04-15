/*Context cannot be null here, code relies on that everywhere.

Change-Id:I285b8697e7ed04eebeea481aafba55981ec8c952*/
//Synthetic comment -- diff --git a/core/java/android/view/View.java b/core/java/android/view/View.java
//Synthetic comment -- index 232dd6a..71032b3 100644

//Synthetic comment -- @@ -1868,10 +1868,15 @@
*
* @param context The Context the view is running in, through which it can
*        access the current theme, resources, etc.
*/
public View(Context context) {
mContext = context;
        mResources = context != null ? context.getResources() : null;
mViewFlags = SOUND_EFFECTS_ENABLED | HAPTIC_FEEDBACK_ENABLED;
// Used for debug only
//++sInstanceCount;








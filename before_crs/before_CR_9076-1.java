/*Add RINGER_ON & RINGER_OFF keyCodes.*/
//Synthetic comment -- diff --git a/core/java/android/view/KeyEvent.java b/core/java/android/view/KeyEvent.java
//Synthetic comment -- index 1575aad..39c4f70 100644

//Synthetic comment -- @@ -117,7 +117,9 @@
public static final int KEYCODE_PREVIOUSSONG    = 88;
public static final int KEYCODE_REWIND          = 89;
public static final int KEYCODE_FORWARD         = 90;
    private static final int LAST_KEYCODE           = KEYCODE_FORWARD;

// NOTE: If you add a new keycode here you must also add it to:
//  isSystem()
//Synthetic comment -- @@ -483,6 +485,8 @@
case KEYCODE_CAMERA:
case KEYCODE_FOCUS:
case KEYCODE_SEARCH:
return true;
default:
return false;








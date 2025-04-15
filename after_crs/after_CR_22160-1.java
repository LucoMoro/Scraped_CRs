/*To prevent the reference to null pointer

If Keycode value is KeyEvent.KEYCODE_BACK and mDecor is null, then 'dispatcher.startTracking' will occur Exception.

Change-Id:I050c68ce5f3bda5f157f3f5a7544a2f53ea0e239*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/PhoneWindow.java b/policy/src/com/android/internal/policy/impl/PhoneWindow.java
old mode 100644
new mode 100755
//Synthetic comment -- index dffccf8..461525b

//Synthetic comment -- @@ -1215,6 +1215,7 @@
if (event.getRepeatCount() > 0) break;
if (featureId < 0) break;
// Currently don't do anything with long press.
                if (dispatcher == null) break;
dispatcher.startTracking(event, this);
return true;
}








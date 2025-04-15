/*Added missing media keycode (KEYCODE_MEDIA_REWIND), removed duplicate keycode (KEYCODE_MEDIA_PREVIOUS)*/
//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindowManager.java b/phone/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index e57fbe8..5a94165 100755

//Synthetic comment -- @@ -1608,7 +1608,7 @@
code == KeyEvent.KEYCODE_MEDIA_STOP || 
code == KeyEvent.KEYCODE_MEDIA_NEXT ||
code == KeyEvent.KEYCODE_MEDIA_PREVIOUS || 
                code == KeyEvent.KEYCODE_MEDIA_PREVIOUS ||
code == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
return true;
}








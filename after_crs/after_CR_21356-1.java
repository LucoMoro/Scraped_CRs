/*Just adding TODO; someone should review this. Seems lika an anti-pattern in this class.

Change-Id:I79ed734da833112a239a9bf92d43178e20687dd7*/




//Synthetic comment -- diff --git a/media/java/android/media/audiofx/AudioEffect.java b/media/java/android/media/audiofx/AudioEffect.java
//Synthetic comment -- index 615a57f..f847260 100644

//Synthetic comment -- @@ -591,7 +591,7 @@
if (value.length > vSize[0]) {
byte[] resizedValue = new byte[vSize[0]];
System.arraycopy(value, 0, resizedValue, 0, vSize[0]);
            value = resizedValue; // TODO: THIS IS LIKELY A BUG - IT DOES NOT WORK LIKE INTENDED. IN REALITY, THIS HAS _NO_ EFFECT.
}
return status;
}








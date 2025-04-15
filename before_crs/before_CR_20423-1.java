/*Allow also chars in parsing keycode in Keyboard:parseCSV

Change-Id:I125c131833b7af9b878f5f1ff1292bfa0af1c5be*/
//Synthetic comment -- diff --git a/core/java/android/inputmethodservice/Keyboard.java b/core/java/android/inputmethodservice/Keyboard.java
//Synthetic comment -- index 269e1c9..f140f27 100644

//Synthetic comment -- @@ -422,7 +422,13 @@
StringTokenizer st = new StringTokenizer(value, ",");
while (st.hasMoreTokens()) {
try {
                    values[count++] = Integer.parseInt(st.nextToken());
} catch (NumberFormatException nfe) {
Log.e(TAG, "Error parsing keycodes " + value);
}








/*Removing unnecessary computation.

Change-Id:Id201e56fa60baa64204cd204169d119f7b4db3d7*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccUtils.java b/telephony/java/com/android/internal/telephony/IccUtils.java
//Synthetic comment -- index 5bd7523..2244ac4 100644

//Synthetic comment -- @@ -446,7 +446,6 @@
int colorNumber = data[valueIndex++] & 0xFF;
int clutOffset = ((data[valueIndex++] & 0xFF) << 8)
| (data[valueIndex++] & 0xFF);

int[] colorIndexArray = getCLUT(data, clutOffset, colorNumber);
if (true == transparency) {








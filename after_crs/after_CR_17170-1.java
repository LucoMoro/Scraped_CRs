/*Fixes byte overflow problem, when the size of a line of the cache is greater than 127

Change-Id:Ie6ce7867b87930158c2d3d437114ab730664b5a1*/




//Synthetic comment -- diff --git a/src/com/android/settings/ManageApplications.java b/src/com/android/settings/ManageApplications.java
//Synthetic comment -- index dfff8c9..48fcf7a 100644

//Synthetic comment -- @@ -1780,7 +1780,10 @@
mAppPropCache.clear();
while(fis.available() > 0) {
fis.read(lenBytes, 0, 2);
                    // byte is signed, storing in ints before the logical or
                    int buffLen0 = lenBytes[0] & 0x00ff;
                    int buffLen1 = lenBytes[1] & 0x00ff;
                    int buffLen = (buffLen0 << 8) | buffLen1;
if ((buffLen <= 0) || (buffLen > byteBuff.length)) {
err = true;
break;








/*Fixes byte overflow problem, when the size of a line of the cache is greater than 127

Change-Id:Ie6ce7867b87930158c2d3d437114ab730664b5a1*/
//Synthetic comment -- diff --git a/src/com/android/settings/ManageApplications.java b/src/com/android/settings/ManageApplications.java
//Synthetic comment -- index dfff8c9..48fcf7a 100644

//Synthetic comment -- @@ -1780,7 +1780,10 @@
mAppPropCache.clear();
while(fis.available() > 0) {
fis.read(lenBytes, 0, 2);
                    int buffLen = (lenBytes[0] << 8) | lenBytes[1];
if ((buffLen <= 0) || (buffLen > byteBuff.length)) {
err = true;
break;








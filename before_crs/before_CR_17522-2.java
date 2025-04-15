/*Omitting Big Endian caused corrupt characters

When sending a terminal reponse to SIM containing unicode
characters, big endian must be explicitly stated to
avoid corrupt characters.

Change-Id:I69a10a380247be7b511d6b9d37b7f222da4e6992*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/ResponseData.java b/telephony/java/com/android/internal/telephony/cat/ResponseData.java
//Synthetic comment -- index 677d66b..567436d 100644

//Synthetic comment -- @@ -106,7 +106,7 @@
} else if (mInData != null && mInData.length() > 0) {
try {
if (mIsUcs2) {
                    data = mInData.getBytes("UTF-16");
} else if (mIsPacked) {
int size = mInData.length();









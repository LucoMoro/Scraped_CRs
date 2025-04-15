/*Omitting Big Endian caused corrupt characters

When sending a terminal reponse to SIM containing unicode
characters, big endian must be explicitly stated to
avoid corrupt characters.

Change-Id:Iac4dcadfd860b5de15ad2a65c4ccdf3fae9292d4*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/ResponseData.java b/src/java/com/android/internal/telephony/cat/ResponseData.java
//Synthetic comment -- index 1157c1a..bdfcf09 100644

//Synthetic comment -- @@ -112,7 +112,7 @@
} else if (mInData != null && mInData.length() > 0) {
try {
if (mIsUcs2) {
                    data = mInData.getBytes("UTF-16");
} else if (mIsPacked) {
int size = mInData.length();









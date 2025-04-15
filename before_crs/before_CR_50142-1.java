/*HarmonyJSSE: convert byte correctly in padding check

This gives a better error message since the byte could be negative
without the mask.

Change-Id:Ifbba9fdf647b7ecf8bc300fb1034011ba8357401*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ConnectionStateSSLv3.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ConnectionStateSSLv3.java
//Synthetic comment -- index 00705cc..99a67af 100644

//Synthetic comment -- @@ -294,7 +294,7 @@
byte[] content;
if (block_size != 0) {
// check padding
            int padding_length = data[data.length-1];
for (int i=0; i<padding_length; i++) {
if (data[data.length-2-i] != padding_length) {
throw new AlertException(








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ConnectionStateTLS.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ConnectionStateTLS.java
//Synthetic comment -- index 4e05192..08cfd03 100644

//Synthetic comment -- @@ -304,7 +304,7 @@
byte[] content;
if (block_size != 0) {
// check padding
            int padding_length = data[data.length-1];
for (int i=0; i<padding_length; i++) {
if (data[data.length-2-i] != padding_length) {
throw new AlertException(








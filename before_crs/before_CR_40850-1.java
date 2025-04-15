/*Fix bug, parse error exception

Occur the parse error exception when received message has user data over 127 bytes in parsePduFromEfRecord()
so, read parameter- length using readUnsignedByte() , not using readByte()
Signed-off-by: kyunga kim <kyunga1.kim@lge.com>

Change-Id:Ib455b05575f68954ac516d75c5b040aac62f92cb*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/SmsMessage.java b/src/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index 6254a50..e2d4464 100644

//Synthetic comment -- @@ -539,7 +539,12 @@

while (dis.available() > 0) {
int parameterId = dis.readByte();
                int parameterLen = dis.readByte();
byte[] parameterData = new byte[parameterLen];

switch (parameterId) {








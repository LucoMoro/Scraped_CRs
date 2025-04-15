/*Parameters length is able to be over 127 bytes.

When the received message has user data over 127 bytes,
occur the parse error exception because using readByte().
Therefore we use readUnsignedByte() for reading parameters length.
Signed-off-by: kyunga kim <kyunga1.kim@lge.com>
Change-Id:I195807ee59a0d9117e0d835a4b3c355635337be0*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/SmsMessage.java b/src/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index 6254a50..a90c90c 100644

//Synthetic comment -- @@ -539,7 +539,7 @@

while (dis.available() > 0) {
int parameterId = dis.readByte();
                int parameterLen = dis.readByte();
byte[] parameterData = new byte[parameterLen];

switch (parameterId) {








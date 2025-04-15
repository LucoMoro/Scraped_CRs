/*Parameters length is able to be over 127 bytes.

When the received message has user data over 127 bytes,
occur the parse error exception because using readByte().
Therefore we use readUnsignedByte() for reading parameters length.
Signed-off-by: kyunga kim <kyunga1.kim@lge.com>
Change-Id:I195807ee59a0d9117e0d835a4b3c355635337be0(cherry picked from commit 700e7df9a0ac480e688d37786c0c146d5d1e406d)*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/SmsMessage.java b/src/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index ec155a2..4777cbb 100644

//Synthetic comment -- @@ -488,7 +488,7 @@
private void parsePdu(byte[] pdu) {
ByteArrayInputStream bais = new ByteArrayInputStream(pdu);
DataInputStream dis = new DataInputStream(bais);
        byte length;
int bearerDataLength;
SmsEnvelope env = new SmsEnvelope();
CdmaSmsAddress addr = new CdmaSmsAddress();
//Synthetic comment -- @@ -503,7 +503,7 @@
addr.ton = dis.readByte();
addr.numberPlan = dis.readByte();

            length = dis.readByte();
addr.numberOfDigits = length;
addr.origBytes = new byte[length];
dis.read(addr.origBytes, 0, length); // digits
//Synthetic comment -- @@ -547,7 +547,7 @@

while (dis.available() > 0) {
int parameterId = dis.readByte();
                int parameterLen = dis.readByte();
byte[] parameterData = new byte[parameterLen];

switch (parameterId) {








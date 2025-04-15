/*Parameter's length of message is over 127 bytes.

Occur the parse error exception when received message has user data over 127 bytes in parsePduFromEfRecord()
so, read parameter-length using readUnsignedByte() , not using readByte()
Signed-off-by: kyunga kim <kyunga1.kim@lge.com>

Change-Id:I195807ee59a0d9117e0d835a4b3c355635337be0*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/SmsMessage.java b/src/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index 6254a50..e2d4464 100644

//Synthetic comment -- @@ -539,7 +539,12 @@

while (dis.available() > 0) {
int parameterId = dis.readByte();
				 /* Received Message's Length can be over 127 bytes (ex.UserData)
 				  * So, read parameter Length used readUnsignedByte() 
  			      */
					int parameterLen =dis.readUnsignedByte();
					/* originate code
					int parameterLen = dis.readByte(); */
byte[] parameterData = new byte[parameterLen];

switch (parameterId) {








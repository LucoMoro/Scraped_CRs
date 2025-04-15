/*parse custom text header and ignore it.
Some MMS carrier append own custom header as text into PduData. We should parse it and ignore it at the moment.

Change-Id:Ie3b9133746d6766a502223ba44a5d41744a2f33b*/
//Synthetic comment -- diff --git a/core/java/com/google/android/mms/pdu/PduParser.java b/core/java/com/google/android/mms/pdu/PduParser.java
//Synthetic comment -- index d465c5a..0c094fe 100644

//Synthetic comment -- @@ -200,7 +200,19 @@
PduHeaders headers = new PduHeaders();

while (keepParsing && (pduDataStream.available() > 0)) {
int headerField = extractByteValue(pduDataStream);
switch (headerField) {
case PduHeaders.MESSAGE_TYPE:
{








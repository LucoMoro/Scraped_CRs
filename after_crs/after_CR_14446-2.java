/*Parse custom text header and ignore it.

Some MMS carrier append own custom header as text into PduData. We should parse it and ignore it at the moment.

Change-Id:I4d6cf20f5cf99172ebbe310ab18101316eb04c77*/




//Synthetic comment -- diff --git a/core/java/com/google/android/mms/pdu/PduParser.java b/core/java/com/google/android/mms/pdu/PduParser.java
//Synthetic comment -- index 60b35cf..1cd118b 100644

//Synthetic comment -- @@ -200,7 +200,18 @@
PduHeaders headers = new PduHeaders();

while (keepParsing && (pduDataStream.available() > 0)) {
            pduDataStream.mark(1);
int headerField = extractByteValue(pduDataStream);
            /* parse custom text header */
            if ((headerField >= TEXT_MIN) && (headerField <= TEXT_MAX)) {
                pduDataStream.reset();
                byte [] bVal = parseWapString(pduDataStream, TYPE_TEXT_STRING);
                if (LOCAL_LOGV) {
                    Log.v(LOG_TAG, "TextHeader: " + new String(bVal));
                }
                /* we should ignore it at the moment */
                continue;
            }
switch (headerField) {
case PduHeaders.MESSAGE_TYPE:
{








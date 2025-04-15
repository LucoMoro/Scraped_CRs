/*Should accept "application/vnd.wap.multipart.alternative" message.

Change-Id:Ic93259c91331d1e67100439114b7c4f43a834368*/
//Synthetic comment -- diff --git a/core/java/com/google/android/mms/pdu/PduParser.java b/core/java/com/google/android/mms/pdu/PduParser.java
//Synthetic comment -- index 1cd118b..92d5cc4 100644

//Synthetic comment -- @@ -161,6 +161,13 @@
// The MMS content type must be "application/vnd.wap.multipart.mixed"
// or "application/vnd.wap.multipart.related"
return retrieveConf;
}
return null;
case PduHeaders.MESSAGE_TYPE_DELIVERY_IND:








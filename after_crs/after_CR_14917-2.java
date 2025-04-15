/*Should accept "application/vnd.wap.multipart.alternative" message.

Unfortunately, PduParser does not allow application/vnd.wap.multipart.alternative as M-RETRIEVE.CONF but some Mobile carrier send message with this type.
This patch allows that and take the first part of multipart as message body.

It fixeshttp://code.google.com/p/android/issues/detail?id=8957Change-Id:Ic93259c91331d1e67100439114b7c4f43a834368*/




//Synthetic comment -- diff --git a/core/java/com/google/android/mms/pdu/PduParser.java b/core/java/com/google/android/mms/pdu/PduParser.java
//Synthetic comment -- index 1cd118b..92d5cc4 100644

//Synthetic comment -- @@ -161,6 +161,13 @@
// The MMS content type must be "application/vnd.wap.multipart.mixed"
// or "application/vnd.wap.multipart.related"
return retrieveConf;
                } else if (ctTypeStr.equals(ContentType.MULTIPART_ALTERNATIVE)) {
                    // "application/vnd.wap.multipart.alternative"
                    // should take only the first part.
                    PduPart firstPart = mBody.getPart(0);
                    mBody.removeAll();
                    mBody.addPart(0, firstPart);
                    return retrieveConf;
}
return null;
case PduHeaders.MESSAGE_TYPE_DELIVERY_IND:








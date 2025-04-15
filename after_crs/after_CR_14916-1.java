/*Should accept application/vnd.wap.multipart.alternative message.

Change-Id:Iec27b331c3f9a50a9e288207db63b66414a66756*/




//Synthetic comment -- diff --git a/core/java/com/google/android/mms/pdu/PduParser.java b/core/java/com/google/android/mms/pdu/PduParser.java
//Synthetic comment -- index 1cd118b..d1306f4 100644

//Synthetic comment -- @@ -161,7 +161,14 @@
// The MMS content type must be "application/vnd.wap.multipart.mixed"
// or "application/vnd.wap.multipart.related"
return retrieveConf;
                } else if (ctTypeStr.equals(ContentType.MULTIPART_ALTERNATIVE)) {
		    PduPart firstPart = mBody.getPart(0);
		    mBody.removeAll();
		    mBody.addPart(0, firstPart);
		    mHeaders.setTextString(firstPart.getContentType(),
					   PduHeaders.CONTENT_TYPE);
		    return retrieveConf;
		}
return null;
case PduHeaders.MESSAGE_TYPE_DELIVERY_IND:
DeliveryInd deliveryInd =








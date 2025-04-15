/*Return error code when composing PDU fails

When the PduComposer fails to make the message body,
a successful result code was still returned.*/




//Synthetic comment -- diff --git a/core/java/com/google/android/mms/pdu/PduComposer.java b/core/java/com/google/android/mms/pdu/PduComposer.java
//Synthetic comment -- index 8940945..d426f89 100644

//Synthetic comment -- @@ -835,9 +835,7 @@
appendOctet(PduHeaders.CONTENT_TYPE);

//  Message body
        return makeMessageBody();
}

/**








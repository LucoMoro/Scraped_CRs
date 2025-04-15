/*Fix the delivery report error

Although TP-status is PENDING or FAILED, delivery report is displayed.
Only in case of SUCCESS, it should be displayed to users.

Change-Id:I00eeed61f8797ff1d468c0bf23a7f3431ed02faeSigned-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/model/ImageModel.java b/src/com/android/mms/model/ImageModel.java
//Synthetic comment -- index d241de9..a751643 100644

//Synthetic comment -- @@ -212,6 +212,14 @@
throw new ExceedMessageSizeException("Not enough memory to turn image into part: " +
getUri());
}
PduPersister persister = PduPersister.getPduPersister(mContext);
this.mSize = part.getData().length;
Uri newUri = persister.persistPart(part, messageId);








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessageStatusReceiver.java b/src/com/android/mms/transaction/MessageStatusReceiver.java
//Synthetic comment -- index 73a4b18..3431fd7 100644

//Synthetic comment -- @@ -51,8 +51,10 @@
boolean isStatusMessage = updateMessageStatus(context, messageUri, pdu);

// Called on the UI thread so don't block.
            MessagingNotification.nonBlockingUpdateNewMessageIndicator(context,
                    true, isStatusMessage);
}
}









//Synthetic comment -- diff --git a/src/com/android/mms/ui/UriImage.java b/src/com/android/mms/ui/UriImage.java
//Synthetic comment -- index fae3acf..3f391a4 100644

//Synthetic comment -- @@ -67,6 +67,10 @@

mSrc = mPath.substring(mPath.lastIndexOf('/') + 1);

// Some MMSCs appear to have problems with filenames
// containing a space.  So just replace them with
// underscores in the name, which is typically not
//Synthetic comment -- @@ -185,13 +189,6 @@

part.setData(data);
part.setContentType(getContentType().getBytes());
        String src = getSrc();
        byte[] srcBytes = src.getBytes();
        part.setContentLocation(srcBytes);
        part.setFilename(srcBytes);
        int period = src.lastIndexOf(".");
        byte[] contentId = period != -1 ? src.substring(0, period).getBytes() : srcBytes;
        part.setContentId(contentId);

return part;
}








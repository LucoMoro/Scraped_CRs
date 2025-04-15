/*Initialize Timestamp to "" instead of NULL

The expired time shows NULL before PduLoadedMessageItemCallback
was executed.

It sometimes canbe reproduced in the expired time of Notification
IND.

Change-Id:Idac021d23e0807f61a5bcead8af1b369398dff26Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageItem.java b/src/com/android/mms/ui/MessageItem.java
//Synthetic comment -- index d21014b..1a5d0a8 100644

//Synthetic comment -- @@ -178,7 +178,8 @@
mBody = null;
mMessageSize = 0;
mTextContentType = null;
            // Initialize the time stamp to "" instead of null
            mTimestamp = "";
mMmsStatus = cursor.getInt(columnsMap.mColumnMmsStatus);

// Start an async load of the pdu. If the pdu is already loaded, the callback








/*Mms app: Show pending/failed/success icon in MessageList.*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 48858ce..79df5ab 100644

//Synthetic comment -- @@ -1138,7 +1138,7 @@
.setOnMenuItemClickListener(l);
menu.add(0, MENU_DELETE_MESSAGE, 0, R.string.delete_message)
.setOnMenuItemClickListener(l);
            if (msgItem.mDeliveryStatus != MessageItem.DeliveryStatus.NONE || msgItem.mReadReport) {
menu.add(0, MENU_DELIVERY_REPORT, 0, R.string.view_delivery_report)
.setOnMenuItemClickListener(l);
}








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageItem.java b/src/com/android/mms/ui/MessageItem.java
//Synthetic comment -- index 07bfffe..86f4ba7 100644

//Synthetic comment -- @@ -51,12 +51,14 @@
public class MessageItem {
private static String TAG = "MessageItem";

    public enum DeliveryStatus  { NONE, INFO, FAILED, PENDING, RECEIVED }

final Context mContext;
final String mType;
final long mMsgId;
final int mBoxId;

    DeliveryStatus mDeliveryStatus;
boolean mReadReport;

String mTimestamp;
//Synthetic comment -- @@ -86,12 +88,26 @@
mContext = context;
mThreadType = threadType;
mMsgId = cursor.getLong(columnsMap.mColumnMsgId);

if ("sms".equals(type)) {
ContactInfoCache infoCache = ContactInfoCache.getInstance();
mReadReport = false; // No read reports in sms

            long status = cursor.getLong(columnsMap.mColumnSmsStatus);
            if (status == Sms.STATUS_NONE) {
                // No delivery report requested
                mDeliveryStatus = DeliveryStatus.NONE;
            } else if (status >= Sms.STATUS_FAILED) {
                // Failure
                mDeliveryStatus = DeliveryStatus.FAILED;
            } else if (status >= Sms.STATUS_PENDING) {
                // Pending
                mDeliveryStatus = DeliveryStatus.PENDING;
            } else {
                // Success
                mDeliveryStatus = DeliveryStatus.RECEIVED;
            }

mMessageUri = ContentUris.withAppendedId(Sms.CONTENT_URI, mMsgId);
// Set contact and message body
mBoxId = cursor.getInt(columnsMap.mColumnSmsType);
//Synthetic comment -- @@ -134,7 +150,7 @@
long timestamp = 0L;
PduPersister p = PduPersister.getPduPersister(mContext);
if (PduHeaders.MESSAGE_TYPE_NOTIFICATION_IND == mMessageType) {
                mDeliveryStatus = DeliveryStatus.NONE;
NotificationInd notifInd = (NotificationInd) p.load(mMessageUri);
interpretFrom(notifInd.getFrom());
// Borrow the mBody to hold the URL of the message.
//Synthetic comment -- @@ -161,16 +177,19 @@
String report = cursor.getString(columnsMap.mColumnMmsDeliveryReport);
if ((report == null) || !mAddress.equals(context.getString(
R.string.messagelist_sender_self))) {
                    mDeliveryStatus = DeliveryStatus.NONE;
} else {
int reportInt;
try {
reportInt = Integer.parseInt(report);
                        if (reportInt == PduHeaders.VALUE_YES) {
                            mDeliveryStatus = DeliveryStatus.INFO;
                        } else {
                            mDeliveryStatus = DeliveryStatus.NONE;
                        }
} catch (NumberFormatException nfe) {
Log.e(TAG, "Value for delivery report was invalid.");
                        mDeliveryStatus = DeliveryStatus.NONE;
}
}









//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageListItem.java b/src/com/android/mms/ui/MessageListItem.java
//Synthetic comment -- index bc45ae6..8204814 100644

//Synthetic comment -- @@ -492,9 +492,18 @@
mRightStatusIndicator.setImageResource(R.drawable.ic_email_pending);
}
mRightStatusIndicator.setVisibility(View.VISIBLE);
        } else if (msgItem.mDeliveryStatus == MessageItem.DeliveryStatus.INFO || msgItem.mReadReport) {
mRightStatusIndicator.setImageResource(R.drawable.ic_mms_message_details);
mRightStatusIndicator.setVisibility(View.VISIBLE);
        } else if (msgItem.mDeliveryStatus == MessageItem.DeliveryStatus.PENDING) {
            mRightStatusIndicator.setImageResource(R.drawable.ic_sms_mms_pending);
            mRightStatusIndicator.setVisibility(View.VISIBLE);
        } else if (msgItem.mDeliveryStatus == MessageItem.DeliveryStatus.FAILED) {
            mRightStatusIndicator.setImageResource(R.drawable.ic_sms_mms_not_delivered);
            mRightStatusIndicator.setVisibility(View.VISIBLE);
        } else if (msgItem.mDeliveryStatus == MessageItem.DeliveryStatus.RECEIVED) {
            mRightStatusIndicator.setImageResource(R.drawable.ic_sms_mms_delivered);
            mRightStatusIndicator.setVisibility(View.VISIBLE);
} else {
mRightStatusIndicator.setVisibility(View.GONE);
}








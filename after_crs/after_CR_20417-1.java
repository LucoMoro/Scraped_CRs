/*Mms: SMS support for Multi SIM functionality.

Extended Mms app to send and receive SMS on multiple subscriptions.

Change-Id:I873cd06f9f336f1a22a72111b6dadd281d0fb1f9*/




//Synthetic comment -- diff --git a/src/com/android/mms/data/WorkingMessage.java b/src/com/android/mms/data/WorkingMessage.java
//Synthetic comment -- index 7e84e74..4be0d82 100644

//Synthetic comment -- @@ -62,6 +62,9 @@
import com.google.android.mms.pdu.PduPersister;
import com.google.android.mms.pdu.SendReq;

import android.telephony.TelephonyManager;
import android.telephony.SmsManager;

/**
* Contains all state related to a message being edited by the user.
*/
//Synthetic comment -- @@ -1095,7 +1098,11 @@
if (Log.isLoggable(LogTag.TRANSACTION, Log.VERBOSE)) {
LogTag.debug("sendSmsWorker sending message");
}
        MessageSender sender;

        sender = new SmsMessageSender(mActivity, dests, msgText, threadId,
               SmsManager.getDefault().getPreferredSmsSubscription());

try {
sender.sendMessage(threadId);

//Synthetic comment -- @@ -1173,6 +1180,10 @@
return;
}

        ContentValues values = new ContentValues(1);
        values.put(Mms.SUB_ID, TelephonyManager.getDefault().getPreferredDataSubscription());
        SqliteWrapper.update(mActivity, mContentResolver, mmsUri, values, null, null);

MessageSender sender = new MmsMessageSender(mActivity, mmsUri,
slideshow.getCurrentMessageSize());
try {








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessagingNotification.java b/src/com/android/mms/transaction/MessagingNotification.java
//Synthetic comment -- index 4dd4558..619286f 100644

//Synthetic comment -- @@ -52,6 +52,7 @@
import android.preference.PreferenceManager;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Sms;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
//Synthetic comment -- @@ -79,11 +80,11 @@

// This must be consistent with the column constants below.
private static final String[] MMS_STATUS_PROJECTION = new String[] {
        Mms.THREAD_ID, Mms.DATE, Mms._ID, Mms.SUBJECT, Mms.SUBJECT_CHARSET, Mms.SUB_ID };

// This must be consistent with the column constants below.
private static final String[] SMS_STATUS_PROJECTION = new String[] {
        Sms.THREAD_ID, Sms.DATE, Sms.ADDRESS, Sms.SUBJECT, Sms.BODY, Sms.SUB_ID };

// These must be consistent with MMS_STATUS_PROJECTION and
// SMS_STATUS_PROJECTION.
//Synthetic comment -- @@ -94,6 +95,7 @@
private static final int COLUMN_SUBJECT     = 3;
private static final int COLUMN_SUBJECT_CS  = 4;
private static final int COLUMN_SMS_BODY    = 4;
    private static final int COLUMN_SUB_ID      = 5;

private static final String NEW_INCOMING_SM_CONSTRAINT =
"(" + Sms.TYPE + " = " + Sms.MESSAGE_TYPE_INBOX
//Synthetic comment -- @@ -239,17 +241,19 @@
public String mDescription;
public int mIconResourceId;
public CharSequence mTicker;
        public int mSubId;
public long mTimeMillis;
public String mTitle;
public int mCount;

public MmsSmsNotificationInfo(
Intent clickIntent, String description, int iconResourceId,
                CharSequence ticker, int subId, long timeMillis, String title, int count) {
mClickIntent = clickIntent;
mDescription = description;
mIconResourceId = iconResourceId;
mTicker = ticker;
            mSubId = subId;
mTimeMillis = timeMillis;
mTitle = title;
mCount = count;
//Synthetic comment -- @@ -259,7 +263,7 @@
updateNotification(
context, mClickIntent, mDescription, mIconResourceId, isNew,
(isNew? mTicker : null), // only display the ticker if the message is new
                    mSubId, mTimeMillis, mTitle, count, uniqueThreads);
}

public long getTime() {
//Synthetic comment -- @@ -304,6 +308,7 @@
cursor.getString(COLUMN_SUBJECT), cursor.getInt(COLUMN_SUBJECT_CS));
long threadId = cursor.getLong(COLUMN_THREAD_ID);
long timeMillis = cursor.getLong(COLUMN_DATE) * 1000;
            int subId = cursor.getInt(COLUMN_SUB_ID);

if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) {
Log.d(TAG, "getMmsNewMessageNotificationInfo: count=" + cursor.getCount() +
//Synthetic comment -- @@ -313,7 +318,7 @@
MmsSmsNotificationInfo info = getNewMessageNotificationInfo(
address, subject, context,
R.drawable.stat_notify_mms, null, threadId,
                    subId, timeMillis, cursor.getCount());

threads.add(threadId);
while (cursor.moveToNext()) {
//Synthetic comment -- @@ -371,6 +376,7 @@
String body = cursor.getString(COLUMN_SMS_BODY);
long threadId = cursor.getLong(COLUMN_THREAD_ID);
long timeMillis = cursor.getLong(COLUMN_DATE);
            int subId = cursor.getInt(COLUMN_SUB_ID);

if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) 
{
//Synthetic comment -- @@ -380,7 +386,7 @@

MmsSmsNotificationInfo info = getNewMessageNotificationInfo(
address, body, context, R.drawable.stat_notify_sms,
                    null, threadId, subId, timeMillis, cursor.getCount());

threads.add(threadId);
while (cursor.moveToNext()) {
//Synthetic comment -- @@ -400,6 +406,7 @@
int iconResourceId,
String subject,
long threadId,
            int subId,
long timeMillis,
int count) {
Intent clickIntent = ComposeMessageActivity.createIntent(context, threadId);
//Synthetic comment -- @@ -408,14 +415,14 @@
| Intent.FLAG_ACTIVITY_CLEAR_TOP);

String senderInfo = buildTickerMessage(
                context, address, null, null, subId).toString();
String senderInfoName = senderInfo.substring(
                0, senderInfo.length());
CharSequence ticker = buildTickerMessage(
                context, address, subject, body, subId);

return new MmsSmsNotificationInfo(
                clickIntent, body, iconResourceId, ticker, subId, timeMillis,
senderInfoName, count);
}

//Synthetic comment -- @@ -454,6 +461,7 @@
int iconRes,
boolean isNew,
CharSequence ticker,
            int subId,
long timeMillis,
String title,
int messageCount,
//Synthetic comment -- @@ -472,6 +480,7 @@
// user to the conversation list instead of the specific thread.
if (uniqueThreadCount > 1) {
title = context.getString(R.string.notification_multiple_title);

clickIntent = new Intent(Intent.ACTION_MAIN);

clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//Synthetic comment -- @@ -539,7 +548,7 @@
}

protected static CharSequence buildTickerMessage(
            Context context, String address, String subject, String body, int subId) {
String displayAddress = Contact.get(address, true).getName();

StringBuilder buf = new StringBuilder(
//Synthetic comment -- @@ -548,6 +557,11 @@
: displayAddress.replace('\n', ' ').replace('\r', ' '));
buf.append(':').append(' ');

       if (TelephonyManager.isMultiSimEnabled()) {
            buf.append( (subId == 0) ? "Sub1" : "Sub2");
            buf.append("-");
       }

int offset = buf.length();
if (!TextUtils.isEmpty(subject)) {
subject = subject.replace('\n', ' ').replace('\r', ' ');








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/PushReceiver.java b/src/com/android/mms/transaction/PushReceiver.java
//Synthetic comment -- index 8b46cd8..fb68b91 100644

//Synthetic comment -- @@ -84,6 +84,12 @@
long threadId = -1;

try {
                int subId = intent.getIntExtra("sub_id", 0);
                ContentValues values = new ContentValues(1);
                values.put(Mms.SUB_ID, subId);
                Uri uri = p.persist(pdu, Inbox.CONTENT_URI);
                SqliteWrapper.update(mContext, cr, uri, values, null, null);

switch (type) {
case MESSAGE_TYPE_DELIVERY_IND:
case MESSAGE_TYPE_READ_ORIG_IND: {
//Synthetic comment -- @@ -94,9 +100,7 @@
break;
}

// Update thread ID for ReadOrigInd & DeliveryInd.
values.put(Mms.THREAD_ID, threadId);
SqliteWrapper.update(mContext, cr, uri, values, null, null);
break;
//Synthetic comment -- @@ -119,7 +123,6 @@
}

if (!isDuplicateNotification(mContext, nInd)) {
// Start service to finish the notification transaction.
Intent svc = new Intent(mContext, TransactionService.class);
svc.putExtra(TransactionBundle.URI, uri.toString());








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsMessageSender.java b/src/com/android/mms/transaction/SmsMessageSender.java
//Synthetic comment -- index 4c70485..0c55bee 100644

//Synthetic comment -- @@ -40,6 +40,7 @@
protected final String mServiceCenter;
protected final long mThreadId;
protected long mTimestamp;
    protected int mSubscription;

// Default preference values
private static final boolean DEFAULT_DELIVERY_REPORT_MODE  = false;
//Synthetic comment -- @@ -52,7 +53,8 @@
private static final int COLUMN_REPLY_PATH_PRESENT = 0;
private static final int COLUMN_SERVICE_CENTER     = 1;

    public SmsMessageSender(Context context, String[] dests,
                 String msgText, long threadId, int subscription) {
mContext = context;
mMessageText = msgText;
if (dests != null) {
//Synthetic comment -- @@ -66,11 +68,12 @@
mTimestamp = System.currentTimeMillis();
mThreadId = threadId;
mServiceCenter = getOutgoingServiceCenter(mThreadId);
        mSubscription = subscription;
}

public boolean sendMessage(long token) throws MmsException {
// In order to send the message one by one, instead of sending now, the message will split,
        // and be put into the queue along with each destinations
return queueMessage(token);
}

//Synthetic comment -- @@ -87,12 +90,13 @@

for (int i = 0; i < mNumberOfDests; i++) {
try {
                log("updating Database with sub = " + mSubscription);
                Sms.addMessageToUri(mContext.getContentResolver(),
Uri.parse("content://sms/queued"), mDests[i],
mMessageText, null, mTimestamp,
true /* read */,
requestDeliveryReport,
                        mThreadId, mSubscription);
} catch (SQLiteException e) {
SqliteWrapper.checkSQLiteException(mContext, e);
}








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsReceiverService.java b/src/com/android/mms/transaction/SmsReceiverService.java
//Synthetic comment -- index 4cc86cb..163b0d3 100755

//Synthetic comment -- @@ -67,6 +67,7 @@
*/
public class SmsReceiverService extends Service {
private static final String TAG = "SmsReceiverService";
    private final String SUBSCRIPTION = "SUBSCRIPTION";

private ServiceHandler mServiceHandler;
private Looper mServiceLooper;
//Synthetic comment -- @@ -188,7 +189,13 @@
private void handleServiceStateChanged(Intent intent) {
// If service just returned, start sending out the queued messages
ServiceState serviceState = ServiceState.newFromBundle(intent.getExtras());
        int subscription = intent.getIntExtra(SUBSCRIPTION, 0);
        int prefSubscription = SmsManager.getDefault().getPreferredSmsSubscription();
        // if service state is IN_SERVICE & current subscription is same as
        // preferred SMS subscription.i.e.as set under MultiSIM Settings,then
        // sendFirstQueuedMessage.
        if (serviceState.getState() == ServiceState.STATE_IN_SERVICE &&
            subscription == prefSubscription) {
sendFirstQueuedMessage();
}
}
//Synthetic comment -- @@ -218,10 +225,11 @@

int msgId = c.getInt(SEND_COLUMN_ID);
Uri msgUri = ContentUris.withAppendedId(Sms.CONTENT_URI, msgId);
                    SmsMessageSender sender;

                    sender = new SmsSingleRecipientSender(this,
address, msgText, threadId, status == Sms.STATUS_PENDING,
                            msgUri, SmsManager.getDefault().getPreferredSmsSubscription());

if (Log.isLoggable(LogTag.TRANSACTION, Log.VERBOSE)) {
Log.v(TAG, "sendFirstQueuedMessage " + msgUri +
//Synthetic comment -- @@ -397,12 +405,19 @@
ContentResolver resolver = context.getContentResolver();
String originatingAddress = sms.getOriginatingAddress();
int protocolIdentifier = sms.getProtocolIdentifier();
        String selection;
        String[] selectionArgs;

        if (Log.isLoggable(LogTag.TRANSACTION, Log.VERBOSE)) {
            Log.v(TAG, " SmsReceiverService: replaceMessage:");
        }
        selection = Sms.ADDRESS + " = ? AND " +
                    Sms.PROTOCOL + " = ? AND" +
                    Sms.SUB_ID +  " = ? ";
        selectionArgs = new String[] {
                originatingAddress, Integer.toString(protocolIdentifier),
                Integer.toString(sms.mSubscription)
            };

Cursor cursor = SqliteWrapper.query(context, resolver, Inbox.CONTENT_URI,
REPLACE_PROJECTION, selection, selectionArgs, null);
//Synthetic comment -- @@ -431,6 +446,8 @@
// Store the message in the content provider.
ContentValues values = extractContentValues(sms);
values.put(Sms.ERROR_CODE, error);
        values.put(Sms.SUB_ID, sms.mSubscription);

int pduCount = msgs.length;

if (pduCount == 1) {








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsSingleRecipientSender.java b/src/com/android/mms/transaction/SmsSingleRecipientSender.java
//Synthetic comment -- index c9f8c29..a6d5de2 100644

//Synthetic comment -- @@ -24,8 +24,8 @@
private Uri mUri;

public SmsSingleRecipientSender(Context context, String dest, String msgText, long threadId,
            boolean requestDeliveryReport, Uri uri, int subscription) {
        super(context, null, msgText, threadId, subscription);
mRequestDeliveryReport = requestDeliveryReport;
mDest = dest;
mUri = uri;
//Synthetic comment -- @@ -96,7 +96,8 @@
sentIntents.add(PendingIntent.getBroadcast(mContext, requestCode, intent, 0));
}
try {
            smsManager.sendMultipartTextMessage(mDest, mServiceCenter, messages, sentIntents,
                       deliveryIntents, mSubscription);
} catch (Exception ex) {
throw new MmsException("SmsMessageSender.sendMessage: caught " + ex +
" from SmsManager.sendTextMessage()");








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageItem.java b/src/com/android/mms/ui/MessageItem.java
//Synthetic comment -- index 996dd19..77cbbed 100644

//Synthetic comment -- @@ -69,6 +69,7 @@
String mAddress;
String mContact;
String mBody; // Body of SMS, first text of MMS.
    int mSubscription;   // Holds current mms/sms subscription value.
String mTextContentType; // ContentType of text of MMS.
Pattern mHighlight; // portion of message to highlight (from search)

//Synthetic comment -- @@ -134,6 +135,8 @@
}
mBody = cursor.getString(columnsMap.mColumnSmsBody);

            mSubscription = cursor.getInt(columnsMap.mColumnSubId);

if (!isOutgoingMessage()) {
// Set "sent" time stamp
long date = cursor.getLong(columnsMap.mColumnSmsDate);
//Synthetic comment -- @@ -149,6 +152,8 @@
mMessageType = cursor.getInt(columnsMap.mColumnMmsMessageType);
mErrorType = cursor.getInt(columnsMap.mColumnMmsErrorType);
String subject = cursor.getString(columnsMap.mColumnMmsSubject);
            mSubscription = cursor.getInt(columnsMap.mColumnSubId);

if (!TextUtils.isEmpty(subject)) {
EncodedStringValue v = new EncodedStringValue(
cursor.getInt(columnsMap.mColumnMmsSubjectCharset),








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageListAdapter.java b/src/com/android/mms/ui/MessageListAdapter.java
//Synthetic comment -- index bc2bea5..3fa5322 100644

//Synthetic comment -- @@ -76,6 +76,7 @@
// For SMS
Sms.ADDRESS,
Sms.BODY,
        Sms.SUB_ID,
Sms.DATE,
Sms.READ,
Sms.TYPE,
//Synthetic comment -- @@ -102,22 +103,23 @@
static final int COLUMN_THREAD_ID           = 2;
static final int COLUMN_SMS_ADDRESS         = 3;
static final int COLUMN_SMS_BODY            = 4;
    static final int COLUMN_SUB_ID              = 5;
    static final int COLUMN_SMS_DATE            = 6;
    static final int COLUMN_SMS_READ            = 7;
    static final int COLUMN_SMS_TYPE            = 8;
    static final int COLUMN_SMS_STATUS          = 9;
    static final int COLUMN_SMS_LOCKED          = 10;
    static final int COLUMN_SMS_ERROR_CODE      = 11;
    static final int COLUMN_MMS_SUBJECT         = 12;
    static final int COLUMN_MMS_SUBJECT_CHARSET = 13;
    static final int COLUMN_MMS_DATE            = 14;
    static final int COLUMN_MMS_READ            = 15;
    static final int COLUMN_MMS_MESSAGE_TYPE    = 16;
    static final int COLUMN_MMS_MESSAGE_BOX     = 17;
    static final int COLUMN_MMS_DELIVERY_REPORT = 18;
    static final int COLUMN_MMS_READ_REPORT     = 19;
    static final int COLUMN_MMS_ERROR_TYPE      = 20;
    static final int COLUMN_MMS_LOCKED          = 21;

private static final int CACHE_SIZE         = 50;

//Synthetic comment -- @@ -289,6 +291,7 @@
public int mColumnMsgId;
public int mColumnSmsAddress;
public int mColumnSmsBody;
        public int mColumnSubId;
public int mColumnSmsDate;
public int mColumnSmsRead;
public int mColumnSmsType;
//Synthetic comment -- @@ -311,6 +314,7 @@
mColumnMsgId              = COLUMN_ID;
mColumnSmsAddress         = COLUMN_SMS_ADDRESS;
mColumnSmsBody            = COLUMN_SMS_BODY;
            mColumnSubId              = COLUMN_SUB_ID;
mColumnSmsDate            = COLUMN_SMS_DATE;
mColumnSmsType            = COLUMN_SMS_TYPE;
mColumnSmsStatus          = COLUMN_SMS_STATUS;
//Synthetic comment -- @@ -355,6 +359,12 @@
}

try {
                mColumnSubId = cursor.getColumnIndexOrThrow(Sms.SUB_ID);
            } catch (IllegalArgumentException e) {
                Log.w("colsMap", e.getMessage());
            }

            try {
mColumnSmsDate = cursor.getColumnIndexOrThrow(Sms.DATE);
} catch (IllegalArgumentException e) {
Log.w("colsMap", e.getMessage());








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageListItem.java b/src/com/android/mms/ui/MessageListItem.java
//Synthetic comment -- index e849ca4..398dce2 100644

//Synthetic comment -- @@ -181,8 +181,8 @@
+ String.valueOf((msgItem.mMessageSize + 1023) / 1024)
+ mContext.getString(R.string.kilobyte);

        mBodyTextView.setText(formatMessage(msgItem, msgItem.mContact, null, msgItem.mSubscription,
                                            msgItem.mSubject, msgSizeText + "\n" + msgItem.mTimestamp,
msgItem.mHighlight, msgItem.mTextContentType));

int state = DownloadManager.getInstance().getState(msgItem.mMessageUri);
//Synthetic comment -- @@ -262,8 +262,9 @@
CharSequence formattedMessage = msgItem.getCachedFormattedMessage();
if (formattedMessage == null) {
formattedMessage = formatMessage(msgItem, msgItem.mContact, msgItem.mBody,
                                             msgItem.mSubscription, msgItem.mSubject,
                                             msgItem.mTimestamp, msgItem.mHighlight,
                                             msgItem.mTextContentType);
}
mBodyTextView.setText(formattedMessage);

//Synthetic comment -- @@ -359,14 +360,19 @@
ForegroundColorSpan mColorSpan = null;  // set in ctor

private CharSequence formatMessage(MessageItem msgItem, String contact, String body,
                                       int subId, String subject, String timestamp,
                                       Pattern highlight, String contentType) {
CharSequence template = mContext.getResources().getText(R.string.name_colon);
SpannableStringBuilder buf =
new SpannableStringBuilder(TextUtils.replace(template,
new String[] { "%s" },
new CharSequence[] { contact }));

       if (TelephonyManager.isMultiSimEnabled()) {
           buf.append( (subId == 0) ? "SUB1:" : "SUB2:");
           buf.append("\n");
       }

boolean hasSubject = !TextUtils.isEmpty(subject);
if (hasSubject) {
buf.append(mContext.getResources().getString(R.string.inline_subject, subject));








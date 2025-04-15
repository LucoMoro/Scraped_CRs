/*Fix for phone app crash in Icc Card.

- Check for active phone in ICC handler before processing messages.

- Boundary check for gsm/cdma subscription app index
while retrieving the ICC Card App.

Change-Id:I3d54447e8d48e3482763e78eeb2a737a34cec321*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCard.java b/telephony/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index 90f9e8c..61824e2 100644

//Synthetic comment -- @@ -487,6 +487,12 @@
CommandsInterface.SERVICE_CLASS_DATA +
CommandsInterface.SERVICE_CLASS_FAX;

            if (!mPhone.mIsTheCurrentActivePhone) {
                Log.e(mLogTag, "Received message " + msg + "[" + msg.what
                        + "] while being destroyed. Ignoring.");
                return;
            }

switch (msg.what) {
case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:
mState = null;
//Synthetic comment -- @@ -626,7 +632,13 @@
index = mIccCardStatus.getGsmUmtsSubscriptionAppIndex();
}

            IccCardApplication app;
            if (index < mIccCardStatus.CARD_MAX_APPS && index >= 0) {
                app = mIccCardStatus.getApplication(index);
            } else {
                Log.e(mLogTag, "[IccCard] Invalid Subscription Application index:" + index);
                return IccCard.State.ABSENT;
            }

if (app == null) {
Log.e(mLogTag, "[IccCard] Subscription Application in not present");








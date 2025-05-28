
//<Beginning of snippet n. 0>


CommandsInterface.SERVICE_CLASS_DATA +
CommandsInterface.SERVICE_CLASS_FAX;

switch (msg.what) {
case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:
mState = null;
index = mIccCardStatus.getGsmUmtsSubscriptionAppIndex();
}

            IccCardApplication app = mIccCardStatus.getApplication(index);

if (app == null) {
Log.e(mLogTag, "[IccCard] Subscription Application in not present");

//<End of snippet n. 0>









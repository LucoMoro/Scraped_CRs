/*Stk: Fix crash due to race condition

StkAppService doesn't really need mStkService reference in the
constructor. It will need it to respond to proactive command.
By that time StkService has to exist (proactive command comes
from it)*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index 1007a4a..9dcd25b 100644

//Synthetic comment -- @@ -144,20 +144,11 @@
@Override
public void onCreate() {
// Initialize members
mStkService = com.android.internal.telephony.cat.CatService
.getInstance();

        // NOTE mStkService is a singleton and continues to exist even if the GSMPhone is disposed
        //   after the radio technology change from GSM to CDMA so the PHONE_TYPE_CDMA check is
        //   needed. In case of switching back from CDMA to GSM the GSMPhone constructor updates
        //   the instance. (TODO: test).
        if ((mStkService == null)
                && (TelephonyManager.getDefault().getPhoneType()
                                != TelephonyManager.PHONE_TYPE_CDMA)) {
            CatLog.d(this, " Unable to get Service handle");
            return;
        }

mCmdsQ = new LinkedList<DelayedCmd>();
Thread serviceThread = new Thread(null, this, "Stk App Service");
serviceThread.start();
//Synthetic comment -- @@ -488,6 +479,15 @@
if (mCurrentCmd == null) {
return;
}
CatResponseMessage resMsg = new CatResponseMessage(mCurrentCmd);

// set result code








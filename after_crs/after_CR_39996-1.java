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
        // This can return null if StkService is not yet instantiated, but it's ok
        // If this is null we will do getInstance before we need to use this
mStkService = com.android.internal.telephony.cat.CatService
.getInstance();

mCmdsQ = new LinkedList<DelayedCmd>();
Thread serviceThread = new Thread(null, this, "Stk App Service");
serviceThread.start();
//Synthetic comment -- @@ -488,6 +479,15 @@
if (mCurrentCmd == null) {
return;
}
        if (mStkService == null) {
            mStkService = com.android.internal.telephony.cat.CatService.getInstance();
            if (mStkService == null) {
                // This should never happen (we should be responding only to a message
                // that arrived from StkService). It has to exist by this time
                throw new RuntimeException("mStkService is null when we need to send response");
            }
        }

CatResponseMessage resMsg = new CatResponseMessage(mCurrentCmd);

// set result code








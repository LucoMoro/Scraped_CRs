/*StkAppSerice: Stop the service when CatService Instance is null

ANR is seen in StkAppService because of ServiceHandler is not
created due to CatService is not running.

Stop Stkapp Service if Catservice is not running to avoid ANR.
Whenever service is started using StartService first onCreate
followed by onStart will be called. Next time onwards only
Onstart will be called due to this ServiceHandler will not intialize.
Hence moving corresponding code to onStart from onCreate.

Change-Id:I5a0a78d2012e3bf5aff2b5512dd21aa7bb96ec4a*/




//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index 9dcd25b..bed2caa 100644

//Synthetic comment -- @@ -144,11 +144,6 @@
@Override
public void onCreate() {
// Initialize members
mCmdsQ = new LinkedList<DelayedCmd>();
Thread serviceThread = new Thread(null, this, "Stk App Service");
serviceThread.start();
//Synthetic comment -- @@ -160,8 +155,17 @@

@Override
public void onStart(Intent intent, int startId) {

        mStkService = com.android.internal.telephony.cat.CatService
                .getInstance();

        if (mStkService == null) {
            stopSelf();
            CatLog.d(this, " Unable to get Service handle");
            return;
        }

        waitForLooper();
// onStart() method can be passed a null intent
// TODO: replace onStart() with onStartCommand()
if (intent == null) {








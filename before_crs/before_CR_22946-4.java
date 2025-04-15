/*Telephony: Cleanup CatService thread on dispose

When the CatService is disposed by CDMAPhone or GSMPhone the handler
thread is still running so the CatService thread never quits causing
another CatService thread to start next time when CatService is started.

Change-Id:I8594d1e8255576070dd18747e5f6e1df1b0a9cd6*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 36059ad..4a1d93e 100644

//Synthetic comment -- @@ -121,6 +121,7 @@
// Protects singleton instance lazy initialization.
private static final Object sInstanceLock = new Object();
private static CatService sInstance;
private CommandsInterface mCmdIf;
private Context mContext;
private CatCmdMessage mCurrntCmd = null;
//Synthetic comment -- @@ -179,13 +180,18 @@
}

public void dispose() {
        mIccRecords.unregisterForRecordsLoaded(this);
        mCmdIf.unSetOnCatSessionEnd(this);
        mCmdIf.unSetOnCatProactiveCmd(this);
        mCmdIf.unSetOnCatEvent(this);
        mCmdIf.unSetOnCatCallSetUp(this);

        this.removeCallbacksAndMessages(null);
}

protected void finalize() {
//Synthetic comment -- @@ -523,8 +529,8 @@
|| ic == null) {
return null;
}
                HandlerThread thread = new HandlerThread("Cat Telephony service");
                thread.start();
sInstance = new CatService(ci, ir, context, fh, ic);
CatLog.d(sInstance, "NEW sInstance");
} else if ((ir != null) && (mIccRecords != ir)) {








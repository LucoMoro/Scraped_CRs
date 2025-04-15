/*Telephony: Cleanup CatService thread on dispose

When the CatService is disposed by CDMAPhone or GSMPhone the handler
thread is still running so the CatService thread never quits causing
another CatService thread to start next time when CatService is started.

Change-Id:I8594d1e8255576070dd18747e5f6e1df1b0a9cd6*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 36059ad..31f77f8 100644

//Synthetic comment -- @@ -121,6 +121,7 @@
// Protects singleton instance lazy initialization.
private static final Object sInstanceLock = new Object();
private static CatService sInstance;
    private static HandlerThread handlerThread;
private CommandsInterface mCmdIf;
private Context mContext;
private CatCmdMessage mCurrntCmd = null;
//Synthetic comment -- @@ -184,6 +185,9 @@
mCmdIf.unSetOnCatProactiveCmd(this);
mCmdIf.unSetOnCatEvent(this);
mCmdIf.unSetOnCatCallSetUp(this);
        sInstance = null;
        handlerThread.quit();
        handlerThread = null;

this.removeCallbacksAndMessages(null);
}
//Synthetic comment -- @@ -523,8 +527,8 @@
|| ic == null) {
return null;
}
                handlerThread = new HandlerThread("Cat Telephony service");
                handlerThread.start();
sInstance = new CatService(ci, ir, context, fh, ic);
CatLog.d(sInstance, "NEW sInstance");
} else if ((ir != null) && (mIccRecords != ir)) {








/*Telephony: Cleanup CatService thread on dispose

When the CatService is disposed by CDMAPhone or GSMPhone the handler
thread is still running so the CatService thread never quits causing
another CatService thread to start next time when CatService is started.

Change-Id:I8594d1e8255576070dd18747e5f6e1df1b0a9cd6*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 36059ad..aa60375 100644

//Synthetic comment -- @@ -121,6 +121,7 @@
// Protects singleton instance lazy initialization.
private static final Object sInstanceLock = new Object();
private static CatService sInstance;
private CommandsInterface mCmdIf;
private Context mContext;
private CatCmdMessage mCurrntCmd = null;
//Synthetic comment -- @@ -184,6 +185,9 @@
mCmdIf.unSetOnCatProactiveCmd(this);
mCmdIf.unSetOnCatEvent(this);
mCmdIf.unSetOnCatCallSetUp(this);

this.removeCallbacksAndMessages(null);
}
//Synthetic comment -- @@ -523,8 +527,8 @@
|| ic == null) {
return null;
}
                HandlerThread thread = new HandlerThread("Cat Telephony service");
                thread.start();
sInstance = new CatService(ci, ir, context, fh, ic);
CatLog.d(sInstance, "NEW sInstance");
} else if ((ir != null) && (mIccRecords != ir)) {








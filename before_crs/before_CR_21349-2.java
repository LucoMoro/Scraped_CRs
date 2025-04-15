/*Lazy initialization must be synchronized to avoid parallel instances cretation.

Change-Id:I9c6887c714f42534a465c266689dc03ee7298900*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 1e23e34..36059ad 100644

//Synthetic comment -- @@ -118,6 +118,8 @@
private static IccRecords mIccRecords;

// Service members.
private static CatService sInstance;
private CommandsInterface mCmdIf;
private Context mContext;
//Synthetic comment -- @@ -515,26 +517,28 @@
*/
public static CatService getInstance(CommandsInterface ci, IccRecords ir,
Context context, IccFileHandler fh, IccCard ic) {
        if (sInstance == null) {
            if (ci == null || ir == null || context == null || fh == null
                    || ic == null) {
                return null;
            }
            HandlerThread thread = new HandlerThread("Cat Telephony service");
            thread.start();
            sInstance = new CatService(ci, ir, context, fh, ic);
            CatLog.d(sInstance, "NEW sInstance");
        } else if ((ir != null) && (mIccRecords != ir)) {
            CatLog.d(sInstance, "Reinitialize the Service with SIMRecords");
            mIccRecords = ir;

            // re-Register for SIM ready event.
            mIccRecords.registerForRecordsLoaded(sInstance, MSG_ID_ICC_RECORDS_LOADED, null);
            CatLog.d(sInstance, "sr changed reinitialize and return current sInstance");
        } else {
            CatLog.d(sInstance, "Return current sInstance");
}
        return sInstance;
}

/**








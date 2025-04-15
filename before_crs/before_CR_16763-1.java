/*Add "suspended" state to thread display.

Change-Id:Id13f96d1e490228a2fc9a7452a34cd4ac7e5aa46*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/ThreadPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/ThreadPanel.java
//Synthetic comment -- index d94d4f3..bf7a58d 100644

//Synthetic comment -- @@ -101,7 +101,8 @@

private static final String[] THREAD_STATUS = {
"zombie", "running", "timed-wait", "monitor",
        "wait", "init", "start", "native", "vmwait"
};

/**








/*Telephony: Send complete path for EF-IMG(4F20) for RUIM cards.

Change-Id:I76f981d5334274b34e2e1a752fd0a550458e4e86*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/RuimFileHandler.java b/src/java/com/android/internal/telephony/uicc/RuimFileHandler.java
//Synthetic comment -- index 180b277..16ff0fb 100644

//Synthetic comment -- @@ -42,7 +42,7 @@
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        mCi.iccIOForApp(COMMAND_GET_RESPONSE, fileid, "img", 0, 0,
GET_RESPONSE_EF_IMG_SIZE_BYTES, null, null,
mAid, response);
}








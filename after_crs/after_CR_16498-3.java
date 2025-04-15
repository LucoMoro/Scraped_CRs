/*Revert "InputMethodService: Fix ignore case in showInputMethodPickerFromClient"

This reverts commit f2e67a5779d404b4791c6ab5b0d30dfb2c463514.

Open Source Project CL:https://review.source.android.com/#change,16419This change unexpectedly started preventing users from changing Input-Method.

How to reproduce
1. Long press "?123" button in LatinIME
2. Select "Input method"
3. No window will be opened.

Change-Id:Ie0d4b58900a9b06aa9d43f9fa0bea5165294fa30*/




//Synthetic comment -- diff --git a/services/java/com/android/server/InputMethodManagerService.java b/services/java/com/android/server/InputMethodManagerService.java
//Synthetic comment -- index bc8607e..5a995ae 100644

//Synthetic comment -- @@ -1225,7 +1225,6 @@
if (mCurClient == null || client == null
|| mCurClient.client.asBinder() != client.asBinder()) {
Slog.w(TAG, "Ignoring showInputMethodDialogFromClient of: " + client);
}

mHandler.sendEmptyMessage(MSG_SHOW_IM_PICKER);








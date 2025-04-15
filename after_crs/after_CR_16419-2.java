/*InputMethodService: Fix ignore case in showInputMethodPickerFromClient

Fix to avoid posting MSG_SHOW_IM_PICKER in
showInputMethodPickerFromClient when ignoring the request from the
client.

Change-Id:Iff8a0809aea892b4493d5cffbb91adba2d74414e*/




//Synthetic comment -- diff --git a/services/java/com/android/server/InputMethodManagerService.java b/services/java/com/android/server/InputMethodManagerService.java
//Synthetic comment -- index 5a995ae..bc8607e 100644

//Synthetic comment -- @@ -1225,6 +1225,7 @@
if (mCurClient == null || client == null
|| mCurClient.client.asBinder() != client.asBinder()) {
Slog.w(TAG, "Ignoring showInputMethodDialogFromClient of: " + client);
                return;
}

mHandler.sendEmptyMessage(MSG_SHOW_IM_PICKER);








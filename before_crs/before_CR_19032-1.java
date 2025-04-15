/*SearchDialog: Fix for NullpointerException while using Suggestions in
Browser

Change-Id:Ic208ae51e4f7678363ea6d7de9db97707713bf6d*/
//Synthetic comment -- diff --git a/core/java/android/app/SearchDialog.java b/core/java/android/app/SearchDialog.java
//Synthetic comment -- index 2fb746c..9a3d621 100644

//Synthetic comment -- @@ -1107,6 +1107,9 @@
* @return true if a successful launch, false if could not (e.g. bad position).
*/
protected boolean launchSuggestion(int position, int actionKey, String actionMsg) {
Cursor c = mSuggestionsAdapter.getCursor();
if ((c != null) && c.moveToPosition(position)) {









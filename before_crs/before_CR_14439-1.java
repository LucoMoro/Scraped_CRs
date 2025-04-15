/*Fixed FindBugs warnings in ErrorConsoleView.java

Save some memory by marking ErrorConsoleMessageList static.

Change-Id:I6034eb295fde9639073f2c15c872743afd5aefe1*/
//Synthetic comment -- diff --git a/src/com/android/browser/ErrorConsoleView.java b/src/com/android/browser/ErrorConsoleView.java
//Synthetic comment -- index 56f663b..9f1873c 100644

//Synthetic comment -- @@ -228,7 +228,7 @@
* This class is an adapter for ErrorConsoleListView that contains the error console
* message data.
*/
        private class ErrorConsoleMessageList extends android.widget.BaseAdapter
implements android.widget.ListAdapter {

private Vector<ErrorConsoleMessage> mMessages;








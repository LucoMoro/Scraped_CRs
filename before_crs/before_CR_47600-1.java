/*Make sure mController has instanced incorrect before used it.

After about one night running MONKEY, browser will be no responsed
and with a WHITE Page. No responsing for any KEY and touch event except
HOME key.

MONKEY COMMAND:
monkey -p com.android.browser -s 100 --throttle 500 -v 214783647

Change-Id:Idf499791f8c17084ad8e0c5d3009c920900f81aeSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index 4166b11..db23751 100644

//Synthetic comment -- @@ -101,6 +101,15 @@
@Override
protected void onNewIntent(Intent intent) {
if (shouldIgnoreIntents()) return;
if (ACTION_RESTART.equals(intent.getAction())) {
Bundle outState = new Bundle();
mController.onSaveInstanceState(outState);








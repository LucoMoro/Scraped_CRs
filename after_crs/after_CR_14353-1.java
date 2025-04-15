/*Removed findbugs warnings from BrowserActivity.

Change-Id:I4b5bc7b66ffb44483d0783f05b02558f709382ee*/




//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index 9030cf7..a754900 100644

//Synthetic comment -- @@ -1464,7 +1464,7 @@
if (mTabControl.getTabCount() == 1) {
// This is the last tab.  Open a new one, with the home
// page and close the current one.
            openTabToHomePage();
closeTab(current);
return;
}
//Synthetic comment -- @@ -3772,7 +3772,7 @@
((TextView) pageInfoView.findViewById(R.id.title)).setText(title);

mPageInfoView = tab;
        mPageInfoFromShowSSLCertificateOnError = Boolean.valueOf(fromShowSSLCertificateOnError);

AlertDialog.Builder alertDialogBuilder =
new AlertDialog.Builder(this)








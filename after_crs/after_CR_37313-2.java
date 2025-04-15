/*Browser: Use ICS alert icon

Still using upscaled GB drawable in a few places.

Change-Id:I52471b10809117b5a68e8236dc320fde0da2c533*/




//Synthetic comment -- diff --git a/src/com/android/browser/BookmarkUtils.java b/src/com/android/browser/BookmarkUtils.java
//Synthetic comment -- index 4834c39..f0c01f7 100644

//Synthetic comment -- @@ -234,7 +234,7 @@
final Context context, final Message msg) {

new AlertDialog.Builder(context)
                .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(context.getString(R.string.delete_bookmark_warning,
title))
.setPositiveButton(R.string.ok,








//Synthetic comment -- diff --git a/src/com/android/browser/BrowserHistoryPage.java b/src/com/android/browser/BrowserHistoryPage.java
//Synthetic comment -- index 69febc5..9493b85 100644

//Synthetic comment -- @@ -293,7 +293,7 @@
final ClearHistoryTask clear = new ClearHistoryTask(resolver);
AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
.setMessage(R.string.pref_privacy_clear_history_dlg)
                .setIconAttribute(android.R.attr.alertDialogIcon)
.setNegativeButton(R.string.cancel, null)
.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
@Override








//Synthetic comment -- diff --git a/src/com/android/browser/DownloadHandler.java b/src/com/android/browser/DownloadHandler.java
//Synthetic comment -- index 1da6795..6d0b1e6 100644

//Synthetic comment -- @@ -163,7 +163,7 @@

new AlertDialog.Builder(activity)
.setTitle(title)
                .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(msg)
.setPositiveButton(R.string.ok, null)
.show();








//Synthetic comment -- diff --git a/src/com/android/browser/HttpAuthenticationDialog.java b/src/com/android/browser/HttpAuthenticationDialog.java
//Synthetic comment -- index ac4119c..0c16632 100644

//Synthetic comment -- @@ -128,7 +128,7 @@

mDialog = new AlertDialog.Builder(mContext)
.setTitle(title)
                .setIconAttribute(android.R.attr.alertDialogIcon)
.setView(v)
.setPositiveButton(R.string.action, new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int whichButton) {








//Synthetic comment -- diff --git a/src/com/android/browser/Tab.java b/src/com/android/browser/Tab.java
//Synthetic comment -- index 28734bd..6200286 100644

//Synthetic comment -- @@ -515,7 +515,7 @@
new AlertDialog.Builder(mContext)
.setTitle(R.string.security_warning)
.setMessage(R.string.ssl_warnings_header)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setPositiveButton(R.string.ssl_continue,
new DialogInterface.OnClickListener() {
@Override
//Synthetic comment -- @@ -716,7 +716,7 @@
if (dialog && mSubView != null) {
new AlertDialog.Builder(mContext)
.setTitle(R.string.too_many_subwindows_dialog_title)
                        .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(R.string.too_many_subwindows_dialog_message)
.setPositiveButton(R.string.ok, null)
.show();
//Synthetic comment -- @@ -724,7 +724,7 @@
} else if (!mWebViewController.getTabControl().canCreateNewTab()) {
new AlertDialog.Builder(mContext)
.setTitle(R.string.too_many_windows_dialog_title)
                        .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(R.string.too_many_windows_dialog_message)
.setPositiveButton(R.string.ok, null)
.show();
//Synthetic comment -- @@ -757,7 +757,7 @@
// Build a confirmation dialog to display to the user.
final AlertDialog d =
new AlertDialog.Builder(mContext)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(R.string.popup_window_attempt)
.setPositiveButton(R.string.allow, allowListener)
.setNegativeButton(R.string.block, blockListener)








//Synthetic comment -- diff --git a/src/com/android/browser/preferences/WebsiteSettingsFragment.java b/src/com/android/browser/preferences/WebsiteSettingsFragment.java
//Synthetic comment -- index da06428..c84c669 100644

//Synthetic comment -- @@ -596,7 +596,7 @@
notifyDataSetChanged();
}})
.setNegativeButton(R.string.webstorage_clear_data_dialog_cancel_button, null)
                            .setIconAttribute(android.R.attr.alertDialogIcon)
.show();
break;
case Site.FEATURE_GEOLOCATION:
//Synthetic comment -- @@ -614,7 +614,7 @@
notifyDataSetChanged();
}})
.setNegativeButton(R.string.geolocation_settings_page_dialog_cancel_button, null)
                            .setIconAttribute(android.R.attr.alertDialogIcon)
.show();
break;
}
//Synthetic comment -- @@ -695,7 +695,7 @@
finish();
}})
.setNegativeButton(R.string.website_settings_clear_all_dialog_cancel_button, null)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.show();
break;
}








//<Beginning of snippet n. 0>
final Context context, final Message msg) {

new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_dialog_alert) // Updated to use appropriate ICS alert icon
.setMessage(context.getString(R.string.delete_bookmark_warning,
title))
.setPositiveButton(R.string.ok,
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
final ClearHistoryTask clear = new ClearHistoryTask(resolver);
AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
.setMessage(R.string.pref_privacy_clear_history_dlg)
                .setIcon(R.drawable.ic_dialog_alert) // Updated to use appropriate ICS alert icon
.setNegativeButton(R.string.cancel, null)
.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
@Override
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
new AlertDialog.Builder(activity)
.setTitle(title)
                .setIcon(R.drawable.ic_dialog_alert) // Updated to use appropriate ICS alert icon
.setMessage(msg)
.setPositiveButton(R.string.ok, null)
.show();
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
mDialog = new AlertDialog.Builder(mContext)
.setTitle(title)
                .setIcon(R.drawable.ic_dialog_alert) // Updated to use appropriate ICS alert icon
.setView(v)
.setPositiveButton(R.string.action, new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int whichButton) {
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
new AlertDialog.Builder(mContext)
.setTitle(R.string.security_warning)
.setMessage(R.string.ssl_warnings_header)
                    .setIcon(R.drawable.ic_dialog_alert) // Updated to use appropriate ICS alert icon
.setPositiveButton(R.string.ssl_continue,
new DialogInterface.OnClickListener() {
@Override
if (dialog && mSubView != null) {
new AlertDialog.Builder(mContext)
.setTitle(R.string.too_many_subwindows_dialog_title)
                        .setIcon(R.drawable.ic_dialog_alert) // Updated to use appropriate ICS alert icon
.setMessage(R.string.too_many_subwindows_dialog_message)
.setPositiveButton(R.string.ok, null)
.show();
} else if (!mWebViewController.getTabControl().canCreateNewTab()) {
new AlertDialog.Builder(mContext)
.setTitle(R.string.too_many_windows_dialog_title)
                        .setIcon(R.drawable.ic_dialog_alert) // Updated to use appropriate ICS alert icon
.setMessage(R.string.too_many_windows_dialog_message)
.setPositiveButton(R.string.ok, null)
.show();
// Build a confirmation dialog to display to the user.
final AlertDialog d =
new AlertDialog.Builder(mContext)
                    .setIcon(R.drawable.ic_dialog_alert) // Updated to use appropriate ICS alert icon
.setMessage(R.string.popup_window_attempt)
.setPositiveButton(R.string.allow, allowListener)
.setNegativeButton(R.string.block, blockListener)
//<End of snippet n. 4>

//<Beginning of snippet n. 5>
notifyDataSetChanged();
}})
.setNegativeButton(R.string.webstorage_clear_data_dialog_cancel_button, null)
                            .setIcon(R.drawable.ic_dialog_alert) // Updated to use appropriate ICS alert icon
.show();
break;
case Site.FEATURE_GEOLOCATION:
notifyDataSetChanged();
}})
.setNegativeButton(R.string.geolocation_settings_page_dialog_cancel_button, null)
                            .setIcon(R.drawable.ic_dialog_alert) // Updated to use appropriate ICS alert icon
.show();
break;
}
finish();
}})
.setNegativeButton(R.string.website_settings_clear_all_dialog_cancel_button, null)
                    .setIcon(R.drawable.ic_dialog_alert) // Updated to use appropriate ICS alert icon
.show();
break;
//<End of snippet n. 5>
//<Beginning of snippet n. 0>
private AlertDialog createAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveAction) {
    return new AlertDialog.Builder(context)
        .setIcon(R.drawable.ic_ics_alert)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(R.string.ok, positiveAction)
        .create();
}

final Context context, final Message msg) {
    createAlertDialog(context, null, context.getString(R.string.delete_bookmark_warning, title), null).show();
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
final ClearHistoryTask clear = new ClearHistoryTask(resolver);
createAlertDialog(getActivity(), null, getString(R.string.pref_privacy_clear_history_dlg), new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        // Handle positive button click
    }
}).setNegativeButton(R.string.cancel, null).show();
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
createAlertDialog(activity, title, msg, null).show();
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
mDialog = createAlertDialog(mContext, title, null, new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int whichButton) {
        // Handle action
    }
}).setView(v).show();
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
createAlertDialog(mContext, getString(R.string.security_warning), getString(R.string.ssl_warnings_header), null).show();

if (dialog && mSubView != null) {
    createAlertDialog(mContext, getString(R.string.too_many_subwindows_dialog_title), getString(R.string.too_many_subwindows_dialog_message), null).show();
} else if (!mWebViewController.getTabControl().canCreateNewTab()) {
    createAlertDialog(mContext, getString(R.string.too_many_windows_dialog_title), getString(R.string.too_many_windows_dialog_message), null).show();
}

// Build a confirmation dialog to display to the user.
final AlertDialog d = createAlertDialog(mContext, null, getString(R.string.popup_window_attempt), allowListener);
d.setNegativeButton(R.string.block, blockListener).show();
//<End of snippet n. 4>

//<Beginning of snippet n. 5>
notifyDataSetChanged();
}}
.setNegativeButton(R.string.webstorage_clear_data_dialog_cancel_button, null)
    .setIcon(R.drawable.ic_ics_alert)
    .show();
break;
case Site.FEATURE_GEOLOCATION:
    notifyDataSetChanged();
}})
.setNegativeButton(R.string.geolocation_settings_page_dialog_cancel_button, null)
    .setIcon(R.drawable.ic_ics_alert)
    .show();
break;
}
finish();
}})
.setNegativeButton(R.string.website_settings_clear_all_dialog_cancel_button, null)
    .setIcon(R.drawable.ic_ics_alert)
    .show();
break;
//<End of snippet n. 5>
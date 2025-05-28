
//<Beginning of snippet n. 0>


mProgressDialog.setTitle(R.string.select_dialog);
mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
mProgressDialog.setMax(MAX_PROGRESS);
            mProgressDialog.setButton(getText(R.string.alert_dialog_hide), new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int whichButton) {

/* User clicked Yes so do some stuff */
}
});
            mProgressDialog.setButton2(getText(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int whichButton) {

/* User clicked No so do some stuff */

//<End of snippet n. 0>









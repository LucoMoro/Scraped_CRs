//<Beginning of snippet n. 0>
if (isSimLockEnabled()) {  // Check if SIM lock is enabled
    // Existing logic for changing PIN1
} else {
    sb.append(context.getText(
        com.android.internal.R.string.requestNotSupported)); // Error for SIM lock not enabled
    sb.append("\n");
    sb.append(context.getText(
        com.android.internal.R.string.needPuk2));
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
} else if (err == CommandException.Error.FDN_CHECK_FAILURE) {
    Log.i(LOG_TAG, "FDN_CHECK_FAILURE");
    sb.append(context.getText(com.android.internal.R.string.mmiFdnError));
//<End of snippet n. 1>
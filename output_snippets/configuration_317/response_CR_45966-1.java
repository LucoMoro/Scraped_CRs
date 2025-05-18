//<Beginning of snippet n. 0>
if (err == CommandException.Error.REQUEST_NOT_SUPPORTED) {
    Log.i(LOG_TAG, "REQUEST_NOT_SUPPORTED: Attempt to change PIN1 without enabling SIM lock. User ID: " + userId + ", Timestamp: " + System.currentTimeMillis());
    sb.append(context.getText(com.android.internal.R.string.mmiError));
    sb.append(" Please enable SIM lock to change your PIN1.");
} else if (err == CommandException.Error.FDN_CHECK_FAILURE) {
    Log.i(LOG_TAG, "FDN_CHECK_FAILURE");
    sb.append(context.getText(com.android.internal.R.string.mmiFdnError));
} else {
    sb.append(context.getText(
    com.android.internal.R.string.mmiError));
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
sb.append("\n");
sb.append(context.getText(
com.android.internal.R.string.needPuk2));
//<End of snippet n. 1>
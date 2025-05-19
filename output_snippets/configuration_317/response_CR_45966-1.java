//<Beginning of snippet n. 0>
if (!isSimLockEnabled()) {
    sb.append(context.getText(com.android.internal.R.string.simLockEnableRequired));
    Log.i(LOG_TAG, "PIN1 change failed: SIM lock is not enabled.");
    return;
}

if (err == CommandException.Error.REQUEST_NOT_SUPPORTED) {
    Log.i(LOG_TAG, "REQUEST_NOT_SUPPORTED: User attempted to change PIN1 without enabling SIM lock.");
    sb.append(context.getText(com.android.internal.R.string.simLockEnableRequired));
    return;
} else if (err == CommandException.Error.FDN_CHECK_FAILURE) {
    Log.i(LOG_TAG, "FDN_CHECK_FAILURE: FDN check failed.");
    sb.append(context.getText(com.android.internal.R.string.mmiFdnError));
    return;
}
sb.append(context.getText(com.android.internal.R.string.passwordIncorrect)); 
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
sb.append("\n");
sb.append(context.getText(com.android.internal.R.string.needPuk2));
//<End of snippet n. 1>
//<Beginning of snippet n. 0>
String resolvedType = null;
if (intent != null) {
    resolvedType = intent.resolveTypeIfNeeded(context.getContentResolver());
    intent.setAllowFds(false);
    IIntentSender target = ActivityManagerNative.getDefault().getIntentSender(
        ActivityManager.INTENT_SENDER_ACTIVITY, packageName, 
        // other parameters...
    );
} else {
    showError("Intent cannot be null");
    return;
}
//<End of snippet n. 0>
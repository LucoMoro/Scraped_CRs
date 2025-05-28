
//<Beginning of snippet n. 0>


String resolvedType = intent != null ? intent.resolveTypeIfNeeded(
context.getContentResolver()) : null;
try {
            intent.setAllowFds(false);
IIntentSender target =
ActivityManagerNative.getDefault().getIntentSender(
ActivityManager.INTENT_SENDER_ACTIVITY, packageName,

//<End of snippet n. 0>









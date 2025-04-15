/*Start app with several activities with intent filter [Main, Launcher] was broken

When application have more than one activity with intent filter
[Main, Launcher], then the process of resolving which activity to start
was broken. Made changes so default category isn't needed for
[Main, Launcher], this is according to documentation. In the case a
ResolverActivity is needed the intent wasn't created correct.

Change-Id:I2617122e07c35284862d2e0643888966ec0f7221*/
//Synthetic comment -- diff --git a/core/java/android/app/ApplicationContext.java b/core/java/android/app/ApplicationContext.java
//Synthetic comment -- index f48f150..6467aed 100644

//Synthetic comment -- @@ -1514,8 +1514,8 @@
if (resolveInfo == null) {
return null;
}
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setClassName(packageName, resolveInfo.activityInfo.name);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
return intent;
}








//Synthetic comment -- diff --git a/core/java/com/android/internal/app/ResolverActivity.java b/core/java/com/android/internal/app/ResolverActivity.java
//Synthetic comment -- index 7466cc46..e9b1801 100644

//Synthetic comment -- @@ -212,9 +212,12 @@
mIntent.setComponent(null);
mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

List<ResolveInfo> rList = mPm.queryIntentActivities(
                    intent, PackageManager.MATCH_DEFAULT_ONLY
                    | (mAlwaysCheck != null ? PackageManager.GET_RESOLVED_FILTER : 0));
int N;
if ((rList != null) && ((N = rList.size()) > 0)) {
// Only display the first matches that are either of equal








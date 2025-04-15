/*Add android.intent.category.SEARCH category.

Now the Application Search function in the GlobSearchBox can only search
the Launcher category. If add a SEARCH category for others Activity this
will help users more easily using there device.

LIKE: if you want set an Alarm, you need go to the Clock first, then
Press the "Set alarm" button. if you can open "Set alarm" Activity
by search "alarm" in the GlobSearchBox this will be great. The
developers just need add an IntentFilter on the Set alarm Activity.

Change-Id:I77e913dc4ca0ed80f215eeaa2daa6d6a16cf123a*/




//Synthetic comment -- diff --git a/src/com/android/providers/applications/ApplicationsProvider.java b/src/com/android/providers/applications/ApplicationsProvider.java
//Synthetic comment -- index 16b072a..9bb5bbc 100644

//Synthetic comment -- @@ -513,6 +513,16 @@
}
final PackageManager manager = getPackageManager();
List<ResolveInfo> activities = manager.queryIntentActivities(mainIntent, 0);
            
            Intent searchIntent = new Intent(Intent.ACTION_MAIN, null);
            searchIntent.addCategory("android.intent.category.SEARCH");
            if (packageName != null) {
                // Limit to activities in the package, if given
                searchIntent.setPackage(packageName);
            }
            List<ResolveInfo> searchActivities = manager.queryIntentActivities(searchIntent, 0);
            activities.addAll(searchActivities);
            
int activityCount = activities == null ? 0 : activities.size();
for (int i = 0; i < activityCount; i++) {
ResolveInfo info = activities.get(i);








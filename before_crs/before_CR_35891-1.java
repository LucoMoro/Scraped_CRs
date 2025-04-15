/*Add android.intent.category.SEARCH category.

Now the Application Search function in the GlobSearchBox can only search
the Launcher category. If add a SEARCH category for others Activity this
will help Android users more easily using there device.

LIKE: if you want set an Alarm, you need go to the Clock first, then
Press the "Set alarm" button. if you can open Set alarm Activity
by search "alarm" in the GlobSearchBox this will be great. The
developers just need add an IntentFilter on the Set alarm Activity.

Change-Id:I68a32922c80ded3a36a266ada9d3af997d0b24f1*/
//Synthetic comment -- diff --git a/src/com/android/providers/applications/ApplicationsProvider.java b/src/com/android/providers/applications/ApplicationsProvider.java
//Synthetic comment -- index 16b072a..9bb5bbc 100644

//Synthetic comment -- @@ -513,6 +513,16 @@
}
final PackageManager manager = getPackageManager();
List<ResolveInfo> activities = manager.queryIntentActivities(mainIntent, 0);
int activityCount = activities == null ? 0 : activities.size();
for (int i = 0; i < activityCount; i++) {
ResolveInfo info = activities.get(i);








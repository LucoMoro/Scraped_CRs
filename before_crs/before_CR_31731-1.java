/*To avoid the occurrence of IndexOutOfBoundsException in onReceive(),
We modified that the loop count of for statement was set again,
if mInstalledProviders.size had changed.

Occurrence condition:
 1. sim card was inserted.
 2. one package has two more AppWidgets, both of AppWidget was set on HomeApplication.
 3. power on the device as safe mode.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/AppWidgetService.java b/services/java/com/android/server/AppWidgetService.java
//Synthetic comment -- index 4f81178..1b188d1 100644

//Synthetic comment -- @@ -84,6 +84,7 @@

private static final String SETTINGS_FILENAME = "appwidgets.xml";
private static final int MIN_UPDATE_PERIOD = 30 * 60 * 1000; // 30 minutes

/*
* When identifying a Host or Provider based on the calling process, use the uid field.
//Synthetic comment -- @@ -1006,6 +1007,7 @@
p.instances.clear();
mInstalledProviders.remove(index);
mDeletedProviders.add(p);
// no need to send the DISABLE broadcast, since the receiver is gone anyway
cancelBroadcasts(p);
}
//Synthetic comment -- @@ -1444,10 +1446,15 @@
synchronized (mAppWidgetIds) {
ensureStateLoadedLocked();
int N = mInstalledProviders.size();
for (int i=N-1; i>=0; i--) {
Provider p = mInstalledProviders.get(i);
String pkgName = p.info.provider.getPackageName();
updateProvidersForPackageLocked(pkgName);
}
saveStateLocked();
}








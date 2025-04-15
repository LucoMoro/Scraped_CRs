/*Crash when starting in safe mode

The phone would crash when starting it up in safe mode if there
were two or more 3'rd party widget added to Home screen where
both widgets came from the same package.

While going through the list of installed widget providers at
start-up, unused providers are removed from this list. When starting
in safe mode, all 3rd party installed widgets are removed. Since we
were removing items from the same list we access, there was
a risk of getting an IndexOutOfBoundsException.

The fix is to create a copy of list before we traverse it.

Change-Id:I9503e3220320de0415560dd6727f3edc90d920ef*/




//Synthetic comment -- diff --git a/services/java/com/android/server/AppWidgetServiceImpl.java b/services/java/com/android/server/AppWidgetServiceImpl.java
//Synthetic comment -- index f4e43ff..dff147d 100644

//Synthetic comment -- @@ -210,9 +210,10 @@

synchronized (mAppWidgetIds) {
ensureStateLoadedLocked();
                ArrayList<Provider> providers = new ArrayList(mInstalledProviders);
                int N = providers.size();
for (int i = N - 1; i >= 0; i--) {
                    Provider p = providers.get(i);
String pkgName = p.info.provider.getPackageName();
updateProvidersForPackageLocked(pkgName);
}








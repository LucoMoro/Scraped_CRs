/*Memory leak in TextKeyListener.

If a call to getPrefs is made after weak reference to content resolver
is gone, a new observer will be created and registered at the
resolver. At registration, the resolver will allocate ObserverEntry
objects that will eventually fill the JNI global reference pool.
There is no need to create and register a new observer if one is already
registered.

Change-Id:If8442b3370299980b73d4ea83757c5eae6a85408*/




//Synthetic comment -- diff --git a/core/java/android/text/method/TextKeyListener.java b/core/java/android/text/method/TextKeyListener.java
//Synthetic comment -- index 5be2a48..0c12c55 100644

//Synthetic comment -- @@ -246,8 +246,11 @@
private void initPrefs(Context context) {
final ContentResolver contentResolver = context.getContentResolver();
mResolver = new WeakReference<ContentResolver>(contentResolver);

        if (mObserver == null) {
            mObserver = new SettingsObserver();
            contentResolver.registerContentObserver(Settings.System.CONTENT_URI, true, mObserver);
        }

updatePrefs(contentResolver);
mPrefsInited = true;








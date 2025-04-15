/*Document that OnSharedPreferenceChangeListener is weakly referenced

A SharedPreferences object stores the listeners inside a WeakHashMap.
This is untypical for a listener pattern. Users may use an anonymous
inner class to create the listener which gets gc-ed after a short period
of time.

This patch describes this behaviour in the official documentation. Fixes
AOSP issue #6052.

Change-Id:If9c0235205fbff990d67a0cd6729677f16a75313*/
//Synthetic comment -- diff --git a/core/java/android/content/SharedPreferences.java b/core/java/android/content/SharedPreferences.java
//Synthetic comment -- index da5480e..679dec9 100644

//Synthetic comment -- @@ -352,6 +352,9 @@
/**
* Registers a callback to be invoked when a change happens to a preference.
* 
* @param listener The callback that will run.
* @see #unregisterOnSharedPreferenceChangeListener
*/








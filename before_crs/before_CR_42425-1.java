/*Reset to default did not reset cached settings

Some settings such as the current search engine to use was cached
in the BrowserSetting object and not always read from shared
preferences. When settings were reset to default these cached values
were not reset causing the old (possibly non-default) value to still
be used.

Change-Id:I805a339a6238c96dc73cbda47981053bd4f6eace*/
//Synthetic comment -- diff --git a/src/com/android/browser/BrowserSettings.java b/src/com/android/browser/BrowserSettings.java
//Synthetic comment -- index 8dfd4d7..0e9d219 100644

//Synthetic comment -- @@ -506,9 +506,16 @@
.clear()
.putLong(GoogleAccountLogin.PREF_AUTOLOGIN_TIME, gal)
.apply();
syncManagedSettings();
}

public AutoFillProfile getAutoFillProfile() {
return mAutofillHandler.getAutoFillProfile();
}








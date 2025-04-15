/*Replaced deprecated getIntent with parseUri

Change-Id:Ie3918b5896c922403e4fb58b81aacd52d364e11d*/




//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/ShortcutManager.java b/phone/com/android/internal/policy/impl/ShortcutManager.java
//Synthetic comment -- index d86ac44..5b2b973 100644

//Synthetic comment -- @@ -82,7 +82,7 @@
String intentURI = c.getString(COLUMN_INTENT);
Intent intent = null;
try {
                intent = Intent.parseUri(intentURI, 0);
} catch (URISyntaxException e) {
Log.w(TAG, "Intent URI for shortcut invalid.", e);
}








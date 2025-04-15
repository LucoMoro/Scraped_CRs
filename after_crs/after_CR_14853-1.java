/*replaced deprecated getIntent with parseURI

Change-Id:Ice9e74fd8f729d7efad934d2de4c747d73be562e*/




//Synthetic comment -- diff --git a/core/java/android/provider/Settings.java b/core/java/android/provider/Settings.java
//Synthetic comment -- index f7e55db..69d3b77 100644

//Synthetic comment -- @@ -3779,7 +3779,7 @@
while (intent == null && c.moveToNext()) {
try {
String intentURI = c.getString(c.getColumnIndexOrThrow(INTENT));
                        intent = Intent.parseUri(intentURI, 0);
} catch (java.net.URISyntaxException e) {
// The stored URL is bad...  ignore it.
} catch (IllegalArgumentException e) {
//Synthetic comment -- @@ -3889,7 +3889,7 @@

Intent intent;
try {
                intent = Intent.parseUri(intentUri, 0);
} catch (URISyntaxException e) {
return "";
}








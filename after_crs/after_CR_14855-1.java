/*Replaced deprecated getIntent with parseUri

Change-Id:Iadfd4d2c5a6981eb4facc167b89e76a8e55090fe*/




//Synthetic comment -- diff --git a/src/com/android/voicedialer/RecognizerEngine.java b/src/com/android/voicedialer/RecognizerEngine.java
//Synthetic comment -- index 0a71c5a..a919a09 100644

//Synthetic comment -- @@ -1172,7 +1172,7 @@
else if ("Intent".equalsIgnoreCase(commands[0])) {
for (int i = 1; i < commands.length; i++) {
try {
                        Intent intent = Intent.parseUri(commands[i], 0);
if (intent.getStringExtra(SENTENCE_EXTRA) == null) {
intent.putExtra(SENTENCE_EXTRA, literal);
}








/*return value of String.replace() is ignored.

Change-Id:Id7330e1ffc9f429b22f153d8e644fa7c64354173Signed-off-by: You Kim <you.kim72@gmail.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/NativeDaemonEvent.java b/services/java/com/android/server/NativeDaemonEvent.java
//Synthetic comment -- index f11ae1d..2095152 100644

//Synthetic comment -- @@ -223,8 +223,8 @@
current++;  // skip the trailing quote
}
// unescape stuff within the word
            word.replace("\\\\", "\\");
            word.replace("\\\"", "\"");

if (DEBUG_ROUTINE) Slog.e(LOGTAG, "found '" + word + "'");
parsed.add(word);








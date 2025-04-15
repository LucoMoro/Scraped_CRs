/*return value is not used in unescaping argument.

Change-Id:I0c5fdd4791ce109d6cd2ee2d6871d06d9a32fecdSigned-off-by: You Kim <you.kim72@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/widget/TimePicker.java b/core/java/android/widget/TimePicker.java
//Synthetic comment -- index cb9ed61..9b576b3 100644

//Synthetic comment -- @@ -391,7 +391,7 @@
*/
public void setCurrentHour(Integer currentHour) {
// why was Integer used in the first place?
        if (currentHour == null || currentHour == getCurrentHour()) {
return;
}
if (!is24HourView()) {








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








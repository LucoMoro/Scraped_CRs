/*Avoid adding FORWARD_LOCK flag to updated system app

Consider the following case:
- an app exists in /system/app or /vendor/app
- if it is updated as a forward-lock app by download,
  then installed in /data/app-private
- and moreover, if it is updated by the whole system image
Then the system app shouldn't be added FORWARD_LOCK flag.

Change-Id:I49e7ee7655250bbd090ce0251e61d686c4d047fb*/
//Synthetic comment -- diff --git a/services/java/com/android/server/PackageManagerService.java b/services/java/com/android/server/PackageManagerService.java
old mode 100644
new mode 100755
//Synthetic comment -- index 8c74566..009826b

//Synthetic comment -- @@ -2658,10 +2658,13 @@
return null;
}
// The apk is forward locked (not public) if its code and resources
        // are kept in different files.
// TODO grab this value from PackageSettings
        if (ps != null && !ps.codePath.equals(ps.resourcePath)) {
            parseFlags |= PackageParser.PARSE_FORWARD_LOCK;
}

String codePath = null;








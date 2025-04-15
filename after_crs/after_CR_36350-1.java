/*Avoid adding FORWARD_LOCK flag to an updated system app

Consider the following case:
1. An app exists in /system/app or /vendor/app. (i.e, it's a system app)
2. If the app would be updated as a forward-lock app by downloading from
   market(google play), then it would be installed in /data/app-private.
3. Moreover, if it would be updated by the whole system image (i.e, FOTA
   or something like that), then the app would be applied a FORWARD_LOCK
   flag.

The app SHOULD NOT have a FORWARD_LCOK flag. Because the resource path
of such app does not refer to the proper resources to access.

Change-Id:I0750f69a93a3115f4201029796b598507de3555f*/




//Synthetic comment -- diff --git a/services/java/com/android/server/pm/PackageManagerService.java b/services/java/com/android/server/pm/PackageManagerService.java
old mode 100644
new mode 100755
//Synthetic comment -- index 938d93a..f010f87

//Synthetic comment -- @@ -2918,10 +2918,13 @@
return null;
}
// The apk is forward locked (not public) if its code and resources
        // are kept in different files. (except for app in either system or
        // vendor path).
// TODO grab this value from PackageSettings
        if ((parseFlags & PackageParser.PARSE_IS_SYSTEM_DIR) == 0) {
            if (ps != null && !ps.codePath.equals(ps.resourcePath)) {
                parseFlags |= PackageParser.PARSE_FORWARD_LOCK;
            }
}

String codePath = null;








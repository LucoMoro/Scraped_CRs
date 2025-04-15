/*Honor exported=false attribute for providers.

A provider defined in an AndroidManifest.xml file may set its `exported'
attribute to false (the default value is true), indicating that only
activities running in the same process as the provider may access it
(except root and the system process, which are always granted access).
This patch fixes an error which caused the exported attribute to be
ignored, thus granting any process access.

Change-Id:I5e12875893c785629a2f57362a190d340926f4f6*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 804af9c..2183de2 100644

//Synthetic comment -- @@ -7770,13 +7770,12 @@
}

private final String checkContentProviderPermissionLocked(
            ProviderInfo cpi, ProcessRecord r, int mode) {
final int callingPid = (r != null) ? r.pid : Binder.getCallingPid();
final int callingUid = (r != null) ? r.info.uid : Binder.getCallingUid();
if (checkComponentPermission(cpi.readPermission, callingPid, callingUid,
cpi.exported ? -1 : cpi.applicationInfo.uid)
                == PackageManager.PERMISSION_GRANTED
                && mode == ParcelFileDescriptor.MODE_READ_ONLY || mode == -1) {
return null;
}
if (checkComponentPermission(cpi.writePermission, callingPid, callingUid,
//Synthetic comment -- @@ -7793,8 +7792,7 @@
PathPermission pp = pps[i];
if (checkComponentPermission(pp.getReadPermission(), callingPid, callingUid,
cpi.exported ? -1 : cpi.applicationInfo.uid)
                        == PackageManager.PERMISSION_GRANTED
                        && mode == ParcelFileDescriptor.MODE_READ_ONLY || mode == -1) {
return null;
}
if (checkComponentPermission(pp.getWritePermission(), callingPid, callingUid,
//Synthetic comment -- @@ -7834,7 +7832,7 @@
cpr = (ContentProviderRecord)mProvidersByName.get(name);
if (cpr != null) {
cpi = cpr.info;
                if (checkContentProviderPermissionLocked(cpi, r, -1) != null) {
return new ContentProviderHolder(cpi,
cpi.readPermission != null
? cpi.readPermission : cpi.writePermission);
//Synthetic comment -- @@ -7897,7 +7895,7 @@
return null;
}

                if (checkContentProviderPermissionLocked(cpi, r, -1) != null) {
return new ContentProviderHolder(cpi,
cpi.readPermission != null
? cpi.readPermission : cpi.writePermission);








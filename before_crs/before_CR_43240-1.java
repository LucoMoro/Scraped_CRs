/*Remove group access from private app storage

The directories created by the Context for each application (cache,
databases, shared_prefs, & files) are all readable and writeable by
the group.  In addition, certain files under these directories (eg
shared prefs and databases) are also created as readable and writeable
by the group by default.

In most cases, group access is harmless, because each app (or related
group of apps) is allocated its own unique UID, and its primary GID is
set to the same.  However, in the case of a system app, group access
creates a potential problem, because any process that has the same ID
in its groups is able to access the private storage of the app.  This
is also mostly harmless, except when the system app specifies
android.uid.log, because the adb shell user has AID_LOG as a group,
and is therefore able to read and write the private storage of the app.

The simplest course of action to close this hole is to remove group
access entirely from all apps.  This relies on the following
assumptions:
- Group access to private app storage does not convey any benefits to
non-system apps, because UID always equals GID for apps, and the only
type of process that might have the same GID would be another app.
- Group access to private app storage is undesirable in all cases for
system apps, because only a process with the same UID as the app
should be allowed access to the private storage of the app.

The docs for Context only specify a distinction between MODE_PRIVATE
and MODE_WORLD_READABLE|WRITEABLE, and make no mention of group.  So,
this doesn't break the published API.

Change-Id:I69172dd1506702c5fd9175e691e066bcd4cb4883*/
//Synthetic comment -- diff --git a/core/java/android/app/ContextImpl.java b/core/java/android/app/ContextImpl.java
//Synthetic comment -- index b902550..090ac92 100644

//Synthetic comment -- @@ -632,6 +632,17 @@
if (mPreferencesDir == null) {
mPreferencesDir = new File(getDataDirFile(), "shared_prefs");
}
return mPreferencesDir;
}
}
//Synthetic comment -- @@ -659,7 +670,7 @@
parent.mkdir();
FileUtils.setPermissions(
parent.getPath(),
            FileUtils.S_IRWXU|FileUtils.S_IRWXG|FileUtils.S_IXOTH,
-1, -1);
FileOutputStream fos = new FileOutputStream(f, append);
setFilePermissionsFromMode(f.getPath(), mode, 0);
//Synthetic comment -- @@ -685,7 +696,7 @@
}
FileUtils.setPermissions(
mFilesDir.getPath(),
                        FileUtils.S_IRWXU|FileUtils.S_IRWXG|FileUtils.S_IXOTH,
-1, -1);
}
return mFilesDir;
//Synthetic comment -- @@ -748,7 +759,7 @@
}
FileUtils.setPermissions(
mCacheDir.getPath(),
                        FileUtils.S_IRWXU|FileUtils.S_IRWXG|FileUtils.S_IXOTH,
-1, -1);
}
}
//Synthetic comment -- @@ -1526,7 +1537,7 @@
if (!file.exists()) {
file.mkdir();
setFilePermissionsFromMode(file.getPath(), mode,
                    FileUtils.S_IRWXU|FileUtils.S_IRWXG|FileUtils.S_IXOTH);
}
return file;
}
//Synthetic comment -- @@ -1627,7 +1638,6 @@
static void setFilePermissionsFromMode(String name, int mode,
int extraPermissions) {
int perms = FileUtils.S_IRUSR|FileUtils.S_IWUSR
            |FileUtils.S_IRGRP|FileUtils.S_IWGRP
|extraPermissions;
if ((mode&MODE_WORLD_READABLE) != 0) {
perms |= FileUtils.S_IROTH;
//Synthetic comment -- @@ -1658,7 +1668,7 @@

if (createDirectory && !dir.isDirectory() && dir.mkdir()) {
FileUtils.setPermissions(dir.getPath(),
                FileUtils.S_IRWXU|FileUtils.S_IRWXG|FileUtils.S_IXOTH,
-1, -1);
}









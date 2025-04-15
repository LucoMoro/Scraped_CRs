/*SDK Manager: fix File.listfiles()

This fixes a couple instances of File.listfiles() that
were not validating that either a/ the file is a directory
or b/ the list is not null.

This also adds a couple toString() methods to some repo
classes, which are nice when debugging.

Change-Id:I8912d12c5344c8b511d84a58fe4693632315dff0*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 963fc3d..27d849c 100644

//Synthetic comment -- @@ -1062,16 +1062,19 @@
* @throws SecurityException like {@link File#delete()} does if file/folder is not writable.
*/
private boolean deleteContentOf(File folder) throws SecurityException {
        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                if (deleteContentOf(f) == false) {
return false;
}
            }
            if (f.delete() == false) {
                return false;
            }

}

return true;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java
//Synthetic comment -- index 86f041c..3f2f966 100755

//Synthetic comment -- @@ -333,6 +333,22 @@
}

/**
* Generates a short description for this archive.
*/
public String getShortDescription() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java
//Synthetic comment -- index 51549f3..8d79ced 100755

//Synthetic comment -- @@ -238,8 +238,11 @@
// We're not going to check that all tools are present. At the very least
// we should expect to find android and an emulator adapted to the current OS.
Set<String> names = new HashSet<String>();
        for (File file : toolFolder.listFiles()) {
            names.add(file.getName());
}
if (!names.contains(SdkConstants.androidCmdName()) ||
!names.contains(SdkConstants.FN_EMULATOR)) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index 48c3e79..a6c28a9 100755

//Synthetic comment -- @@ -345,6 +345,22 @@
}

/**
* Returns a short description for an {@link IDescription}.
* Can be empty but not null.
*/








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index cee7fd6..db6067b 100755

//Synthetic comment -- @@ -171,6 +171,22 @@
mPackages = null;
}

public String getShortDescription() {

// TODO extract domain from URL and add to UiName if not present.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FolderWrapper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FolderWrapper.java
//Synthetic comment -- index d009b7f..a4601d3 100644

//Synthetic comment -- @@ -83,15 +83,17 @@

public IAbstractResource[] listMembers() {
File[] files = listFiles();
        final int count = files.length;
IAbstractResource[] afiles = new IAbstractResource[count];

        for (int i = 0 ; i < count ; i++) {
            File f = files[i];
            if (f.isFile()) {
                afiles[i] = new FileWrapper(f);
            } else {
                afiles[i] = new FolderWrapper(f);
}
}

//Synthetic comment -- @@ -135,7 +137,7 @@

public String[] list(FilenameFilter filter) {
File[] files = listFiles();
        if (files.length > 0) {
ArrayList<String> list = new ArrayList<String>();

for (File file : files) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java
//Synthetic comment -- index 593f007..038b86a 100755

//Synthetic comment -- @@ -177,4 +177,22 @@
}
return "";
}
}








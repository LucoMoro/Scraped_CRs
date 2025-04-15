/*SdkMan2: fix package deletion.

2 minor fixes:
- make it possible to delete a local package even if it has
  updates available.
- refresh the local sdk parser after installing/deleting
  packages to make sure we're not showing an obsolete state.

Change-Id:If3aa197cdd16f414133ce6af09696c2676cb02ac*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 53cbf73..29e0370 100755

//Synthetic comment -- @@ -901,7 +901,8 @@
if (checked != null) {
for (Object c : checked) {
if (c instanceof PkgItem) {
                    if (((PkgItem) c).getState() == PkgState.INSTALLED) {
canDelete = true;
break;
}
//Synthetic comment -- @@ -972,6 +973,8 @@
archives,
mCheckFilterObsolete.getSelection() /* includeObsoletes */);
} finally {
// loadPackages will also re-enable the UI
loadPackages();
}
//Synthetic comment -- @@ -991,18 +994,21 @@
final List<Archive> archives = new ArrayList<Archive>();

for (Object c : checked) {
            if (c instanceof PkgItem && ((PkgItem) c).getState() == PkgState.INSTALLED) {
                Package p = ((PkgItem) c).getPackage();

                Archive[] as = p.getArchives();
                if (as.length == 1 && as[0] != null && as[0].isLocal()) {
                    Archive archive = as[0];
                    String osPath = archive.getLocalOsPath();

                    File dir = new File(osPath);
                    if (dir.isDirectory()) {
                        msg += "\n - " + p.getShortDescription();
                        archives.add(archive);
}
}
}
//Synthetic comment -- @@ -1033,6 +1039,8 @@
}
});
} finally {
// loadPackages will also re-enable the UI
loadPackages();
}








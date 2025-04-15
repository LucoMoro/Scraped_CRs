/*Add --rename-manifest-package option to aapt Ant task.*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecTask.java b/anttasks/src/com/android/ant/AaptExecTask.java
//Synthetic comment -- index c01afe2..9ca8830 100644

//Synthetic comment -- @@ -50,6 +50,7 @@
* <tr><td>-0 extension</td><td>&lt;nocompress extension=""&gt;<br>&lt;nocompress&gt;</td><td>nested element(s)<br>with attribute (String)</td></tr>
* <tr><td>-F apk-file</td><td>apkfolder<br>outfolder<br>apkbasename<br>basename</td><td>attribute (Path)<br>attribute (Path) deprecated<br>attribute (String)<br>attribute (String) deprecated</td></tr>
* <tr><td>-J R-file-dir</td><td>rfolder</td><td>attribute (Path)<br>-m always enabled</td></tr>
* <tr><td></td><td></td><td></td></tr>
* </table>
*/
//Synthetic comment -- @@ -83,6 +84,7 @@
private int mVersionCode = 0;
private String mVersionName;
private String mManifest;
private ArrayList<Path> mResources;
private String mAssets;
private String mAndroidJar;
//Synthetic comment -- @@ -211,6 +213,20 @@
}

/**
* Sets the value of the "resources" attribute.
* @param resources the value.
*
//Synthetic comment -- @@ -536,6 +552,12 @@
task.createArg().setValue(mManifest);
}

// resources locations.
if (mResources.size() > 0) {
for (Path pathList : mResources) {








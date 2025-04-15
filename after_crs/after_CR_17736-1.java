/*Fix a couple of bugs.

Using File.toURL() is dangerous because the reverse operation
(URLDecoder.decode, which for example is used by URLClassLoader to
produce a path from URLs) doesn't always succeed - for example on a
filename like "/tmp/te#st". Use File.toURI().toURL() instead.

The second problem looks like an unintentional || instead of &&.

Change-Id:Ib4e2ac13c914c002eff53455ed6d6ed898ce35fb*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/TablePanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/TablePanel.java
//Synthetic comment -- index b037193..c473b23 100644

//Synthetic comment -- @@ -89,7 +89,7 @@

// now add that to the clipboard if the string has content
String data = sb.toString();
                if (data != null && data.length() > 0) {
clipboard.setContents(
new Object[] { data },
new Transfer[] { TextTransfer.getInstance() });








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/DexWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/DexWrapper.java
//Synthetic comment -- index db442bf..d9b8499 100644

//Synthetic comment -- @@ -72,7 +72,7 @@
return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, String.format(
Messages.DexWrapper_s_does_not_exists, osFilepath));
}
            URL url = f.toURI().toURL();

URLClassLoader loader = new URLClassLoader(new URL[] { url },
DexWrapper.class.getClassLoader());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectClassLoader.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectClassLoader.java
//Synthetic comment -- index 3e1259c..e4403df 100644

//Synthetic comment -- @@ -221,7 +221,7 @@
local = true;
try {
oslibraryList.add(
                                        new File(resource.getLocation().toOSString()).toURI().toURL());
} catch (MalformedURLException mue) {
// pass
}
//Synthetic comment -- @@ -235,7 +235,7 @@
File f = new File(osFullPath);
if (f.exists()) {
try {
                                    oslibraryList.add(f.toURI().toURL());
} catch (MalformedURLException mue) {
// pass
}








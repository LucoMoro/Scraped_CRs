/*Properly parse mipmap names.

The extension was not removed making the references not found.

Change-Id:Ie420ea26df5bb87b86bd5b156597962b8c49252f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java
//Synthetic comment -- index 953e57a..ea24392 100644

//Synthetic comment -- @@ -144,7 +144,7 @@
if (m.matches()) {
return m.group(1);
}
        } else if (type == ResourceType.DRAWABLE) {
for (Pattern p : sDrawablePattern) {
Matcher m = p.matcher(name);
if (m.matches()) {
//Synthetic comment -- @@ -157,6 +157,12 @@
if (m.matches()) {
return m.group(1);
}
}
return name;
}








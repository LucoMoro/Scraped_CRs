/*Fix typo in options page

On some platforms, the newlines in the strings aren't respected,
so we end up with two words squashed together. Insert an extra
space to avoid this.

Change-Id:Id7a911888df9bf5c0dd83593da666ef5f68b09dehttp://code.google.com/p/android/issues/detail?id=34570*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/EditorsPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/EditorsPage.java
//Synthetic comment -- index 0aeadc0..09261c2 100644

//Synthetic comment -- @@ -67,7 +67,7 @@

final MyBooleanFieldEditor editor = new MyBooleanFieldEditor(
AdtPrefs.PREFS_USE_CUSTOM_XML_FORMATTER,
                "Format XML files using the standard Android XML style rather than the\n" +
"configured Eclipse XML style (additional options below)",
parent);
addField(editor);
//Synthetic comment -- @@ -89,7 +89,7 @@

mIndentEditor = new BooleanFieldEditor(AdtPrefs.PREFS_USE_ECLIPSE_INDENT,
"Use Eclipse setting for indentation width and space or tab character "
                + "indentation\n(Android default is 4 space characters)",
parent);
addField(mIndentEditor);









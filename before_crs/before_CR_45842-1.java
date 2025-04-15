/*Create new XML files in configuration of selection

If you invoke the New XML File Wizard with a selection
which implies a specific configuration (such as -v11
or -port), the New XML File Wizard will now initialize
the second page's folder configuration selection with
the same configuration. The net result is that right
clicking on values-v14 and selecting New XML File
will now create the file in that folder.

Change-Id:Ia874af628e8fa936700f58d6267b973414c24d6a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/ChooseConfigurationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/ChooseConfigurationPage.java
//Synthetic comment -- index aec6b92..1d6467e 100644

//Synthetic comment -- @@ -121,6 +121,8 @@
});

setControl(composite);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index 68c7f4c..ace88a1 100644

//Synthetic comment -- @@ -733,6 +733,7 @@
}
String[] folderSegments = targetWsFolderPath.split(RES_QUALIFIER_SEP);
if (folderSegments.length > 0) {
String folderName = folderSegments[0];
selectTypeFromFolder(folderName);
}








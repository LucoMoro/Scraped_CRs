/*Fix lib properties on windows.

This is a very crude first fix while we figure out
how to properly escape/unescape all chars.

Change-Id:I783f8fdfd802e1a8ff44bbc7f6056fb607a3e523*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/AndroidPropertyPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/AndroidPropertyPage.java
//Synthetic comment -- index b956fdc..584d49f 100644

//Synthetic comment -- @@ -92,17 +92,6 @@

mLibraryDependencies = new LibraryProperties(libraryGroup);

        /*
         * APK-SPLIT: This is not yet supported, so we hide the UI
        Group g = new Group(top, SWT.NONE);
        g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        g.setLayout(new GridLayout(1, false));
        g.setText("APK Generation");

        mSplitByDensity = new Button(g, SWT.CHECK);
        mSplitByDensity.setText("One APK per density");

*/
// fill the ui
fillUi();

//Synthetic comment -- @@ -188,13 +177,6 @@

mIsLibrary.setSelection(state.isLibrary());
mLibraryDependencies.setContent(state, mPropertiesWorkingCopy);

            /*
             * APK-SPLIT: This is not yet supported, so we hide the UI
            // get the project settings
            ApkSettings settings = currentSdk.getApkSettings(mProject);
            mSplitByDensity.setSelection(settings.isSplitByDpi());
            */
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/LibraryProperties.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/LibraryProperties.java
//Synthetic comment -- index 38b4425..a734b41 100644

//Synthetic comment -- @@ -347,7 +347,7 @@
}
item.setData(data);
item.setText(0, data.relativePath);
        item.setImage( data.project != null ? mMatchIcon : mErrorIcon);
item.setText(1, data.project != null ? data.project.getName() : "?");
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java
//Synthetic comment -- index 09a35c5..9d578c4 100644

//Synthetic comment -- @@ -394,7 +394,7 @@

Matcher m = PATTERN_PROP.matcher(line);
if (m.matches()) {
                        map.put(m.group(1), m.group(2));
} else {
log.warning("Error parsing '%1$s': \"%2$s\" is not a valid syntax",
propFile.getOsLocation(),
//Synthetic comment -- @@ -430,7 +430,6 @@
return null;
}


/**
* Private constructor.
* <p/>
//Synthetic comment -- @@ -443,4 +442,12 @@
mProperties = map;
mType = type;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java
//Synthetic comment -- index 797b505..2d3f9b7 100644

//Synthetic comment -- @@ -226,8 +226,7 @@
}
}

        value = value.replaceAll("\\\\", "\\\\\\\\");
        writer.write(String.format("%s=%s\n", key, value));
}

/**








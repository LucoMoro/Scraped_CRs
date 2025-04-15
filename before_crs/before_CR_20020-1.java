/*Improve custom class loading failure handling

For background, seehttp://code.google.com/p/android/issues/detail?id=13389We currently both load and instantiate custom view classes under the
same try/catch block, and if there is a failure, the user is informed
that the class could not be -found-. However, in many cases the real
failure is in actually -instantiating- the class, so telling the user
that the class could not be found is misleading and can make the user
hunt down library dependencies, checking jar contents, etc.

This changeset improves the situation in the following ways:

- The code to load and instantiate are handled separately, and the
  list of missing classes is kept separate from the list of
  uninstantiatable classes.

- The error display in the layout editor lists these two categories
  separately.

- For instantiation errors, we dig up the root cause and log that
  one. The error display points to the Error Log for more details.

- If the class looks like it might be a custom view class (rather than
  a loading or instantiation failure in one of the Android or Add-Ons
  classes) then the error message also includes a tip about using
  View.isInEditMode to try to do less work for design time rendering.

Change-Id:I947ad91e0d7973e9c3aefed1824f61c92c5fb1ed*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 0b7e9fa..a7924c3 100644

//Synthetic comment -- @@ -43,6 +43,7 @@

private final HashMap<String, Class<?>> mLoadedClasses = new HashMap<String, Class<?>>();
private final Set<String> mMissingClasses = new TreeSet<String>();
private final IProject mProject;
private final ClassLoader mParentClassLoader;
private final ProjectResources mProjectRes;
//Synthetic comment -- @@ -60,6 +61,10 @@
return mMissingClasses;
}

/**
* {@inheritDoc}
*
//Synthetic comment -- @@ -84,18 +89,33 @@
mLoader = new ProjectClassLoader(mParentClassLoader, mProject);
}
clazz = mLoader.loadClass(className);

if (clazz != null) {
mUsed = true;
mLoadedClasses.put(className, clazz);
return instantiateClass(clazz, constructorSignature, constructorParameters);
}
        } catch (Exception e) {
            // Log this error with the class name we're trying to load and abort.
            AdtPlugin.log(e, "ProjectCallback.loadView failed to find class %1$s", className); //$NON-NLS-1$

// Add the missing class to the list so that the renderer can print them later.
            mMissingClasses.add(className);
}

// Create a mock view instead. We don't cache it in the mLoadedClasses map.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 23bbc1f..d7bc7d5 100644

//Synthetic comment -- @@ -1433,10 +1433,11 @@
// Success means there was no exception. But we might have detected
// some missing classes and swapped them by a mock view.
Set<String> missingClasses = mProjectCallback.getMissingClasses();
            if (missingClasses.size() > 0) {
                displayMissingClasses(missingClasses);
} else {
                // Nope, no missing classes. Clear success, congrats!
hideError();
}
}
//Synthetic comment -- @@ -1755,13 +1756,39 @@
* Switches the sash to display the error label to show a list of
* missing classes and give options to create them.
*/
    private void displayMissingClasses(Set<String> missingClasses) {
mErrorLabel.setText("");
        addText(mErrorLabel, "The following classes could not be found:\n");
        for (String clazz : missingClasses) {
            addText(mErrorLabel, "- ");
            addClassLink(mErrorLabel, clazz);
            addText(mErrorLabel, "\n");
}

mSashError.setMaximizedControl(null);








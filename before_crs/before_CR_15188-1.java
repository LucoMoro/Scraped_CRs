/*Fix library-project link for library whose name isn't a valid path var.

Change-Id:I42414bc8d560b57dbd58302a7a00203472a0d937*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 2503185..ef044ba 100644

//Synthetic comment -- @@ -949,21 +949,20 @@
ResourcesPlugin.getWorkspace().getPathVariableManager();
IPath libPath = libProject.getLocation();

        final String libName = libProject.getName();
        final String varName = "_android_" + libName; //$NON-NLS-1$

if (libPath.equals(pathVarMgr.getValue(varName)) == false) {
try {
pathVarMgr.setValue(varName, libPath);
} catch (CoreException e) {
                String message = String.format(
                        "Unable to set linked path var '%1$s' for library %2$s", //$NON-NLS-1$
                        varName, libPath.toOSString());
                AdtPlugin.log(e, message);
}
}
}

private void disposeLibraryProject(IProject project) {
disposeLibraryProject(project.getName());
}
//Synthetic comment -- @@ -972,7 +971,7 @@
IPathVariableManager pathVarMgr =
ResourcesPlugin.getWorkspace().getPathVariableManager();

        final String varName = "_android_" + libName; //$NON-NLS-1$

// remove the value by setting the value to null.
try {
//Synthetic comment -- @@ -985,6 +984,14 @@
}

/**
* Links a project and a library so that the project can use the library code and resources.
* <p/>This can be done in a job in case the workspace is not locked for resource
* modification. See <var>doInJob</var>.
//Synthetic comment -- @@ -1040,7 +1047,7 @@

// add a linked resource for the source of the library and add it to the project
final String libName = library.getName();
                    final String varName = "_android_" + libName; //$NON-NLS-1$

// create a linked resource for the library using the path var.
IFolder libSrc = project.getFolder(libName);
//Synthetic comment -- @@ -1093,6 +1100,10 @@

return Status.OK_STATUS;
} catch (CoreException e) {
return e.getStatus();
}
}








/*Issue 6894:   Eclipse ADT layout editor can't handle custom Views: ClassCastException (multiple ClassLoaders?)

Change-Id:I7f273613891c56c0983890270a724777a659eb7c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 46461b0..1df5bbe 100644

//Synthetic comment -- @@ -45,6 +45,7 @@
private final ProjectResources mProjectRes;
private boolean mUsed = false;
private String mNamespace;

public ProjectCallback(ClassLoader classLoader, ProjectResources projectRes, IProject project) {
mParentClassLoader = classLoader;
//Synthetic comment -- @@ -74,9 +75,12 @@
}

// load the class.
        ProjectClassLoader loader = new ProjectClassLoader(mParentClassLoader, mProject);
try {
            clazz = loader.loadClass(className);

if (clazz != null) {
mUsed = true;
//Synthetic comment -- @@ -94,7 +98,7 @@
// Create a mock view instead. We don't cache it in the mLoadedClasses map.
// If any exception is thrown, we'll return a CFN with the original class name instead.
try {
            clazz = loader.loadClass(SdkConstants.CLASS_MOCK_VIEW);
Object view = instantiateClass(clazz, constructorSignature, constructorParameters);

// Set the text of the mock view to the simplified name of the custom class








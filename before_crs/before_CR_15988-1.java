/*Fix a potential npe preventing the library project selection from working.

If the workspace has broken projects, Sdk.getProjectState() can return
null for them, causing an NPE in the IProjectChooserFilter.

I had fixed NonLibraryProjectOnlyFilter but apparently, not
LibraryProjectOnlyFilter.

Change-Id:I9a85ce61885888e0107acf2f9cb5e3491ed8f1fb*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectChooserHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectChooserHelper.java
//Synthetic comment -- index 2b80d31..27b26bf 100644

//Synthetic comment -- @@ -84,7 +84,11 @@
public final static class LibraryProjectOnlyFilter implements IProjectChooserFilter {
public boolean accept(IProject project) {
ProjectState state = Sdk.getProjectState(project);
            return state.isLibrary();
}

public boolean useCache() {








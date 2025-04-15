/*Fix the library project filter to not NPE on broken projects.

Change-Id:I51ec8f699b631171fcfa92ce8cc658ba1f50e017*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectChooserHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectChooserHelper.java
//Synthetic comment -- index a7a6e72..aa5813a 100644

//Synthetic comment -- @@ -65,7 +65,11 @@
public final static class NonLibraryProjectOnlyFilter implements IProjectChooserFilter {
public boolean accept(IProject project) {
ProjectState state = Sdk.getProjectState(project);
            if (state != null) {
                return state.isLibrary() == false;
            }

            return false;
}

public boolean useCache() {








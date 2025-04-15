/*Import projects under a workspace runnable.

This makes sure that the project creation is done atomically.
Similar to 55b42b37afc532db41f but for the Import wizard
rather than the New Project Wizard.

Change-Id:I5112d7d550ba9841a49c9d56921a63906a7c1230*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java
//Synthetic comment -- index dac4889..3a6e777 100644

//Synthetic comment -- @@ -94,6 +94,7 @@
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
* The actual project creator invoked from the New Project Wizard
//Synthetic comment -- @@ -282,7 +283,20 @@
*/
public boolean createAndroidProjects() {
if (mValues.importProjects != null && !mValues.importProjects.isEmpty()) {
            return importProjects();
}

final ProjectInfo mainData = collectMainPageInfo();








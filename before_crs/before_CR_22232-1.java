/*Turn off palette previews during unit tests

Set the default palette mode to icon+text rather than preview during
unit test runs. Since each test run will create a blank workspace, the
previews have to be recomputed each time, which adds a couple of
seconds. Also, there are some problems on Windows when the previewing
code runs under unit tests, which this will work around.

Change-Id:Ic6a7bdaee731a219ff6042075977e12b0eeda111*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/AdtProjectTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/AdtProjectTest.java
//Synthetic comment -- index 85cca2b..b82d5ba 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreationPage;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectWizard;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewTestProjectCreationPage;
//Synthetic comment -- @@ -87,6 +88,9 @@
protected void setUp() throws Exception {
super.setUp();

getProject();
}









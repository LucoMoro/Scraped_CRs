/*NPE fix

Change-Id:I35d0407ef20557c19b6eca5324075de9fbbf8583*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 4cd5e8a..6018d17 100644

//Synthetic comment -- @@ -29,7 +29,6 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder;
import com.android.ide.eclipse.adt.internal.editors.menu.MenuEditor;
import com.android.ide.eclipse.adt.internal.editors.resources.ResourcesEditor;
import com.android.ide.eclipse.adt.internal.editors.xml.XmlEditor;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
//Synthetic comment -- @@ -1819,8 +1818,18 @@
* @throws PartInitException if something goes wrong
*/
public static void openFile(IFile file, IRegion region) throws PartInitException {
        IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench == null) {
            return;
        }
        IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
        if (activeWorkbenchWindow == null) {
            return;
        }
        IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
        if (page == null) {
            return;
        }
IEditorPart targetEditor = IDE.openEditor(page, file, true);
if (targetEditor instanceof AndroidXmlEditor) {
AndroidXmlEditor editor = (AndroidXmlEditor) targetEditor;








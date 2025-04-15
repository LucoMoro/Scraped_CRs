/*ADT GLE2: Properly handle custom classes.

This makes it possible to drag'n'drop or paste a custom
class in a project that doesn't have the class. The paste
operation will succeed and the layout rendering will provide
a clear error indicating the class is missing.

Change-Id:I80bbc70cfdba68277120dff3e770ce31651e7ebc*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index fd5198f..ab3b1f5 100644

//Synthetic comment -- @@ -28,8 +28,8 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle1.GraphicalLayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle1.UiContentOutlinePage;
import com.android.ide.eclipse.adt.internal.editors.layout.gle1.UiPropertySheetPage;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.OutlinePage2;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.PropertySheetPage2;
import com.android.ide.eclipse.adt.internal.editors.ui.tree.UiActions;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
//Synthetic comment -- @@ -496,54 +496,66 @@
}

/**
     * Creates a new {@link ElementDescriptor} for an unknown XML local name
* (i.e. one that was not mapped by the current descriptors).
* <p/>
* Since we deal with layouts, we returns either a descriptor for a custom view
* or one for the base View.
*
* @param xmlLocalName The XML local name to match.
     * @return A non-null {@link ElementDescriptor}.
*/
    private ElementDescriptor createUnknownDescriptor(String xmlLocalName) {
IEditorInput editorInput = getEditorInput();
if (editorInput instanceof IFileEditorInput) {
IFileEditorInput fileInput = (IFileEditorInput)editorInput;
IProject project = fileInput.getFile().getProject();

// Check if we can find a custom view specific to this project.
            ElementDescriptor desc = CustomViewDescriptorService.getInstance().getDescriptor(
                    project, xmlLocalName);

            if (desc != null) {
                return desc;
            }

            // If we didn't find a custom view, reuse the base View descriptor.
            // This is a layout after all, so every XML node should represent
            // a view.

            Sdk currentSdk = Sdk.getCurrent();
            if (currentSdk != null) {
                IAndroidTarget target = currentSdk.getTarget(project);
                if (target != null) {
                    AndroidTargetData data = currentSdk.getTargetData(target);
                    if (data != null) {
                        // data can be null when the target is still loading
                        desc = data.getLayoutDescriptors().getBaseViewDescriptor();
}
}
}

            if (desc != null) {
                return desc;
            }
}

        // We get here if the editor input is not of the right type or if the
        // SDK hasn't finished loading. In either case, return something just
        // because we should not return null.
        return new ViewElementDescriptor(xmlLocalName, xmlLocalName);
}

private void onDescriptorsChanged(Document document) {
//Synthetic comment -- @@ -584,18 +596,26 @@
* Will return null if we can't find that FQCN or we lack the editor/data/descriptors info.
*/
public ViewElementDescriptor getFqcnViewDescritor(String fqcn) {
AndroidTargetData data = getTargetData();
if (data != null) {
LayoutDescriptors layoutDesc = data.getLayoutDescriptors();
if (layoutDesc != null) {
DocumentDescriptor docDesc = layoutDesc.getDescriptor();
if (docDesc != null) {
                    return internalFindFqcnViewDescritor(fqcn, docDesc.getChildren(), null);
}
}
}

        return null;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java
//Synthetic comment -- index 3ba5303..d290b05 100644

//Synthetic comment -- @@ -35,18 +35,18 @@
import java.util.List;

/**
 * Service responsible for creating/managing {@link ElementDescriptor} objects for custom
* View classes per project.
* <p/>
* The service provides an on-demand monitoring of custom classes to check for changes. Monitoring
 * starts once a request for an {@link ElementDescriptor} object has been done for a specific
 * class.<br>
 * The monitoring will notify a listen of any changes in the class triggering a change in its
 * associated {@link ElementDescriptor} object.
* <p/>
* If the custom class does not exist, no monitoring is put in place to avoid having to listen
* to all class changes in the projects.
 *
*/
public final class CustomViewDescriptorService {

//Synthetic comment -- @@ -56,13 +56,13 @@
* Map where keys are the project, and values are another map containing all the known
* custom View class for this project. The custom View class are stored in a map
* where the keys are the fully qualified class name, and the values are their associated
     * {@link ElementDescriptor}.
*/
    private HashMap<IProject, HashMap<String, ElementDescriptor>> mCustomDescriptorMap =
        new HashMap<IProject, HashMap<String, ElementDescriptor>>();

/**
     * TODO will be used to update the ElementDescriptor of the custom view when it
* is modified (either the class itself or its attributes.xml)
*/
@SuppressWarnings("unused")
//Synthetic comment -- @@ -74,12 +74,16 @@
*/
public interface ICustomViewDescriptorListener {
/**
         * Sent when a custom View class has changed and its {@link ElementDescriptor} was modified.
* @param project the project containing the class.
* @param className the fully qualified class name.
* @param descriptor the updated ElementDescriptor.
*/
        public void updatedClassInfo(IProject project, String className, ElementDescriptor descriptor);
}

/**
//Synthetic comment -- @@ -93,7 +97,7 @@
* Sets the listener receiving custom View class modification notifications.
* @param listener the listener to receive the notifications.
*
     * TODO will be used to update the ElementDescriptor of the custom view when it
* is modified (either the class itself or its attributes.xml)
*/
public void setListener(ICustomViewDescriptorListener listener) {
//Synthetic comment -- @@ -101,24 +105,28 @@
}

/**
     * Returns the {@link ElementDescriptor} for a particular project/class.
* <p/>
     * If it is the first time the <code>ElementDescriptor</code> is requested, the method
* will check that the specified class is in fact a custom View class. Once this is
* established, a monitoring for that particular class is initiated. Any change will
* trigger a notification to the {@link ICustomViewDescriptorListener}.
* @param project the project containing the class.
* @param fqcn the fully qualified name of the class.
     * @return a <code>ElementDescriptor</code> or <code>null</code> if the class was not
     * a custom View class.
*/
    public ElementDescriptor getDescriptor(IProject project, String fqcn) {
// look in the map first
synchronized (mCustomDescriptorMap) {
            HashMap<String, ElementDescriptor> map = mCustomDescriptorMap.get(project);

if (map != null) {
                ElementDescriptor descriptor = map.get(fqcn);
if (descriptor != null) {
return descriptor;
}
//Synthetic comment -- @@ -144,11 +152,11 @@
ITypeHierarchy hierarchy = type.newSupertypeHierarchy(
new NullProgressMonitor());

                    ElementDescriptor parentDescriptor = getDescriptor(
hierarchy.getSuperclass(type), project, hierarchy);

if (parentDescriptor != null) {
                        // we have a valid parent, lets create a new ElementDescriptor.

ViewElementDescriptor descriptor = new ViewElementDescriptor(fqcn,
fqcn, // ui_name
//Synthetic comment -- @@ -160,10 +168,12 @@
null, // children
false /* mandatory */);

synchronized (mCustomDescriptorMap) {
map = mCustomDescriptorMap.get(project);
if (map == null) {
                                map = new HashMap<String, ElementDescriptor>();
mCustomDescriptorMap.put(project, map);
}

//Synthetic comment -- @@ -180,19 +190,15 @@
}
}


return null;
}

/**
     * Computes (if needed) and returns the {@link ElementDescriptor} for the specified type.
*
     * @param type
     * @param project
     * @param typeHierarchy
     * @return A ViewElementDescriptor or null if type or typeHierarchy is null.
*/
    private ViewElementDescriptor getDescriptor(IType type, IProject project,
ITypeHierarchy typeHierarchy) {
// check if the type is a built-in View class.
List<ElementDescriptor> builtInList = null;
//Synthetic comment -- @@ -232,7 +238,7 @@

IType parentType = typeHierarchy.getSuperclass(type);
if (parentType != null) {
            ViewElementDescriptor parentDescriptor = getDescriptor(parentType, project,
typeHierarchy);

if (parentDescriptor != null) {
//Synthetic comment -- @@ -248,12 +254,14 @@
null, // children
false /* mandatory */);

// add it to the map
synchronized (mCustomDescriptorMap) {
                    HashMap<String, ElementDescriptor> map = mCustomDescriptorMap.get(project);

if (map == null) {
                        map = new HashMap<String, ElementDescriptor>();
mCustomDescriptorMap.put(project, map);
}

//Synthetic comment -- @@ -277,10 +285,10 @@
* The array should contain the descriptor for this type and all its supertypes.
*
* @param type the type for which the {@link AttributeDescriptor} are returned.
     * @param parentDescriptor the {@link ElementDescriptor} of the direct superclass.
*/
private AttributeDescriptor[] getAttributeDescriptor(IType type,
            ElementDescriptor parentDescriptor) {
// TODO add the class attribute descriptors to the parent descriptors.
return parentDescriptor.getAttributes();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java
//Synthetic comment -- index 16eacc0..5972df6 100644

//Synthetic comment -- @@ -60,7 +60,7 @@
private List<ElementDescriptor> mROViewDescriptors;

/** The descriptor matching android.view.View. */
    private ElementDescriptor mBaseViewDescriptor;

/** Returns the document descriptor. Contains all layouts and views linked together. */
public DocumentDescriptor getDescriptor() {
//Synthetic comment -- @@ -82,9 +82,10 @@
}

/**
     * Returns the descriptor matching android.view.View.
*/
    public ElementDescriptor getBaseViewDescriptor() {
if (mBaseViewDescriptor == null) {
for (ElementDescriptor desc : mViewDescriptors) {
if (desc instanceof ViewElementDescriptor) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 0e21958..12ac0b3 100755

//Synthetic comment -- @@ -31,6 +31,9 @@
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFolderListener;
import com.android.sdklib.SdkConstants;

import org.codehaus.groovy.control.CompilationFailedException;
//Synthetic comment -- @@ -354,6 +357,22 @@

// ---- private ---

private class ProjectFolderListener implements IFolderListener {
public void folderChanged(IFolder folder, int kind) {
if (folder.getProject() == mProject &&
//Synthetic comment -- @@ -396,16 +415,22 @@
private IViewRule loadRule(UiViewElementNode element) {
if (element == null) {
return null;
        } else {
            // sanity check. this can't fail.
            ElementDescriptor d = element.getDescriptor();
            if (d == null || !(d instanceof ViewElementDescriptor)) {
                return null;
            }
}

String targetFqcn = null;
        ViewElementDescriptor targetDesc = (ViewElementDescriptor) element.getDescriptor();

// Return the rule if we find it in the cache, even if it was stored as null
// (which means we didn't find it earlier, so don't look for it again)
//Synthetic comment -- @@ -422,6 +447,7 @@
// Get the FQCN of this View
String fqcn = desc.getFullClassName();
if (fqcn == null) {
return null;
}









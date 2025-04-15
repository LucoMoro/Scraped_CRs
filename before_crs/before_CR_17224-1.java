/*GLE2: fix layout editor not properly closed when dirty.

The symptom was that once the layout editor had a modification
and was in the "dirty" mode, closing it would keep the model
around and re-opening would show the previous state. That was
a red herring.

The actual issue is that the undo begin/end wrapper methods
were calling getModelForEdit() without doing a proper
releaseFromEdit() call after the fact. Somewhere down in the
overly complex code that disposes the EditorPart there is
a test that would not purge the model if it is still locked.

The fix is thus in the begin/endUndoRecording of the base
AndroidXmlEditor.

This CL also cleans up some IFile usage; when fixing the
code above I noticed we can now get the model without
first getting an SSE internal document, instead we can use
the editor's IFile. However I doubt SSE for 3.3 would have
the API, I need to check that in another CL. Later, it's a P3.

Change-Id:I2437475dfeee9d6689b7b604782ae140d7aff1c3*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidTextEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidTextEditor.java
//Synthetic comment -- index d180b5e..e524826 100755

//Synthetic comment -- @@ -318,6 +318,20 @@
}

/**
* Removes attached listeners.
*
* @see WorkbenchPart








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index ed5e79d..c3970d3 100644

//Synthetic comment -- @@ -56,7 +56,6 @@
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.internal.browser.WorkbenchBrowserSupport;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.WorkbenchPart;
import org.eclipse.wst.sse.core.StructuredModelManager;
//Synthetic comment -- @@ -249,9 +248,8 @@
*/
protected void selectDefaultPage(String defaultPageId) {
if (defaultPageId == null) {
            if (getEditorInput() instanceof IFileEditorInput) {
                IFile file = ((IFileEditorInput) getEditorInput()).getFile();

QualifiedName qname = new QualifiedName(AdtPlugin.PLUGIN_ID,
getClass().getSimpleName() + PREF_CURRENT_PAGE);
String pageId;
//Synthetic comment -- @@ -323,9 +321,8 @@
return;
}

        if (getEditorInput() instanceof IFileEditorInput) {
            IFile file = ((IFileEditorInput) getEditorInput()).getFile();

QualifiedName qname = new QualifiedName(AdtPlugin.PLUGIN_ID,
getClass().getSimpleName() + PREF_CURRENT_PAGE);
try {
//Synthetic comment -- @@ -345,21 +342,19 @@
*/
public void resourceChanged(final IResourceChangeEvent event) {
if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    IWorkbenchPage[] pages = getSite().getWorkbenchWindow()
                            .getPages();
                    for (int i = 0; i < pages.length; i++) {
                        if (((FileEditorInput)mTextEditor.getEditorInput())
                                .getFile().getProject().equals(
                                        event.getResource())) {
                            IEditorPart editorPart = pages[i].findEditor(mTextEditor
                                    .getEditorInput());
pages[i].closeEditor(editorPart, true);
}
}
                }
            });
}
}

//Synthetic comment -- @@ -378,6 +373,21 @@
}

/**
* Removes attached listeners.
*
* @see WorkbenchPart
//Synthetic comment -- @@ -626,6 +636,11 @@
if (document != null) {
IModelManager mm = StructuredModelManager.getModelManager();
if (mm != null) {
return mm.getModelForRead(document);
}
}
//Synthetic comment -- @@ -636,18 +651,25 @@
* Returns a version of the model that has been shared for edit.
* <p/>
* Callers <em>must</em> call model.releaseFromEdit() when done, typically
     * in a try..finally clause. Because of this, it is highly recommended
     * to <b>NOT</b> use this method directly and instead use the wrapper
* {@link #wrapEditXmlModel(Runnable)} which executes a runnable into a
* properly configured model and then performs whatever cleanup is necessary.
*
* @return The model for the XML document or null if cannot be obtained from the editor
*/
    public final IStructuredModel getModelForEdit() {
IStructuredDocument document = getStructuredDocument();
if (document != null) {
IModelManager mm = StructuredModelManager.getModelManager();
if (mm != null) {
return mm.getModelForEdit(document);
}
}
//Synthetic comment -- @@ -700,7 +722,7 @@

if (mIsEditXmlModelPending < 0) {
AdtPlugin.log(IStatus.ERROR,
                            "wrapEditXmlModel finished with invalid nested counter==%d", //$NON-NLS-1$
mIsEditXmlModelPending);
mIsEditXmlModelPending = 0;
}
//Synthetic comment -- @@ -770,16 +792,14 @@
* @return True if the undo recording actually started, false if any kind of error occured.
*         {@link #endUndoRecording()} should only be called if True is returned.
*/
    private final boolean beginUndoRecording(String label) {
        IStructuredDocument document = getStructuredDocument();
        if (document != null) {
            IModelManager mm = StructuredModelManager.getModelManager();
            if (mm != null) {
                IStructuredModel model = mm.getModelForEdit(document);
                if (model != null) {
                    model.beginRecording(this, label);
                    return true;
                }
}
}
return false;
//Synthetic comment -- @@ -792,15 +812,13 @@
* used if the initial call returned true.
* To guarantee that, only access this via {@link #wrapUndoEditXmlModel(String, Runnable)}.
*/
    private final void endUndoRecording() {
        IStructuredDocument document = getStructuredDocument();
        if (document != null) {
            IModelManager mm = StructuredModelManager.getModelManager();
            if (mm != null) {
                IStructuredModel model = mm.getModelForEdit(document);
                if (model != null) {
                    model.endRecording(this);
                }
}
}
}
//Synthetic comment -- @@ -825,16 +843,9 @@
* Returns the {@link IProject} for the edited file.
*/
public IProject getProject() {
        if (mTextEditor != null) {
            IEditorInput input = mTextEditor.getEditorInput();
            if (input instanceof FileEditorInput) {
                FileEditorInput fileInput = (FileEditorInput)input;
                IFile inputFile = fileInput.getFile();

                if (inputFile != null) {
                    return inputFile.getProject();
                }
            }
}

return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/MatchingStrategy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/MatchingStrategy.java
//Synthetic comment -- index a6a19a6..c6c5261 100644

//Synthetic comment -- @@ -37,11 +37,11 @@
// first check that the file being opened is a layout file.
if (input instanceof FileEditorInput) {
FileEditorInput fileInput = (FileEditorInput)input;
            
// get the IFile object and check it's in one of the layout folders.
IFile iFile = fileInput.getFile();
ResourceFolder resFolder = ResourceManager.getInstance().getResourceFolder(iFile);
            
// if it's a layout, we know check the name of the fileInput against the name of the
// file being currently edited by the editor since those are independent of the config.
if (resFolder != null && resFolder.getType() == ResourceFolderType.LAYOUT) {
//Synthetic comment -- @@ -50,7 +50,7 @@
if (editorInput instanceof FileEditorInput) {
FileEditorInput editorFileInput = (FileEditorInput)editorInput;
IFile editorIFile = editorFileInput.getFile();
                        
return editorIFile.getProject().equals(iFile.getProject())
&& editorIFile.getName().equals(iFile.getName());
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java
//Synthetic comment -- index edf0103..6e0936b 100644

//Synthetic comment -- @@ -41,7 +41,7 @@
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

//Synthetic comment -- @@ -198,8 +198,15 @@
}

private void onDescriptorsChanged() {
        Node node = getManifestXmlNode(getXmlDocument(getModelForRead()));
        mUiManifestNode.reloadFromXmlNode(node);

if (mOverviewPage != null) {
mOverviewPage.refreshUiApplicationNode();
//Synthetic comment -- @@ -234,7 +241,8 @@
}
};

            GlobalProjectMonitor.getMonitor().addFileListener(mMarkerMonitor, IResourceDelta.CHANGED);
}
}

//Synthetic comment -- @@ -246,17 +254,17 @@
private void updateFromExistingMarkers(IFile inputFile) {
try {
// get the markers for the file
            IMarker[] markers = inputFile.findMarkers(AndroidConstants.MARKER_ANDROID, true,
                    IResource.DEPTH_ZERO);

AndroidManifestDescriptors desc = getManifestDescriptors();
if (desc != null) {
ElementDescriptor appElement = desc.getApplicationElement();

                if (appElement != null) {
                    UiElementNode app_ui_node = mUiManifestNode.findUiChildNode(
appElement.getXmlName());
                    List<UiElementNode> children = app_ui_node.getUiChildren();

for (IMarker marker : markers) {
processMarker(marker, children, IResourceDelta.ADDED);
//Synthetic comment -- @@ -378,17 +386,4 @@
mUiManifestNode.setEditor(this);
}
}

    /**
     * Returns the {@link IFile} being edited, or <code>null</code> if it couldn't be computed.
     */
    private IFile getInputFile() {
        IEditorInput input = getEditorInput();
        if (input instanceof FileEditorInput) {
            FileEditorInput fileInput = (FileEditorInput) input;
            return fileInput.getFile();
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/PasteAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/PasteAction.java
//Synthetic comment -- index d649af7..9cd7412 100644

//Synthetic comment -- @@ -27,7 +27,6 @@
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
//Synthetic comment -- @@ -63,61 +62,65 @@
@Override
public void run() {
super.run();
        
final String data = (String) mClipboard.getContents(TextTransfer.getInstance());
if (data != null) {
            IStructuredModel model = mEditor.getModelForEdit();
            try {
                IStructuredDocument sse_doc = mEditor.getStructuredDocument();
                if (sse_doc != null) {
                    if (mUiNode.getDescriptor().hasChildren()) {
                        // This UI Node can have children. The new XML is
                        // inserted as the first child.
                        
                        if (mUiNode.getUiChildren().size() > 0) {
                            // There's already at least one child, so insert right before it.
                            Node xml_node = mUiNode.getUiChildren().get(0).getXmlNode();
                            if (xml_node instanceof IndexedRegion) { // implies xml_node != null
                                IndexedRegion region = (IndexedRegion) xml_node;
                                sse_doc.replace(region.getStartOffset(), 0, data);
                                return; // we're done, no need to try the other cases
                            }                                
                        }
                        
                        // If there's no first XML node child. Create one by
                        // inserting at the end of the *start* tag.
                        Node xml_node = mUiNode.getXmlNode();
                        if (xml_node instanceof NodeContainer) {
                            NodeContainer container = (NodeContainer) xml_node;
                            IStructuredDocumentRegion start_tag =
                                container.getStartStructuredDocumentRegion();
                            if (start_tag != null) {
                                sse_doc.replace(start_tag.getEndOffset(), 0, data);
                                return; // we're done, no need to try the other case
}
}
                    }
                    
                    // This UI Node doesn't accept children. The new XML is inserted as the
                    // next sibling. This also serves as a fallback if all the previous
                    // attempts failed. However, this is not possible if the current node
                    // has for parent a document -- an XML document can only have one root,
                    // with no siblings.
                    if (!(mUiNode.getUiParent() instanceof UiDocumentNode)) {
                        Node xml_node = mUiNode.getXmlNode();
                        if (xml_node instanceof IndexedRegion) {
                            IndexedRegion region = (IndexedRegion) xml_node;
                            sse_doc.replace(region.getEndOffset(), 0, data);
                        }
}
}

            } catch (BadLocationException e) {
                AdtPlugin.log(e, "ParseAction failed for UI Node %2$s, content '%1$s'", //$NON-NLS-1$
                        mUiNode.getBreadcrumbTrailDescription(true), data);
            } finally {
                model.releaseFromEdit();
            }
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiElementDetail.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiElementDetail.java
//Synthetic comment -- index 0e160b9..b0cd84a 100644

//Synthetic comment -- @@ -73,13 +73,13 @@
private IManagedForm mManagedForm;

private final UiTreeBlock mTree;
    
public UiElementDetail(UiTreeBlock tree) {
mTree = tree;
mMasterPart = mTree.getMasterPart();
mManagedForm = mMasterPart.getManagedForm();
}
    
/* (non-java doc)
* Initializes the part.
*/
//Synthetic comment -- @@ -117,25 +117,21 @@
* Instructs it to commit the new (modified) data back into the model.
*/
public void commit(boolean onSave) {
        
        IStructuredModel model = mTree.getEditor().getModelForEdit();
        try {
            // Notify the model we're about to change data...
            model.aboutToChangeModel();

            if (mCurrentUiElementNode != null) {
                mCurrentUiElementNode.commit();
}
            
            // Finally reset the dirty flag if everything was saved properly
            mIsDirty = false;
        } catch (Exception e) {
            AdtPlugin.log(e, "Detail node failed to commit XML attribute!"); //$NON-NLS-1$
        } finally {
            // Notify the model we're done modifying it. This must *always* be executed.
            model.changedModel();
            model.releaseFromEdit();
        }
}

public void dispose() {
//Synthetic comment -- @@ -183,7 +179,7 @@

/**
* Creates a TableWrapLayout in the DetailsPage, which in turns contains a Section.
     * 
* All the UI should be created in a layout which parent is the mSection itself.
* The hierarchy is:
* <pre>
//Synthetic comment -- @@ -194,7 +190,7 @@
*       + Labels/Forms/etc... [*]
* </pre>
* Both items marked with [*] are created by the derived classes to fit their needs.
     * 
* @param parent Parent of the mSection (from createContents)
* @return The new Section
*/
//Synthetic comment -- @@ -215,7 +211,7 @@
* <p/>
* This is called by the constructor.
* Derived classes can override this if necessary.
     * 
* @param managedForm The managed form
*/
private void createUiAttributeControls(
//Synthetic comment -- @@ -258,7 +254,7 @@
mCurrentTable = masterTable;

mCurrentUiElementNode = ui_node;
                
if (elem_desc.getTooltip() != null) {
String tooltip;
if (Sdk.getCurrent() != null &&
//Synthetic comment -- @@ -285,7 +281,7 @@
"Malformed javadoc, rejected by FormText for node %1$s: '%2$s'", //$NON-NLS-1$
ui_node.getDescriptor().getXmlName(),
tooltip);
                    
// Fallback to a pure text tooltip, no fancy HTML
tooltip = DescriptorsUtils.formatTooltip(elem_desc.getTooltip());
Label label = SectionHelper.createLabel(masterTable, toolkit,
//Synthetic comment -- @@ -294,7 +290,7 @@
}

Composite table = useSubsections ? null : masterTable;
            
for (AttributeDescriptor attr_desc : attr_desc_list) {
if (attr_desc instanceof XmlnsAttributeDescriptor) {
// Do not show hidden attributes
//Synthetic comment -- @@ -318,7 +314,7 @@

if (ui_attr != null) {
ui_attr.createUiControl(table, managedForm);
                    
if (ui_attr.getCurrentValue() != null &&
ui_attr.getCurrentValue().length() > 0) {
((Section) table.getParent()).setExpanded(true);
//Synthetic comment -- @@ -339,7 +335,7 @@
"Unknown XML Attributes");
unknownTable.getParent().setVisible(false); // set section to not visible
final HashSet<UiAttributeNode> reference = new HashSet<UiAttributeNode>();
            
final IUiUpdateListener updateListener = new IUiUpdateListener() {
public void uiElementNodeUpdated(UiElementNode ui_node, UiUpdateState state) {
if (state == UiUpdateState.ATTR_UPDATED) {
//Synthetic comment -- @@ -349,16 +345,16 @@
}
};
ui_node.addUpdateListener(updateListener);
            
// remove the listener when the UI is disposed
unknownTable.addDisposeListener(new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
ui_node.removeUpdateListener(updateListener);
}
});
            
updateUnknownAttributesSection(ui_node, unknownTable, managedForm, reference);
            
mMasterSection.getParent().pack(true /* changed */);
}
}
//Synthetic comment -- @@ -369,7 +365,7 @@
*/
private Composite createSubSectionTable(FormToolkit toolkit,
Composite masterTable, String title) {
        
// The Section composite seems to ignore colspan when assigned a TableWrapData so
// if the parent is a table with more than one column an extra table with one column
// is inserted to respect colspan.
//Synthetic comment -- @@ -381,7 +377,7 @@
twd.colspan = parentNumCol;
masterTable.setLayoutData(twd);
}
        
Composite table;
Section section = toolkit.createSection(masterTable,
Section.TITLE_BAR | Section.TWISTIE);
//Synthetic comment -- @@ -449,28 +445,28 @@
if (has_differences) {
needs_reflow = true;
reference.clear();
            
// Remove all children of the table
for (Control c : unknownTable.getChildren()) {
c.dispose();
}
    
// Recreate all attributes UI
for (UiAttributeNode ui_attr : ui_attrs) {
reference.add(ui_attr);
ui_attr.createUiControl(unknownTable, managedForm);
    
if (ui_attr.getCurrentValue() != null && ui_attr.getCurrentValue().length() > 0) {
section.setExpanded(true);
}
}
}
        
if (needs_reflow) {
reflowMasterSection();
}
}
    
/**
* Marks the part dirty. Called as a result of user interaction with the widgets in the
* section.








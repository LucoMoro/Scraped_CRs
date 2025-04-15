/*Add support for adding non-String value resources

The ResourceChooser only supported adding new Strings. This changeset
lets you add other types of value resources -- dimensions, integers,
etc. It will create a new file in res/values/ if necessary, based on
the plural form of the resource name (e.g. for "string" resources it
will create "strings.xml", etc). For existing files, it will add a new
entry to the existing file, using the same indentation as the last
top-level element in the file.

Change-Id:I09272ff52af38a8a7a059d455f398befbe0d1abc*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 067c731..89edee4 100644

//Synthetic comment -- @@ -1128,11 +1128,21 @@
*         not be computed.
*/
public String getIndent(Node xmlNode) {
        return getIndent(getStructuredDocument(), xmlNode);
    }

    /**
     * Returns the indentation String of the given node.
     *
     * @param document The Eclipse document containing the XML
     * @param xmlNode The node whose indentation we want.
     * @return The indent-string of the given node, or "" if the indentation for some reason could
     *         not be computed.
     */
    public static String getIndent(IStructuredDocument document, Node xmlNode) {
        assert xmlNode.getNodeType() == Node.ELEMENT_NODE;
if (xmlNode instanceof IndexedRegion) {
IndexedRegion region = (IndexedRegion)xmlNode;
int startOffset = region.getStartOffset();
return getIndentAtOffset(document, startOffset);
}
//Synthetic comment -- @@ -1148,7 +1158,7 @@
* @return The indent-string of the given node, or "" if the indentation for some
*         reason could not be computed.
*/
    public static String getIndentAtOffset(IStructuredDocument document, int offset) {
try {
IRegion lineInformation = document.getLineInformationOfOffset(offset);
if (lineInformation != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index 9ca5ae7..fedca9c 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import static com.android.ide.eclipse.adt.AndroidConstants.WS_LAYOUTS;
import static com.android.ide.eclipse.adt.AndroidConstants.WS_SEP;
import static com.android.sdklib.SdkConstants.FD_LAYOUT;
import static org.eclipse.core.resources.IResourceDelta.ADDED;
import static org.eclipse.core.resources.IResourceDelta.CHANGED;
import static org.eclipse.core.resources.IResourceDelta.CONTENT;
//Synthetic comment -- @@ -77,6 +76,7 @@
* The include finder finds other XML files that are including a given XML file, and does
* so efficiently (caching results across IDE sessions etc).
*/
@SuppressWarnings("restriction") // XML model
public class IncludeFinder {
/** Qualified name for the per-project persistent property include-map */
private final static QualifiedName CONFIG_INCLUDES = new QualifiedName(AdtPlugin.PLUGIN_ID,
//Synthetic comment -- @@ -156,11 +156,7 @@
}

/**
     * Gets the list of all other layouts that are including the given layout.
*
* @param included the file that is included
* @return the files that are including the given file, or null or empty
//Synthetic comment -- @@ -879,21 +875,6 @@
return finder;
}

/** A reference to a particular file in the project */
public static class Reference {
/** The unique id referencing the file, such as (for res/layout-land/main.xml)








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/resources/descriptors/ResourcesDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/resources/descriptors/ResourcesDescriptors.java
//Synthetic comment -- index 6e516e6..721e093 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
public static final String ROOT_ELEMENT = "resources";  //$NON-NLS-1$
public static final String STRING_ELEMENT = "string";  //$NON-NLS-1$

    public static final String ITEM_TAG = "item"; //$NON-NLS-1$
public static final String NAME_ATTR = "name"; //$NON-NLS-1$
public static final String TYPE_ATTR = "type"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index 1765112..29545d5 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.manager.FolderTypeRelationship;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
//Synthetic comment -- @@ -323,8 +324,12 @@

/**
* Is this a resource that can be defined in any file within the "values" folder?
     *
     * @param type the resource type to check
     * @return true if the given resource type can be represented as a value under the
     *         values/ folder
*/
    public static boolean isValueResource(ResourceType type) {
ResourceFolderType[] folderTypes = FolderTypeRelationship.getRelatedFolders(type);
for (ResourceFolderType folderType : folderTypes) {
if (folderType == ResourceFolderType.VALUES) {
//Synthetic comment -- @@ -336,6 +341,23 @@
}

/**
     * Returns the XML tag containing an element description for value items of the given
     * resource type
     *
     * @param type the resource type to query the XML tag name for
     * @return the tag name used for value declarations in XML of resources of the given
     *         type
     */
    public static String getTagName(ResourceType type) {
        if (type == ResourceType.ID) {
            // Ids are recorded in <item> tags instead of <id> tags
            return ResourcesDescriptors.ITEM_TAG;
        }

        return type.getName();
    }

    /**
* Computes the actual exact location to jump to for a given XML context.
*
* @param context the XML context to be opened
//Synthetic comment -- @@ -739,11 +761,7 @@
/** Looks within an XML DOM document for the given resource name and returns it */
private static Pair<IFile, IRegion> findValueInDocument(
ResourceType type, String name, IFile file, Document document) {
        String targetTag = getTagName(type);
Element root = document.getDocumentElement();
if (root.getTagName().equals(ROOT_ELEMENT)) {
NodeList children = root.getChildNodes();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 92647c8..1a6a9de 100644

//Synthetic comment -- @@ -16,6 +16,16 @@

package com.android.ide.eclipse.adt.internal.ui;

import static com.android.ide.eclipse.adt.AndroidConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AndroidConstants.WS_SEP;
import static com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors.NAME_ATTR;
import static com.android.sdklib.SdkConstants.FD_RESOURCES;
import static com.android.sdklib.SdkConstants.FD_VALUES;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.editors.xml.Hyperlinks;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringWizard;
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
//Synthetic comment -- @@ -23,27 +33,54 @@
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.AbstractElementListSelectionDialog;
import org.eclipse.ui.dialogs.SelectionStatusDialog;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* A dialog to let the user select a resource based on a resource type.
*/
@SuppressWarnings("restriction") // XML model
public class ResourceChooser extends AbstractElementListSelectionDialog {

private Pattern mProjectResourcePattern;
//Synthetic comment -- @@ -176,7 +213,7 @@
newResButton.setText(title);

// We only support adding new strings right now
        newResButton.setEnabled(Hyperlinks.isValueResource(mResourceType));

newResButton.addSelectionListener(new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -185,11 +222,134 @@

if (mResourceType == ResourceType.STRING) {
createNewString();
                } else {
                    assert Hyperlinks.isValueResource(mResourceType);
                    String newName = createNewValue(mResourceType);
                    if (newName != null) {
                        // Recompute the "current resource" to select the new id
                        setupResourceList();
                        selectItemName(newName);
                    }
}
}
});
}

    private String createNewValue(ResourceType type) {
        // Show a name/value dialog entering the key name and the value
        Shell shell = AdtPlugin.getDisplay().getActiveShell();
        if (shell == null) {
            return null;
        }
        NameValueDialog dialog = new NameValueDialog(shell, getFilter());
        if (dialog.open() != Window.OK) {
            return null;
        }

        String name = dialog.getName();
        String value = dialog.getValue();
        if (name.length() == 0 || value.length() == 0) {
            return null;
        }

        // Find "dimens.xml" file in res/values/ (or corresponding name for other
        // value types)
        String fileName = type.getName() + 's';
        String projectPath = FD_RESOURCES + WS_SEP + FD_VALUES + WS_SEP + fileName + '.' + EXT_XML;
        IResource member = mProject.findMember(projectPath);
        if (member != null) {
            if (member instanceof IFile) {
                IFile file = (IFile) member;
                // File exists: Must add item to the XML
                IModelManager manager = StructuredModelManager.getModelManager();
                IStructuredModel model = null;
                try {
                    model = manager.getExistingModelForEdit(file);
                    if (model == null) {
                        model = manager.getModelForEdit(file);
                    }
                    if (model instanceof IDOMModel) {
                        model.beginRecording(this, String.format("Add %1$s",
                                type.getDisplayName()));
                        IDOMModel domModel = (IDOMModel) model;
                        Document document = domModel.getDocument();
                        Element root = document.getDocumentElement();
                        IStructuredDocument structuredDocument = model.getStructuredDocument();
                        Node lastElement = null;
                        NodeList childNodes = root.getChildNodes();
                        String indent = null;
                        for (int i = childNodes.getLength() - 1; i >= 0; i--) {
                            Node node = childNodes.item(i);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                lastElement = node;
                                indent = AndroidXmlEditor.getIndent(structuredDocument, node);
                                break;
                            }
                        }
                        if (indent == null || indent.length() == 0) {
                            indent = "    "; //$NON-NLS-1$
                        }
                        Node nextChild = lastElement != null ? lastElement.getNextSibling() : null;
                        Text indentNode = document.createTextNode('\n' + indent);
                        root.insertBefore(indentNode, nextChild);
                        Element element = document.createElement(Hyperlinks.getTagName(type));
                        element.setAttribute(NAME_ATTR, name);
                        root.insertBefore(element, nextChild);
                        Text valueNode = document.createTextNode(value);
                        element.appendChild(valueNode);
                        model.save();
                        return name;
                    }
                } catch (Exception e) {
                    AdtPlugin.log(e, "Cannot access XML value model");
                } finally {
                    if (model != null) {
                        model.endRecording(this);
                        model.releaseFromEdit();
                    }
                }
            }

            return null;
        } else {
            // No such file exists: just create it
            String prolog = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"; //$NON-NLS-1$
            StringBuilder sb = new StringBuilder(prolog);

            String root = ResourcesDescriptors.ROOT_ELEMENT;
            sb.append('<').append(root).append('>').append('\n');
            sb.append("    "); //$NON-NLS-1$
            sb.append('<');
            sb.append(type.getName());
            sb.append(" name=\""); //$NON-NLS-1$
            sb.append(name);
            sb.append('"');
            sb.append('>');
            sb.append(value);
            sb.append('<').append('/');
            sb.append(type.getName());
            sb.append(">\n");                            //$NON-NLS-1$
            sb.append('<').append('/').append(root).append('>').append('\n');
            String result = sb.toString();
            String error = null;
            try {
                byte[] buf = result.getBytes("UTF8");    //$NON-NLS-1$
                InputStream stream = new ByteArrayInputStream(buf);
                IFile file = mProject.getFile(new Path(projectPath));
                file.create(stream, true /*force*/, null /*progress*/);
                return name;
            } catch (UnsupportedEncodingException e) {
                error = e.getMessage();
            } catch (CoreException e) {
                error = e.getMessage();
            }

            error = String.format("Failed to generate %1$s: %2$s", name, error);
            AdtPlugin.displayError("New Android XML File", error);
        }
        return null;
    }

private void createNewString() {
ExtractStringRefactoring ref = new ExtractStringRefactoring(
mProject, true /*enforceNew*/);
//Synthetic comment -- @@ -291,4 +451,85 @@
selectItemName(itemName);
}
}

    /** Dialog asking for a Name/Value pair */
    private static class NameValueDialog extends SelectionStatusDialog implements Listener {
        private org.eclipse.swt.widgets.Text mNameText;
        private org.eclipse.swt.widgets.Text mValueText;
        private String mInitialName;
        private String mName;
        private String mValue;

        public NameValueDialog(Shell parent, String initialName) {
            super(parent);
            mInitialName = initialName;
        }

        @Override
        protected Control createDialogArea(Composite parent) {
            Composite container = new Composite(parent, SWT.NONE);
            container.setLayout(new GridLayout(2, false));
            GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
            // Wide enough to accommodate the error label
            gridData.widthHint = 400;
            container.setLayoutData(gridData);


            Label nameLabel = new Label(container, SWT.NONE);
            nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
            nameLabel.setText("Name:");

            mNameText = new org.eclipse.swt.widgets.Text(container, SWT.BORDER);
            mNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            if (mInitialName != null) {
                mNameText.setText(mInitialName);
                mNameText.selectAll();
            }

            Label valueLabel = new Label(container, SWT.NONE);
            valueLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
            valueLabel.setText("Value:");

            mValueText = new org.eclipse.swt.widgets.Text(container, SWT.BORDER);
            mValueText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

            mNameText.addListener(SWT.Modify, this);
            mValueText.addListener(SWT.Modify, this);

            validate();

            return container;
        }

        @Override
        protected void computeResult() {
            mName = mNameText.getText().trim();
            mValue = mValueText.getText().trim();
        }

        private String getName() {
            return mName;
        }

        private String getValue() {
            return mValue;
        }

        public void handleEvent(Event event) {
            validate();
        }

        private void validate() {
            IStatus status;
            computeResult();
            if (mName.length() == 0) {
                status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, "Enter a name");
            } else if (mValue.length() == 0) {
                status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, "Enter a value");
            } else {
                status = new Status(IStatus.OK, AdtPlugin.PLUGIN_ID, null);
            }
            updateStatus(status);
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java
//Synthetic comment -- index 2d40cf9..a87400b 100644

//Synthetic comment -- @@ -178,7 +178,7 @@
String result = sb.toString();
String error = null;
try {
            byte[] buf = result.getBytes("UTF8");    //$NON-NLS-1$
InputStream stream = new ByteArrayInputStream(buf);
if (need_delete) {
file.delete(IResource.KEEP_HISTORY | IResource.FORCE, null /*monitor*/);








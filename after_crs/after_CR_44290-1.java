/*Synchronize layout metadata across layout configurations

If you choose a preview layout to use for a ListView or GridView, or
if you choose a fragment layout to use for a <layout> tag, until now
that setting has only applied to the exact layout you are currently
editing.

This changeset makes the setting also get synchronized to all the
other layout variations (-land, -xlarge etc) of the layout, such that
you don't have to find yourself tweaking this setting each time you
open some new variation of the layout after changing preview settings.

(Also deletes a bunch of the old comment-based metadata code.)

Change-Id:I712213920a8956901da157eb94611b2372368fcc*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 4ef469d..e45e68e 100644

//Synthetic comment -- @@ -18,11 +18,13 @@

import static com.android.SdkConstants.TOOLS_PREFIX;
import static com.android.SdkConstants.TOOLS_URI;
import static org.eclipse.ui.IWorkbenchPage.MATCH_INPUT;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper.IProjectFilter;
//Synthetic comment -- @@ -34,6 +36,10 @@
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -47,6 +53,7 @@
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
//Synthetic comment -- @@ -68,10 +75,16 @@
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//Synthetic comment -- @@ -80,6 +93,7 @@
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
//Synthetic comment -- @@ -349,6 +363,47 @@
}

/**
     * Looks through the open editors and returns the editors that have the
     * given file as input.
     *
     * @param file the file to search for
     * @param restore whether editors should be restored (if they have an open
     *            tab, but the editor hasn't been restored since the most recent
     *            IDE start yet
     * @return a collection of editors
     */
    @NonNull
    public static Collection<IEditorPart> findEditorsFor(@NonNull IFile file, boolean restore) {
        FileEditorInput input = new FileEditorInput(file);
        List<IEditorPart> result = null;
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
        for (IWorkbenchWindow window : windows) {
            IWorkbenchPage[] pages = window.getPages();
            for (IWorkbenchPage page : pages) {
                IEditorReference[] editors = page.findEditors(input, null,  MATCH_INPUT);
                if (editors != null) {
                    for (IEditorReference reference : editors) {
                        IEditorPart editor = reference.getEditor(restore);
                        if (editor != null) {
                            if (result == null) {
                                result = new ArrayList<IEditorPart>();
                            }
                            result.add(editor);
                        }
                    }
                }
            }
        }

        if (result == null) {
            return Collections.emptyList();
        }

        return result;
    }

    /**
* Attempts to convert the given {@link URL} into a {@link File}.
*
* @param url the {@link URL} to be converted
//Synthetic comment -- @@ -652,6 +707,23 @@
}

/**
     * Returns the XML editor for the given editor part
     *
     * @param part the editor part to look up the editor for
     * @return the editor or null if this part is not an XML editor
     */
    @Nullable
    public static AndroidXmlEditor getXmlEditor(@NonNull IEditorPart part) {
        if (part instanceof AndroidXmlEditor) {
            return (AndroidXmlEditor) part;
        } else if (part instanceof GraphicalEditorPart) {
            ((GraphicalEditorPart) part).getEditorDelegate().getEditor();
        }

        return null;
    }

    /**
* Sets the given tools: attribute in the given XML editor document, adding
* the tools name space declaration if necessary, formatting the affected
* document region, and optionally comma-appending to an existing value and
//Synthetic comment -- @@ -803,6 +875,97 @@
}

/**
     * Sets the given tools: attribute in the given XML editor document, adding
     * the tools name space declaration if necessary, and optionally
     * comma-appending to an existing value.
     *
     * @param file the file associated with the element
     * @param element the associated element
     * @param description the description of the attribute (shown in the undo
     *            event)
     * @param name the name of the attribute
     * @param value the attribute value
     * @param appendValue if true, add this value as a comma separated value to
     *            the existing attribute value, if any
     */
    public static void setToolsAttribute(
            @NonNull final IFile file,
            @NonNull final Element element,
            @NonNull final String description,
            @NonNull final String name,
            @Nullable final String value,
            final boolean appendValue) {
        IModelManager modelManager = StructuredModelManager.getModelManager();
        if (modelManager == null) {
            return;
        }

        try {
            IStructuredModel model = null;
            if (model == null) {
                model = modelManager.getModelForEdit(file);
            }
            if (model != null) {
                try {
                    model.aboutToChangeModel();
                    if (model instanceof IDOMModel) {
                        IDOMModel domModel = (IDOMModel) model;
                        Document doc = domModel.getDocument();
                        if (doc != null && element.getOwnerDocument() == doc) {
                            String prefix = XmlUtils.lookupNamespacePrefix(element, TOOLS_URI,
                                    null);
                            if (prefix == null) {
                                // Add in new prefix...
                                prefix = XmlUtils.lookupNamespacePrefix(element,
                                        TOOLS_URI, TOOLS_PREFIX);
                            }

                            String v = value;
                            if (appendValue && v != null) {
                                String prev = element.getAttributeNS(TOOLS_URI, name);
                                if (prev.length() > 0) {
                                    v = prev + ',' + value;
                                }
                            }

                            // Use the non-namespace form of set attribute since we can't
                            // reference the namespace until the model has been reloaded
                            if (v != null) {
                                element.setAttribute(prefix + ':' + name, v);
                            } else {
                                element.removeAttribute(prefix + ':' + name);
                            }
                        }
                    }
                } finally {
                    model.changedModel();
                    String updated = model.getStructuredDocument().get();
                    model.releaseFromEdit();
                    model.save(file);

                    // Must also force a save on disk since the above model.save(file) often
                    // (always?) has no effect.
                    ITextFileBufferManager manager = FileBuffers.getTextFileBufferManager();
                    NullProgressMonitor monitor = new NullProgressMonitor();
                    IPath path = file.getFullPath();
                    manager.connect(path, LocationKind.IFILE, monitor);
                    try {
                        ITextFileBuffer buffer = manager.getTextFileBuffer(path,
                                LocationKind.IFILE);
                        IDocument currentDocument = buffer.getDocument();
                        currentDocument.set(updated);
                        buffer.commit(monitor, true);
                    } finally {
                        manager.disconnect(path, LocationKind.IFILE,  monitor);
                    }
                }
            }
        } catch (Exception e) {
            AdtPlugin.log(e, null);
        }
    }

    /**
* Returns the Android version and code name of the given API level
*
* @param api the api level
//Synthetic comment -- @@ -828,7 +991,7 @@
case 15: return "API 15: Android 4.0.3 (IceCreamSandwich)";
case 16: return "API 16: Android 4.1 (Jelly Bean)";
// If you add more versions here, also update #getBuildCodes and
            // SdkConstants#HIGHEST_KNOWN_API

default: {
// Consult SDK manager to see if we know any more (later) names,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilities.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilities.java
//Synthetic comment -- index b364f57..145036b 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.utils.Pair;

//Synthetic comment -- @@ -184,6 +185,31 @@
}

/**
     * Returns the DOM document for the given editor
     *
     * @param editor the XML editor
     * @return the document, or null if not found or not parsed properly (no
     *         errors are generated/thrown)
     */
    @Nullable
    public static Document getDocument(@NonNull AndroidXmlEditor editor) {
        IStructuredModel model = editor.getModelForRead();
        try {
            if (model instanceof IDOMModel) {
                IDOMModel domModel = (IDOMModel) model;
                return domModel.getDocument();
            }
        } finally {
            if (model != null) {
                model.releaseFromRead();
            }
        }

        return null;
    }


    /**
* Returns the XML DOM node corresponding to the given offset of the given
* document.
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java
//Synthetic comment -- index e3ae04e..82172fc 100644

//Synthetic comment -- @@ -28,24 +28,27 @@
import com.android.ide.common.rendering.api.AdapterBinding;
import com.android.ide.common.rendering.api.DataBindingItem;
import com.android.ide.common.rendering.api.ResourceReference;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.progress.WorkbenchJob;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import java.util.Collection;
import java.util.List;
import java.util.Map;

//Synthetic comment -- @@ -63,8 +66,6 @@

/** The string to start metadata comments with */
private static final String COMMENT_PROLOGUE = " Preview: ";
/** The property key, included in comments, which references a list item layout */
public static final String KEY_LV_ITEM = "listitem";        //$NON-NLS-1$
/** The property key, included in comments, which references a list header layout */
//Synthetic comment -- @@ -79,43 +80,6 @@
}

/**
* Returns the given property specified in the <b>current</b> element being
* processed by the given pull parser.
*
//Synthetic comment -- @@ -135,124 +99,25 @@
}

/**
     * Clears the old metadata from the given node
*
* @param node the XML node to associate metadata with
     * @deprecated this method clears metadata using the old comment-based style;
     *             should only be used for migration at this point
*/
@Deprecated
    public static void clearLegacyComment(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0, n = children.getLength(); i < n; i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.COMMENT_NODE) {
                String text = child.getNodeValue();
                if (text.startsWith(COMMENT_PROLOGUE)) {
                    Node commentNode = child;
// Remove the comment, along with surrounding whitespace if applicable
Node previous = commentNode.getPreviousSibling();
if (previous != null && previous.getNodeType() == Node.TEXT_NODE) {
                        if (previous.getNodeValue().trim().length() == 0) {
node.removeChild(previous);
}
}
//Synthetic comment -- @@ -260,55 +125,15 @@
Node first = node.getFirstChild();
if (first != null && first.getNextSibling() == null
&& first.getNodeType() == Node.TEXT_NODE) {
                        if (first.getNodeValue().trim().length() == 0) {
node.removeChild(first);
}
}
}
}
}
}

/**
* Returns the given property of the given DOM node, or null
*
//Synthetic comment -- @@ -343,21 +168,120 @@
* @param value the value to store for the given node and name, or null to remove it
*/
public static void setProperty(
            @NonNull final AndroidXmlEditor editor,
@NonNull final Node node,
@NonNull final String name,
@Nullable final String value) {
// Clear out the old metadata
        clearLegacyComment(node);

if (node.getNodeType() == Node.ELEMENT_NODE) {
            final Element element = (Element) node;
            final String undoLabel = "Bind View";
            AdtUtils.setToolsAttribute(editor, element, undoLabel, name, value,
false /*reveal*/, false /*append*/);

            // Also apply the same layout to any corresponding elements in other configurations
            // of this layout.
            final IFile file = editor.getInputFile();
            if (file != null) {
                final List<IFile> variations = AdtUtils.getResourceVariations(file, false);
                if (variations.isEmpty()) {
                    return;
                }
                Display display = AdtPlugin.getDisplay();
                if (display == null) {
                    return;
                }
                WorkbenchJob job = new WorkbenchJob(display, "Update alternate views") {
                    @Override
                    public IStatus runInUIThread(IProgressMonitor monitor) {
                        for (IFile variation : variations) {
                            if (variation.equals(file)) {
                                continue;
                            }
                            try {
                                // If the corresponding file is open in the IDE, use the
                                // editor version instead
                                if (!AdtPrefs.getPrefs().isSharedLayoutEditor()) {
                                    if (setPropertyInEditor(undoLabel, variation, element, name,
                                            value)) {
                                        return Status.OK_STATUS;
                                    }
                                }

                                boolean old = editor.getIgnoreXmlUpdate();
                                try {
                                    editor.setIgnoreXmlUpdate(true);
                                    setPropertyInFile(undoLabel, variation, element, name, value);
                                } finally {
                                    editor.setIgnoreXmlUpdate(old);
                                }
                            } catch (Exception e) {
                                AdtPlugin.log(e, variation.getFullPath().toOSString());
                            }
                        }
                        return Status.OK_STATUS;
                    }

                };
                job.setSystem(true);
                job.schedule();
            }
}
}

    private static boolean setPropertyInEditor(
            @NonNull String undoLabel,
            @NonNull IFile variation,
            @NonNull final Element equivalentElement,
            @NonNull final String name,
            @Nullable final String value) {
        Collection<IEditorPart> editors =
                AdtUtils.findEditorsFor(variation, false /*restore*/);
        for (IEditorPart part : editors) {
            AndroidXmlEditor editor = AdtUtils.getXmlEditor(part);
            if (editor != null) {
                Document doc = DomUtilities.getDocument(editor);
                if (doc != null) {
                    Element element = DomUtilities.findCorresponding(equivalentElement, doc);
                    if (element != null) {
                        AdtUtils.setToolsAttribute(editor, element, undoLabel, name,
                                value, false /*reveal*/, false /*append*/);
                        if (part instanceof GraphicalEditorPart) {
                            GraphicalEditorPart g = (GraphicalEditorPart) part;
                            g.recomputeLayout();
                            g.getCanvasControl().redraw();
                        }
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean setPropertyInFile(
            @NonNull String undoLabel,
            @NonNull IFile variation,
            @NonNull final Element element,
            @NonNull final String name,
            @Nullable final String value) {
        Document doc = DomUtilities.getDocument(variation);
        if (doc != null && element.getOwnerDocument() != doc) {
            Element other = DomUtilities.findCorresponding(element, doc);
            if (other != null) {
                AdtUtils.setToolsAttribute(variation, other, undoLabel,
                        name, value, false);

                return true;
            }
        }

        return false;
    }

/** Strips out @layout/ or @android:layout/ from the given layout reference */
private static String stripLayoutPrefix(String layout) {
if (layout.startsWith(ANDROID_LAYOUT_RESOURCE_PREFIX)) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadataTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadataTest.java
//Synthetic comment -- index a75fc96..fa9e18f 100644

//Synthetic comment -- @@ -45,111 +45,6 @@
@SuppressWarnings({"restriction", "javadoc", "deprecation"}) // XML DOM model
public class LayoutMetadataTest extends AdtProjectTest {

public void testMetadata1() throws Exception {
Pair<IDocument, UiElementNode> pair = getNode("metadata.xml", "listView1");
UiElementNode uiNode = pair.getSecond();








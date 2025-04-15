/*Fragment Rendering Support

This changeset adds designtime-previewing of fragments, where
a layout which contains fragments will show the contents of
the fragments inline.

Initially, the fragments are empty, but you can right click on them to
bring up a context menu where you can choose which layout to show at
designtime. This is persisted across IDE sessions, just like the
ListView render preview.

In addition to the generic layout chooser, all layout references found
in the associated Fragment class (usually what you want) are listed
directly in the menu.

Change-Id:Ib7f8caae568eff94a57fd50b8e054f5fa52f3da6*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 064094e..f6877f1 100644

//Synthetic comment -- @@ -178,7 +178,13 @@
// Set the text of the mock view to the simplified name of the custom class
Method m = view.getClass().getMethod("setText",
new Class<?>[] { CharSequence.class });
            String label = getShortClassName(className);
            if (label.equals(VIEW_FRAGMENT)) {
                label = "<fragment>\n"
                        + "Pick preview layout from the \"Fragment Layout\" context menu";
            }

            m.invoke(view, label);

// Call MockView.setGravity(Gravity.CENTER) to get the text centered in
// MockViews.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 8ec3111..ca06477 100644

//Synthetic comment -- @@ -20,11 +20,15 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.ATTR_LAYOUT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_INCLUDE;

import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.FragmentMenu;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
//Synthetic comment -- @@ -348,6 +352,13 @@
Node xmlNode = uiNode.getXmlNode();

if (xmlNode != null) {
            if (localName.equals(ATTR_LAYOUT) && xmlNode.getNodeName().equals(VIEW_FRAGMENT)) {
                String layout = FragmentMenu.getFragmentLayout(xmlNode);
                if (layout != null) {
                    return layout;
                }
            }

Node attribute = xmlNode.getAttributes().getNamedItemNS(namespace, localName);
if (attribute != null) {
String value = attribute.getNodeValue();
//Synthetic comment -- @@ -379,7 +390,19 @@

public String getName() {
if (mParsingState == START_TAG || mParsingState == END_TAG) {
            String name = getCurrentNode().getDescriptor().getXmlLocalName();

            if (name.equals(VIEW_FRAGMENT)) {
                // Temporarily translate <fragment> to <include> (and in getAttribute
                // we will also provide a layout-attribute for the corresponding
                // fragment name attribute)
                String layout = FragmentMenu.getFragmentLayout(getCurrentNode().getXmlNode());
                if (layout != null) {
                    return VIEW_INCLUDE;
                }
            }

            return name;
}

return null;
//Synthetic comment -- @@ -478,18 +501,18 @@
}

/** {@link DimensionEntry} complex unit: Value is raw pixels. */
    private static final int COMPLEX_UNIT_PX = 0;
/** {@link DimensionEntry} complex unit: Value is Device Independent
*  Pixels. */
    private static final int COMPLEX_UNIT_DIP = 1;
/** {@link DimensionEntry} complex unit: Value is a scaled pixel. */
    private static final int COMPLEX_UNIT_SP = 2;
/** {@link DimensionEntry} complex unit: Value is in points. */
    private static final int COMPLEX_UNIT_PT = 3;
/** {@link DimensionEntry} complex unit: Value is in inches. */
    private static final int COMPLEX_UNIT_IN = 4;
/** {@link DimensionEntry} complex unit: Value is in millimeters. */
    private static final int COMPLEX_UNIT_MM = 5;

private final static DimensionEntry[] sDimensions = new DimensionEntry[] {
new DimensionEntry("px", COMPLEX_UNIT_PX),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 2a70dd0..286591f 100755

//Synthetic comment -- @@ -448,9 +448,9 @@
}

/**
     * Returns the layout url attribute value for the closest surrounding include or
     * fragment element parent, or null if this {@link CanvasViewInfo} is not rendered as
     * part of an include or fragment tag.
*
* @return the layout url attribute value for the surrounding include tag, or null if
*         not applicable
//Synthetic comment -- @@ -460,14 +460,21 @@
while (curr != null) {
if (curr.mUiViewNode != null) {
Node node = curr.mUiViewNode.getXmlNode();
                if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
                    String nodeName = node.getNodeName();
                    if (node.getNamespaceURI() == null
                            && LayoutDescriptors.VIEW_INCLUDE.equals(nodeName)) {
                        // Note: the layout attribute is NOT in the Android namespace
                        Element element = (Element) node;
                        String url = element.getAttribute(LayoutDescriptors.ATTR_LAYOUT);
                        if (url.length() > 0) {
                            return url;
                        }
                    } else if (LayoutDescriptors.VIEW_FRAGMENT.equals(nodeName)) {
                        String url = FragmentMenu.getFragmentLayout(node);
                        if (url != null) {
                            return url;
                        }
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index f188e90..893849b 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GESTURE_OVERLAY_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LIST_VIEW;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;

import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.IViewRule;
//Synthetic comment -- @@ -210,7 +211,7 @@
}
}

        insertTagSpecificMenus(endId);
insertVisualRefactorings(endId);
}

//Synthetic comment -- @@ -235,8 +236,8 @@
mMenuManager.insertBefore(endId, new Separator());
}

    /** "Preview List Content" pull-right menu for lists, "Preview Fragment" for fragments, etc. */
    private void insertTagSpecificMenus(String endId) {

List<SelectionItem> selection = mCanvas.getSelectionManager().getSelections();
if (selection.size() == 0) {
//Synthetic comment -- @@ -249,6 +250,10 @@
mMenuManager.insertBefore(endId, new Separator());
mMenuManager.insertBefore(endId, new ListViewTypeMenu(mCanvas));
return;
            } else if (name.equals(VIEW_FRAGMENT) && selection.size() == 1) {
                mMenuManager.insertBefore(endId, new Separator());
                mMenuManager.insertBefore(endId, new FragmentMenu(mCanvas));
                return;
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/FragmentMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/FragmentMenu.java
new file mode 100644
//Synthetic comment -- index 0000000..bb519fb

//Synthetic comment -- @@ -0,0 +1,310 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NAME;
import static com.android.ide.common.layout.LayoutConstants.LAYOUT_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata.KEY_FRAGMENT_LAYOUT;

import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.CyclicDependencyValidator;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.ui.ResourceChooser;
import com.android.resources.ResourceType;
import com.android.util.Pair;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment context menu allowing a layout to be chosen for previewing in the fragment frame.
 */
public class FragmentMenu extends SubmenuAction {
    private static final String R_LAYOUT_PREFIX = "R.layout."; //$NON-NLS-1$
    private static final String ANDROID_R_PREFIX = "android.R"; //$NON-NLS-1$

    /** Associated canvas */
    private final LayoutCanvas mCanvas;

    /**
     * Creates a "Preview Fragment" menu
     *
     * @param canvas associated canvas
     */
    public FragmentMenu(LayoutCanvas canvas) {
        super("Fragment Layout");
        mCanvas = canvas;
    }

    @Override
    protected void addMenuItems(Menu menu) {
        IAction action = new PickLayoutAction("Choose Layout...");
        new ActionContributionItem(action).fill(menu, -1);

        SelectionManager selectionManager = mCanvas.getSelectionManager();
        List<SelectionItem> selections = selectionManager.getSelections();
        if (selections.size() == 0) {
            return;
        }

        SelectionItem first = selections.get(0);
        UiViewElementNode node = first.getViewInfo().getUiViewNode();
        Element element = (Element) node.getXmlNode();

        String selected = getSelectedLayout();
        if (selected != null) {
            if (selected.startsWith(ANDROID_LAYOUT_PREFIX)) {
                selected = selected.substring(ANDROID_LAYOUT_PREFIX.length());
            }
        }

        String fqcn = getFragmentClass(element);
        if (fqcn != null) {
            // Look up the corresponding activity class and try to figure out
            // which layouts it is referring to and list these here as reasonable
            // guesses
            IProject project = mCanvas.getLayoutEditor().getProject();
            String source = null;
            try {
                IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
                IType type = javaProject.findType(fqcn);
                if (type != null) {
                    source = type.getSource();
                }
            } catch (CoreException e) {
                AdtPlugin.log(e, null);
            }
            // Find layouts. This is based on just skimming the Fragment class and looking
            // for layout references of the form R.layout.*.
            if (source != null) {
                String self = mCanvas.getLayoutResourceName();
                // Pair of <title,layout> to be displayed to the user
                List<Pair<String, String>> layouts = new ArrayList<Pair<String, String>>();
                int index = 0;
                while (true) {
                    index = source.indexOf(R_LAYOUT_PREFIX, index);
                    if (index == -1) {
                        break;
                    } else {
                        index += R_LAYOUT_PREFIX.length();
                        int end = index;
                        while (end < source.length()) {
                            char c = source.charAt(end);
                            if (!Character.isJavaIdentifierPart(c)) {
                                break;
                            }
                            end++;
                        }
                        if (end > index) {
                            String title = source.substring(index, end);
                            String layout;
                            // Is this R.layout part of an android.R.layout?
                            int len = ANDROID_R_PREFIX.length() + 1; // prefix length to check
                            if (index > len && source.startsWith(ANDROID_R_PREFIX, index - len)) {
                                layout = ANDROID_LAYOUT_PREFIX + title;
                            } else {
                                layout = LAYOUT_PREFIX + title;
                            }
                            if (!self.equals(title)) {
                                layouts.add(Pair.of(title, layout));
                            }
                        }
                    }

                    index++;
                }

                if (layouts.size() > 0) {
                    new Separator().fill(menu, -1);
                    for (Pair<String, String> layout : layouts) {
                        action = new SetFragmentLayoutAction(layout.getFirst(),
                                layout.getSecond(), selected);
                        new ActionContributionItem(action).fill(menu, -1);
                    }
                }
            }
        }

        if (selected != null) {
            new Separator().fill(menu, -1);
            action = new SetFragmentLayoutAction("Clear", null, null);
            new ActionContributionItem(action).fill(menu, -1);
        }
    }

    /**
     * Returns the class name of the fragment associated with the given {@code <fragment>}
     * element.
     *
     * @param element the element for the fragment tag
     * @return the fully qualified fragment class name, or null
     */
    public static String getFragmentClass(Element element) {
        String fqcn = element.getAttribute(ATTR_CLASS);
        if (fqcn == null || fqcn.length() == 0) {
            fqcn = element.getAttributeNS(ANDROID_URI, ATTR_NAME);
        }
        if (fqcn != null && fqcn.length() > 0) {
            return fqcn;
        } else {
            return null;
        }
    }

    /**
     * Returns the layout to be shown for the given {@code <fragment>} node.
     *
     * @param node the node corresponding to the {@code <fragment>} element
     * @return the resource path to a layout to render for this fragment, or null
     */
    public static String getFragmentLayout(Node node) {
        LayoutMetadata metadata = LayoutMetadata.get();
        String layout = metadata.getProperty((IDocument) null, node,
                LayoutMetadata.KEY_FRAGMENT_LAYOUT);
        if (layout != null) {
            return layout;
        }

        return null;
    }

    /** Returns the name of the currently displayed layout in the fragment, or null */
    private String getSelectedLayout() {
        SelectionManager selectionManager = mCanvas.getSelectionManager();
        for (SelectionItem item : selectionManager.getSelections()) {
            UiViewElementNode node = item.getViewInfo().getUiViewNode();
            String layout = getFragmentLayout(node.getXmlNode());
            if (layout != null) {
                return layout;
            }
        }
        return null;
    }

    /**
     * Set the given layout as the new fragment layout
     *
     * @param layout the layout resource name to show in this fragment
     */
    public void setNewLayout(String layout) {
        LayoutEditor editor = mCanvas.getLayoutEditor();
        GraphicalEditorPart graphicalEditor = editor.getGraphicalEditor();
        LayoutMetadata metadata = LayoutMetadata.get();
        SelectionManager selectionManager = mCanvas.getSelectionManager();

        for (SelectionItem item : selectionManager.getSelections()) {
            UiViewElementNode node = item.getViewInfo().getUiViewNode();
            Node xmlNode = node.getXmlNode();
            metadata.setProperty(editor, xmlNode, KEY_FRAGMENT_LAYOUT, layout);
        }

        // Refresh
        graphicalEditor.recomputeLayout();
        mCanvas.redraw();
    }

    /** Action to set the given layout as the new layout in a fragment */
    private class SetFragmentLayoutAction extends Action {
        private final String mLayout;

        public SetFragmentLayoutAction(String title, String layout, String selected) {
            super(title, IAction.AS_RADIO_BUTTON);
            mLayout = layout;

            if (layout != null && layout.equals(selected)) {
                setChecked(true);
            }
        }

        @Override
        public void run() {
            setNewLayout(mLayout);
        }
    }

    /**
     * Action which brings up the "Create new XML File" wizard, pre-selected with the
     * animation category
     */
    private class PickLayoutAction extends Action {

        public PickLayoutAction(String title) {
            super(title, IAction.AS_PUSH_BUTTON);
        }

        @Override
        public void run() {
            LayoutEditor editor = mCanvas.getLayoutEditor();
            IProject project = editor.getProject();
            // get the resource repository for this project and the system resources.
            ResourceRepository projectRepository = ResourceManager.getInstance()
                    .getProjectResources(project);
            Shell shell = mCanvas.getShell();

            AndroidTargetData data = editor.getTargetData();
            ResourceRepository systemRepository = data.getFrameworkResources();

            ResourceChooser dlg = new ResourceChooser(project,
                    ResourceType.LAYOUT, projectRepository,
                    systemRepository, shell);

            IInputValidator validator = CyclicDependencyValidator.create(editor.getInputFile());

            if (validator != null) {
                // Ensure wide enough to accommodate validator error message
                dlg.setSize(70, 10);
                dlg.setInputValidator(validator);
            }

            String layout = getSelectedLayout();
            if (layout != null) {
                dlg.setCurrentResource(layout);
            }

            int result = dlg.open();
            if (result == ResourceChooser.CLEAR_RETURN_CODE) {
                setNewLayout(null);
            } else if (result == Window.OK) {
                String newType = dlg.getCurrentResource();
                setNewLayout(newType);
            }
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 337ad5c..462aa81 100644

//Synthetic comment -- @@ -1428,7 +1428,7 @@
float xdpi = mConfigComposite.getXDpi();
float ydpi = mConfigComposite.getYDpi();

        UiElementPullParser modelParser = new UiElementPullParser(model,
mUseExplodeMode, explodeNodes, density, xdpi, project);
ILayoutPullParser topParser = modelParser;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java
//Synthetic comment -- index 2bfa841..7e1f88e 100644

//Synthetic comment -- @@ -56,6 +56,8 @@
public static final String KEY_LV_HEADER = "listheader";    //$NON-NLS-1$
/** The property key, included in comments, which references a list footer layout */
public static final String KEY_LV_FOOTER = "listfooter";    //$NON-NLS-1$
    /** The property key, included in comments, which references a fragment layout to show */
    public static final String KEY_FRAGMENT_LAYOUT = "layout";        //$NON-NLS-1$

/** The metadata class is a singleton for now since it has no state of its own */
private static final LayoutMetadata sInstance = new LayoutMetadata();
//Synthetic comment -- @@ -84,8 +86,10 @@
public String getProperty(IDocument document, Node node, String name) {
IStructuredModel model = null;
try {
            if (document != null) {
                IModelManager modelManager = StructuredModelManager.getModelManager();
                model = modelManager.getExistingModelForRead(document);
            }

Node comment = findComment(node);
if (comment != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java
//Synthetic comment -- index e522957..5476c60 100644

//Synthetic comment -- @@ -132,8 +132,8 @@
}

/**
     * Action which brings up a resource chooser to choose an arbitrary layout as the
     * layout to be previewed in the list.
*/
private class PickLayoutAction extends Action {
private final String mType;








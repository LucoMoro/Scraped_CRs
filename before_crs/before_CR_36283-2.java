/*Fix 30018: Flashing layout actions bar on Windows

This changeset makes the layout actions bar attempt to update itself
less often. Frequently, the existing toolbar buttons can be repurposed
(just bound to a new underlying selection object) instead of
completely replacing the contents each time. This fixes a flashing
issue on Windows but is also more efficient on all platforms.

Change-Id:I82f496e546c5104ef55630b0fb8243c75ba26e63*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 58e156e..3c976b0 100644

//Synthetic comment -- @@ -246,6 +246,18 @@
mIgnoreXmlUpdate = ignore;
}

// ---- Base Class Overrides, Interfaces Implemented ----

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java
//Synthetic comment -- index 325e284..4bc7641 100644

//Synthetic comment -- @@ -487,6 +487,9 @@
public void selectionChanged(SelectionChangedEvent event) {
ISelection selection = event.getSelection();
getEditor().getSite().getSelectionProvider().setSelection(selection);
SelectionManager manager =
mGraphicalEditor.getCanvasControl().getSelectionManager();
manager.setSelection(selection);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java
//Synthetic comment -- index 8f950b9..49157a7 100644

//Synthetic comment -- @@ -18,6 +18,7 @@
import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.RuleAction;
import com.android.ide.common.api.RuleAction.Choices;
//Synthetic comment -- @@ -31,6 +32,7 @@
import com.android.ide.eclipse.adt.internal.lint.EclipseLintClient;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.sdkuilib.internal.widgets.ResolutionChooserDialog;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.window.Window;
//Synthetic comment -- @@ -72,6 +74,7 @@
private ToolItem mZoomInButton;
private ToolItem mZoomFitButton;
private ToolItem mLintButton;

/**
* Creates a new {@link LayoutActionBar} and adds it to the given parent.
//Synthetic comment -- @@ -98,14 +101,14 @@
mLintToolBar.setLayoutData(lintData);
}

/** Updates the layout contents based on the current selection */
void updateSelection() {
        // Get rid of any previous children
        for (ToolItem c : mLayoutToolBar.getItems()) {
            c.dispose();
        }
        mLayoutToolBar.pack();

NodeProxy parent = null;
LayoutCanvas canvas = mEditor.getCanvasControl();
SelectionManager selectionManager = canvas.getSelectionManager();
//Synthetic comment -- @@ -167,12 +170,94 @@
}
}

        addActions(actions, index, label);

mLayoutToolBar.pack();
mLayoutToolBar.layout();
}

private void addActions(List<RuleAction> actions, int labelIndex, String label) {
if (actions.size() > 0) {
// Flag used to indicate that if there are any actions -after- this, it
//Synthetic comment -- @@ -226,7 +311,7 @@
}
}

    private void addToggle(final Toggle toggle) {
final ToolItem button = new ToolItem(mLayoutToolBar, SWT.CHECK);

URL iconUrl = toggle.getIconUrl();
//Synthetic comment -- @@ -237,10 +322,12 @@
} else {
button.setText(title);
}

button.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
toggle.getCallback().action(toggle, getSelectedNodes(),
toggle.getId(), button.getSelection());
updateSelection();
//Synthetic comment -- @@ -263,7 +350,7 @@
}


    private void addPlainAction(final RuleAction menuAction) {
final ToolItem button = new ToolItem(mLayoutToolBar, SWT.PUSH);

URL iconUrl = menuAction.getIconUrl();
//Synthetic comment -- @@ -274,10 +361,12 @@
} else {
button.setText(title);
}

button.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
menuAction.getCallback().action(menuAction, getSelectedNodes(), menuAction.getId(),
false);
updateSelection();
//Synthetic comment -- @@ -285,7 +374,7 @@
});
}

    private void addRadio(final RuleAction.Choices choices) {
List<URL> icons = choices.getIconUrls();
List<String> titles = choices.getTitles();
List<String> ids = choices.getIds();
//Synthetic comment -- @@ -301,10 +390,13 @@
final ToolItem item = new ToolItem(mLayoutToolBar, SWT.RADIO);
item.setToolTipText(title);
item.setImage(IconFactory.getInstance().getIcon(iconUrl));
item.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
if (item.getSelection()) {
choices.getCallback().action(choices, getSelectedNodes(), id, null);
updateSelection();
}
//Synthetic comment -- @@ -317,7 +409,7 @@
}
}

    private void addDropdown(final RuleAction.Choices choices) {
final ToolItem combo = new ToolItem(mLayoutToolBar, SWT.DROP_DOWN);
URL iconUrl = choices.getIconUrl();
if (iconUrl != null) {
//Synthetic comment -- @@ -326,6 +418,7 @@
} else {
combo.setText(choices.getTitle());
}

Listener menuListener = new Listener() {
@Override
//Synthetic comment -- @@ -335,7 +428,7 @@
point = combo.getDisplay().map(mLayoutToolBar, null, point);

Menu menu = new Menu(mLayoutToolBar.getShell(), SWT.POP_UP);

List<URL> icons = choices.getIconUrls();
List<String> titles = choices.getTitles();
List<String> ids = choices.getIds();
//Synthetic comment -- @@ -360,6 +453,7 @@
item.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
choices.getCallback().action(choices, getSelectedNodes(), id, null);
updateSelection();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvasViewer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvasViewer.java
//Synthetic comment -- index bef4fba..e349a1c 100755

//Synthetic comment -- @@ -114,6 +114,9 @@
*/
@Override
public void setSelection(ISelection selection, boolean reveal) {
mCanvas.getSelectionManager().setSelection(selection);
}









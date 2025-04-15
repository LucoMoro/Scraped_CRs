/*Add workaround for the Eclipse+Mountain Lion bug

On Mountain Lion, the property sheet's property editor inline text
editor is broken. This is due tohttps://bugs.eclipse.org/bugs/show_bug.cgi?id=388574The bug has been fixed for Eclipse 4.3, but right now it's a very poor
user experience; you click to edit, and nothing happens.

This changeset adds a workaround: *If* the bug is triggered (an
exception is thrown during property editor activation), then the
customizer dialog is shown instead (equivalent to clicking on the
"..." button next to the property).

The dialog code is modified to show an error message on the bottom
explaining why the dialog was shown instead (since this behavior is
annoying, we should explain to users why we are doing), and it
explains that the bug should be fixed in Eclipse 4.3, so if anyone in
the future is running Eclipse 4.2 with this behavior and 4.3 is
available, they will have an incentive to upgrade.

Change-Id:I338a26c39aca0042075d7a12527b723d9783509c*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/FlagXmlPropertyDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/FlagXmlPropertyDialog.java
//Synthetic comment -- index 0276b6c..5e1e702 100644

//Synthetic comment -- @@ -82,6 +82,11 @@
mTable = mViewer.getTable();
mTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        Composite workaround = PropertyFactory.addWorkaround(container);
        if (workaround != null) {
            workaround.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        }

mViewer.setContentProvider(this);
mViewer.setInput(mFlags);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertyFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertyFactory.java
//Synthetic comment -- index 59754af..9678135 100644

//Synthetic comment -- @@ -37,12 +37,26 @@
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.wb.internal.core.editor.structure.property.PropertyListIntersector;
import org.eclipse.wb.internal.core.model.property.ComplexProperty;
import org.eclipse.wb.internal.core.model.property.Property;
import org.eclipse.wb.internal.core.model.property.category.PropertyCategory;
import org.eclipse.wb.internal.core.model.property.editor.PropertyEditor;
import org.eclipse.wb.internal.core.model.property.editor.presentation.ButtonPropertyEditorPresentation;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
//Synthetic comment -- @@ -685,4 +699,48 @@
public void setSortingMode(SortingMode sortingMode) {
mSortMode = sortingMode;
}

    // https://bugs.eclipse.org/bugs/show_bug.cgi?id=388574
    public static Composite addWorkaround(Composite parent) {
        if (ButtonPropertyEditorPresentation.isInWorkaround) {
            Composite top = new Composite(parent, SWT.NONE);
            top.setLayout(new GridLayout(1, false));
            Label label = new Label(top, SWT.WRAP);
            label.setText(
                    "This dialog is shown instead of an inline text editor as a\n" +
                    "workaround for an Eclipse bug specific to OSX Mountain Lion.\n" +
                    "It should be fixed in Eclipse 4.3.");
            label.setForeground(top.getDisplay().getSystemColor(SWT.COLOR_RED));
            GridData data = new GridData();
            data.grabExcessVerticalSpace = false;
            data.grabExcessHorizontalSpace = false;
            data.horizontalAlignment = GridData.FILL;
            data.verticalAlignment = GridData.BEGINNING;
            label.setLayoutData(data);

            Link link = new Link(top, SWT.NO_FOCUS);
            link.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
            link.setText("<a>https://bugs.eclipse.org/bugs/show_bug.cgi?id=388574</a>");
            link.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent event) {
                    try {
                        IWorkbench workbench = PlatformUI.getWorkbench();
                        IWebBrowser browser = workbench.getBrowserSupport().getExternalBrowser();
                        browser.openURL(new URL(event.text));
                    } catch (Exception e) {
                        String message = String.format(
                                "Could not open browser. Vist\n%1$s\ninstead.",
                                event.text);
                        MessageDialog.openError(((Link)event.getSource()).getShell(),
                                "Browser Error", message);
                    }
                }
            });

            return top;
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/StringXmlPropertyDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/StringXmlPropertyDialog.java
//Synthetic comment -- index 3fb72a9..fb7e459 100644

//Synthetic comment -- @@ -15,6 +15,10 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.internal.core.model.property.Property;
import org.eclipse.wb.internal.core.model.property.editor.string.StringPropertyDialog;
//Synthetic comment -- @@ -28,4 +32,16 @@
protected boolean isMultiLine() {
return false;
}

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);

        Composite workaround = PropertyFactory.addWorkaround(area);
        if (workaround != null) {
            workaround.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        }

        return area;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ReferenceChooserDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ReferenceChooserDialog.java
//Synthetic comment -- index 4906730..6c62865 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ide.common.resources.ResourceItem;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.properties.PropertyFactory;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringWizard;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -142,6 +143,11 @@
// create the "New Resource" button
createNewResButtons(top);

        Composite workaround = PropertyFactory.addWorkaround(top);
        if (workaround != null) {
            workaround.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        }

return top;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 6796e91..a215cdc 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.assetstudio.OpenCreateAssetSetWizardAction;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.properties.PropertyFactory;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringWizard;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
//Synthetic comment -- @@ -561,6 +562,11 @@
data.verticalAlignment = GridData.BEGINNING;
mResolvedLabel.setLayoutData(data);
}

        Composite workaround = PropertyFactory.addWorkaround(top);
        if (workaround != null) {
            workaround.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        }
}

private void updateResolvedLabel() {








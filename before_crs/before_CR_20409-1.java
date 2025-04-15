/*Form focus experiment. DO NOT SUBMIT.

Change-Id:I67486460f857362fcb099f85165e9d9df9e18782*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/OverviewInfoPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/OverviewInfoPart.java
//Synthetic comment -- index 98f2f9c..6b2fa2d 100644

//Synthetic comment -- @@ -57,7 +57,7 @@
return editor.getUiRootNode().findUiChildNode(desc.getXmlName());
}
}
        
// No manifest descriptor: we have a dummy UiRootNode, so we return that.
// The editor will be reloaded once we have the proper descriptors anyway.
return editor.getUiRootNode();
//Synthetic comment -- @@ -65,12 +65,12 @@

/**
* Overridden in order to capture the current managed form.
     * 
* {@inheritDoc}
*/
@Override
protected void createFormControls(final IManagedForm managedForm) {
        mManagedForm = managedForm; 
super.createFormControls(managedForm);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/OverviewPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/OverviewPage.java
//Synthetic comment -- index 84ba67f..72d1673 100644

//Synthetic comment -- @@ -89,6 +89,7 @@
mOverviewPart = new OverviewInfoPart(body, toolkit, mEditor);
mOverviewPart.getSection().setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
managedForm.addPart(mOverviewPart);

newManifestExtrasPart(managedForm);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/UiElementPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/UiElementPart.java
//Synthetic comment -- index 5e7ca30..46fcfbb 100644

//Synthetic comment -- @@ -25,8 +25,14 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
//Synthetic comment -- @@ -71,7 +77,7 @@
public ManifestEditor getEditor() {
return mEditor;
}
    
/**
* Returns the {@link UiElementNode} associated with this part.
*/
//Synthetic comment -- @@ -81,13 +87,13 @@

/**
* Changes the element node handled by this part.
     * 
     * @param uiElementNode The new element node for the part. 
*/
public void setUiElementNode(UiElementNode uiElementNode) {
mUiElementNode = uiElementNode;
}
    
/**
* Initializes the form part.
* <p/>
//Synthetic comment -- @@ -108,7 +114,7 @@
* <br/>
* Derived class override this if needed, however in most cases the default
* implementation should be enough.
     * 
* @param sectionTitle The section part's title
* @param sectionDescription The section part's description
*/
//Synthetic comment -- @@ -125,7 +131,7 @@
* <code>initialize</code> (i.e. right after the form part is added to the managed form.)
* <p/>
* Derived classes can override this if necessary.
     * 
* @param managedForm The owner managed form
*/
protected void createFormControls(IManagedForm managedForm) {
//Synthetic comment -- @@ -150,7 +156,7 @@

/**
* Add all the attribute UI widgets into the underlying table layout.
     * 
* @param managedForm The owner managed form
*/
protected void createUiAttributes(IManagedForm managedForm) {
//Synthetic comment -- @@ -159,7 +165,7 @@
return;
}

        // Remove any old UI controls 
for (Control c : table.getChildren()) {
c.dispose();
}
//Synthetic comment -- @@ -171,20 +177,20 @@
}

/**
     * Actually fills the table. 
* This is called by {@link #createUiAttributes(IManagedForm)} to populate the new
* table. The default implementation is to use
* {@link #insertUiAttributes(UiElementNode, Composite, IManagedForm)} to actually
* place the attributes of the default {@link UiElementNode} in the table.
* <p/>
* Derived classes can override this to add controls in the table before or after.
     * 
* @param table The table to fill. It must have 2 columns.
* @param managedForm The managed form for new controls.
*/
protected void fillTable(Composite table, IManagedForm managedForm) {
int inserted = insertUiAttributes(mUiElementNode, table, managedForm);
        
if (inserted == 0) {
createLabel(table, managedForm.getToolkit(),
"No attributes to display, waiting for SDK to finish loading...",
//Synthetic comment -- @@ -194,7 +200,7 @@

/**
* Insert the UI attributes of the given {@link UiElementNode} in the given table.
     * 
* @param uiNode The {@link UiElementNode} that contains the attributes to display.
*               Must not be null.
* @param table The table to fill. It must have 2 columns.
//Synthetic comment -- @@ -237,7 +243,7 @@
* <p/>
* This is done by iterating over all attributes and updating the super's
* internal dirty flag. Stop once at least one attribute is dirty.
     * 
* @return <code>true</code> if the part is dirty, <code>false</code>
*         otherwise.
*/
//Synthetic comment -- @@ -253,11 +259,11 @@
}
return super.isDirty();
}
    
/**
* If part is displaying information loaded from a model, this method
* instructs it to commit the new (modified) data back into the model.
     * 
* @param onSave
*            indicates if commit is called during 'save' operation or for
*            some other reason (for example, if form is contained in a
//Synthetic comment -- @@ -280,4 +286,70 @@
// reset the dirty flag after all the side effects from committing have occurred.
super.commit(onSave);
}
}








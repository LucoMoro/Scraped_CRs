/*Add framework resources to the Resource Chooser

Fix & reenable the support for framework resources in the Resource
Chooser dialog.

Change-Id:I98611668473543aaec56ce3bc2e28e6606c867fd*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 1a6a9de..25d66a2 100644

//Synthetic comment -- @@ -84,19 +84,14 @@
public class ResourceChooser extends AbstractElementListSelectionDialog {

private Pattern mProjectResourcePattern;
private ResourceType mResourceType;
private IResourceRepository mProjectResources;
private Pattern mSystemResourcePattern;
private IResourceRepository mSystemResources;
private Button mProjectButton;
private Button mSystemButton;
    private Button mNewButton;
private String mCurrentResource;
private final IProject mProject;

/**
//Synthetic comment -- @@ -120,11 +115,9 @@
mProjectResourcePattern = Pattern.compile(
"@" + mResourceType.getName() + "/(.+)"); //$NON-NLS-1$ //$NON-NLS-2$

        mSystemResources = systemResources;
        mSystemResourcePattern = Pattern.compile(
                "@android:" + mResourceType.getName() + "/(.+)"); //$NON-NLS-1$ //$NON-NLS-2$

setTitle("Resource Chooser");
setMessage(String.format("Choose a %1$s resource",
//Synthetic comment -- @@ -146,7 +139,7 @@
ResourceItem item = (ResourceItem)elements[0];

mCurrentResource = ResourceHelper.getXmlString(mResourceType, item,
                    mSystemButton.getSelection());
}
}

//Synthetic comment -- @@ -174,9 +167,6 @@
* @param top the parent composite
*/
private void createButtons(Composite top) {
mProjectButton = new Button(top, SWT.RADIO);
mProjectButton.setText("Project Resources");
mProjectButton.addSelectionListener(new SelectionAdapter() {
//Synthetic comment -- @@ -185,6 +175,7 @@
super.widgetSelected(e);
if (mProjectButton.getSelection()) {
setListElements(mProjectResources.getResources(mResourceType));
                    mNewButton.setEnabled(true);
}
}
});
//Synthetic comment -- @@ -194,8 +185,9 @@
@Override
public void widgetSelected(SelectionEvent e) {
super.widgetSelected(e);
                if (mSystemButton.getSelection()) {
setListElements(mSystemResources.getResources(mResourceType));
                    mNewButton.setEnabled(false);
}
}
});
//Synthetic comment -- @@ -206,16 +198,15 @@
* @param top the parent composite
*/
private void createNewResButtons(Composite top) {
        mNewButton = new Button(top, SWT.NONE);

String title = String.format("New %1$s...", mResourceType.getDisplayName());
        mNewButton.setText(title);

// We only support adding new strings right now
        mNewButton.setEnabled(Hyperlinks.isValueResource(mResourceType));

        mNewButton.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
super.widgetSelected(e);
//Synthetic comment -- @@ -375,12 +366,7 @@
* @return The repository currently selected.
*/
private IResourceRepository getCurrentRepository() {
        return mSystemButton.getSelection() ? mSystemResources : mProjectResources;
}

/**
//Synthetic comment -- @@ -421,27 +407,23 @@

// Is this a system resource?
// If not a system resource or if they are not available, this will be a project res.
        Matcher m = mSystemResourcePattern.matcher(resourceString);
        if (m.matches()) {
            itemName = m.group(1);
            isSystem = true;
}

if (!isSystem && itemName == null) {
// Try to match project resource name
            m = mProjectResourcePattern.matcher(resourceString);
if (m.matches()) {
itemName = m.group(1);
}
}

// Update the repository selection
        mProjectButton.setSelection(!isSystem);
        mSystemButton.setSelection(isSystem);

// Update the list
setupResourceList();








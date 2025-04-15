/*SDK Manager: rework license display in install chooser dialog.

Packages are sorted by license and displayed in a
tree form. The root branches are the licenses and the
leaf nodes are the packages.

The "accept all" action becomes "accept all packages
for this specific license".

Change-Id:Ie6555bcb07739fbbb02ff01cd3bb4bc2257b1795*/
//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java
//Synthetic comment -- index 48f5553..5aebc88 100755

//Synthetic comment -- @@ -17,21 +17,24 @@
package com.android.sdkuilib.internal.repository;

import com.android.SdkConstants;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -54,11 +57,16 @@
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import java.util.ArrayList;
import java.util.Collection;


/**
//Synthetic comment -- @@ -68,19 +76,20 @@

/** Last dialog size for this session. */
private static Point sLastSize;
    private boolean mLicenseAcceptAll;
private boolean mInternalLicenseRadioUpdate;

// UI fields
private SashForm mSashForm;
private Composite mPackageRootComposite;
    private TableViewer mTableViewPackage;
    private Table mTablePackage;
    private TableColumn mTableColum;
private StyledText mPackageText;
private Button mLicenseRadioAccept;
private Button mLicenseRadioReject;
    private Button mLicenseRadioAcceptAll;
private Group mPackageTextGroup;
private final UpdaterData mUpdaterData;
private Group mTableGroup;
//Synthetic comment -- @@ -93,7 +102,7 @@
* is currently done using a simple linear search, which is fine since we only have a very
* limited number of archives to deal with (e.g. < 10 now). We might want to revisit
* this later if it becomes an issue. Right now just do the simple thing.
     *<p/>
* Typically we could add a map Archive=>ArchiveInfo later.
*/
private final Collection<ArchiveInfo> mArchives;
//Synthetic comment -- @@ -102,6 +111,7 @@

/**
* Create the dialog.
* @param parentShell The shell to use, typically updaterData.getWindowShell()
* @param updaterData The updater data
* @param archives The archives to be installed
//Synthetic comment -- @@ -123,8 +133,8 @@
* Returns the results, i.e. the list of selected new archives to install.
* This is similar to the {@link ArchiveInfo} list instance given to the constructor
* except only accepted archives are present.
     *
     * An empty list is returned if cancel was choosen.
*/
public ArrayList<ArchiveInfo> getResult() {
ArrayList<ArchiveInfo> ais = new ArrayList<ArchiveInfo>();
//Synthetic comment -- @@ -157,12 +167,12 @@
mTableGroup.setText("Packages");
mTableGroup.setLayout(new GridLayout(1, false/*makeColumnsEqual*/));

        mTableViewPackage = new TableViewer(mTableGroup, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
        mTablePackage = mTableViewPackage.getTable();
        mTablePackage.setHeaderVisible(false);
        mTablePackage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        mTablePackage.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
onPackageSelected();  //$hide$
//Synthetic comment -- @@ -173,12 +183,12 @@
}
});

        mTableColum = new TableColumn(mTablePackage, SWT.NONE);
        mTableColum.setWidth(100);
        mTableColum.setText("Packages");


// Right part of Sash form
mPackageRootComposite = new Composite(mSashForm, SWT.NONE);
mPackageRootComposite.setLayout(new GridLayout(4, false/*makeColumnsEqual*/));
mPackageRootComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//Synthetic comment -- @@ -233,9 +243,9 @@
});


        mLicenseRadioAcceptAll = new Button(mPackageRootComposite, SWT.RADIO);
        mLicenseRadioAcceptAll.setText("Accept All");
        mLicenseRadioAcceptAll.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
onLicenseRadioSelected();
//Synthetic comment -- @@ -309,20 +319,23 @@
for (ArchiveInfo ai : mArchives) {
Archive a = ai.getNewArchive();
if (a != null) {
                String license = a.getParentPackage().getLicense();
                ai.setAccepted(license == null || license.trim().length() == 0);
}
}

// Fill the list with the replacement packages
        mTableViewPackage.setLabelProvider(new NewArchivesLabelProvider());
        mTableViewPackage.setContentProvider(new NewArchivesContentProvider());
        mTableViewPackage.setInput(mArchives);

adjustColumnsWidth();

// select first item
        mTablePackage.select(0);
onPackageSelected();
}

//Synthetic comment -- @@ -332,7 +345,7 @@
private void setWindowImage() {
String imageName = "android_icon_16.png"; //$NON-NLS-1$
if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_DARWIN) {
            imageName = "android_icon_128.png"; //$NON-NLS-1$
}

if (mUpdaterData != null) {
//Synthetic comment -- @@ -354,11 +367,11 @@
ControlAdapter resizer = new ControlAdapter() {
@Override
public void controlResized(ControlEvent e) {
                Rectangle r = mTablePackage.getClientArea();
                mTableColum.setWidth(r.width);
}
};
        mTablePackage.addControlListener(resizer);
resizer.controlResized(null);
}

//Synthetic comment -- @@ -393,25 +406,73 @@
* Callback invoked when a package item is selected in the list.
*/
private void onPackageSelected() {
        ArchiveInfo ai = getSelectedArchive();
        displayInformation(ai);
displayMissingDependency(ai);
updateLicenceRadios(ai);
}

    /** Returns the currently selected {@link ArchiveInfo} or null. */
    private ArchiveInfo getSelectedArchive() {
        ISelection sel = mTableViewPackage.getSelection();
if (sel instanceof IStructuredSelection) {
Object elem = ((IStructuredSelection) sel).getFirstElement();
            if (elem instanceof ArchiveInfo) {
                return (ArchiveInfo) elem;
}
}
return null;
}

/**
* Updates the package description and license text depending on the selected package.
* <p/>
* Note that right now there is no logic to support more than one level of dependencies
//Synthetic comment -- @@ -420,19 +481,16 @@
* if we had a need for it, though. This would require changes to {@link ArchiveInfo} and
* {@link SdkUpdaterLogic}.
*/
    private void displayInformation(ArchiveInfo ai) {
        if (ai == null) {
            mPackageText.setText("Please select a package.");
return;
}

        Archive aNew = ai.getNewArchive();
        if (aNew == null) {
            // Only missing archives have a null archive, so we shouldn't be here.
            return;
        }

        Package pNew = aNew.getParentPackage();

mPackageText.setText("");                   //$NON-NLS-1$

//Synthetic comment -- @@ -495,10 +553,13 @@
addSectionTitle("Archive Description\n");
addText(aNew.getLongDescription(), "\n\n");                             //$NON-NLS-1$

        String license = pNew.getLicense();
if (license != null) {
            addSectionTitle("License\n");
            addText(license.trim(), "\n\n");                                       //$NON-NLS-1$
}

addSectionTitle("Site\n");
//Synthetic comment -- @@ -509,6 +570,46 @@
}

/**
* Computes and displays missing dependencies.
*
* If there's a selected package, check the dependency for that one.
//Synthetic comment -- @@ -613,20 +714,12 @@

boolean oneAccepted = false;

        if (mLicenseAcceptAll) {
            mLicenseRadioAcceptAll.setSelection(true);
            mLicenseRadioAccept.setEnabled(true);
            mLicenseRadioReject.setEnabled(true);
            mLicenseRadioAccept.setSelection(false);
            mLicenseRadioReject.setSelection(false);
        } else {
            mLicenseRadioAcceptAll.setSelection(false);
            oneAccepted = ai != null && ai.isAccepted();
            mLicenseRadioAccept.setEnabled(ai != null);
            mLicenseRadioReject.setEnabled(ai != null);
            mLicenseRadioAccept.setSelection(oneAccepted);
            mLicenseRadioReject.setSelection(ai != null && ai.isRejected());
        }

// The install button is enabled if there's at least one package accepted.
// If the current one isn't, look for another one.
//Synthetic comment -- @@ -649,7 +742,7 @@
* Callback invoked when one of the radio license buttons is selected.
*
* - accept/refuse: toggle, update item checkbox
     * - accept all: set accept-all, check all items
*/
private void onLicenseRadioSelected() {
if (mInternalLicenseRadioUpdate) {
//Synthetic comment -- @@ -657,32 +750,41 @@
}
mInternalLicenseRadioUpdate = true;

        ArchiveInfo ai = getSelectedArchive();

        if (ai == null) {
            // Should never happen.
            return;
        }

boolean needUpdate = true;

        if (!mLicenseAcceptAll && mLicenseRadioAcceptAll.getSelection()) {
// Accept all has been switched on. Mark all packages as accepted
            mLicenseAcceptAll = true;
            for(ArchiveInfo ai2 : mArchives) {
                ai2.setAccepted(true);
                ai2.setRejected(false);
}

        } else if (mLicenseRadioAccept.getSelection()) {
// Accept only this one
            mLicenseAcceptAll = false;
ai.setAccepted(true);
ai.setRejected(false);

        } else if (mLicenseRadioReject.getSelection()) {
// Reject only this one
            mLicenseAcceptAll = false;
ai.setAccepted(false);
ai.setRejected(true);

//Synthetic comment -- @@ -693,10 +795,13 @@
mInternalLicenseRadioUpdate = false;

if (needUpdate) {
            if (mLicenseAcceptAll) {
                mTableViewPackage.refresh();
} else {
               mTableViewPackage.refresh(ai);
}
displayMissingDependency(ai);
updateLicenceRadios(ai);
//Synthetic comment -- @@ -707,73 +812,312 @@
* Callback invoked when a package item is double-clicked in the list.
*/
private void onPackageDoubleClick() {
        ArchiveInfo ai = getSelectedArchive();

        if (ai == null) {
            // Should never happen.
            return;
}

        boolean wasAccepted = ai.isAccepted();
        ai.setAccepted(!wasAccepted);
        ai.setRejected(wasAccepted);

        // update state
        mLicenseAcceptAll = false;
        mTableViewPackage.refresh(ai);
        displayMissingDependency(ai);
        updateLicenceRadios(ai);
}

private class NewArchivesLabelProvider extends LabelProvider {
@Override
public Image getImage(Object element) {
            assert element instanceof ArchiveInfo;
            ArchiveInfo ai = (ArchiveInfo) element;

            ImageFactory imgFactory = mUpdaterData.getImageFactory();
            if (imgFactory != null) {
                if (ai.isAccepted()) {
                    return imgFactory.getImageByName("accept_icon16.png");
                } else if (ai.isRejected()) {
                    return imgFactory.getImageByName("reject_icon16.png");
}
                return imgFactory.getImageByName("unknown_icon16.png");
}
            return super.getImage(element);
}

@Override
public String getText(Object element) {
            assert element instanceof ArchiveInfo;
            ArchiveInfo ai = (ArchiveInfo) element;

            String desc = ai.getShortDescription();

            if (ai.isDependencyFor()) {
                desc += " [*]";
}

            return desc;
}
}

    private class NewArchivesContentProvider implements IStructuredContentProvider {

@Override
public void dispose() {
// pass
}

@Override
public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // Ignore. The input is always mArchives
}

@Override
        public Object[] getElements(Object inputElement) {
            return mArchives.toArray();
}
}

// End of hiding from SWT Designer








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/PackagesPage.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/PackagesPage.java
//Synthetic comment -- index d9a1102..85a4df8 100755

//Synthetic comment -- @@ -569,6 +569,10 @@
case SORT_SOURCE:
button = mCheckSortSource;
break;
}

if (button != null && !button.isDisposed()) {








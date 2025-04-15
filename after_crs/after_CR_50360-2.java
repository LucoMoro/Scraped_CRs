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
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.License;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -54,11 +57,16 @@
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


/**
//Synthetic comment -- @@ -68,19 +76,20 @@

/** Last dialog size for this session. */
private static Point sLastSize;
    /** Precomputed flag indicating whether the "accept license" radio is checked. */
    private boolean mAcceptSameAllLicense;
private boolean mInternalLicenseRadioUpdate;

// UI fields
private SashForm mSashForm;
private Composite mPackageRootComposite;
    private TreeViewer mTreeViewPackage;
    private Tree mTreePackage;
    private TreeColumn mTreeColum;
private StyledText mPackageText;
private Button mLicenseRadioAccept;
private Button mLicenseRadioReject;
    private Button mLicenseRadioAcceptLicense;
private Group mPackageTextGroup;
private final UpdaterData mUpdaterData;
private Group mTableGroup;
//Synthetic comment -- @@ -93,7 +102,7 @@
* is currently done using a simple linear search, which is fine since we only have a very
* limited number of archives to deal with (e.g. < 10 now). We might want to revisit
* this later if it becomes an issue. Right now just do the simple thing.
     * <p/>
* Typically we could add a map Archive=>ArchiveInfo later.
*/
private final Collection<ArchiveInfo> mArchives;
//Synthetic comment -- @@ -102,6 +111,7 @@

/**
* Create the dialog.
     *
* @param parentShell The shell to use, typically updaterData.getWindowShell()
* @param updaterData The updater data
* @param archives The archives to be installed
//Synthetic comment -- @@ -123,8 +133,8 @@
* Returns the results, i.e. the list of selected new archives to install.
* This is similar to the {@link ArchiveInfo} list instance given to the constructor
* except only accepted archives are present.
     * <p/>
     * An empty list is returned if cancel was chosen.
*/
public ArrayList<ArchiveInfo> getResult() {
ArrayList<ArchiveInfo> ais = new ArrayList<ArchiveInfo>();
//Synthetic comment -- @@ -157,12 +167,12 @@
mTableGroup.setText("Packages");
mTableGroup.setLayout(new GridLayout(1, false/*makeColumnsEqual*/));

        mTreeViewPackage = new TreeViewer(mTableGroup, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
        mTreePackage = mTreeViewPackage.getTree();
        mTreePackage.setHeaderVisible(false);
        mTreePackage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        mTreePackage.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
onPackageSelected();  //$hide$
//Synthetic comment -- @@ -173,12 +183,12 @@
}
});

        mTreeColum = new TreeColumn(mTreePackage, SWT.NONE);
        mTreeColum.setWidth(100);
        mTreeColum.setText("Packages");

// Right part of Sash form

mPackageRootComposite = new Composite(mSashForm, SWT.NONE);
mPackageRootComposite.setLayout(new GridLayout(4, false/*makeColumnsEqual*/));
mPackageRootComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//Synthetic comment -- @@ -233,9 +243,9 @@
});


        mLicenseRadioAcceptLicense = new Button(mPackageRootComposite, SWT.RADIO);
        mLicenseRadioAcceptLicense.setText("Accept License");
        mLicenseRadioAcceptLicense.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
onLicenseRadioSelected();
//Synthetic comment -- @@ -309,20 +319,23 @@
for (ArchiveInfo ai : mArchives) {
Archive a = ai.getNewArchive();
if (a != null) {
                License license = a.getParentPackage().getLicense();
                boolean hasLicense = license != null &&
                                     license.getLicense() != null &&
                                     license.getLicense().length() > 0;
                ai.setAccepted(!hasLicense);
}
}

// Fill the list with the replacement packages
        mTreeViewPackage.setLabelProvider(new NewArchivesLabelProvider());
        mTreeViewPackage.setContentProvider(new NewArchivesContentProvider());
        mTreeViewPackage.setInput(createTreeInput(mArchives));
        mTreeViewPackage.expandAll();

adjustColumnsWidth();

// select first item
onPackageSelected();
}

//Synthetic comment -- @@ -332,7 +345,7 @@
private void setWindowImage() {
String imageName = "android_icon_16.png"; //$NON-NLS-1$
if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_DARWIN) {
            imageName = "android_icon_128.png";   //$NON-NLS-1$
}

if (mUpdaterData != null) {
//Synthetic comment -- @@ -354,11 +367,11 @@
ControlAdapter resizer = new ControlAdapter() {
@Override
public void controlResized(ControlEvent e) {
                Rectangle r = mTreePackage.getClientArea();
                mTreeColum.setWidth(r.width);
}
};
        mTreePackage.addControlListener(resizer);
resizer.controlResized(null);
}

//Synthetic comment -- @@ -393,25 +406,73 @@
* Callback invoked when a package item is selected in the list.
*/
private void onPackageSelected() {
        Object item = getSelectedItem();

        // Update mAcceptSameAllLicense : true if all items under the same license are accepted.
        ArchiveInfo ai = null;
        List<ArchiveInfo> list = null;
        if (item instanceof ArchiveInfo) {
            ai = (ArchiveInfo) item;

            Object p =
                ((NewArchivesContentProvider) mTreeViewPackage.getContentProvider()).getParent(ai);
            if (p instanceof LicenseEntry) {
                list = ((LicenseEntry) p).getArchives();
            }
            displayPackageInformation(ai);

        } else if (item instanceof LicenseEntry) {
            LicenseEntry entry = (LicenseEntry) item;
            list = entry.getArchives();
            displayLicenseInformation(entry);

        } else {
            // Fallback, should not happen.
            displayEmptyInformation();
        }

        // the "Accept License" radio is selected if there's a license with >= 0 items
        // and they are all in "accepted" state.
        mAcceptSameAllLicense = list != null && list.size() > 0;
        if (mAcceptSameAllLicense) {
            assert list != null;
            License lic0 = getLicense(list.get(0));
            for (ArchiveInfo ai2 : list) {
                License lic2 = getLicense(ai2);
                if (ai2.isAccepted() && (lic0 == lic2 || lic0.equals(lic2))) {
                    continue;
                } else {
                    mAcceptSameAllLicense = false;
                    break;
                }
            }
        }

displayMissingDependency(ai);
updateLicenceRadios(ai);
}

    /** Returns the currently selected tree item.
     * @return Either {@link ArchiveInfo} or {@link LicenseEntry} or null. */
    private Object getSelectedItem() {
        ISelection sel = mTreeViewPackage.getSelection();
if (sel instanceof IStructuredSelection) {
Object elem = ((IStructuredSelection) sel).getFirstElement();
            if (elem instanceof ArchiveInfo || elem instanceof LicenseEntry) {
                return elem;
}
}
return null;
}

/**
     * Information displayed when nothing valid is selected.
     */
    private void displayEmptyInformation() {
        mPackageText.setText("Please select a package or a license.");
    }

    /**
* Updates the package description and license text depending on the selected package.
* <p/>
* Note that right now there is no logic to support more than one level of dependencies
//Synthetic comment -- @@ -420,19 +481,16 @@
* if we had a need for it, though. This would require changes to {@link ArchiveInfo} and
* {@link SdkUpdaterLogic}.
*/
    private void displayPackageInformation(ArchiveInfo ai) {
        Archive aNew = ai   == null ? null : ai.getNewArchive();
        Package pNew = aNew == null ? null : aNew.getParentPackage();

        if (pNew == null) {
            displayEmptyInformation();
return;
}
        assert ai   != null;                        // make Eclipse null detector happy
        assert aNew != null;

mPackageText.setText("");                   //$NON-NLS-1$

//Synthetic comment -- @@ -495,10 +553,13 @@
addSectionTitle("Archive Description\n");
addText(aNew.getLongDescription(), "\n\n");                             //$NON-NLS-1$

        License license = pNew.getLicense();
if (license != null) {
            String text = license.getLicense();
            if (text != null) {
                addSectionTitle("License\n");
                addText(text.trim(), "\n\n");                                   //$NON-NLS-1$
            }
}

addSectionTitle("Site\n");
//Synthetic comment -- @@ -509,6 +570,46 @@
}

/**
     * Updates the description for a license entry.
     */
    private void displayLicenseInformation(LicenseEntry entry) {
        List<ArchiveInfo> archives = entry == null ? null : entry.getArchives();
        if (archives == null) {
            // There should not be a license entry without any package in it.
            displayEmptyInformation();
            return;
        }
        assert entry != null;

        mPackageText.setText("");                   //$NON-NLS-1$

        License license = null;
        addSectionTitle("Packages\n");
        for (ArchiveInfo ai : entry.getArchives()) {
            Archive aNew = ai.getNewArchive();
            if (aNew != null) {
                Package pNew = aNew.getParentPackage();
                if (pNew != null) {
                    if (license == null) {
                        license = pNew.getLicense();
                    } else {
                        assert license.equals(pNew.getLicense()); // all items have the same license
                    }
                    addText("- ", pNew.getShortDescription(), "\n"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }

        if (license != null) {
            String text = license.getLicense();
            if (text != null) {
                addSectionTitle("\nLicense\n");
                addText(text.trim(), "\n\n");                                   //$NON-NLS-1$
            }
        }
    }

    /**
* Computes and displays missing dependencies.
*
* If there's a selected package, check the dependency for that one.
//Synthetic comment -- @@ -613,20 +714,12 @@

boolean oneAccepted = false;

        mLicenseRadioAcceptLicense.setSelection(mAcceptSameAllLicense);
        oneAccepted = ai != null && ai.isAccepted();
        mLicenseRadioAccept.setEnabled(ai != null);
        mLicenseRadioReject.setEnabled(ai != null);
        mLicenseRadioAccept.setSelection(oneAccepted);
        mLicenseRadioReject.setSelection(ai != null && ai.isRejected());

// The install button is enabled if there's at least one package accepted.
// If the current one isn't, look for another one.
//Synthetic comment -- @@ -649,7 +742,7 @@
* Callback invoked when one of the radio license buttons is selected.
*
* - accept/refuse: toggle, update item checkbox
     * - accept all: set accept-all, check all items with the *same* license
*/
private void onLicenseRadioSelected() {
if (mInternalLicenseRadioUpdate) {
//Synthetic comment -- @@ -657,32 +750,41 @@
}
mInternalLicenseRadioUpdate = true;

        Object item = getSelectedItem();
        ArchiveInfo ai = (item instanceof ArchiveInfo) ? (ArchiveInfo) item : null;
boolean needUpdate = true;

        if (!mAcceptSameAllLicense && mLicenseRadioAcceptLicense.getSelection()) {
// Accept all has been switched on. Mark all packages as accepted

            List<ArchiveInfo> list = null;
            if (item instanceof LicenseEntry) {
                list = ((LicenseEntry) item).getArchives();
            } else if (ai != null) {
                Object p = ((NewArchivesContentProvider) mTreeViewPackage.getContentProvider())
                                                                         .getParent(ai);
                if (p instanceof LicenseEntry) {
                    list = ((LicenseEntry) p).getArchives();
                }
}

            if (list != null && list.size() > 0) {
                mAcceptSameAllLicense = true;
                for(ArchiveInfo ai2 : list) {
                    ai2.setAccepted(true);
                    ai2.setRejected(false);
                }
            }

        } else if (ai != null && mLicenseRadioAccept.getSelection()) {
// Accept only this one
            mAcceptSameAllLicense = false;
ai.setAccepted(true);
ai.setRejected(false);

        } else if (ai != null && mLicenseRadioReject.getSelection()) {
// Reject only this one
            mAcceptSameAllLicense = false;
ai.setAccepted(false);
ai.setRejected(true);

//Synthetic comment -- @@ -693,10 +795,13 @@
mInternalLicenseRadioUpdate = false;

if (needUpdate) {
            if (mAcceptSameAllLicense) {
                mTreeViewPackage.refresh();
} else {
               mTreeViewPackage.refresh(ai);
               mTreeViewPackage.refresh(
                       ((NewArchivesContentProvider) mTreeViewPackage.getContentProvider()).
                       getParent(ai));
}
displayMissingDependency(ai);
updateLicenceRadios(ai);
//Synthetic comment -- @@ -707,73 +812,312 @@
* Callback invoked when a package item is double-clicked in the list.
*/
private void onPackageDoubleClick() {
        Object item = getSelectedItem();

        if (item instanceof ArchiveInfo) {
            ArchiveInfo ai = (ArchiveInfo) item;
            boolean wasAccepted = ai.isAccepted();
            ai.setAccepted(!wasAccepted);
            ai.setRejected(wasAccepted);

            // update state
            mAcceptSameAllLicense = false;
            mTreeViewPackage.refresh(ai);
            // refresh parent since its icon might have changed.
            mTreeViewPackage.refresh(
                    ((NewArchivesContentProvider) mTreeViewPackage.getContentProvider()).
                    getParent(ai));

            displayMissingDependency(ai);
            updateLicenceRadios(ai);

        } else if (item instanceof LicenseEntry) {
            mTreeViewPackage.setExpandedState(item, !mTreeViewPackage.getExpandedState(item));
}
}

    /**
     * Provides the labels for the tree view.
     * Root branches are {@link LicenseEntry} elements.
     * Leave nodes are {@link ArchiveInfo} which all have the same license.
     */
private class NewArchivesLabelProvider extends LabelProvider {
@Override
public Image getImage(Object element) {
            if (element instanceof ArchiveInfo) {
                // Archive icon: accepted (green), rejected (red), not set yet (question mark)
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

            } else if (element instanceof LicenseEntry) {
                // License icon: green if all below are accepted, red if all rejected, otherwise
                // no icon.
                ImageFactory imgFactory = mUpdaterData.getImageFactory();
                if (imgFactory != null) {
                    boolean allAccepted = true;
                    boolean allRejected = true;
                    for (ArchiveInfo ai : ((LicenseEntry) element).getArchives()) {
                        allAccepted = allAccepted && ai.isAccepted();
                        allRejected = allRejected && ai.isRejected();
                    }
                    if (allAccepted && !allRejected) {
                        return imgFactory.getImageByName("accept_icon16.png");
                    } else if (!allAccepted && allRejected) {
                        return imgFactory.getImageByName("reject_icon16.png");
                    }
                }
}

            return null;
}

@Override
public String getText(Object element) {
            if (element instanceof LicenseEntry) {
                return ((LicenseEntry) element).getLicenseRef();

            } else if (element instanceof ArchiveInfo) {
                ArchiveInfo ai = (ArchiveInfo) element;

                String desc = ai.getShortDescription();

                if (ai.isDependencyFor()) {
                    desc += " [*]";
                }

                return desc;

}

            assert element instanceof String || element instanceof ArchiveInfo;
            return null;
}
}

    /**
     * Provides the content for the tree view.
     * Root branches are {@link LicenseEntry} elements.
     * Leave nodes are {@link ArchiveInfo} which all have the same license.
     */
    private class NewArchivesContentProvider implements ITreeContentProvider {
        private List<LicenseEntry> mInput;

@Override
public void dispose() {
// pass
}

        @SuppressWarnings("unchecked")
@Override
public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // Input should be the result from createTreeInput.
            if (newInput instanceof List<?> &&
                    ((List<?>) newInput).size() > 0 &&
                    ((List<?>) newInput).get(0) instanceof LicenseEntry) {
                mInput = (List<LicenseEntry>) newInput;
            } else {
                mInput = null;
            }
}

@Override
        public boolean hasChildren(Object parent) {
            if (parent instanceof List<?>) {
                // This is the root of the tree.
                return true;

            } else if (parent instanceof LicenseEntry) {
                return ((LicenseEntry) parent).getArchives().size() > 0;
            }

            return false;
}

        @Override
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }

        @Override
        public Object[] getChildren(Object parent) {
            if (parent instanceof List<?>) {
                return ((List<?>) parent).toArray();

            } else if (parent instanceof LicenseEntry) {
                return ((LicenseEntry) parent).getArchives().toArray();
            }

            return new Object[0];
        }

        @Override
        public Object getParent(Object child) {
            if (child instanceof LicenseEntry) {
                return ((LicenseEntry) child).getRoot();

            } else if (child instanceof ArchiveInfo && mInput != null) {
                for (LicenseEntry entry : mInput) {
                    if (entry.getArchives().contains(child)) {
                        return entry;
                    }
                }
            }

            return null;
        }
    }

    /**
     * Represents a branch in the view tree: an entry where all the sub-archive info
     * share the same license. Contains a link to the share root list for convenience.
     */
    private static class LicenseEntry {
        private final List<LicenseEntry> mRoot;
        private final String mLicenseRef;
        private final List<ArchiveInfo> mArchives;

        public LicenseEntry(
                @NonNull List<LicenseEntry> root,
                @NonNull String licenseRef,
                @NonNull List<ArchiveInfo> archives) {
            mRoot = root;
            mLicenseRef = licenseRef;
            mArchives = archives;
        }

        @NonNull
        public List<LicenseEntry> getRoot() {
            return mRoot;
        }

        @NonNull
        public String getLicenseRef() {
            return mLicenseRef;
        }

        @NonNull
        public List<ArchiveInfo> getArchives() {
            return mArchives;
        }
    }

    /**
     * Creates the tree structure based on the given archives.
     * The current structure is to have a branch per license type,
     * with all the archives sharing the same license under it.
     * Elements with no license are left at the root.
     *
     * @param archives The non-null collection of archive info to display. Ideally non-empty.
     * @return A list of {@link LicenseEntry}, each containing a list of {@link ArchiveInfo}.
     */
    @NonNull
    private List<LicenseEntry> createTreeInput(@NonNull Collection<ArchiveInfo> archives) {
        // Build an ordered map with all the licenses, ordered by license ref name.
        final String noLicense = "No license";      //NLS

        Comparator<String> comp = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                boolean first1 = noLicense.equals(s1);
                boolean first2 = noLicense.equals(s2);
                if (first1 && first2) {
                    return 0;
                } else if (first1) {
                    return -1;
                } else if (first2) {
                    return 1;
                }
                return s1.compareTo(s2);
            }
        };

        Map<String, List<ArchiveInfo>> map = new TreeMap<String, List<ArchiveInfo>>(comp);

        for (ArchiveInfo info : archives) {
            String ref = noLicense;
            License license = getLicense(info);
            if (license != null && license.getLicenseRef() != null) {
                ref = prettyLicenseRef(license.getLicenseRef());
            }

            List<ArchiveInfo> list = map.get(ref);
            if (list == null) {
                map.put(ref, list = new ArrayList<ArchiveInfo>());
            }
            list.add(info);
        }

        // Transform result into a list
        List<LicenseEntry> licensesList = new ArrayList<LicenseEntry>();
        for (Map.Entry<String, List<ArchiveInfo>> entry : map.entrySet()) {
            licensesList.add(new LicenseEntry(licensesList, entry.getKey(), entry.getValue()));
        }

        return licensesList;
    }

    /**
     * Helper method to retrieve the {@link License} for a given {@link ArchiveInfo}.
     *
     * @param ai The archive info. Can be null.
     * @return The license for the package owning the archive. Can be null.
     */
    @Nullable
    private License getLicense(@Nullable ArchiveInfo ai) {
        if (ai != null) {
            Archive aNew = ai.getNewArchive();
            if (aNew != null) {
                Package pNew = aNew.getParentPackage();
                if (pNew != null) {
                    return pNew.getLicense();
                }
            }
        }
        return null;
    }

    /**
     * Reformats the licenseRef to be more human-readable.
     * It's an XML ref and in practice it looks like [oem-]android-[type]-license.
     * If it's not a format we can deal with, leave it alone.
     */
    private String prettyLicenseRef(String ref) {
        // capitalize every word
        StringBuilder sb = new StringBuilder();
        boolean capitalize = true;
        for (char c : ref.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                if (capitalize) {
                    c = (char) (c + 'A' - 'a');
                    capitalize = false;
                }
            } else {
                if (c == '-') {
                    c = ' ';
                }
                capitalize = true;
            }
            sb.append(c);
        }

        ref = sb.toString();

        // A few acronyms should stay upper-case
        for (String w : new String[] { "Sdk", "Mips", "Arm" }) {
            ref = ref.replaceAll(w, w.toUpperCase(Locale.US));
        }

        return ref;
}

// End of hiding from SWT Designer








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/PackagesPage.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/PackagesPage.java
//Synthetic comment -- index d9a1102..85a4df8 100755

//Synthetic comment -- @@ -569,6 +569,10 @@
case SORT_SOURCE:
button = mCheckSortSource;
break;
            case RELOAD:
            case SHOW_ADDON_SITES:
                // No checkmark to update
                break;
}

if (button != null && !button.isDisposed()) {








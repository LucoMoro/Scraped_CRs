/*Merge "SDK Manager: fix to auto-select top platfrom at startup."

SDK Bug:  40456

(cherry picked from commit 3fae5f5a94d6b8ef9a6126bfc3e45823696b0542)

Change-Id:Ibcf97431a64a8cdcd9bbe6b269cfcd4e66e9fbab*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java
//Synthetic comment -- index d1fc6af..d9a1102 100755

//Synthetic comment -- @@ -150,6 +150,12 @@
mGroupPackages.getDisplay().syncExec(runnable);
}
};

            @Override
            protected void syncViewerSelection() {
                PackagesPage.this.syncViewerSelection();
            }

@Override
protected void refreshViewerInput() {
PackagesPage.this.refreshViewerInput();
//Synthetic comment -- @@ -478,7 +484,6 @@
mTreeViewer.setInput(null);
refreshViewerInput();
syncViewerSelection();
break;
case TOGGLE_SHOW_INSTALLED_PKG:
button = mCheckFilterInstalled;
//Synthetic comment -- @@ -882,10 +887,9 @@
* for initial run.
*/
private void onSelectNewUpdates(boolean selectNew, boolean selectUpdates, boolean selectTop) {
        // This will update the tree's "selected" state and then invoke syncViewerSelection()
        // which will in turn update tree.
mImpl.onSelectNewUpdates(selectNew, selectUpdates, selectTop);
}

/**
//Synthetic comment -- @@ -895,7 +899,6 @@
// This does not update the tree itself, syncViewerSelection does it below.
mImpl.onDeselectAll();
syncViewerSelection();
}

/**
//Synthetic comment -- @@ -935,33 +938,38 @@
ITreeContentProvider provider = (ITreeContentProvider) mTreeViewer.getContentProvider();

Object input = mTreeViewer.getInput();
        if (input != null) {
            for (Object cat : provider.getElements(input)) {
                Object[] children = provider.getElements(cat);
                boolean allChecked = children.length > 0;
                for (Object child : children) {
                    if (child instanceof PkgItem) {
                        PkgItem item = (PkgItem) child;
                        boolean checked = item.isChecked();
                        allChecked &= checked;

                        if (checked != mTreeViewer.getChecked(item)) {
                            if (checked) {
                                if (!mTreeViewer.getExpandedState(cat)) {
                                    mTreeViewer.setExpandedState(cat, true);
                                }
}
                            checkAndExpandItem(
                                    item,
                                    checked,
                                    true/*fixChildren*/,
                                    false/*fixParent*/);
}
}
}

                if (allChecked != mTreeViewer.getChecked(cat)) {
                    mTreeViewer.setChecked(cat, allChecked);
                }
}
}

        updateButtonsState();
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPageImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPageImpl.java
//Synthetic comment -- index 3ca0ee3..96d9db2 100755

//Synthetic comment -- @@ -82,6 +82,11 @@
*/
abstract protected void syncExec(Runnable runnable);

    /**
     * Synchronizes the 'checked' state of PkgItems in the tree with their internal isChecked state.
     */
    abstract protected void syncViewerSelection();

void performFirstLoad() {
// First a package loader is created that only checks
// the local cache xml files. It populates the package
//Synthetic comment -- @@ -310,6 +315,7 @@
selectUpdates,
selectTop,
SdkConstants.CURRENT_PLATFORM);
        syncViewerSelection();
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/MockPackagesPageImpl.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/MockPackagesPageImpl.java
//Synthetic comment -- index 9ab36c1..04b3027 100755

//Synthetic comment -- @@ -49,6 +49,11 @@
runnable.run();
}

    @Override
    protected void syncViewerSelection() {
        // No-op. There is no real tree viewer to synchronize.
    }

private MockTreeViewer mTreeViewer;

@Override








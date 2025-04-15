/*SDK Manager: fix to auto-select top platfrom at startup.

SDK Bug:  40456

Change-Id:I0d19c4cc7f0ea97ad62a0914c75c50f1e604847e*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java
//Synthetic comment -- index d1fc6af..d9a1102 100755

//Synthetic comment -- @@ -150,6 +150,12 @@
mGroupPackages.getDisplay().syncExec(runnable);
}
};
@Override
protected void refreshViewerInput() {
PackagesPage.this.refreshViewerInput();
//Synthetic comment -- @@ -478,7 +484,6 @@
mTreeViewer.setInput(null);
refreshViewerInput();
syncViewerSelection();
                    updateButtonsState();
break;
case TOGGLE_SHOW_INSTALLED_PKG:
button = mCheckFilterInstalled;
//Synthetic comment -- @@ -882,10 +887,9 @@
* for initial run.
*/
private void onSelectNewUpdates(boolean selectNew, boolean selectUpdates, boolean selectTop) {
        // This does not update the tree itself, syncViewerSelection does it below.
mImpl.onSelectNewUpdates(selectNew, selectUpdates, selectTop);
        syncViewerSelection();
        updateButtonsState();
}

/**
//Synthetic comment -- @@ -895,7 +899,6 @@
// This does not update the tree itself, syncViewerSelection does it below.
mImpl.onDeselectAll();
syncViewerSelection();
        updateButtonsState();
}

/**
//Synthetic comment -- @@ -935,33 +938,38 @@
ITreeContentProvider provider = (ITreeContentProvider) mTreeViewer.getContentProvider();

Object input = mTreeViewer.getInput();
        if (input == null) {
            return;
        }
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
                        checkAndExpandItem(item, checked, true/*fixChildren*/, false/*fixParent*/);
}
}
            }

            if (allChecked != mTreeViewer.getChecked(cat)) {
                mTreeViewer.setChecked(cat, allChecked);
}
}
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPageImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPageImpl.java
//Synthetic comment -- index 3ca0ee3..96d9db2 100755

//Synthetic comment -- @@ -82,6 +82,11 @@
*/
abstract protected void syncExec(Runnable runnable);

void performFirstLoad() {
// First a package loader is created that only checks
// the local cache xml files. It populates the package
//Synthetic comment -- @@ -310,6 +315,7 @@
selectUpdates,
selectTop,
SdkConstants.CURRENT_PLATFORM);
}

/**








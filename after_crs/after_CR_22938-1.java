/*SdkMan2: don't display update packages as sub-items.

Change-Id:I3261be52c637a49dbeba7b373947eb27807a05f9*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index b761510..7e93f90 100755

//Synthetic comment -- @@ -96,7 +96,7 @@
enum MenuAction {
RELOAD                      (SWT.NONE,  "Reload"),
SHOW_ADDON_SITES            (SWT.NONE,  "Manage Sources..."),
        TOGGLE_SHOW_ARCHIVES        (SWT.CHECK, "Show Archives Details"),
TOGGLE_SHOW_INSTALLED_PKG   (SWT.CHECK, "Show Installed Packages"),
TOGGLE_SHOW_OBSOLETE_PKG    (SWT.CHECK, "Show Obsolete Packages"),
TOGGLE_SHOW_UPDATE_NEW_PKG  (SWT.CHECK, "Show Updates/New Packages"),
//Synthetic comment -- @@ -972,7 +972,8 @@
}
} else {
// In non-detail mode, we install all the compatible archives
            // found in the selected pkg items. We also automatically
            // select update packages rather than the root package if any.

Object[] checked = mTreeViewer.getCheckedElements();
if (checked != null) {
//Synthetic comment -- @@ -983,6 +984,15 @@
p = (Package) c;
} else if (c instanceof PkgItem) {
p = ((PkgItem) c).getPackage();

                        PkgItem pi = (PkgItem) c;
                        if (pi.getState() == PkgState.HAS_UPDATE) {
                            List<Package> updates = pi.getUpdatePkgs();
                            // If there's one and only one update, auto-select it instead.
                            if (updates != null && updates.size() == 1) {
                                p = updates.get(0);
                            }
                        }
}
if (p != null) {
for (Archive a : p.getArchives()) {
//Synthetic comment -- @@ -1126,9 +1136,17 @@
case INSTALLED:
return "Installed";
case HAS_UPDATE:
                        List<Package> updates = pkg.getUpdatePkgs();
                        if (updates.size() == 1) {
                            return String.format("Update available: rev. %1$s",
                                    updates.get(0).getRevision());
                        } else {
                            // This case should not happen in *our* typical release workflow.
                            return "Multiple updates available";
                        }
case NEW:
                        return "Not installed";
                        // TODO display pkg.getRevision() in a tooltip
}
return pkg.getState().toString();

//Synthetic comment -- @@ -1211,7 +1229,10 @@

} else if (parentElement instanceof PkgItem) {
List<Package> pkgs = ((PkgItem) parentElement).getUpdatePkgs();

                // Display update packages as sub-items if there's more than one
                // or if the archive/details mode is activated.
                if (pkgs != null && (pkgs.size() > 1 || mDisplayArchives)) {
return pkgs.toArray();
}

//Synthetic comment -- @@ -1230,7 +1251,8 @@
}

public Object getParent(Object element) {
            // Pass. We don't try to compute the parent element and so far
            // that doesn't seem to affect the behavior of the tree widget.
return null;
}

//Synthetic comment -- @@ -1243,7 +1265,10 @@

} else if (parentElement instanceof PkgItem) {
List<Package> pkgs = ((PkgItem) parentElement).getUpdatePkgs();

                // Display update packages as sub-items if there's more than one
                // or if the archive/details mode is activated.
                if (pkgs != null && (pkgs.size() > 1 || mDisplayArchives)) {
return !pkgs.isEmpty();
}









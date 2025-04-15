/*Sdkman2: Rename "Sort by Source" to "Sort by Repository"

Change-Id:I3f1f0f8781cae29400f7b49d2c92e6f2b83783c2*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index df5dafa..570f769 100755

//Synthetic comment -- @@ -94,13 +94,13 @@

enum MenuAction {
RELOAD                      (SWT.NONE,  "Reload"),
        SHOW_ADDON_SITES            (SWT.NONE,  "Manage Sources..."),
TOGGLE_SHOW_ARCHIVES        (SWT.CHECK, "Show Archives Details"),
TOGGLE_SHOW_INSTALLED_PKG   (SWT.CHECK, "Show Installed Packages"),
TOGGLE_SHOW_OBSOLETE_PKG    (SWT.CHECK, "Show Obsolete Packages"),
TOGGLE_SHOW_UPDATE_NEW_PKG  (SWT.CHECK, "Show Updates/New Packages"),
SORT_API_LEVEL              (SWT.RADIO, "Sort by API Level"),
        SORT_SOURCE                 (SWT.RADIO, "Sort by Source")
;

private final int mMenuStyle;
//Synthetic comment -- @@ -301,7 +301,7 @@
mCheckSortApi.setSelection(true);

mCheckSortSource = new Button(mGroupOptions, SWT.RADIO);
        mCheckSortSource.setToolTipText("Sort by Source");
mCheckSortSource.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -310,7 +310,7 @@
expandInitial(mCategories);
}
});
        mCheckSortSource.setText("Source");

new Label(mGroupOptions, SWT.NONE);
new Label(mGroupOptions, SWT.NONE);
//Synthetic comment -- @@ -741,7 +741,7 @@
});

for (SdkSource source : sources) {
            Object key = source != null ? source : "Installed Packages";
Object iconRef = source != null ? source :
mUpdaterData.getImageFactory().getImageByName(ICON_PKG_INSTALLED);









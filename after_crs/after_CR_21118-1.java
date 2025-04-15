/*SDK Manager: 'update all' was not selecting addon updates.

Fix SDK Manager to correctly fetch the remote add-on list
prior to starting an 'update all'. This makes sure we pick
up new add-ons or extra packages when available.

SDK Bug 14392

Change-Id:If03d08f80549dbf46c4a077a4b81503c05f968ce*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java
//Synthetic comment -- index 4ca35d2..d579094 100755

//Synthetic comment -- @@ -223,6 +223,7 @@
mDescriptionLabel.setText("");  //$NON-NLS1-$
}

    /** User selected the 'update all' button. */
private void onUpdateSelected() {
mUpdaterData.updateOrInstallAll_WithGUI(
null /*selectedArchives*/,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 2216a29..b0160d9 100755

//Synthetic comment -- @@ -686,6 +686,7 @@
includeObsoletes);

if (selectedArchives == null) {
            loadRemoteAddonsList();
ul.addNewPlatforms(
archives,
getSources(),
//Synthetic comment -- @@ -733,6 +734,7 @@
getLocalSdkParser().getPackages(),
includeObsoletes);

        loadRemoteAddonsList();
ul.addNewPlatforms(
archives,
getSources(),








/*Refactoring: move SDK Updater core code into sdklib.

This requires the 2 following changes:
in       sdk.git:I79742d366b176cee2443bbed1f96dc253e6c74bbin tools/swt.git:I97c5874e6b5dcb5d6c0ca25ca921a291c6330fccChange-Id:I507a2bebe348fae598bc6e6fe24af3c5bf78acf0*/
//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/ArchiveInfo.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/ArchiveInfo.java
new file mode 100755
//Synthetic comment -- index 0000000..8068d4b

//Synthetic comment -- @@ -0,0 +1,160 @@








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/ISettingsPage.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/ISettingsPage.java
new file mode 100755
//Synthetic comment -- index 0000000..a1a4e76

//Synthetic comment -- @@ -0,0 +1,108 @@








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/IUpdaterData.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/IUpdaterData.java
new file mode 100755
//Synthetic comment -- index 0000000..f25dde5

//Synthetic comment -- @@ -0,0 +1,44 @@








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/PackageLoader.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/PackageLoader.java
new file mode 100755
//Synthetic comment -- index 0000000..48c9a7b

//Synthetic comment -- @@ -0,0 +1,497 @@








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/PkgItem.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/PkgItem.java
new file mode 100755
//Synthetic comment -- index 0000000..9e0912e

//Synthetic comment -- @@ -0,0 +1,277 @@








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SdkUpdaterLogic.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SdkUpdaterLogic.java
new file mode 100755
//Synthetic comment -- index 0000000..ffe8fa0

//Synthetic comment -- @@ -0,0 +1,1477 @@








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SdkUpdaterNoWindow.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SdkUpdaterNoWindow.java
new file mode 100755
//Synthetic comment -- index 0000000..f0f377d

//Synthetic comment -- @@ -0,0 +1,624 @@








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SettingsController.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SettingsController.java
new file mode 100755
//Synthetic comment -- index 0000000..dc45443

//Synthetic comment -- @@ -0,0 +1,382 @@








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/UpdaterData.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/UpdaterData.java
new file mode 100755
//Synthetic comment -- index 0000000..3acb0c4

//Synthetic comment -- @@ -0,0 +1,1088 @@








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/repository/ISdkChangeListener.java b/sdklib/src/main/java/com/android/sdklib/repository/ISdkChangeListener.java
new file mode 100755
//Synthetic comment -- index 0000000..5c0cab8

//Synthetic comment -- @@ -0,0 +1,54 @@








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/MockDownloadCache.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/MockDownloadCache.java
new file mode 100755
//Synthetic comment -- index 0000000..78e5b85

//Synthetic comment -- @@ -0,0 +1,236 @@








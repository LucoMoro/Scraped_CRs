/*Make "Finish" button that cannot press before KeyCheck page.
Fix wrong message when creating a new keystore.
Prevent "Could not find key" error on KeyCreationMode == false.

Change-Id:I2465efbc205cd02988f2b4be13add9f95ca54c58*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/ExportWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/ExportWizard.java
//Synthetic comment -- index 62090d4..2db9a76 100644

//Synthetic comment -- @@ -274,8 +274,7 @@
mCertificate = (X509Certificate)entry.getCertificate();

AdtPlugin.printToConsole(mProject,
                            String.format("New keystore %s has been created.", mKeystore),
"Certificate fingerprints:",
String.format("  MD5 : %s", getCertMd5Fingerprint()),
String.format("  SHA1: %s", getCertSha1Fingerprint()));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/KeyCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/KeyCreationPage.java
//Synthetic comment -- index aea94ad..b9a42d3 100644

//Synthetic comment -- @@ -188,6 +188,9 @@

@Override
void onShow() {
        // reset the destination
        mWizard.resetDestination();

// fill the texts with information loaded from the project.
if ((mProjectDataChanged & (DATA_PROJECT | DATA_KEYSTORE)) != 0) {
// reset the keystore/alias from the content of the project








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/KeySelectionPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/KeySelectionPage.java
//Synthetic comment -- index 604a208..bf5a53d 100644

//Synthetic comment -- @@ -111,6 +111,7 @@
@Override
public void widgetSelected(SelectionEvent e) {
mWizard.setKeyCreationMode(!mUseExistingKey.getSelection());
                mWizard.setKeyAlias(mKeyAliases.getItem(mKeyAliases.getSelectionIndex()));
enableWidgets();
onChange();
}
//Synthetic comment -- @@ -135,6 +136,9 @@

@Override
void onShow() {
        // reset the destination
        mWizard.resetDestination();

// fill the texts with information loaded from the project.
if ((mProjectDataChanged & (DATA_PROJECT | DATA_KEYSTORE)) != 0) {
// disable onChange for now. we'll call it once at the end.








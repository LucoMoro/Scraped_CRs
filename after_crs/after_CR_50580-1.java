/*Provision: Set Settings.Secure.USER_SETUP_COMPLETE bit

Set Settings.Secure.USER_SETUP_COMPLETE bit to allow other apps to know the user has complete the
setup wizard (like gapps SetupWizard apk does). Settings.Secure.USER_SETUP_COMPLETE bit is needed
in order to things working from the first steps (like QuickSettings or KeyguardSelector stuff)

Change-Id:Id6609df200c2ef8b98b3f5c2e37426dac54a889cSigned-off-by: Jorge Ruesga <j.ruesga.criado@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/provision/DefaultActivity.java b/src/com/android/provision/DefaultActivity.java
//Synthetic comment -- index 3607c3e..5bd88a7 100644

//Synthetic comment -- @@ -34,6 +34,11 @@
// Add a persistent setting to allow other apps to know the device has been provisioned.
Settings.Global.putInt(getContentResolver(), Settings.Global.DEVICE_PROVISIONED, 1);

        // Add a persistent setting to allow other apps to know the user has complete the
        // setup wizard. This value is used by some components of the system (like QuickSettings
        // or KeyguardSelector). This flag need to be set, before the provisioned bit was set.
        Settings.Secure.putInt(getContentResolver(), Settings.Secure.USER_SETUP_COMPLETE, 1);

// remove this activity from the package manager.
PackageManager pm = getPackageManager();
ComponentName name = new ComponentName(this, DefaultActivity.class);








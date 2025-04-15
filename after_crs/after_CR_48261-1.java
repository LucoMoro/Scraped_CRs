/*Settings: Backup account becomes grey and cannot be tapped after check the "Back up my data".

Rootcause: If there is no valid back up transport, by default, Android
will use LocalTransport, which is only for debug perpose and dose not
have valid config intent.
The back  up transport is set  in settings provider.
And in back settings, the config intent is not
validated after enable back up is checked.
platform should think about to provide valid back up transport in RHB.

Change-Id:I2924b1a7c656eb8f3a7c079866bc3f012cb01f74Author: Jianchun Shen <jianchunx.shen@intel.com>
Signed-off-by: Jianchun Shen <jianchunx.shen@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 51126*/




//Synthetic comment -- diff --git a/src/com/android/settings/PrivacySettings.java b/src/com/android/settings/PrivacySettings.java
//Synthetic comment -- index d936f46..09736f7 100644

//Synthetic comment -- @@ -201,7 +201,15 @@
}
mBackup.setChecked(enable);
mAutoRestore.setEnabled(enable);
        try {
            String transport = mBackupManager.getCurrentTransport();
            Intent configIntent = mBackupManager.getConfigurationIntent(transport);
            if ((!enable) || (enable && configIntent != null)) {
                mConfigure.setEnabled(enable);
            }
        } catch (RemoteException e) {
            //To do Nothing
        }
}

@Override








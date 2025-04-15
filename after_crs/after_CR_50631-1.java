/*Added CTS test cases for the new Error event channel

Change-Id:I754c0fad3f6cf0ed232bd0de255d7e6efec076a5*/




//Synthetic comment -- diff --git a/tests/tests/drm/src/android/drm/cts/Config.java b/tests/tests/drm/src/android/drm/cts/Config.java
//Synthetic comment -- index 736c0ad..fcd5751 100644

//Synthetic comment -- @@ -27,4 +27,5 @@
HashMap<String, String> getInfoOfRegistration();
HashMap<String, String> getInfoOfUnregistration();
HashMap<String, String> getInfoOfRightsAcquisition();
    HashMap<String, String> getInfoOfErrorRegistration();
}








//Synthetic comment -- diff --git a/tests/tests/drm/src/android/drm/cts/DRMTest.java b/tests/tests/drm/src/android/drm/cts/DRMTest.java
//Synthetic comment -- index bb77668..3f8e3c2 100644

//Synthetic comment -- @@ -28,6 +28,7 @@

import android.drm.DrmManagerClient;
import android.drm.DrmConvertedStatus;
import android.drm.DrmErrorEvent;
import android.drm.DrmEvent;
import android.drm.DrmInfo;
import android.drm.DrmInfoRequest;
//Synthetic comment -- @@ -81,6 +82,13 @@
executeProcessDrmInfo(drmInfo, config);
}

    private void error(Config config) throws Exception {
        DrmInfo drmInfo = executeAcquireDrmInfo(DrmInfoRequest.TYPE_REGISTRATION_INFO,
                                            config.getInfoOfErrorRegistration(),
                                            config.getMimeType());
        executeProcessDrmInfo(drmInfo, config);
    }

public void testIsDrmDirectoryExist() {
assertTrue("/data/drm/ does not exist", new File("/data/drm/").exists());
}
//Synthetic comment -- @@ -100,6 +108,12 @@
}
}

    public void testErrorListener() throws Exception {
        for (Config config : mConfigs) {
            error(config);
        }
    }

public void testGetConstraints() throws Exception {
for (Config config : mConfigs) {
register(config);
//Synthetic comment -- @@ -184,6 +198,7 @@
}

mDrmManagerClient.setOnEventListener(new OnEventListenerImpl(config));
        mDrmManagerClient.setOnErrorListener(new OnErrorListenerImpl(config));
drmInfo.put(DrmInfoRequest.ACCOUNT_ID, config.getAccountId());
assertEquals("Failed on plugin: " + config.getPluginName(),
DrmManagerClient.ERROR_NONE, mDrmManagerClient.processDrmInfo(drmInfo));
//Synthetic comment -- @@ -241,4 +256,19 @@
}
}
}

    private class OnErrorListenerImpl implements DrmManagerClient.OnErrorListener {
        private Config mConfig;
        public OnErrorListenerImpl(Config config) {
            mConfig = config;
        }

        @Override
        public void onError(DrmManagerClient client, DrmErrorEvent event) {
            assertEquals(DrmErrorEvent.TYPE_PROCESS_DRM_INFO_FAILED, event.getType());
            synchronized (mLock) {
                mLock.notify();
            }
        }
    }
}








//Synthetic comment -- diff --git a/tests/tests/drm/src/android/drm/cts/configs/FwdLockConfig.java b/tests/tests/drm/src/android/drm/cts/configs/FwdLockConfig.java
//Synthetic comment -- index e67ff03..560582a8 100644

//Synthetic comment -- @@ -51,11 +51,17 @@
public HashMap<String, String> getInfoOfRightsAcquisition(){
return sInfoOfRightsAcquisition;
}
    public HashMap<String, String> getInfoOfErrorRegistration() {
        return sInfoOfErrorRegistration;
    }


private static HashMap<String, String> sInfoOfRegistration = new HashMap<String, String>();
private static HashMap<String, String> sInfoOfUnregistration = new HashMap<String, String>();
private static HashMap<String, String> sInfoOfRightsAcquisition =
new HashMap<String, String>();
    private static HashMap<String, String> sInfoOfErrorRegistration =
        new HashMap<String, String>();

static {
sInfoOfRegistration.put("Dummy-FwdLock-1", "Dummy-FwdLock-1");
//Synthetic comment -- @@ -63,5 +69,7 @@
sInfoOfUnregistration.put("Dummy-FwdLock-2", "Dummy-FwdLock-2");

sInfoOfRightsAcquisition.put("Dummy-FwdLock-3", "Dummy-FwdLock-3");

        sInfoOfErrorRegistration.put("Error", "Error");
}
}








//Synthetic comment -- diff --git a/tests/tests/drm/src/android/drm/cts/configs/PassthruConfig.java b/tests/tests/drm/src/android/drm/cts/configs/PassthruConfig.java
//Synthetic comment -- index 7310825..9d2d080 100644

//Synthetic comment -- @@ -50,10 +50,14 @@
public HashMap<String, String> getInfoOfRightsAcquisition(){
return sInfoOfRightsAcquisition;
}
    public HashMap<String, String> getInfoOfErrorRegistration() {
        return sInfoOfErrorRegistration;
    }

private static HashMap<String, String> sInfoOfRegistration = new HashMap<String, String>();
private static HashMap<String, String> sInfoOfUnregistration = new HashMap<String, String>();
private static HashMap<String, String> sInfoOfRightsAcquisition = new HashMap<String, String>();
    private static HashMap<String, String> sInfoOfErrorRegistration = new HashMap<String, String>();

static {
sInfoOfRegistration.put("Foo-1", "foo-1");
//Synthetic comment -- @@ -61,5 +65,7 @@
sInfoOfUnregistration.put("Foo-2", "foo-2");

sInfoOfRightsAcquisition.put("Foo-3", "foo-3");

        sInfoOfErrorRegistration.put("Error", "Error");
}
}








//<Beginning of snippet n. 0>


import dalvik.annotation.TestTargetNew;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.test.AndroidTestCase;

import java.util.List;

private static final String NORMAL_URL = "http://www.google.com/";
private static final String SECURE_URL = "https://www.google.com/";

/**
 * Assert target intent can be handled by at least one Activity.
 * @param intent - the Intent will be handled.
 */
public void testDialPhoneNumber() {
    if (!isPhoneDevice()) {
        // Implement an alternative action for non-phone devices
        handleNonPhoneDevice("Cannot dial a phone number on a non-phone device.");
        return;
    }
    Uri uri = Uri.parse("tel:(212)5551212");
    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
    if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
        assertCanBeHandled(intent);
    } else {
        fail("Permission CALL_PHONE denied.");
    }
}

public void testDialVoicemail() {
    if (!isPhoneDevice()) {
        // Implement an alternative action for non-phone devices
        handleNonPhoneDevice("Cannot dial voicemail on a non-phone device.");
        return;
    }
    Uri uri = Uri.parse("voicemail:");
    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
    if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
        assertCanBeHandled(intent);
    } else {
        fail("Permission CALL_PHONE denied.");
    }
}

private boolean isPhoneDevice() {
    PackageManager pm = getContext().getPackageManager();
    return pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
}

private void assertCanBeHandled(Intent intent) {
    ResolveInfo resolveInfo = getContext().getPackageManager().resolveActivity(intent, 0);
    if (resolveInfo == null) {
        throw new IllegalArgumentException("Cannot handle intent: " + intent.toString());
    }
}

private void handleNonPhoneDevice(String message) {
    // Implement alternative action for non-phone devices
    // This could involve logging, showing a message, or other actions
    fail(message);
}

//<End of snippet n. 0>
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
        handleNonPhoneDevice("Cannot dial a phone number on a non-phone device.");
        return;
    }
    Uri uri = Uri.parse("tel:(212)5551212");
    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
    if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
        checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
        assertCanBeHandled(intent);
    } else {
        fail("Permissions CALL_PHONE or READ_PHONE_STATE denied.");
    }
}

public void testDialVoicemail() {
    if (!isPhoneDevice()) {
        handleNonPhoneDevice("Cannot dial voicemail on a non-phone device.");
        return;
    }
    Uri uri = Uri.parse("voicemail:");
    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
    if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
        checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
        assertCanBeHandled(intent);
    } else {
        fail("Permissions CALL_PHONE or READ_PHONE_STATE denied.");
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
    fail(message);
}

//<End of snippet n. 0>
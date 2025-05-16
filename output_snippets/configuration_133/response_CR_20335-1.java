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

private boolean isTelephonySupported() {
    PackageManager pm = getContext().getPackageManager();
    return pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
}

/**
* Assert target intent can be handled by at least one Activity.
* @param intent - the Intent will be handled.
*/
private void assertCanBeHandled(Intent intent) {
    PackageManager pm = getContext().getPackageManager();
    List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
    assertFalse("No activities found to handle intent", activities.isEmpty());
}

public void testDialPhoneNumber() {
    if (!isTelephonySupported()) {
        return; // Early exit if telephony is not supported
    }
    Uri uri = Uri.parse("tel:(212)5551212");
    if (uri != null) {
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        assertCanBeHandled(intent);
    }
}

public void testDialVoicemail() {
    if (!isTelephonySupported()) {
        return; // Early exit if telephony is not supported
    }
    Uri uri = Uri.parse("voicemail:");
    if (uri != null) {
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        assertCanBeHandled(intent);
    }
}

//<End of snippet n. 0>
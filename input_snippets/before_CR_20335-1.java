
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
args = {java.lang.String.class, android.net.Uri.class}
)
public void testDialPhoneNumber() {
Uri uri = Uri.parse("tel:(212)5551212");
Intent intent = new Intent(Intent.ACTION_DIAL, uri);
assertCanBeHandled(intent);
args = {java.lang.String.class, android.net.Uri.class}
)
public void testDialVoicemail() {
Uri uri = Uri.parse("voicemail:");
Intent intent = new Intent(Intent.ACTION_DIAL, uri);
assertCanBeHandled(intent);

//<End of snippet n. 0>









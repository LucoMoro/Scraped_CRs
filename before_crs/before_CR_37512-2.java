/*Skip MIFARE Ultralight test for an unsupported device

If a device doesn't have the feature "com.nxp.mifare",
MIFARE Ultralight is not supported on such device.

Change-Id:I1fb9bcdd2c0759ccf8598c78244a0e72e397c035*/
//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/nfc/NfcTestActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/nfc/NfcTestActivity.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3a85860..cc40131

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.cts.verifier.TestListAdapter.TestListItem;

import android.content.Intent;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.TagTechnology;
//Synthetic comment -- @@ -29,13 +30,14 @@

/** Activity that lists all the NFC tests. */
public class NfcTestActivity extends PassFailButtons.TestListActivity {

private static final String NDEF_ID =
TagVerifierActivity.getTagTestId(Ndef.class);

private static final String MIFARE_ULTRALIGHT_ID =
TagVerifierActivity.getTagTestId(MifareUltralight.class);

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -56,8 +58,10 @@
adapter.add(TestListItem.newCategory(this, R.string.nfc_tag_verification));
adapter.add(TestListItem.newTest(this, R.string.nfc_ndef,
NDEF_ID, getTagIntent(Ndef.class), null));
        adapter.add(TestListItem.newTest(this, R.string.nfc_mifare_ultralight,
                MIFARE_ULTRALIGHT_ID, getTagIntent(MifareUltralight.class), null));

setTestListAdapter(adapter);
}








/*Skip MIFARE Ultralight test for an unsupported device

If a device doesn't have the feature "com.nxp.mifare",
MIFARE Ultralight is not supported.

Change-Id:I1fb9bcdd2c0759ccf8598c78244a0e72e397c035*/




//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/nfc/NfcTestActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/nfc/NfcTestActivity.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3a85860..9b1fe1c

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.cts.verifier.TestListAdapter.TestListItem;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.TagTechnology;
//Synthetic comment -- @@ -29,6 +30,7 @@

/** Activity that lists all the NFC tests. */
public class NfcTestActivity extends PassFailButtons.TestListActivity {
    private PackageManager mPackageManager;

private static final String NDEF_ID =
TagVerifierActivity.getTagTestId(Ndef.class);
//Synthetic comment -- @@ -36,12 +38,15 @@
private static final String MIFARE_ULTRALIGHT_ID =
TagVerifierActivity.getTagTestId(MifareUltralight.class);

    private static final String FEATURE_NFC_MIFARE = "com.nxp.mifare";

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.pass_fail_list);
setInfoResources(R.string.nfc_test, R.string.nfc_test_info, 0);
setPassFailButtonClickListeners();
        mPackageManager = getPackageManager();

ArrayTestListAdapter adapter = new ArrayTestListAdapter(this);

//Synthetic comment -- @@ -56,8 +61,10 @@
adapter.add(TestListItem.newCategory(this, R.string.nfc_tag_verification));
adapter.add(TestListItem.newTest(this, R.string.nfc_ndef,
NDEF_ID, getTagIntent(Ndef.class), null));
        if (mPackageManager.hasSystemFeature(FEATURE_NFC_MIFARE)) {
            adapter.add(TestListItem.newTest(this, R.string.nfc_mifare_ultralight,
                    MIFARE_ULTRALIGHT_ID, getTagIntent(MifareUltralight.class), null));
        }

setTestListAdapter(adapter);
}








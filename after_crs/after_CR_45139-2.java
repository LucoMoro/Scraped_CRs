/*Telephony: handle SPN Dispaly rule for GSM/WCDMA

DisplayRule must be fetched from SIMRecords to display
operator name. Currently it is not fetched, corrected this.

Change-Id:Idee69fd2878abd1bce2fad0ea3989c03790949de*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index a0be5d0..70c5de8 100755

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.IccCardStatus;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RestrictedState;
//Synthetic comment -- @@ -484,11 +485,12 @@
}

protected void updateSpnDisplay() {
        IccRecords iccRecords = phone.mIccRecords.get();
        if (iccRecords == null) {
return;
}
        int rule = iccRecords.getDisplayRule(ss.getOperatorNumeric());
        String spn = iccRecords.getServiceProviderName();
String plmn = ss.getOperatorAlphaLong();

// For emergency calls only, pass the EmergencyCallsOnly string via EXTRA_PLMN








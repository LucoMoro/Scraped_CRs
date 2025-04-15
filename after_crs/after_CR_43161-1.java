/*Telephony: Fix npe in IccPhoneBookInterfaceManager

Create new interface to get current uicc app type from
phone and use that

Bug: 7167839
Change-Id:I4787985593918c660a254c9826734cf9c963bff0*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java b/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java
//Synthetic comment -- index dabe4ee..9f561da 100644

//Synthetic comment -- @@ -293,7 +293,7 @@
private int updateEfForIccType(int efid) {
// Check if we are trying to read ADN records
if (efid == IccConstants.EF_ADN) {
            if (phone.getCurrentUiccAppType() == AppType.APPTYPE_USIM) {
return IccConstants.EF_PBR;
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 113e8f4..1f69f7a 100755

//Synthetic comment -- @@ -38,6 +38,7 @@

import com.android.internal.R;
import com.android.internal.telephony.IccCardApplicationStatus.AppState;
import com.android.internal.telephony.IccCardApplicationStatus.AppType;
import com.android.internal.telephony.gsm.UsimServiceTable;
import com.android.internal.telephony.ims.IsimRecords;
import com.android.internal.telephony.test.SimulatedRadioControl;
//Synthetic comment -- @@ -681,6 +682,14 @@
return null;
}

    public AppType getCurrentUiccAppType() {
        UiccCardApplication currentApp = mUiccApplication.get();
        if (currentApp != null) {
            return currentApp.getType();
        }
        return AppType.APPTYPE_UNKNOWN;
    }

@Override
public IccCard getIccCard() {
return null;








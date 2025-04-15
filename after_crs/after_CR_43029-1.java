/*Telephony: Add null pointer checks in UiccCardApplicaton.update

Bug: 7174617
Change-Id:Iad5ef2352ea5e6ef4ede87214036ef27cc6a95ef*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCardApplication.java b/src/java/com/android/internal/telephony/UiccCardApplication.java
//Synthetic comment -- index 9ec16fc..006bbb0 100644

//Synthetic comment -- @@ -122,8 +122,8 @@
mPin2State = as.pin2;

if (mAppType != oldAppType) {
            	if (mIccFh != null) { mIccFh.dispose();}
                if (mIccRecords != null) { mIccRecords.dispose();}
mIccFh = createIccFileHandler(as.app_type);
mIccRecords = createIccRecords(as.app_type, c, ci);
}








/*Telephony: Remove unused mRuimFileHandler reference from CDMAPhone

Change-Id:I73b7d87e8e18047f9e0bcd6c1969a32c808cd3c7*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 7aecf5b..1c81a07 100755

//Synthetic comment -- @@ -101,7 +101,6 @@
CdmaCallTracker mCT;
CdmaSMSDispatcher mSMS;
CdmaServiceStateTracker mSST;
    RuimFileHandler mRuimFileHandler;
RuimRecords mRuimRecords;
RuimCard mRuimCard;
ArrayList <CdmaMmiCode> mPendingMmis = new ArrayList<CdmaMmiCode>();








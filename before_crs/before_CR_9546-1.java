/*This changes is to support read/write upto 4 records from EF_MSISDN file of SIM.*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Phone.java b/telephony/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 05e61f2..40ef3a0 100644

//Synthetic comment -- @@ -649,6 +649,14 @@
boolean getCallForwardingIndicator();

/**
* Get the line 1 phone number (MSISDN).<p>
*
* @return phone number. May return null if not
//Synthetic comment -- @@ -657,6 +665,13 @@
String getLine1Number();

/**
* Returns the alpha tag associated with the msisdn number.
* If there is no alpha tag associated or the record is not yet available,
* returns a default localized string. <p>
//Synthetic comment -- @@ -664,6 +679,17 @@
String getLine1AlphaTag();

/**
* Sets the MSISDN phone number in the SIM card.
*
* @param alphaTag the alpha tag associated with the MSISDN phone number








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneSubInfo.java b/telephony/java/com/android/internal/telephony/PhoneSubInfo.java
//Synthetic comment -- index 644d1f4..64e59d5 100644

//Synthetic comment -- @@ -48,6 +48,14 @@
return mPhone.getSimSerialNumber();
}

/**
* Retrieves the phone number string for line 1.
*/
//Synthetic comment -- @@ -57,6 +65,14 @@
}

/**
* Retrieves the alpha identifier for line 1.
*/
public String getLine1AlphaTag() {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index f93c724..7e07380 100644

//Synthetic comment -- @@ -906,14 +906,30 @@
return mSIMRecords.iccid;
}

public String getLine1Number() {
        return mSIMRecords.getMsisdnNumber();
}

public String getLine1AlphaTag() {
String ret;

        ret = mSIMRecords.getMsisdnAlphaTag();

if (ret == null || ret.length() == 0) {
return mContext.getText(
//Synthetic comment -- @@ -923,8 +939,12 @@
return ret;
}

public void setLine1Number(String alphaTag, String number, Message onComplete) {
        mSIMRecords.setMsisdnNumber(alphaTag, number, onComplete);
}

public void setVoiceMailNumber(String alphaTag,








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 4467536..9bb3b0e 100644

//Synthetic comment -- @@ -62,6 +62,13 @@
String iccid;
String msisdn = null;  // My mobile number
String msisdnTag = null;
String voiceMailNum = null;
String voiceMailTag = null;
String newVoiceMailNum = null;
//Synthetic comment -- @@ -233,9 +240,21 @@
return imsi;
}

    public String getMsisdnNumber()
{
        return msisdn;
}

/**
//Synthetic comment -- @@ -253,8 +272,7 @@
*        ((AsyncResult)onComplete.obj).exception == null on success
*        ((AsyncResult)onComplete.obj).exception != null on fail
*/
    public void setMsisdnNumber(String alphaTag, String number,
            Message onComplete) {

msisdn = number;
msisdnTag = alphaTag;
//Synthetic comment -- @@ -264,12 +282,24 @@

AdnRecord adn = new AdnRecord(msisdnTag, msisdn);

        new AdnRecordLoader(phone).updateEF(adn, EF_MSISDN, EF_EXT1, 1, null,
obtainMessage(EVENT_SET_MSISDN_DONE, onComplete));
}

    public String getMsisdnAlphaTag() {
        return msisdnTag;
}

public String getVoiceMailNumber()
//Synthetic comment -- @@ -1282,6 +1312,17 @@
obtainMessage(EVENT_GET_MSISDN_DONE));
recordsToLoad++;

// Record number is subscriber profile
phone.mSIMFileHandler.loadEFLinearFixed(EF_MBI, 1, 
obtainMessage(EVENT_GET_MBI_DONE));








/*Telephony: Pass IccCard object to GsmMmiCode

Change-Id:I9252897d5076eb4cf8a521e1d8a0a162394d35de*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index bc8aa0f..86b31c9 100644

//Synthetic comment -- @@ -715,7 +715,7 @@

// Only look at the Network portion for mmi
String networkPortion = PhoneNumberUtils.extractNetworkPortionAlt(newDialString);
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(networkPortion, this, mIccCard.get());
if (LOCAL_DEBUG) Log.d(LOG_TAG,
"dialing w/ mmi '" + mmi + "'...");

//Synthetic comment -- @@ -734,7 +734,7 @@
}

public boolean handlePinMmi(String dialString) {
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(dialString, this, mIccCard.get());

if (mmi != null && mmi.isPinCommand()) {
mPendingMMIs.add(mmi);
//Synthetic comment -- @@ -747,7 +747,7 @@
}

public void sendUssdResponse(String ussdMessge) {
        GsmMmiCode mmi = GsmMmiCode.newFromUssdUserInput(ussdMessge, this, mIccCard.get());
mPendingMMIs.add(mmi);
mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));
mmi.sendUssd(ussdMessge);
//Synthetic comment -- @@ -1144,7 +1144,8 @@
GsmMmiCode mmi;
mmi = GsmMmiCode.newNetworkInitiatedUssd(ussdMessage,
isUssdRequest,
                                                   GSMPhone.this,
                                                   mIccCard.get());
onNetworkInitiatedUssd(mmi);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index 1f7836e..2c6934c 100644

//Synthetic comment -- @@ -110,6 +110,8 @@

GSMPhone phone;
Context context;
    IccCard mIccCard;
    IccRecords mIccRecords;

String action;              // One of ACTION_*
String sc;                  // Service Code
//Synthetic comment -- @@ -173,7 +175,7 @@
*/

static GsmMmiCode
    newFromDialString(String dialString, GSMPhone phone, IccCard card) {
Matcher m;
GsmMmiCode ret = null;

//Synthetic comment -- @@ -181,7 +183,7 @@

// Is this formatted like a standard supplementary service code?
if (m.matches()) {
            ret = new GsmMmiCode(phone, card);
ret.poundString = makeEmptyNull(m.group(MATCH_GROUP_POUND_STRING));
ret.action = makeEmptyNull(m.group(MATCH_GROUP_ACTION));
ret.sc = makeEmptyNull(m.group(MATCH_GROUP_SERVICE_CODE));
//Synthetic comment -- @@ -196,14 +198,14 @@
// "Entry of any characters defined in the 3GPP TS 23.038 [8] Default Alphabet
// (up to the maximum defined in 3GPP TS 24.080 [10]), followed by #SEND".

            ret = new GsmMmiCode(phone, card);
ret.poundString = dialString;
} else if (isTwoDigitShortCode(phone.getContext(), dialString)) {
//Is a country-specific exception to short codes as defined in TS 22.030, 6.5.3.2
ret = null;
} else if (isShortCode(dialString, phone)) {
// this may be a short code, as defined in TS 22.030, 6.5.3.2
            ret = new GsmMmiCode(phone, card);
ret.dialingNumber = dialString;
}

//Synthetic comment -- @@ -212,10 +214,10 @@

static GsmMmiCode
newNetworkInitiatedUssd (String ussdMessage,
                                boolean isUssdRequest, GSMPhone phone, IccCard card) {
GsmMmiCode ret;

        ret = new GsmMmiCode(phone, card);

ret.message = ussdMessage;
ret.isUssdRequest = isUssdRequest;
//Synthetic comment -- @@ -231,8 +233,8 @@
return ret;
}

    static GsmMmiCode newFromUssdUserInput(String ussdMessge, GSMPhone phone, IccCard card) {
        GsmMmiCode ret = new GsmMmiCode(phone, card);

ret.message = ussdMessge;
ret.state = State.PENDING;
//Synthetic comment -- @@ -383,12 +385,16 @@

//***** Constructor

    GsmMmiCode (GSMPhone phone, IccCard card) {
// The telephony unit-test cases may create GsmMmiCode's
// in secondary threads
super(phone.getHandler().getLooper());
this.phone = phone;
this.context = phone.getContext();
        mIccCard = card;
        if (card != null) {
            mIccRecords = card.getIccRecords();
        }
}

//***** MmiCode implementation
//Synthetic comment -- @@ -764,8 +770,8 @@
} else if (pinLen < 4 || pinLen > 8 ) {
// invalid length
handlePasswordError(com.android.internal.R.string.invalidPin);
                    } else if (sc.equals(SC_PIN) &&
                               mIccCard.getState() == IccCardConstants.State.PUK_REQUIRED ) {
// Sim is puk-locked
handlePasswordError(com.android.internal.R.string.needPuk);
} else {
//Synthetic comment -- @@ -885,9 +891,8 @@
*/
if ((ar.exception == null) && (msg.arg1 == 1)) {
boolean cffEnabled = (msg.arg2 == 1);
                    if (mIccRecords != null) {
                        mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled);
}
}

//Synthetic comment -- @@ -1206,9 +1211,8 @@
(info.serviceClass & serviceClassMask)
== CommandsInterface.SERVICE_CLASS_VOICE) {
boolean cffEnabled = (info.status == 1);
            if (mIccRecords != null) {
                mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled);
}
}

//Synthetic comment -- @@ -1234,9 +1238,8 @@
sb.append(context.getText(com.android.internal.R.string.serviceDisabled));

// Set unconditional CFF in SIM to false
                if (mIccRecords != null) {
                    mIccRecords.setVoiceCallForwardingFlag(1, false);
}
} else {









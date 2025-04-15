/*Telephony: Fix for MMI codes

Fix for MMI codes for call forwarding
- Fix for issue - The UI doesn't register for call forwarding when
  *SC*SI# is dialed where SI is the number to which call should
  be forwarded.

  3GPP specification 22.030 sec. 6.5.2
  The UE shall determine from the context whether,
  an entry of a single *, activation or registration
  was intended. For example, a call forwarding request with a single *
  would be interpreted as registration if containing a
  forwarded-to number, or an activation if not.

Enable MMI codes for CDMA Global devices operating in GSM/UMTS
- Detects MMI codes for CustomerSupport, Voice Mail, CLIP and CLIR and
  translates the service code to corresponding dialing numbers when the
  CDMA Global phone is in GSM/UMTS networks.

Handle COLP/COLR/CNAP MMI strings.
- Currently COLP/COLR/CNAP interrogation is not supported and incorrectly
  processed as USSD request. Changes done to reject the MMI command and
  notify user regarding the unsupported command.

Telephony: Enable MMI codes for CDMA Global devices roaming in UMTS
- This fix detects the "+" prefix to Phone numbers dialed along with the
  MMI codes for CDMA Global devices operating in GSM/UMTS

Change-Id:I2ec290ae226330b3ccada9495a9183a8c78fab8f*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index c1cd019..49b5589 100644

//Synthetic comment -- @@ -721,6 +721,9 @@
return mCT.dial(newDialString, uusInfo);
} else if (mmi.isTemporaryModeCLIR()) {
return mCT.dial(mmi.dialingNumber, mmi.getCLIRMode(), uusInfo);
} else {
mPendingMMIs.add(mmi);
mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));
//Synthetic comment -- @@ -871,6 +874,9 @@
return (r != null) ? r.getMsisdnNumber() : "";
}

public String getLine1AlphaTag() {
IccRecords r = mIccRecords.get();
return (r != null) ? r.getMsisdnAlphaTag() : "";








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index fdc0606..9d0f3ac 100644

//Synthetic comment -- @@ -64,6 +64,11 @@
//Called line presentation
static final String SC_CLIP    = "30";
static final String SC_CLIR    = "31";

// Call Forwarding
static final String SC_CFU     = "21";
//Synthetic comment -- @@ -97,6 +102,15 @@
static final String SC_PUK          = "05";
static final String SC_PUK2         = "052";

//***** Event Constants

static final int EVENT_SET_COMPLETE         = 1;
//Synthetic comment -- @@ -126,6 +140,9 @@

private boolean isUssdRequest;

State state = State.PENDING;
CharSequence message;

//Synthetic comment -- @@ -147,6 +164,17 @@
10 = dialing number which must not include #, e.g. *SCn*SI#DN format
*/

static final int MATCH_GROUP_POUND_STRING = 1;

static final int MATCH_GROUP_ACTION = 2;
//Synthetic comment -- @@ -159,6 +187,9 @@
static final int MATCH_GROUP_PWD_CONFIRM = 11;
static final int MATCH_GROUP_DIALING_NUMBER = 12;
static private String[] sTwoDigitNumberPattern;

//***** Public Class methods

//Synthetic comment -- @@ -180,6 +211,36 @@
Matcher m;
GsmMmiCode ret = null;

m = sPatternSuppService.matcher(dialString);

// Is this formatted like a standard supplementary service code?
//Synthetic comment -- @@ -359,6 +420,14 @@
|| sc.equals(SC_BA_MT));
}

static String
scToBarringFacility(String sc) {
if (sc == null) {
//Synthetic comment -- @@ -561,7 +630,14 @@
}

/**
     * See TS 22.030 Annex B.
* In temporary mode, to suppress CLIR for a single call, enter:
*      " * 31 # [called number] SEND "
*  In temporary mode, to invoke CLIR for a single call enter:
//Synthetic comment -- @@ -671,7 +747,12 @@
int cfAction;

if (isActivate()) {
                        cfAction = CommandsInterface.CF_ACTION_ENABLE;
} else if (isDeactivate()) {
cfAction = CommandsInterface.CF_ACTION_DISABLE;
} else if (isRegister()) {
//Synthetic comment -- @@ -797,6 +878,11 @@
} else {
throw new RuntimeException ("Invalid or Unsupported MMI Code");
}
} else if (poundString != null) {
sendUssd(poundString);
} else {
//Synthetic comment -- @@ -1020,8 +1106,13 @@
}
} else if (isActivate()) {
state = State.COMPLETE;
            sb.append(context.getText(
                    com.android.internal.R.string.serviceEnabled));
// Record CLIR setting
if (sc.equals(SC_CLIR)) {
phone.saveClirSetting(CommandsInterface.CLIR_INVOCATION);








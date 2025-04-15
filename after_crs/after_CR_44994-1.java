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
        } else if (SystemProperties.getBoolean("ro.config.multimode_cdma", false) &&
                   mmi.isGlobalDevMmi()) {
            return mCT.dial(mmi.dialingNumber, uusInfo);
} else {
mPendingMMIs.add(mmi);
mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));
//Synthetic comment -- @@ -871,6 +874,9 @@
return (r != null) ? r.getMsisdnNumber() : "";
}

    public String getMdn() {
        return mSST.getMdnNumber();
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
    static final String SC_COLP    = "76";
    static final String SC_COLR    = "77";

    //Calling name presentation
    static final String SC_CNAP    = "300";

// Call Forwarding
static final String SC_CFU     = "21";
//Synthetic comment -- @@ -97,6 +102,15 @@
static final String SC_PUK          = "05";
static final String SC_PUK2         = "052";

    //Global device(CDMA phone operating on GSM/UMTS) MMI Codes,
    //as specified in the specifications for CDMA Global devices.
    //TODO : Need to remove these declaration and to get from database or from system properties.
    static final String SC_GLOBALDEV_VM        = "86";
    static final String SC_GLOBALDEV_CS        = "611";
    static final String SC_GLOBALDEV_CLIR_INVK = "67";
    static final String SC_GLOBALDEV_CLIR_SUPP = "82";
    static final String GLOBALDEV_CS           = "+19085594899";

//***** Event Constants

static final int EVENT_SET_COMPLETE         = 1;
//Synthetic comment -- @@ -126,6 +140,9 @@

private boolean isUssdRequest;


    private boolean isCallFwdRegister = false;

State state = State.PENDING;
CharSequence message;

//Synthetic comment -- @@ -147,6 +164,17 @@
10 = dialing number which must not include #, e.g. *SCn*SI#DN format
*/

    static Pattern sPatternSuppServiceGlobalDev = Pattern.compile(
    "((\\*)(\\d{2})(\\+{0,1})(\\d{0,}))");
/*   1   2      3     4         5

    1 = Full string
    2 = action
    3 = service code
    4 = dialing prefix
    5 = dialing number
*/

static final int MATCH_GROUP_POUND_STRING = 1;

static final int MATCH_GROUP_ACTION = 2;
//Synthetic comment -- @@ -159,6 +187,9 @@
static final int MATCH_GROUP_PWD_CONFIRM = 11;
static final int MATCH_GROUP_DIALING_NUMBER = 12;
static private String[] sTwoDigitNumberPattern;
    static final int MATCH_GROUP_GLOBALDEV_DIALPREFIX = 4;
    static final int MATCH_GROUP_GLOBALDEV_DIALNUM = 5;


//***** Public Class methods

//Synthetic comment -- @@ -180,6 +211,36 @@
Matcher m;
GsmMmiCode ret = null;

        if (SystemProperties.getBoolean("ro.config.multimode_cdma", false)) {
            m = sPatternSuppServiceGlobalDev.matcher(dialString);
            if (m.matches()) {
                ret = new GsmMmiCode(phone, app);
                ret.action = makeEmptyNull(m.group(MATCH_GROUP_ACTION));
                String DialCode = makeEmptyNull(m.group(MATCH_GROUP_SERVICE_CODE));
                String DialPrefix = m.group(MATCH_GROUP_GLOBALDEV_DIALPREFIX);
                String DialNumber = makeEmptyNull(m.group(MATCH_GROUP_GLOBALDEV_DIALNUM));
                if (DialCode.equals(SC_GLOBALDEV_VM)) {
                    ret.sc = SC_GLOBALDEV_VM;
                    ret.dialingNumber = "+1" + phone.getMdn();
                    return ret;
                } else if (DialCode.equals(SC_GLOBALDEV_CS.substring(0, 2))
                        && !DialPrefix.equals("+") &&
                        DialNumber.equals(SC_GLOBALDEV_CS.substring(2))) {
                    ret.sc = SC_GLOBALDEV_CS;
                    ret.dialingNumber = GLOBALDEV_CS;
                    return ret;
                } else if (DialCode.equals(SC_GLOBALDEV_CLIR_INVK) && (DialNumber != null)) {
                    // Dial "#31#PhoneNum" to invoke CLIR temporarily
                    dialString = ACTION_DEACTIVATE + SC_CLIR + ACTION_DEACTIVATE + DialPrefix
                            + DialNumber;
                } else if (DialCode.equals(SC_GLOBALDEV_CLIR_SUPP) && (DialNumber != null)) {
                    // Dial "*31#PhoneNum" to suppress CLIR temporarily
                    dialString = ACTION_ACTIVATE + SC_CLIR + ACTION_DEACTIVATE + DialPrefix
                            + DialNumber;
                }
            }
        }

m = sPatternSuppService.matcher(dialString);

// Is this formatted like a standard supplementary service code?
//Synthetic comment -- @@ -359,6 +420,14 @@
|| sc.equals(SC_BA_MT));
}

    static boolean
    isServiceCodeUnsupported(String sc) {
        return sc != null &&
                (sc.equals(SC_COLP)
                || sc.equals(SC_COLR)
                || sc.equals(SC_CNAP));
    }

static String
scToBarringFacility(String sc) {
if (sc == null) {
//Synthetic comment -- @@ -561,7 +630,14 @@
}

/**
     * @return true if the Mmi is Service Code for Cdma GlobalDevice
     */
    boolean isGlobalDevMmi() {
        return sc != null && (sc.equals(SC_GLOBALDEV_VM) || sc.equals(SC_GLOBALDEV_CS));
     }

    /**
     * *See TS 22.030 Annex B
* In temporary mode, to suppress CLIR for a single call, enter:
*      " * 31 # [called number] SEND "
*  In temporary mode, to invoke CLIR for a single call enter:
//Synthetic comment -- @@ -671,7 +747,12 @@
int cfAction;

if (isActivate()) {
                        if (dialingNumber != null) {
                            isCallFwdRegister = true;
                            cfAction = CommandsInterface.CF_ACTION_REGISTRATION;
                        } else {
                            cfAction = CommandsInterface.CF_ACTION_ENABLE;
                        }
} else if (isDeactivate()) {
cfAction = CommandsInterface.CF_ACTION_DISABLE;
} else if (isRegister()) {
//Synthetic comment -- @@ -797,6 +878,11 @@
} else {
throw new RuntimeException ("Invalid or Unsupported MMI Code");
}
            } else if (isServiceCodeUnsupported(sc)) {
                Log.d(LOG_TAG,"Unsupported MMI code: " + sc);
                state = State.FAILED;
                message = context.getText(com.android.internal.R.string.unsupportedMmiCode);
                phone.onMMIDone(this);
} else if (poundString != null) {
sendUssd(poundString);
} else {
//Synthetic comment -- @@ -1020,8 +1106,13 @@
}
} else if (isActivate()) {
state = State.COMPLETE;
            if (isCallFwdRegister) {
                sb.append(context.getText(com.android.internal.R.string.serviceRegistered));
                isCallFwdRegister = false;
            } else {
                sb.append(context.getText(
                        com.android.internal.R.string.serviceEnabled));
            }
// Record CLIR setting
if (sc.equals(SC_CLIR)) {
phone.saveClirSetting(CommandsInterface.CLIR_INVOCATION);








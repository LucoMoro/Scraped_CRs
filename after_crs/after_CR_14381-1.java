/*Notify user regarding invalid number during MO call.

When user dials an invalid number, network returns an error indicating
"unobtainable number" (See the Table 10.5.123/3GPP TS 24.008 for
the possible failure causes). Changes done to display message to
indicate user that an invalid number has been dialed.

Change-Id:I477d64bc3b59cc205396f911ff9630c640f619f3*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Connection.java b/telephony/java/com/android/internal/telephony/Connection.java
//Synthetic comment -- index 37e8a99..6b4c551 100644

//Synthetic comment -- @@ -50,6 +50,7 @@
CS_RESTRICTED,                  /* call was blocked by restricted all voice access */
CS_RESTRICTED_NORMAL,           /* call was blocked by restricted normal voice access */
CS_RESTRICTED_EMERGENCY,        /* call was blocked by restricted emergency voice access */
        UNOBTAINABLE_NUMBER,            /* Unassigned number (3GPP TS 24.008 table 10.5.123) */
CDMA_LOCKED_UNTIL_POWER_CYCLE,  /* MS is locked until next power cycle */
CDMA_DROP,
CDMA_INTERCEPT,                 /* INTERCEPT order received, MS state idle entered */








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/CallFailCause.java b/telephony/java/com/android/internal/telephony/gsm/CallFailCause.java
//Synthetic comment -- index e7fbf6b..af2ad48 100644

//Synthetic comment -- @@ -25,6 +25,9 @@
*
*/
public interface CallFailCause {
    // Unassigned/Unobtainable number
    static final int UNOBTAINABLE_NUMBER = 1;

static final int NORMAL_CLEARING     = 16;
// Busy Tone
static final int USER_BUSY           = 17;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java b/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 445be39..985ec9a 100644

//Synthetic comment -- @@ -356,6 +356,9 @@
case CallFailCause.FDN_BLOCKED:
return DisconnectCause.FDN_BLOCKED;

            case CallFailCause.UNOBTAINABLE_NUMBER:
                return DisconnectCause.UNOBTAINABLE_NUMBER;

case CallFailCause.ERROR_UNSPECIFIED:
case CallFailCause.NORMAL_CLEARING:
default:








/*Fix for Call forwarding registration using MMI codes

We don't register for call forwarding when *SC*SI# is
dialed where SI is the number to which call should be
forwarded. We activate instead a previously registered
number.

As per 3GPP specification 22.030 sec. 6.5.2
The UE shall determine from the context whether, an entry
of a single *, activation or registration was intended.
For example, a call forwarding request with a single *
would be interpreted as registration if containing a
forwarded-to number, or an activation if not.

Bug: 7903480
Change-Id:I15e12efd5f1691fae166f2e5bba77323766e5801*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index 3f30dfe..2737c08 100644

//Synthetic comment -- @@ -128,6 +128,11 @@

private boolean isUssdRequest;

State state = State.PENDING;
CharSequence message;

//Synthetic comment -- @@ -659,20 +664,25 @@
} else if (isServiceCodeCallForwarding(sc)) {
Rlog.d(LOG_TAG, "is CF");

                String dialingNumber = sia;
int serviceClass = siToServiceClass(sib);
int reason = scToCallForwardReason(sc);
int time = siToTime(sic);

if (isInterrogate()) {
phone.mCM.queryCallForwardStatus(
                            reason, serviceClass,  dialingNumber,
obtainMessage(EVENT_QUERY_CF_COMPLETE, this));
} else {
int cfAction;

if (isActivate()) {
                        cfAction = CommandsInterface.CF_ACTION_ENABLE;
} else if (isDeactivate()) {
cfAction = CommandsInterface.CF_ACTION_DISABLE;
} else if (isRegister()) {
//Synthetic comment -- @@ -695,7 +705,7 @@

Rlog.d(LOG_TAG, "is CF setCallForward");
phone.mCM.setCallForward(cfAction, reason, serviceClass,
                            dialingNumber, time, obtainMessage(
EVENT_SET_CFF_COMPLETE,
isSettingUnconditionalVoice,
isEnableDesired, this));
//Synthetic comment -- @@ -1021,8 +1031,13 @@
}
} else if (isActivate()) {
state = State.COMPLETE;
            sb.append(context.getText(
                    com.android.internal.R.string.serviceEnabled));
// Record CLIR setting
if (sc.equals(SC_CLIR)) {
phone.saveClirSetting(CommandsInterface.CLIR_INVOCATION);








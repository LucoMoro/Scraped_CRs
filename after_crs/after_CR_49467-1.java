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

b/7903480

Change-Id:I15e12efd5f1691fae166f2e5bba77323766e5801*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index 3f30dfe..9dbeea1 100644

//Synthetic comment -- @@ -128,6 +128,11 @@

private boolean isUssdRequest;

    /** Set to true to indicate a call forward register operation
     * is in progress as opposed an enable
     */
    private boolean isCallFwdRegister = false;

State state = State.PENDING;
CharSequence message;

//Synthetic comment -- @@ -672,7 +677,12 @@
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
//Synthetic comment -- @@ -1021,8 +1031,13 @@
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








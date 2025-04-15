/*telephony: Fix for MMI code for call forwarding

Fix for issue - The UI doesn't register for call forwarding when
*SC*SI# is dialed where SI is the number to which call should
be forwarded.

3GPP specification 22.030 sec. 6.5.2

The UE shall determine from the context whether,
an entry of a single *, activation  or registration
was intended.

For example, a call forwarding request with a single *
would be interpreted as registration if containing a
forwarded-to number, or an activation if not.

Change-Id:I0531a134802f807a9b3023e69393bd5726ed1129*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index fe7a5cb..636dcf5 100644

//Synthetic comment -- @@ -123,6 +123,8 @@

private boolean isUssdRequest;

    private boolean isCallFwdRegister = false;

State state = State.PENDING;
CharSequence message;

//Synthetic comment -- @@ -637,7 +639,12 @@
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
//Synthetic comment -- @@ -983,8 +990,13 @@
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








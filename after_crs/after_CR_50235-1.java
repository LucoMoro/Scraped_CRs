/*Interprete mmi code *21*num# as registration.

Per 3GPP TS 22.030 6.5.2

A call forwarding request with a singel * would
be interpreted as registration if containing a
forwarded-to number, or an activation if not.

Change-Id:Iaf5754e49454819892fe054938ef8819f759d6bdSigned-off-by: Bin Li <libin@marvell.com>*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index 3f30dfe..55230f2 100644

//Synthetic comment -- @@ -128,6 +128,7 @@

private boolean isUssdRequest;

    private boolean isCallFwdReg;
State state = State.PENDING;
CharSequence message;

//Synthetic comment -- @@ -672,7 +673,17 @@
int cfAction;

if (isActivate()) {
                        // 3GPP TS 22.030 6.5.2
                        // a call forwarding request with a single * would be
                        // interpreted as registration if containing a forwarded-to
                        // number, or an activation if not
                        if (isEmptyOrNull(dialingNumber)) {
                            cfAction = CommandsInterface.CF_ACTION_ENABLE;
                            isCallFwdReg = false;
                        } else {
                            cfAction = CommandsInterface.CF_ACTION_REGISTRATION;
                            isCallFwdReg = true;
                        }
} else if (isDeactivate()) {
cfAction = CommandsInterface.CF_ACTION_DISABLE;
} else if (isRegister()) {
//Synthetic comment -- @@ -1021,8 +1032,13 @@
}
} else if (isActivate()) {
state = State.COMPLETE;
            if (isCallFwdReg) {
                sb.append(context.getText(
                        com.android.internal.R.string.serviceRegistered));
            } else {
                sb.append(context.getText(
                        com.android.internal.R.string.serviceEnabled));
            }
// Record CLIR setting
if (sc.equals(SC_CLIR)) {
phone.saveClirSetting(CommandsInterface.CLIR_INVOCATION);








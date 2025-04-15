/*Telephony: Do not broadcast card error intent when app index is invalid

IccCardProxy broadcasts card error intent when application index
received from UIM is invalid(i.e.mUiccAplication is null) during
initialization of card for fraction of time. As a result of which
"Invalid Card" is displayed on UI momentarily.

To fix this, When app index received from UIM is invalid broadcast
"NOT_READY" intent.

Change-Id:I94e583c9becfc6ee1fcb7c22a7b9965e8b8c794dCRs-Fixed: 399597*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardProxy.java b/src/java/com/android/internal/telephony/IccCardProxy.java
//Synthetic comment -- index eef0c6f..edef47b 100644

//Synthetic comment -- @@ -266,12 +266,16 @@
return;
}

        if (mUiccCard.getCardState() == CardState.CARDSTATE_ERROR) { 
setExternalState(State.UNKNOWN);
return;
}

        if (mUiccApplication == null) {
            setExternalState(State.NOT_READY);
            return;
        }

switch (mUiccApplication.getState()) {
case APPSTATE_UNKNOWN:
case APPSTATE_DETECTED:








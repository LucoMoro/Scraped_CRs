/*Parcel compRequired flag correctly.

This should fix a couple of cases (e.g. SET_UP_MENU responses for Dual
SIM adapters) whether a command response would be rejected due to a
compRequired flag mismatch between the command sent to the Stk app and
the response received from it.

The problem was:
- STK service received command without compRequired flag set
- it passes it to the STK app, which sees compRequired set
- STK app passes back the response with the user selection
- STK service gets the response, which has compRequired set
- it discards the response, as the command details don't match
  (CatService.java:646)*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CommandDetails.java b/telephony/java/com/android/internal/telephony/cat/CommandDetails.java
//Synthetic comment -- index e3f0798..bb875be 100644

//Synthetic comment -- @@ -48,13 +48,14 @@
}

public CommandDetails(Parcel in) {
        compRequired = true;
commandNumber = in.readInt();
typeOfCommand = in.readInt();
commandQualifier = in.readInt();
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeInt(commandNumber);
dest.writeInt(typeOfCommand);
dest.writeInt(commandQualifier);
//Synthetic comment -- @@ -103,4 +104,4 @@
ComprehensionTlvTag getTag() {
return ComprehensionTlvTag.ITEM_ICON_ID_LIST;
}
}
\ No newline at end of file








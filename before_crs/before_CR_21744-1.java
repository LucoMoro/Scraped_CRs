/*Fix comprehension required in proactive commands

If a proactive command was sent that should be terminated by the Stk
(including SETUP_CALL) without comprehension required. No terminal
response was sent, which resulted in hanging of the uicc session.

The comprehension required flag is now correctly parceled.

Change-Id:I40d603c2b7c437a0c87f73ba7c6807212e0f96fdSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CommandDetails.java b/telephony/java/com/android/internal/telephony/cat/CommandDetails.java
//Synthetic comment -- index e3f0798..1085ee8 100644

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








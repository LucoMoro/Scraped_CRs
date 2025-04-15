/*Fix comprehension required in proactive commands

If a proactive command was sent that should be terminated by the Stk
(including SETUP_CALL) without comprehension required. No terminal
response was sent, which resulted in hanging of the uicc session.

The comprehension required flag is now correctly parceled.

Change-Id:I9b0c6192db5fde8acac0f2646dc9a7b9304b319aSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CommandDetails.java b/telephony/java/com/android/internal/telephony/cat/CommandDetails.java
//Synthetic comment -- index e3f0798..1085ee8 100644

//Synthetic comment -- @@ -48,13 +48,14 @@
}

public CommandDetails(Parcel in) {
        compRequired = in.readInt() == 1 ? true : false;
commandNumber = in.readInt();
typeOfCommand = in.readInt();
commandQualifier = in.readInt();
}

public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(compRequired ? 1 : 0);
dest.writeInt(commandNumber);
dest.writeInt(typeOfCommand);
dest.writeInt(commandQualifier);








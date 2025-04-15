/*Correct the Terminal response for GET_INKEY variable timeout

The GCF PTCRB Testcase GCF_PTCRB_USAT_GetInkey_27.22.4.2.8.1 checks for
DURATION TLV in the terminal response. At present Android is not sending this.
Added DURATION TLV in the terminal response for GET INKEY, variable time out
of 10 seconds case.

According to TS102.223/TS31.111 section 6.8, Structure of TERMINAL RESPONSE,
"For all SIMPLE-TLV objects with Min=N, the ME should set the
CR(comprehension required) flag to comprehension not required.(CR=0)"
Since DEVICE_IDENTITIES and DURATION TLVs have Min=N, the CR flag is not set.

Change-Id:I762b064f02f44772809f8bb029e8cefb838e7766*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/CommandParamsFactory.java b/telephony/java/com/android/internal/telephony/gsm/stk/CommandParamsFactory.java
//Synthetic comment -- index ce4c459..8deb379 100644

//Synthetic comment -- @@ -380,6 +380,12 @@
iconId = ValueParser.retrieveIconId(ctlv);
}

input.minLen = 1;
input.maxLen = 1;









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/Input.java b/telephony/java/com/android/internal/telephony/gsm/stk/Input.java
//Synthetic comment -- index 19f724b..2901264 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
public boolean echo;
public boolean yesNo;
public boolean helpAvailable;

Input() {
text = "";
//Synthetic comment -- @@ -49,6 +50,7 @@
echo = false;
yesNo = false;
helpAvailable = false;
}

private Input(Parcel in) {
//Synthetic comment -- @@ -63,6 +65,7 @@
echo = in.readInt() == 1 ? true : false;
yesNo = in.readInt() == 1 ? true : false;
helpAvailable = in.readInt() == 1 ? true : false;
}

public int describeContents() {
//Synthetic comment -- @@ -81,6 +84,7 @@
dest.writeInt(echo ? 1 : 0);
dest.writeInt(yesNo ? 1 : 0);
dest.writeInt(helpAvailable ? 1 : 0);
}

public static final Parcelable.Creator<Input> CREATOR = new Parcelable.Creator<Input>() {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/StkService.java b/telephony/java/com/android/internal/telephony/gsm/stk/StkService.java
//Synthetic comment -- index 29ed95c..6615b09 100644

//Synthetic comment -- @@ -315,6 +315,11 @@
}
ByteArrayOutputStream buf = new ByteArrayOutputStream();

// command details
int tag = ComprehensionTlvTag.COMMAND_DETAILS.value();
if (cmdDet.compRequired) {
//Synthetic comment -- @@ -327,7 +332,13 @@
buf.write(cmdDet.commandQualifier);

// device identities
        tag = 0x80 | ComprehensionTlvTag.DEVICE_IDENTITIES.value();
buf.write(tag);
buf.write(0x02); // length
buf.write(DEV_ID_TERMINAL); // source device id
//Synthetic comment -- @@ -348,6 +359,20 @@
// Fill optional data for each corresponding command
if (resp != null) {
resp.format(buf);
}

byte[] rawData = buf.toByteArray();








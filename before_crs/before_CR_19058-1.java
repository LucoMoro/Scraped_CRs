/*Add support for IPv6 PDP/PDN type selection in ApnEditor

This extends the ApnEditor with two types; IPv6 and IPv4v6.
The type is passed down to the RIL, backward compatibility is maintained as the value is added last in the data array.

Change-Id:If88d4393e5bc1d8c4663027a3ae7982fdfdfe7b0*/
//Synthetic comment -- diff --git a/core/java/android/provider/Telephony.java b/core/java/android/provider/Telephony.java
//Synthetic comment -- index fa5cd8b..10958ee 100644

//Synthetic comment -- @@ -1724,6 +1724,8 @@

public static final String TYPE = "type";

public static final String CURRENT = "current";
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index 5de0426..d49a464 100644

//Synthetic comment -- @@ -1347,7 +1347,7 @@
*            Callback message
*/
public void setupDataCall(String radioTechnology, String profile, String apn,
            String user, String password, String authType, Message result);

/**
* Deactivate packet data connection








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 2f7aa21..5a067e5 100644

//Synthetic comment -- @@ -1272,6 +1272,7 @@
setupDefaultPDP(String apn, String user, String password, Message result) {
int radioTechnology;
int authType;
String profile = ""; //profile number, NULL for GSM/UMTS

radioTechnology = RILConstants.SETUP_DATA_TECH_GSM;
//Synthetic comment -- @@ -1280,7 +1281,7 @@
: RILConstants.SETUP_DATA_AUTH_NONE;

setupDataCall(Integer.toString(radioTechnology), profile, apn, user,
                password, Integer.toString(authType), result);

}

//Synthetic comment -- @@ -1299,11 +1300,11 @@
*/
public void
setupDataCall(String radioTechnology, String profile, String apn,
            String user, String password, String authType, Message result) {
RILRequest rr
= RILRequest.obtain(RIL_REQUEST_SETUP_DATA_CALL, result);

        rr.mp.writeInt(6);

rr.mp.writeString(radioTechnology);
rr.mp.writeString(profile);
//Synthetic comment -- @@ -1311,11 +1312,12 @@
rr.mp.writeString(user);
rr.mp.writeString(password);
rr.mp.writeString(authType);

if (RILJ_LOGD) riljLog(rr.serialString() + "> "
+ requestToString(rr.mRequest) + " " + radioTechnology + " "
+ profile + " " + apn + " " + user + " "
                + password + " " + authType);

send(rr);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnection.java b/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnection.java
//Synthetic comment -- index 95cb1c6..8a4a2a8 100644

//Synthetic comment -- @@ -87,7 +87,7 @@
msg.obj = cp;
phone.mCM.setupDataCall(Integer.toString(RILConstants.SETUP_DATA_TECH_CDMA),
Integer.toString(dataProfile), null, null,
                null, Integer.toString(RILConstants.SETUP_DATA_AUTH_PAP_CHAP), msg);
}

@Override








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 9f2a44b..6b4b668 100644

//Synthetic comment -- @@ -432,7 +432,7 @@
} else {
types = mDefaultApnTypes;
}
        mActiveApn = new ApnSetting(0, "", "", "", "", "", "", "", "", "", "", 0, types);

Message msg = obtainMessage();
msg.what = EVENT_DATA_SETUP_COMPLETE;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/ApnSetting.java b/telephony/java/com/android/internal/telephony/gsm/ApnSetting.java
//Synthetic comment -- index 05527af..cdbfb8b 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
String user;
String password;
int authType;
public String[] types;
int id;
String numeric;
//Synthetic comment -- @@ -39,7 +40,7 @@

public ApnSetting(int id, String numeric, String carrier, String apn, String proxy, String port,
String mmsc, String mmsProxy, String mmsPort,
            String user, String password, int authType, String[] types) {
this.id = id;
this.numeric = numeric;
this.carrier = carrier;
//Synthetic comment -- @@ -52,6 +53,7 @@
this.user = user;
this.password = password;
this.authType = authType;
this.types = types;
}

//Synthetic comment -- @@ -68,20 +70,21 @@
// data[10] = mcc
// data[11] = mnc
// data[12] = auth
    // data[13] = first type...
public static ApnSetting fromString(String data) {
if (data == null) return null;
String[] a = data.split("\\s*,\\s*");
        if (a.length < 14) return null;
int authType = 0;
try {
authType = Integer.parseInt(a[12]);
} catch (Exception e) {
}
        String[] typeArray = new String[a.length - 13];
        System.arraycopy(a, 13, typeArray, 0, a.length - 13);
return new ApnSetting(-1,a[10]+a[11],a[0],a[1],a[2],a[3],a[7],a[8],
                a[9],a[4],a[5],authType,typeArray);
}

public String toString() {
//Synthetic comment -- @@ -95,7 +98,8 @@
.append(", ").append(mmsProxy)
.append(", ").append(mmsPort)
.append(", ").append(port)
        .append(", ").append(authType);
for (String t : types) {
sb.append(", ").append(t);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnection.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnection.java
//Synthetic comment -- index 09d46dd..5c812bb 100644

//Synthetic comment -- @@ -106,7 +106,7 @@
}
phone.mCM.setupDataCall(Integer.toString(RILConstants.SETUP_DATA_TECH_GSM),
Integer.toString(RILConstants.DATA_PROFILE_DEFAULT), apn.apn, apn.user,
                apn.password, Integer.toString(authType), msg);
}

@Override








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 5f651e7..08b536d 100644

//Synthetic comment -- @@ -559,6 +559,7 @@
cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.USER)),
cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.PASSWORD)),
cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers.AUTH_TYPE)),
types);
result.add(apn);
} while (cursor.moveToNext());








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java b/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index a120f52..8796704 100644

//Synthetic comment -- @@ -962,7 +962,7 @@
}

public void setupDataCall(String radioTechnology, String profile, String apn, String user,
            String password, String authType, Message result) {
unimplemented(result);
}









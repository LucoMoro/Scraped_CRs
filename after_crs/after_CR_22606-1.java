/*Add the protocol column to the carriers table.

This change fixes the phone app crashing repeatedly when building
the full-eng product in AOSP

NOTE: This change is already in the gingerbread-release branch but was,
      for some reason, left out of the master tree.

Bug 4373119

Change-Id:I567a9ccc2608105beba5c969360bb48e672793a7*/




//Synthetic comment -- diff --git a/src/com/android/providers/telephony/TelephonyProvider.java b/src/com/android/providers/telephony/TelephonyProvider.java
//Synthetic comment -- index 85bd0f6..274d2cb 100644

//Synthetic comment -- @@ -50,7 +50,7 @@
{
private static final String DATABASE_NAME = "telephony.db";

    private static final int DATABASE_VERSION = 6 << 16;
private static final int URL_TELEPHONY = 1;
private static final int URL_CURRENT = 2;
private static final int URL_ID = 3;
//Synthetic comment -- @@ -135,7 +135,9 @@
"mmsc TEXT," +
"authtype INTEGER," +
"type TEXT," +
                    "current INTEGER," +
                    "protocol TEXT," +
                    "roaming_protocol TEXT);");

initDatabase(db);
}
//Synthetic comment -- @@ -204,6 +206,14 @@

oldVersion = 5 << 16 | 6;
}
            if (oldVersion < (6 << 16 | 6)) {
                // Add protcol fields to the APN. The XML file does not change.
                db.execSQL("ALTER TABLE " + CARRIERS_TABLE +
                        " ADD COLUMN protocol TEXT DEFAULT IP;");
                db.execSQL("ALTER TABLE " + CARRIERS_TABLE +
                        " ADD COLUMN roaming_protocol TEXT DEFAULT IP;");
                oldVersion = 6 << 16 | 6;
            }
}

/**
//Synthetic comment -- @@ -260,6 +270,16 @@
map.put(Telephony.Carriers.AUTH_TYPE, Integer.parseInt(auth));
}

            String protocol = parser.getAttributeValue(null, "protocol");
            if (protocol != null) {
                map.put(Telephony.Carriers.PROTOCOL, protocol);
            }

            String roamingProtocol = parser.getAttributeValue(null, "roaming_protocol");
            if (roamingProtocol != null) {
                map.put(Telephony.Carriers.ROAMING_PROTOCOL, roamingProtocol);
            }

return map;
}

//Synthetic comment -- @@ -295,6 +315,12 @@
if (row.containsKey(Telephony.Carriers.AUTH_TYPE) == false) {
row.put(Telephony.Carriers.AUTH_TYPE, -1);
}
            if (row.containsKey(Telephony.Carriers.PROTOCOL) == false) {
                row.put(Telephony.Carriers.PROTOCOL, "IP");
            }
            if (row.containsKey(Telephony.Carriers.ROAMING_PROTOCOL) == false) {
                row.put(Telephony.Carriers.ROAMING_PROTOCOL, "IP");
            }
db.insert(CARRIERS_TABLE, null, row);
}
}
//Synthetic comment -- @@ -400,36 +426,42 @@

// TODO Review this. This code should probably not bet here.
// It is valid for the database to return a null string.
                if (!values.containsKey(Telephony.Carriers.NAME)) {
values.put(Telephony.Carriers.NAME, "");
}
                if (!values.containsKey(Telephony.Carriers.APN)) {
values.put(Telephony.Carriers.APN, "");
}
                if (!values.containsKey(Telephony.Carriers.PORT)) {
values.put(Telephony.Carriers.PORT, "");
}
                if (!values.containsKey(Telephony.Carriers.PROXY)) {
values.put(Telephony.Carriers.PROXY, "");
}
                if (!values.containsKey(Telephony.Carriers.USER)) {
values.put(Telephony.Carriers.USER, "");
}
                if (!values.containsKey(Telephony.Carriers.SERVER)) {
values.put(Telephony.Carriers.SERVER, "");
}
                if (!values.containsKey(Telephony.Carriers.PASSWORD)) {
values.put(Telephony.Carriers.PASSWORD, "");
}
                if (!values.containsKey(Telephony.Carriers.MMSPORT)) {
values.put(Telephony.Carriers.MMSPORT, "");
}
                if (!values.containsKey(Telephony.Carriers.MMSPROXY)) {
values.put(Telephony.Carriers.MMSPROXY, "");
}
                if (!values.containsKey(Telephony.Carriers.AUTH_TYPE)) {
values.put(Telephony.Carriers.AUTH_TYPE, -1);
}
                if (!values.containsKey(Telephony.Carriers.PROTOCOL)) {
                    values.put(Telephony.Carriers.PROTOCOL, "IP");
                }
                if (!values.containsKey(Telephony.Carriers.ROAMING_PROTOCOL)) {
                    values.put(Telephony.Carriers.ROAMING_PROTOCOL, "IP");
                }


long rowID = db.insert(CARRIERS_TABLE, null, values);








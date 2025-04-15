/*read MVNO config and insert DB

Update DB version because of using new field for MVNO.
If there is config of MVNO, insert MVNO data to telephony.db.
Default value of fields related to MVNO sets empty value('').

Bug: 8143480

Change-Id:Ifb25cedf7f85d0fc88abb6742274975cab2eb512*/
//Synthetic comment -- diff --git a/src/com/android/providers/telephony/TelephonyProvider.java b/src/com/android/providers/telephony/TelephonyProvider.java
//Synthetic comment -- index 3f22e2d..3663cfc 100644

//Synthetic comment -- @@ -57,7 +57,7 @@
private static final String DATABASE_NAME = "telephony.db";
private static final boolean DBG = true;

    private static final int DATABASE_VERSION = 7 << 16;
private static final int URL_TELEPHONY = 1;
private static final int URL_CURRENT = 2;
private static final int URL_ID = 3;
//Synthetic comment -- @@ -73,6 +73,7 @@
private static final String APN_CONFIG_CHECKSUM = "apn_conf_checksum";

private static final String PARTNER_APNS_PATH = "etc/apns-conf.xml";

private static final UriMatcher s_urlMatcher = new UriMatcher(UriMatcher.NO_MATCH);

//Synthetic comment -- @@ -148,7 +149,11 @@
"protocol TEXT," +
"roaming_protocol TEXT," +
"carrier_enabled BOOLEAN," +
                    "bearer INTEGER);");

initDatabase(db);
}
//Synthetic comment -- @@ -195,6 +200,32 @@
} finally {
try { if (confreader != null) confreader.close(); } catch (IOException e) { }
}
}

@Override
//Synthetic comment -- @@ -233,6 +264,19 @@
" ADD COLUMN bearer INTEGER DEFAULT 0;");
oldVersion = 7 << 16 | 6;
}
}

/**
//Synthetic comment -- @@ -308,6 +352,26 @@
if (bearer != null) {
map.put(Telephony.Carriers.BEARER, Integer.parseInt(bearer));
}
return map;
}

//Synthetic comment -- @@ -361,6 +425,18 @@
if (row.containsKey(Telephony.Carriers.BEARER) == false) {
row.put(Telephony.Carriers.BEARER, 0);
}
db.insert(CARRIERS_TABLE, null, row);
}
}
//Synthetic comment -- @@ -559,6 +635,18 @@
if (!values.containsKey(Telephony.Carriers.BEARER)) {
values.put(Telephony.Carriers.BEARER, 0);
}

long rowID = db.insert(CARRIERS_TABLE, null, values);
if (rowID > 0)








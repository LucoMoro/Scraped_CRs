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

    private static final int DATABASE_VERSION = 8 << 16;
private static final int URL_TELEPHONY = 1;
private static final int URL_CURRENT = 2;
private static final int URL_ID = 3;
//Synthetic comment -- @@ -73,6 +73,7 @@
private static final String APN_CONFIG_CHECKSUM = "apn_conf_checksum";

private static final String PARTNER_APNS_PATH = "etc/apns-conf.xml";
    private static final String PARTNER_MVNO_PATH = "etc/mvno-conf.xml";

private static final UriMatcher s_urlMatcher = new UriMatcher(UriMatcher.NO_MATCH);

//Synthetic comment -- @@ -148,7 +149,11 @@
"protocol TEXT," +
"roaming_protocol TEXT," +
"carrier_enabled BOOLEAN," +
                    "bearer INTEGER," +
                    "spn TEXT," +
                    "imsi TEXT," +
                    "gid TEXT," +
                    "mvno_type TEXT);");

initDatabase(db);
}
//Synthetic comment -- @@ -195,6 +200,32 @@
} finally {
try { if (confreader != null) confreader.close(); } catch (IOException e) { }
}

            // Read external MVNO data (partner-provided)
            // Environment.getRootDirectory() is a fancy way of saying ANDROID_ROOT or "/system".
            confFile = new File(Environment.getRootDirectory(), PARTNER_MVNO_PATH);
            try {
                confreader = new FileReader(confFile);
                confparser = Xml.newPullParser();
                confparser.setInput(confreader);
                XmlUtils.beginDocument(confparser, "apns");

                // Sanity check. Force internal version and confidential versions to agree
                int confversion = Integer.parseInt(confparser.getAttributeValue(null, "version"));
                if (publicversion != confversion) {
                    throw new IllegalStateException("Internal MVNO file version doesn't match "
                            + confFile.getAbsolutePath());
                }

                loadApns(db, confparser);
            } catch (FileNotFoundException e) {
                // It's ok if the file isn't found. It means there isn't a confidential file
                // Log.e(TAG, "File not found: '" + confFile.getAbsolutePath() + "'");
            } catch (Exception e) {
                Log.e(TAG, "Exception while parsing '" + confFile.getAbsolutePath() + "'", e);
            } finally {
                try { if (confreader != null) confreader.close(); } catch (IOException e) { }
            }
}

@Override
//Synthetic comment -- @@ -233,6 +264,19 @@
" ADD COLUMN bearer INTEGER DEFAULT 0;");
oldVersion = 7 << 16 | 6;
}
            if (oldVersion < (8 << 16 | 6)) {
                // Add spn, imsi, gid, mvno_type fields to the APN.
                // The XML file does not change.
                db.execSQL("ALTER TABLE " + CARRIERS_TABLE +
                        " ADD COLUMN spn TEXT DEFAULT '';");
                db.execSQL("ALTER TABLE " + CARRIERS_TABLE +
                        " ADD COLUMN imsi TEXT DEFAULT '';");
                db.execSQL("ALTER TABLE " + CARRIERS_TABLE +
                        " ADD COLUMN gid TEXT DEFAULT '';");
                db.execSQL("ALTER TABLE " + CARRIERS_TABLE +
                        " ADD COLUMN mvno_type TEXT DEFAULT '';");
                oldVersion = 8 << 16 | 6;
            }
}

/**
//Synthetic comment -- @@ -308,6 +352,26 @@
if (bearer != null) {
map.put(Telephony.Carriers.BEARER, Integer.parseInt(bearer));
}

            String spn = parser.getAttributeValue(null, "spn");
            if (spn != null) {
                map.put(Telephony.Carriers.SPN, spn);
            }

            String imsi = parser.getAttributeValue(null, "imsi");
            if (imsi != null) {
                map.put(Telephony.Carriers.IMSI, imsi);
            }

            String gid = parser.getAttributeValue(null, "gid");
            if (gid != null) {
                map.put(Telephony.Carriers.GID, gid);
            }

            String mvno_type = parser.getAttributeValue(null, "mvno_type");
            if (mvno_type != null) {
                map.put(Telephony.Carriers.MVNO_TYPE, mvno_type);
            }
return map;
}

//Synthetic comment -- @@ -361,6 +425,18 @@
if (row.containsKey(Telephony.Carriers.BEARER) == false) {
row.put(Telephony.Carriers.BEARER, 0);
}
            if (row.containsKey(Telephony.Carriers.SPN) == false) {
                row.put(Telephony.Carriers.SPN, "");
            }
            if (row.containsKey(Telephony.Carriers.IMSI) == false) {
                row.put(Telephony.Carriers.IMSI, "");
            }
            if (row.containsKey(Telephony.Carriers.GID) == false) {
                row.put(Telephony.Carriers.GID, "");
            }
            if (row.containsKey(Telephony.Carriers.MVNO_TYPE) == false) {
                row.put(Telephony.Carriers.MVNO_TYPE, "");
            }
db.insert(CARRIERS_TABLE, null, row);
}
}
//Synthetic comment -- @@ -559,6 +635,18 @@
if (!values.containsKey(Telephony.Carriers.BEARER)) {
values.put(Telephony.Carriers.BEARER, 0);
}
                if (!values.containsKey(Telephony.Carriers.SPN)) {
                    values.put(Telephony.Carriers.SPN, "");
                }
                if (!values.containsKey(Telephony.Carriers.IMSI)) {
                    values.put(Telephony.Carriers.IMSI, "");
                }
                if (!values.containsKey(Telephony.Carriers.GID)) {
                    values.put(Telephony.Carriers.GID, "");
                }
                if (!values.containsKey(Telephony.Carriers.MVNO_TYPE)) {
                    values.put(Telephony.Carriers.MVNO_TYPE, "");
                }

long rowID = db.insert(CARRIERS_TABLE, null, values);
if (rowID > 0)








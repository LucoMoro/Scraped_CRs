/*Call From SIM

When trying to call a number on the SIM, for example by dialing '1#',
Android code does not make a distinction between order and position
on the SIM. The contacts are sorted, which is wrong, and any empty
positions are removed (positions can be empty on a SIM). This means
that trying to make a phone call this way will lead to users dialing
the wrong number.

An API to load SIM positions has been added to solve the issue.

Change-Id:I042a9c63a240031bc7248c0b4c3e95e2df398980*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccProvider.java b/telephony/java/com/android/internal/telephony/IccProvider.java
//Synthetic comment -- index 3471ec2..2aaa879 100644

//Synthetic comment -- @@ -197,6 +197,7 @@
private static final int ADN = 1;
private static final int FDN = 2;
private static final int SDN = 3;
    private static final int SIMPOS = 4;

private static final String STR_TAG = "tag";
private static final String STR_NUMBER = "number";
//Synthetic comment -- @@ -210,6 +211,7 @@
URL_MATCHER.addURI("icc", "adn", ADN);
URL_MATCHER.addURI("icc", "fdn", FDN);
URL_MATCHER.addURI("icc", "sdn", SDN);
        URL_MATCHER.addURI("icc", "adn/#", SIMPOS);
}


//Synthetic comment -- @@ -235,6 +237,17 @@

if (!mSimulator) {
switch (URL_MATCHER.match(url)) {
                case SIMPOS:
                    String SIMRecord = "content://icc/adn/";
                    try {
                        int index = Integer.parseInt(url.toString().substring(SIMRecord.length()));
                        results = loadSIMPosFromEf(index);
                    } catch (NumberFormatException ex) {
                        if (DBG) log(ex.toString());
                        results = null;
                    }
                    break;

case ADN:
results = loadFromEf(IccConstants.EF_ADN);
break;
//Synthetic comment -- @@ -507,6 +520,40 @@
return results;
}

    private ArrayList<ArrayList> loadSIMPosFromEf(int pos) {
        ArrayList<ArrayList> results = new ArrayList<ArrayList>();
        List<AdnRecord> adnRecords = null;

        try {
            IIccPhoneBook iccIpb = IIccPhoneBook.Stub.asInterface(
                    ServiceManager.getService("simphonebook"));
            if (iccIpb != null) {
                adnRecords = iccIpb.getAdnRecordsInEf(IccConstants.EF_ADN);
            }
        } catch (RemoteException ex) {
            // ignore it
        } catch (SecurityException ex) {
            if (DBG) log(ex.toString());
        }
        if (adnRecords != null) {
            // Load the results
            int n = adnRecords.size();
            if (pos > 0 && pos <= n) {
                loadAnyRecord((AdnRecord)adnRecords.get(pos - 1), results);
            } else {
                // No results to load
                Log.w(TAG, "ADN record out of bounds");
                results.clear();
            }
        } else {
            // No results to load
            Log.w(TAG, "Cannot load ADN record");
            results.clear();
        }
        if (DBG) log("loadSIMPosFromEf: return results");
        return results;
    }

private boolean
addIccRecordToEf(int efType, String name, String number, String[] emails, String pin2) {
if (DBG) log("addIccRecordToEf: efType=" + efType + ", name=" + name +
//Synthetic comment -- @@ -616,6 +663,41 @@
}
}

    private void loadAnyRecord(AdnRecord record,
            ArrayList<ArrayList> results) {
        String alphaTag = "";
        String number = "";
        String[] emails = null;
        StringBuilder emailString = null;
        ArrayList<String> contact = new ArrayList<String>();

        if (!record.isEmpty()) {
            alphaTag = record.getAlphaTag();
            number = record.getNumber();
            emails = record.getEmails();

            if (emails != null) {
                emailString = new StringBuilder();
                for (String email: emails) {
                    emailString.append(email);
                    emailString.append(",");
                }
            }
        }

        if (DBG) log("loadAnyRecord: " + alphaTag + ", " + number);
        contact.add(alphaTag);
        contact.add(number);

        if (emailString != null) {
            contact.add(emailString.toString());
            if (DBG) log("email(s) " + emailString.toString());
        } else {
            contact.add(null);
        }
        results.add(contact);
    }

private void log(String msg) {
Log.d(TAG, "[IccProvider] " + msg);
}








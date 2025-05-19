//<Beginning of snippet n. 0>


notifyOffApnsOfAvailability(reason);
}

/**
* Based on the sim operator numeric, create a list for all possible
* Data Connections and setup the preferredApn.
mAllApns = new ArrayList<ApnSetting>();
IccRecords r = mIccRecords.get();
String operator = (r != null) ? r.getOperatorNumeric() : "";
if (operator != null && !operator.isEmpty()) {
    String selection = "numeric = '" + operator + "' and carrier_enabled = 1";
    if (DBG) log("createAllApnList: selection=" + selection);

    try (Cursor cursor = mPhone.getContext().getContentResolver().query(
            Telephony.Carriers.CONTENT_URI, null, selection, null, null)) {
        if (cursor != null && cursor.getCount() > 0) {
            mAllApns = createApnList(cursor);
        }
    } catch (SQLException e) {
        log("SQL error querying APN: " + e.getMessage());
    } catch (Exception e) {
        log("Error querying APN: " + e.getMessage());
    }
}

if (r != null) {
    String mvnoType = r.getMvnoType();
    if (mvnoType != null) {
        switch (mvnoType) {
            case "spn":
                String spn = r.getSpn();
                if (spn != null && !spn.isEmpty()) {
                    // Implement SPN check using SIM attributes
                }
                break;
            case "imsi":
                String imsi = r.getImsi();
                if (imsi != null && !imsi.isEmpty()) {
                    // Implement IMSI check using SIM attributes
                }
                break;
            case "gid":
                String gid = r.getGid();
                if (gid != null && !gid.isEmpty()) {
                    // Implement GID check using SIM attributes
                }
                break;
            default:
                // Existing logic for generating the APN list
                break;
        }
    }
}

//<End of snippet n. 0>
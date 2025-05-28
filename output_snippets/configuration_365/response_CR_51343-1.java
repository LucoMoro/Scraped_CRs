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
    } catch (Exception e) {
        log("Error querying APN: " + e.getMessage());
    }
}

if (r != null) {
    String mvnoType = r.getMvnoType();
    if (mvnoType != null) {
        switch (mvnoType) {
            case "spn":
                // Implement SPN check using SIM attributes
                break;
            case "imsi":
                // Implement IMSI check using SIM attributes
                break;
            case "gid":
                // Implement GID check using SIM attributes
                break;
            default:
                // Existing logic for generating the APN list
                break;
        }
    }
}

//<End of snippet n. 0>
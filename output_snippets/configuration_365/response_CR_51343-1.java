//<Beginning of snippet n. 0>


notifyOffApnsOfAvailability(reason);
}

/**
* Based on the sim operator numeric, create a list for all possible
* Data Connections and setup the preferredApn.
mAllApns = new ArrayList<ApnSetting>();
IccRecords r = mIccRecords.get();
String operator = (r != null) ? r.getOperatorNumeric() : "";
String mvnoType = (r != null) ? r.getMvnoType() : "";
if (operator != null && !operator.isEmpty()) {
    String selection = "numeric = '" + operator + "' and carrier_enabled = 1";
    if (DBG) log("createAllApnList: selection=" + selection);

    Cursor cursor = null;
    try {
        cursor = mPhone.getContext().getContentResolver().query(
                Telephony.Carriers.CONTENT_URI, null, selection, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            mAllApns = createApnList(cursor);
        }
    } catch (Exception e) {
        log("Error querying APNs: " + e.getMessage());
    } finally {
        if (cursor != null) {
            cursor.close();
        }
    }

    if (mvnoType != null && !mvnoType.isEmpty()) {
        // Implement distinct comparison logic based on mvnoType
        if ("spn".equals(mvnoType)) {
            // Logic for SPN type
        } else if ("imsi".equals(mvnoType)) {
            // Logic for IMSI type
        } else if ("gid".equals(mvnoType)) {
            // Logic for GID type
        }
    }
}


//<End of snippet n. 0>
//<Beginning of snippet n. 0>
notifyOffApnsOfAvailability(reason);
}

/**
* Based on the sim operator numeric, create a list for all possible
* Data Connections and setup the preferredApn.
mAllApns = new ArrayList<ApnSetting>();
IccRecords r = mIccRecords.get();
String operator = (r != null) ? r.getOperatorNumeric() : "";
String mvnoType = (r != null) ? r.getMvnoType() : null;
if (operator != null) {
    String selection = "numeric = '" + operator + "'";
    selection += " and carrier_enabled = 1";
    if (DBG) log("createAllApnList: selection=" + selection);

    try (Cursor cursor = mPhone.getContext().getContentResolver().query(
            Telephony.Carriers.CONTENT_URI, null, selection, null, null)) {
        if (cursor != null && cursor.getCount() > 0) {
            mAllApns = createApnList(cursor);
            // Check for mvno_type after retrieving APN items
            if (mvnoType != null) {
                // Logic to retrieve and compare SIM details based on mvnoType
                // Placeholder for MVNO-specific processing logic
            }
        }
    } catch (Exception e) {
        // Handle possible exceptions
    }
}
//<End of snippet n. 0>
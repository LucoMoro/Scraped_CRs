//<Beginning of snippet n. 0>

notifyOffApnsOfAvailability(reason);
}

/**
* Based on the sim operator numeric, create a list for all possible
* Data Connections and setup the preferredApn.
*/
mAllApns = new ArrayList<ApnSetting>();
IccRecords r = mIccRecords.get();
String operator = (r != null) ? r.getOperatorNumeric() : "";
if (operator != null) {
    String selection = "numeric = '" + operator + "' and carrier_enabled = 1";
    if (DBG) log("createAllApnList: selection=" + selection);

    Cursor cursor = null;
    try {
        cursor = mPhone.getContext().getContentResolver().query(
                Telephony.Carriers.CONTENT_URI, null, selection, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            mAllApns = createApnList(cursor);
        }

        String mvnoType = cursor.getString(cursor.getColumnIndex("mvno_type"));
        String mvnoData = cursor.getString(cursor.getColumnIndex("mvno_match_data"));
        
        if (mvnoType != null && mvnoData != null) {
            switch (mvnoType) {
                case "spn":
                    // Compare SPN with corresponding APN value
                    break;
                case "imsi":
                    // Compare IMSI with relevant APN value
                    break;
                case "gid":
                    // Compare GID with relevant APN value
                    break;
                default:
                    // Fallback logic if mvnoType doesn't match
                    break;
            }
        } else {
            // Existing routine for creating the APN list executes as before
        }
    } catch (Exception e) {
        log("Error during APN retrieval: " + e.getMessage());
    } finally {
        if (cursor != null) {
            cursor.close();
        }
    }
}

//<End of snippet n. 0>
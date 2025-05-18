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

    Cursor cursor = null;
    try {
        cursor = mPhone.getContext().getContentResolver().query(
                Telephony.Carriers.CONTENT_URI, null, selection, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            mAllApns = createApnList(cursor);
            String mvnoType = null;

            if (cursor.moveToFirst()) {
                mvnoType = cursor.getString(cursor.getColumnIndex("mvno_type")); // Assuming mvno_type column exists
            }

            if (mvnoType != null) {
                switch (mvnoType) {
                    case "spn":
                        // Logic for handling spn type
                        break;
                    case "imsi":
                        // Logic for handling imsi type
                        break;
                    case "gid":
                        // Logic for handling gid type
                        break;
                    default:
                        // Handle non-existent mvno_type case
                        break;
                }
            }
        }
    } catch (SQLException e) {
        log("SQL error querying APNs: " + e.getMessage());
    } catch (Exception e) {
        log("Error querying APNs: " + e.getMessage());
    } finally {
        if (cursor != null) {
            cursor.close();
        }
    }
}

//<End of snippet n. 0>
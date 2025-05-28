
//<Beginning of snippet n. 0>


notifyOffApnsOfAvailability(reason);
}

/**
* Based on the sim operator numeric, create a list for all possible
* Data Connections and setup the preferredApn.
mAllApns = new ArrayList<ApnSetting>();
IccRecords r = mIccRecords.get();
String operator = (r != null) ? r.getOperatorNumeric() : "";
if (operator != null) {
String selection = "numeric = '" + operator + "'";
// query only enabled apn.
// carrier_enabled : 1 means enabled apn, 0 disabled apn.
selection += " and carrier_enabled = 1";
            if (DBG) log("createAllApnList: selection=" + selection);

            Cursor cursor = mPhone.getContext().getContentResolver().query(
                    Telephony.Carriers.CONTENT_URI, null, selection, null, null);

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    mAllApns = createApnList(cursor);
}
                cursor.close();
}
}


//<End of snippet n. 0>









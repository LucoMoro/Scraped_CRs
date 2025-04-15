/*PullvCardList implemented according to specification

The value tag sent as a byte array was not parsed correctly
leading to illegal strings. The list count value was not used
according to the specification, now refers to number of entries
that should be returned. Specification for Phone book access
profile chapter 5.3 default max list size of OxFFFF (65535)
taken from 5.3.4.4. Now uses default values when Application
Parameter headers are missing.

Change-Id:Ib17f2208a91b4f896d9beebaf3a21566502ac291*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java b/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java
//Synthetic comment -- index ff014b9..58ced0c 100644

//Synthetic comment -- @@ -396,7 +396,7 @@
}
}

        if ((appParam != null) && !parseApplicationParameter(appParam, appParamValue)) {
return ResponseCodes.OBEX_HTTP_BAD_REQUEST;
}

//Synthetic comment -- @@ -452,7 +452,7 @@
public boolean vcard21;

public AppParamValue() {
            maxListCount = 0xFFFF;
listStartOffset = 0;
searchValue = "";
searchAttr = "";
//Synthetic comment -- @@ -486,11 +486,10 @@
break;
case ApplicationParameter.TRIPLET_TAGID.SEARCH_VALUE_TAGID:
i += 1; // length field in triplet
// length of search value is variable
                    int length = appParam[i];
                    appParamValue.searchValue = new String(appParam, i + 1, length);
                    i += length;
i += 1;
break;
case ApplicationParameter.TRIPLET_TAGID.SEARCH_ATTRIBUTE_TAGID:
//Synthetic comment -- @@ -547,72 +546,14 @@

// Phonebook listing request
if (type == ContentType.PHONEBOOK) {
            if (searchAttr.equals("0")) { // search by name
ArrayList<String> nameList = mVcardManager.getPhonebookNameList(mOrderBy );
                itemsFound = createList(maxListCount, listStartOffset, searchValue, result,
                        nameList, "name");
            } else if (searchAttr.equals("1")) { // search by number
ArrayList<String> numberList = mVcardManager.getPhonebookNumberList();
                itemsFound = createList(maxListCount, listStartOffset, searchValue, result,
                        numberList, "number");
}// end of search by number
else {
return ResponseCodes.OBEX_HTTP_PRECON_FAILED;
//Synthetic comment -- @@ -643,6 +584,26 @@
return pushBytes(op, result.toString());
}

    private int createList(final int maxListCount, final int listStartOffset,
            final String searchValue, StringBuilder result,
            ArrayList<String> dataList, String type) {
        int itemsFound = 0;
        int requestSize = dataList.size() >= maxListCount ? maxListCount : dataList.size();

        if (D) Log.d(TAG, "search by " + type + ", size=" + requestSize + " offset="
                    + listStartOffset + " searchValue=" + searchValue);

        for (int pos = listStartOffset; pos < dataList.size() && itemsFound < requestSize; pos++) {
            String currentValue = dataList.get(pos);
            if (searchValue == null || currentValue.startsWith(searchValue.trim())) {
                itemsFound++;
                result.append("<card handle=\"" + pos + ".vcf\" " + type + "=\""
                        + currentValue + "\"" + "/>");
            }
        }
        return itemsFound;
    }

/**
* Function to send obex header back to client such as get phonebook size
* request








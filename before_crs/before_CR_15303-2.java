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

        if (!parseApplicationParameter(appParam, appParamValue)) {
return ResponseCodes.OBEX_HTTP_BAD_REQUEST;
}

//Synthetic comment -- @@ -452,7 +452,7 @@
public boolean vcard21;

public AppParamValue() {
            maxListCount = 0;
listStartOffset = 0;
searchValue = "";
searchAttr = "";
//Synthetic comment -- @@ -486,11 +486,10 @@
break;
case ApplicationParameter.TRIPLET_TAGID.SEARCH_VALUE_TAGID:
i += 1; // length field in triplet
                    for (int k = 1; k <= appParam[i]; k++) {
                        appParamValue.searchValue += Byte.toString(appParam[i + k]);
                    }
// length of search value is variable
                    i += appParam[i];
i += 1;
break;
case ApplicationParameter.TRIPLET_TAGID.SEARCH_ATTRIBUTE_TAGID:
//Synthetic comment -- @@ -547,72 +546,14 @@

// Phonebook listing request
if (type == ContentType.PHONEBOOK) {
            // begin of search by name
            if (searchAttr.equals("0")) {
ArrayList<String> nameList = mVcardManager.getPhonebookNameList(mOrderBy );
                int requestSize = nameList.size() >= maxListCount ? maxListCount : nameList.size();
                int startPoint = listStartOffset;
                int endPoint = startPoint + requestSize;
                if (endPoint > nameList.size()) {
                    endPoint = nameList.size();
                }

                if (D) Log.d(TAG, "search by name, size=" + requestSize + " offset=" +
                        listStartOffset + " searchValue=" + searchValue);


                // if searchValue if not set by client,provide the entire
                // list by name
                if (searchValue == null || searchValue.trim().length() == 0) {
                    for (int j = startPoint; j < endPoint; j++) {
                        result.append("<card handle=\"" + j + ".vcf\" name=\"" + nameList.get(j)
                                + "\"" + "/>");
                        itemsFound++;
                    }
                } else {
                    for (int j = startPoint; j < endPoint; j++) {
                        // only find the name which begins with the searchValue
                        if (nameList.get(j).startsWith(searchValue.trim())) {
                            // TODO: PCE not work with it
                            itemsFound++;
                            result.append("<card handle=\"" + j + ".vcf\" name=\""
                                    + nameList.get(j) + "\"" + "/>");
                        }
                    }
                }
            }// end of search by name
            // begin of search by number
            else if (searchAttr.equals("1")) {
ArrayList<String> numberList = mVcardManager.getPhonebookNumberList();
                int requestSize = numberList.size() >= maxListCount ? maxListCount : numberList
                        .size();
                int startPoint = listStartOffset;
                int endPoint = startPoint + requestSize;
                if (endPoint > numberList.size()) {
                    endPoint = numberList.size();
                }

                if (D) Log.d(TAG, "search by number, size=" + requestSize + " offset="
                            + listStartOffset + " searchValue=" + searchValue);

                // if searchValue if not set by client,provide the entire
                // list by number
                if (searchValue == null || searchValue.trim().length() == 0) {
                    for (int j = startPoint; j < endPoint; j++) {
                        result.append("<card handle=\"" + j + ".vcf\" number=\""
                                + numberList.get(j) + "\"" + "/>");
                        itemsFound++;
                    }
                } else {
                    for (int j = startPoint; j < endPoint; j++) {
                        // only find the name which begins with the searchValue
                        if (numberList.get(j).startsWith(searchValue.trim())) {
                            itemsFound++;
                            result.append("<card handle=\"" + j + ".vcf\" number=\""
                                    + numberList.get(j) + "\"" + "/>");
                        }
                    }
                }
}// end of search by number
else {
return ResponseCodes.OBEX_HTTP_PRECON_FAILED;
//Synthetic comment -- @@ -643,6 +584,26 @@
return pushBytes(op, result.toString());
}

/**
* Function to send obex header back to client such as get phonebook size
* request








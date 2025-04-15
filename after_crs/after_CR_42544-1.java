/*Not able to read sim phonebook without ext1

This patch allows us to access sim phonebook on sim cards
without EF_EXT1 file. The patch simply ignores to read
EF_EXT1 file if it doesn't exist or the file id is
invalid (0x0). This also allows us to gracefully handle
incorrectly configured EF_ADN files which references
EF_EXT1 records while the EF_EXT1 file doesn't exist.

Change-Id:I83b0596ddda0418a272372d68ffc7057cff402b0*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/AdnRecordLoader.java b/src/java/com/android/internal/telephony/AdnRecordLoader.java
//Synthetic comment -- index 084fae6..8e9550d 100644

//Synthetic comment -- @@ -195,7 +195,8 @@
adn = new AdnRecord(ef, recordNumber, data);
result = adn;

                    // Read ext1 number only if the file id is valid
                    if (extensionEF != 0 && adn.hasExtendedRecord()) {
// If we have a valid value in the ext record field,
// we're not done yet: we need to read the corresponding
// ext record and append it
//Synthetic comment -- @@ -245,7 +246,8 @@
adn = new AdnRecord(ef, 1 + i, datas.get(i));
adns.add(adn);

                        // Read ext1 number only if the file id is valid
                        if (extensionEF != 0 && adn.hasExtendedRecord()) {
// If we have a valid value in the ext record field,
// we're not done yet: we need to read the corresponding
// ext record and append it








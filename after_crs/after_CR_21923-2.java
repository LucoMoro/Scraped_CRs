/*Fix aggregation exception problem

This patch fixes a bug in the aggregation algorithm. When adding
aggregation exceptions sometimes when excluding a raw contact from
a Contact the contact information was not updated.

Change-Id:If527c0fe0bd51eec77a71e466492691375889037*/




//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactAggregator.java b/src/com/android/providers/contacts/ContactAggregator.java
//Synthetic comment -- index cad5d50..9b4ecd9 100644

//Synthetic comment -- @@ -728,6 +728,10 @@
mContactUpdate.execute();
mDbHelper.updateContactVisible(txContext, contactId);
updateAggregatedStatusUpdate(contactId);
            // Make sure the raw contact does not contribute to the current contact
            if (currentContactId != 0) {
                updateAggregateData(txContext, currentContactId);
            }
}

if (contactIdToSplit != -1) {








//Synthetic comment -- diff --git a/tests/src/com/android/providers/contacts/ContactAggregatorTest.java b/tests/src/com/android/providers/contacts/ContactAggregatorTest.java
//Synthetic comment -- index 5c7c0ff..268bcb4 100644

//Synthetic comment -- @@ -541,6 +541,42 @@
assertEquals("Johnm Smithm", displayName4);
}

    public void testAggregationExceptionKeepOutCheckResultDisplayNames() {
        long rawContactId1 = createRawContactWithName("c", "c", ACCOUNT_1);
        long rawContactId2 = createRawContactWithName("b", "b", ACCOUNT_2);
        long rawContactId3 = createRawContactWithName("a", "a", ACCOUNT_3);

        // Join all contacts
        setAggregationException(AggregationExceptions.TYPE_KEEP_TOGETHER,
                rawContactId1, rawContactId2);
        setAggregationException(AggregationExceptions.TYPE_KEEP_TOGETHER,
                rawContactId1, rawContactId3);
        setAggregationException(AggregationExceptions.TYPE_KEEP_TOGETHER,
                rawContactId2, rawContactId3);

        // Separate all contacts. The order (2-3 , 1-2, 1-3) is important
        setAggregationException(AggregationExceptions.TYPE_KEEP_SEPARATE,
                rawContactId2, rawContactId3);
        setAggregationException(AggregationExceptions.TYPE_KEEP_SEPARATE,
                rawContactId1, rawContactId2);
        setAggregationException(AggregationExceptions.TYPE_KEEP_SEPARATE,
                rawContactId1, rawContactId3);

        // Verify that we have three different contacts
        long contactId1 = queryContactId(rawContactId1);
        long contactId2 = queryContactId(rawContactId2);
        long contactId3 = queryContactId(rawContactId3);

        assertTrue(contactId1 != contactId2);
        assertTrue(contactId1 != contactId3);
        assertTrue(contactId2 != contactId3);

        // Verify that each raw contact contribute to the contact display name
        assertDisplayNameEquals(contactId1, rawContactId1);
        assertDisplayNameEquals(contactId2, rawContactId2);
        assertDisplayNameEquals(contactId3, rawContactId3);
    }

public void testNonAggregationWithMultipleAffinities() {
long rawContactId1 = createRawContactWithName("John", "Doe", ACCOUNT_1);
long rawContactId2 = createRawContactWithName("John", "Doe", ACCOUNT_1);
//Synthetic comment -- @@ -1288,4 +1324,18 @@

cursor.close();
}

    private void assertDisplayNameEquals(long contactId, long rawContactId) {

        String contactDisplayName = queryDisplayName(contactId);

        Cursor c = queryRawContact(rawContactId);
        assertTrue(c.moveToFirst());
        String rawDisplayName = c.getString(c.getColumnIndex(RawContacts.DISPLAY_NAME_PRIMARY));
        c.close();

        assertTrue(contactDisplayName != null);
        assertTrue(rawDisplayName != null);
        assertEquals(rawDisplayName, contactDisplayName);
    }
}








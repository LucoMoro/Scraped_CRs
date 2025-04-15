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
}

if (contactIdToSplit != -1) {








//Synthetic comment -- diff --git a/tests/src/com/android/providers/contacts/ContactAggregatorTest.java b/tests/src/com/android/providers/contacts/ContactAggregatorTest.java
//Synthetic comment -- index 5c7c0ff..268bcb4 100644

//Synthetic comment -- @@ -541,6 +541,42 @@
assertEquals("Johnm Smithm", displayName4);
}

public void testNonAggregationWithMultipleAffinities() {
long rawContactId1 = createRawContactWithName("John", "Doe", ACCOUNT_1);
long rawContactId2 = createRawContactWithName("John", "Doe", ACCOUNT_1);
//Synthetic comment -- @@ -1288,4 +1324,18 @@

cursor.close();
}
}








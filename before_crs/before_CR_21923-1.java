/*Fix aggregation exception problem

This patch fixes a bug in the aggregation algorithm. When adding
aggregation exceptions sometimes when excluding a raw contact from
a Contact the contact information was not updated.

Change-Id:If527c0fe0bd51eec77a71e466492691375889037*/
//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactAggregator.java b/src/com/android/providers/contacts/ContactAggregator.java
//Synthetic comment -- index 314ed0a..c5a83b6 100644

//Synthetic comment -- @@ -592,6 +592,10 @@
mContactUpdate.execute();
mDbHelper.updateContactVisible(contactId);
updateAggregatedPresence(contactId);
}

if (contactIdToSplit != -1) {








//Synthetic comment -- diff --git a/tests/src/com/android/providers/contacts/ContactAggregatorTest.java b/tests/src/com/android/providers/contacts/ContactAggregatorTest.java
//Synthetic comment -- index 328b03c..761aa9d 100644

//Synthetic comment -- @@ -516,6 +516,42 @@
assertEquals("Johnm Smithm", displayName4);
}

public void testNonAggregationWithMultipleAffinities() {
long rawContactId1 = createRawContactWithName("John", "Doe", ACCOUNT_1);
long rawContactId2 = createRawContactWithName("John", "Doe", ACCOUNT_1);
//Synthetic comment -- @@ -995,4 +1031,18 @@

cursor.close();
}
}








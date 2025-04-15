/*Fix crash when separating multiple empty raw contacts

This changes the back reference logic a little bit to prevent
adding back references where there is nothing to back reference.
This fixes weird cases where a user could hit "new contact" twice
without saving data and then try to separate the two dummy
contacts.

Change-Id:I1a6b4e1a6bda99f5d8ee07ea57cace75a6bed4ab*/
//Synthetic comment -- diff --git a/src/com/android/contacts/model/EntitySet.java b/src/com/android/contacts/model/EntitySet.java
//Synthetic comment -- index 83fe338..830f8da 100644

//Synthetic comment -- @@ -130,11 +130,13 @@
// Second pass builds actual operations
for (EntityDelta delta : this) {
final int firstBatch = diff.size();
            backRefs[rawContactIndex++] = firstBatch;
delta.buildDiff(diff);

// Only create rules for inserts
            if (!delta.isContactInsert()) continue;

// If we are going to split all contacts, there is no point in first combining them
if (mSplitRawContacts) continue;
//Synthetic comment -- @@ -208,18 +210,25 @@
builder.withValue(AggregationExceptions.TYPE, AggregationExceptions.TYPE_KEEP_SEPARATE);

Long rawContactId1 = get(index1).getValues().getAsLong(RawContacts._ID);
if (rawContactId1 != null && rawContactId1 >= 0) {
builder.withValue(AggregationExceptions.RAW_CONTACT_ID1, rawContactId1);
} else {
            builder.withValueBackReference(AggregationExceptions.RAW_CONTACT_ID1, backRefs[index1]);
}

Long rawContactId2 = get(index2).getValues().getAsLong(RawContacts._ID);
if (rawContactId2 != null && rawContactId2 >= 0) {
builder.withValue(AggregationExceptions.RAW_CONTACT_ID2, rawContactId2);
} else {
            builder.withValueBackReference(AggregationExceptions.RAW_CONTACT_ID2, backRefs[index2]);
}
diff.add(builder.build());
}









//Synthetic comment -- diff --git a/tests/src/com/android/contacts/EntitySetTests.java b/tests/src/com/android/contacts/EntitySetTests.java
//Synthetic comment -- index edfca6d..037c927 100644

//Synthetic comment -- @@ -358,6 +358,58 @@
assertEquals("Unexpected exception updates", 2, exceptionCount);
}

public void testMergeDataRemoteInsert() {
final EntitySet first = buildSet(buildBeforeEntity(CONTACT_BOB, VER_FIRST,
buildPhone(PHONE_RED)));








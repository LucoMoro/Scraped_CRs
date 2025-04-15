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
            final boolean isInsert = delta.isContactInsert();
            backRefs[rawContactIndex++] = isInsert ? firstBatch : -1;

delta.buildDiff(diff);

// Only create rules for inserts
            if (!isInsert) continue;

// If we are going to split all contacts, there is no point in first combining them
if (mSplitRawContacts) continue;
//Synthetic comment -- @@ -208,18 +210,25 @@
builder.withValue(AggregationExceptions.TYPE, AggregationExceptions.TYPE_KEEP_SEPARATE);

Long rawContactId1 = get(index1).getValues().getAsLong(RawContacts._ID);
        int backRef1 = backRefs[index1];
if (rawContactId1 != null && rawContactId1 >= 0) {
builder.withValue(AggregationExceptions.RAW_CONTACT_ID1, rawContactId1);
        } else if (backRef1 >= 0) {
            builder.withValueBackReference(AggregationExceptions.RAW_CONTACT_ID1, backRef1);
} else {
            return;
}

Long rawContactId2 = get(index2).getValues().getAsLong(RawContacts._ID);
        int backRef2 = backRefs[index2];
if (rawContactId2 != null && rawContactId2 >= 0) {
builder.withValue(AggregationExceptions.RAW_CONTACT_ID2, rawContactId2);
        } else if (backRef2 >= 0) {
            builder.withValueBackReference(AggregationExceptions.RAW_CONTACT_ID2, backRef2);
} else {
            return;
}

diff.add(builder.build());
}









//Synthetic comment -- diff --git a/tests/src/com/android/contacts/EntitySetTests.java b/tests/src/com/android/contacts/EntitySetTests.java
//Synthetic comment -- index edfca6d..037c927 100644

//Synthetic comment -- @@ -358,6 +358,58 @@
assertEquals("Unexpected exception updates", 2, exceptionCount);
}

    public void testInsertInsertSeparate() {
        // This assumes getInsert() will return back an "empty" raw
        // contact meaning it will contain no actual information
        final EntityDelta insertFirst = getInsert();
        final EntityDelta insertSecond = getInsert();
        final EntitySet set = buildSet(insertFirst, insertSecond);

        // This would normally build a TYPE_KEEP_SEPARATE aggregation exception,
        // but since the raw contacts won't be added because they are empty,
        // we should get 0 exceptions back
        set.splitRawContacts();

        final ArrayList<ContentProviderOperation> diff = set.buildDiff();
        final int exceptionCount = countExceptionUpdates(diff);
        assertEquals("Unexpected exception updates", 0, exceptionCount);
    }

    public void testUpdateInsertSeparate() {
        // This assumes getInsert() will return back an "empty" raw
        // contact meaning it will contain no actual information
        final EntityDelta update = getUpdate(CONTACT_FIRST);
        final EntityDelta insert = getInsert();
        final EntitySet set = buildSet(update, insert);

        // This would normally build a KEEP_SEPARATE aggregation exception,
        // but since the insert won't be added because it is empty,
        // we should get 0 exceptions back
        set.splitRawContacts();

        final ArrayList<ContentProviderOperation> diff = set.buildDiff();
        final int exceptionCount = countExceptionUpdates(diff);
        assertEquals("Unexpected exception updates", 0, exceptionCount);
    }

    public void testUpdateInsertInsertSeparate() {
        // This assumes getInsert() will return back an "empty" raw
        // contact meaning it will contain no actual information
        final EntityDelta update = getUpdate(CONTACT_FIRST);
        final EntityDelta insertFirst = getInsert();
        final EntityDelta insertSecond = getInsert();
        final EntitySet set = buildSet(update, insertFirst, insertSecond);

        // This would normally build a KEEP_SEPARATE aggregation exception,
        // but since the inserts won't be added because they are empty,
        // we should get 0 exceptions back
        set.splitRawContacts();

        final ArrayList<ContentProviderOperation> diff = set.buildDiff();
        final int exceptionCount = countExceptionUpdates(diff);
        assertEquals("Unexpected exception updates", 0, exceptionCount);
    }

public void testMergeDataRemoteInsert() {
final EntitySet first = buildSet(buildBeforeEntity(CONTACT_BOB, VER_FIRST,
buildPhone(PHONE_RED)));








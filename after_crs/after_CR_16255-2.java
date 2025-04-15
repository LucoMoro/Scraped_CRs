/*Fix crash when separating multiple empty raw contacts

This changes the back reference logic a little bit to prevent
adding back references where there is nothing to back reference.
This fixes weird cases where a user could hit "new contact" twice
without saving data and then try to separate the two dummy
contacts.

Change-Id:I1a6b4e1a6bda99f5d8ee07ea57cace75a6bed4ab*/




//Synthetic comment -- diff --git a/src/com/android/contacts/model/EntitySet.java b/src/com/android/contacts/model/EntitySet.java
//Synthetic comment -- index 83fe338..5f49979 100644

//Synthetic comment -- @@ -123,14 +123,16 @@
}

final int assertMark = diff.size();
        ArrayList<Integer> backRefs = new ArrayList<Integer>();

// Second pass builds actual operations
for (EntityDelta delta : this) {
final int firstBatch = diff.size();

            if (firstBatch > 0) {
                backRefs.add(firstBatch);
            }

delta.buildDiff(diff);

// Only create rules for inserts
//Synthetic comment -- @@ -187,7 +189,7 @@
* separate contacts.
*/
private void buildSplitContactDiff(final ArrayList<ContentProviderOperation> diff,
            ArrayList<Integer> backRefs) {
int count = size();
for (int i = 0; i < count; i++) {
for (int j = 0; j < count; j++) {
//Synthetic comment -- @@ -202,7 +204,7 @@
* Construct a {@link AggregationExceptions#TYPE_KEEP_SEPARATE}.
*/
private void buildSplitContactDiff(ArrayList<ContentProviderOperation> diff, int index1,
            int index2, ArrayList<Integer> backRefs) {
Builder builder =
ContentProviderOperation.newUpdate(AggregationExceptions.CONTENT_URI);
builder.withValue(AggregationExceptions.TYPE, AggregationExceptions.TYPE_KEEP_SEPARATE);
//Synthetic comment -- @@ -210,16 +212,23 @@
Long rawContactId1 = get(index1).getValues().getAsLong(RawContacts._ID);
if (rawContactId1 != null && rawContactId1 >= 0) {
builder.withValue(AggregationExceptions.RAW_CONTACT_ID1, rawContactId1);
        } else if (index1 < backRefs.size()) {
            builder.withValueBackReference(AggregationExceptions.RAW_CONTACT_ID1,
                    backRefs.get(index1));
} else {
            return;
}

Long rawContactId2 = get(index2).getValues().getAsLong(RawContacts._ID);
if (rawContactId2 != null && rawContactId2 >= 0) {
builder.withValue(AggregationExceptions.RAW_CONTACT_ID2, rawContactId2);
        } else if (index2 < backRefs.size()) {
            builder.withValueBackReference(AggregationExceptions.RAW_CONTACT_ID2,
                    backRefs.get(index2));
} else {
            return;
}

diff.add(builder.build());
}









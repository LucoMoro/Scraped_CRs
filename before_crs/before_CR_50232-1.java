/*Fix of Settings application sort crash

The original implementation of compareTo function
will sometimes cause crash when the power usage list
above 32 items.

When comparing double values a proper comparison method
needs to be used. Using just subtraction does not take
into account NaN:S, infinities and +/-0 numbers. In certain
cirtumstances it seems that using subtraction causes
compareTo to return values that is not expected by the
sorting code and causes an illegal argument exception with
"Comparison method violates its general contract!".

This problem only happens if the sort code is called arrays
containing more than 32 (currently) due to how
ComparableTimSort works (call chain is Collections.sort ->
Arrays.sort(Object[]) -> ComparableTimSort.

Change-Id:If732f04797a3c8b2a43568c90bb73a1ec69a4c98*/
//Synthetic comment -- diff --git a/src/com/android/settings/fuelgauge/BatterySipper.java b/src/com/android/settings/fuelgauge/BatterySipper.java
//Synthetic comment -- index f11d6ca..ffc6651 100644

//Synthetic comment -- @@ -93,7 +93,7 @@

public int compareTo(BatterySipper other) {
// Return the flipped value because we want the items in descending order
        return (int) (other.getSortValue() - getSortValue());
}

void getQuickNameIconForUid(Uid uidObj) {








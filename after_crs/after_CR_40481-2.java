/*Telephony: Fix IntRangeManager

-Fix to populate SmsBroadcastConfigInfo list with all the possible
 range lists upon enable and disable to match previous implementation.
-Fix ClientRange to be ordered by start id, then by end id.
-Fix enableRange to handle missing cases:
 -enable exact same range, i.e. new [x, y], existing [x, y]
 -enable range that is already enclosed in existing range i.e.
  adding [3, 3] to existing range [1,3]
 -enable (startId -1) == range.endId, i.e. new [3, y] existing [1, 2]
-Fix where range.endId is not updated correctly
-Fix disableRange to first update mRange then call updateRange.
-Add more unit test cases.

Change-Id:I83308c76ed28c6f753326aeaac6bec1d88c01b39*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IntRangeManager.java b/src/java/com/android/internal/telephony/IntRangeManager.java
//Synthetic comment -- index cc7774d..b511a99 100644

//Synthetic comment -- @@ -101,7 +101,7 @@
}

/**
         * Insert new ClientRange in order by start id, then by end id
* <p>If the new ClientRange is known to be sorted before or after the
* existing ClientRanges, or at a particular index, it can be added
* to the clients array list directly, instead of via this method.
//Synthetic comment -- @@ -111,16 +111,31 @@
*/
void insert(ClientRange range) {
int len = clients.size();
            int insert = -1;
for (int i=0; i < len; i++) {
ClientRange nextRange = clients.get(i);
if (range.startId <= nextRange.startId) {
// ignore duplicate ranges from the same client
if (!range.equals(nextRange)) {
                        // check if same startId, then order by endId
                        if (range.startId == nextRange.startId && range.endId > nextRange.endId) {
                            insert = i+1;
                            if (insert < len) {
                                // there may be more client following with same startId
                                // new [1, 5] existing [1, 2] [1, 4] [1, 7]
                                continue;
                            }
                            break;
                        }
clients.add(i, range);
}
return;
}
}
            if (insert != -1 && insert < len) {
                clients.add(insert, range);
                return;
            }
clients.add(range);    // append to end of list
}
}
//Synthetic comment -- @@ -179,7 +194,7 @@

// empty range list: add the initial IntRange
if (len == 0) {
            if (tryAddRanges(startId, endId, true)) {
mRanges.add(new IntRange(startId, endId, client));
return true;
} else {
//Synthetic comment -- @@ -189,20 +204,65 @@

for (int startIndex = 0; startIndex < len; startIndex++) {
IntRange range = mRanges.get(startIndex);
            if ((startId) >= range.startId && (endId) <= range.endId) {
                // exact same range:  new [1, 1] existing [1, 1]
                // range already enclosed in existing: new [3, 3], [1,3]
                // no radio update necessary.
                // duplicate "client" check is done in insert, attempt to insert.
                range.insert(new ClientRange(startId, endId, client));
                return true;
            } else if ((startId - 1) == range.endId) {
                // new [3, x] existing [1, 2]  OR new [2, 2] existing [1, 1]
                // found missing link? check if next range can be joined
                int newRangeEndId = endId;
                IntRange nextRange = null;
                if ((startIndex + 1) < len) {
                    nextRange = mRanges.get(startIndex + 1);
                    if ((nextRange.startId - 1) <= endId) {
                        // new [3, x] existing [1, 2] [5, 7] OR  new [2 , 2] existing [1, 1] [3, 5]
                        if(endId <= nextRange.endId) {
                            // new [3, 6] existing [1, 2] [5, 7]
                            newRangeEndId = nextRange.startId - 1; // need to enable [3, 4]
                        }
                    } else {
                        // mark nextRange to be joined as null.
                        nextRange = null;
                    }
                }
                if (tryAddRanges(startId, newRangeEndId, true)) {
                    range.endId = endId;
                    range.insert(new ClientRange(startId, endId, client));

                    // found missing link? check if next range can be joined
                    if (nextRange != null) {
                        if (range.endId < nextRange.endId) {
                            // new [3, 6] existing [1, 2] [5, 10]
                            range.endId = nextRange.endId;
                        }
                        range.clients.addAll(nextRange.clients);
                        mRanges.remove(nextRange);
                    }
                    return true;
                } else {
                    return false;   // failed to update radio
                }
            } else if (startId < range.startId) {
                // new [1, x] , existing [5, y]
// test if new range completely precedes this range
// note that [1, 4] and [5, 6] coalesce to [1, 6]
if ((endId + 1) < range.startId) {
                    // new [1, 3] existing [5, 6] non contiguous case
// insert new int range before previous first range
                    if (tryAddRanges(startId, endId, true)) {
mRanges.add(startIndex, new IntRange(startId, endId, client));
return true;
} else {
return false;   // failed to update radio
}
} else if (endId <= range.endId) {
                    // new [1, 4] existing [5, 6]  or  new [1, 1] existing [2, 2]
// extend the start of this range
                    if (tryAddRanges(startId, range.startId - 1, true)) {
range.startId = startId;
range.clients.add(0, new ClientRange(startId, endId, client));
return true;
//Synthetic comment -- @@ -214,8 +274,9 @@
for (int endIndex = startIndex+1; endIndex < len; endIndex++) {
IntRange endRange = mRanges.get(endIndex);
if ((endId + 1) < endRange.startId) {
                            // new [1, 10] existing [2, 3] [14, 15]
// try to add entire new range
                            if (tryAddRanges(startId, endId, true)) {
range.startId = startId;
range.endId = endId;
// insert new ClientRange before existing ranges
//Synthetic comment -- @@ -227,6 +288,7 @@
// about the number of loop iterations, not the value of i.
int joinIndex = startIndex + 1;
for (int i = joinIndex; i < endIndex; i++) {
                                    // new [1, 10] existing [2, 3] [5, 6] [14, 15]
IntRange joinRange = mRanges.get(joinIndex);
range.clients.addAll(joinRange.clients);
mRanges.remove(joinRange);
//Synthetic comment -- @@ -236,9 +298,10 @@
return false;   // failed to update radio
}
} else if (endId <= endRange.endId) {
                            // new [1, 10] existing [2, 3] [5, 15]
// add range from start id to start of last overlapping range,
// values from endRange.startId to endId are already enabled
                            if (tryAddRanges(startId, endRange.startId - 1, true)) {
range.startId = startId;
range.endId = endRange.endId;
// insert new ClientRange before existing ranges
//Synthetic comment -- @@ -261,8 +324,9 @@
}
}

                    // new [1, 10] existing [2, 3]
// endId extends past all existing IntRanges: combine them all together
                    if (tryAddRanges(startId, endId, true)) {
range.startId = startId;
range.endId = endId;
// insert new ClientRange before existing ranges
//Synthetic comment -- @@ -274,6 +338,7 @@
// about the number of loop iterations, not the value of i.
int joinIndex = startIndex + 1;
for (int i = joinIndex; i < len; i++) {
                            // new [1, 10] existing [2, 3] [5, 6]
IntRange joinRange = mRanges.get(joinIndex);
range.clients.addAll(joinRange.clients);
mRanges.remove(joinRange);
//Synthetic comment -- @@ -284,11 +349,14 @@
}
}
} else if ((startId + 1) <= range.endId) {
                // new [2, x] existing [1, 4]
if (endId <= range.endId) {
                    // new [2, 3] existing [1, 4]
// completely contained in existing range; no radio changes
range.insert(new ClientRange(startId, endId, client));
return true;
} else {
                    // new [2, 5] existing [1, 4]
// find last range that can coalesce into the new combined range
int endIndex = startIndex;
for (int testIndex = startIndex+1; testIndex < len; testIndex++) {
//Synthetic comment -- @@ -301,9 +369,10 @@
}
// no adjacent IntRanges to combine
if (endIndex == startIndex) {
                        // new [2, 5] existing [1, 4]
// add range from range.endId+1 to endId,
// values from startId to range.endId are already enabled
                        if (tryAddRanges(range.endId + 1, endId, true)) {
range.endId = endId;
range.insert(new ClientRange(startId, endId, client));
return true;
//Synthetic comment -- @@ -318,17 +387,20 @@
// else enable range from range.endId+1 to endRange.startId-1, because
// values from endRange.startId to endId have already been added.
int newRangeEndId = (endId <= endRange.endId) ? endRange.startId - 1 : endId;
                    // new [2, 10] existing [1, 4] [7, 8] OR
                    // new [2, 10] existing [1, 4] [7, 15]
                    if (tryAddRanges(range.endId + 1, newRangeEndId, true)) {
                        newRangeEndId = (endId <= endRange.endId) ? endRange.endId : endId;
                        range.endId = newRangeEndId;
// insert new ClientRange in place
range.insert(new ClientRange(startId, endId, client));
                        // coalesce range with following ranges up to endIndex
// remove each range after adding its elements, so the index
// of the next range to join is always startIndex+1 (joinIndex).
// i is the index if no elements had been removed: we only care
// about the number of loop iterations, not the value of i.
int joinIndex = startIndex + 1;
                        for (int i = joinIndex; i <= endIndex; i++) {
IntRange joinRange = mRanges.get(joinIndex);
range.clients.addAll(joinRange.clients);
mRanges.remove(joinRange);
//Synthetic comment -- @@ -341,8 +413,9 @@
}
}

        // new [5, 6], existing [1, 3]
// append new range after existing IntRanges
        if (tryAddRanges(startId, endId, true)) {
mRanges.add(new IntRange(startId, endId, client));
return true;
} else {
//Synthetic comment -- @@ -377,12 +450,15 @@
if (crLength == 1) {
ClientRange cr = clients.get(0);
if (cr.startId == startId && cr.endId == endId && cr.client.equals(client)) {
                        // mRange contains only what's enabled.
                        // remove the range from mRange then update the radio
                        mRanges.remove(i);
                        if (updateRanges()) {
return true;
} else {
                            // failed to update radio.  insert back the range
                            mRanges.add(i, range);
                            return false;
}
} else {
return false;   // not found
//Synthetic comment -- @@ -393,25 +469,31 @@
// Save the original start and end id for the original IntRange
// in case the radio update fails and we have to revert it. If the
// update succeeds, we remove the client range and insert the new IntRanges.
                // clients are ordered by startId then by endId, so client with largest endId
                // can be anywhere.  Need to loop thru to find largestEndId.
int largestEndId = Integer.MIN_VALUE;  // largest end identifier found
boolean updateStarted = false;

                // crlength >= 2
for (int crIndex=0; crIndex < crLength; crIndex++) {
ClientRange cr = clients.get(crIndex);
if (cr.startId == startId && cr.endId == endId && cr.client.equals(client)) {
// found the ClientRange to remove, check if it's the last in the list
if (crIndex == crLength - 1) {
if (range.endId == largestEndId) {
                                // remove [2, 5] from [1, 7] [2, 5]
// no channels to remove from radio; return success
clients.remove(crIndex);
return true;
} else {
// disable the channels at the end and lower the end id
                                clients.remove(crIndex);
                                range.endId = largestEndId;
                                if (updateRanges()) {
return true;
} else {
                                    clients.add(crIndex, cr);
                                    range.endId = cr.endId;
return false;
}
}
//Synthetic comment -- @@ -426,12 +508,11 @@
// removing the first ClientRange, so we may need to increase
// the start id of the IntRange.
// We know there are at least two ClientRanges in the list,
                            // because check for just one ClientRangees case is already handled
// so clients.get(1) should always succeed.
int nextStartId = clients.get(1).startId;
if (nextStartId != range.startId) {
updateStarted = true;
rangeCopy.startId = nextStartId;
}
// init largestEndId
//Synthetic comment -- @@ -448,15 +529,14 @@
for (int nextIndex = crIndex + 1; nextIndex < crLength; nextIndex++) {
ClientRange nextCr = clients.get(nextIndex);
if (nextCr.startId > largestEndId + 1) {
                                updateStarted = true;
currentRange.endId = largestEndId;
newRanges.add(currentRange);
currentRange = new IntRange(nextCr);
} else {
                                if (currentRange.endId < nextCr.endId) {
                                    currentRange.endId = nextCr.endId;
                                }
currentRange.clients.add(nextCr);
}
if (nextCr.endId > largestEndId) {
//Synthetic comment -- @@ -466,22 +546,21 @@

// remove any channels between largestEndId and endId
if (largestEndId < endId) {
                            updateStarted = true;
currentRange.endId = largestEndId;
}
newRanges.add(currentRange);

// replace the original IntRange with newRanges
mRanges.remove(i);
mRanges.addAll(i, newRanges);
                        if (updateStarted && !updateRanges()) {
                            // failed to update radio.  revert back mRange.
                            mRanges.removeAll(newRanges);
                            mRanges.add(i, range);
                            return false;
                        }

return true;
} else {
// not the ClientRange to remove; save highest end ID seen so far
//Synthetic comment -- @@ -504,28 +583,8 @@
*/
public boolean updateRanges() {
startUpdate();

        populateAllRanges();
return finishUpdate();
}

//Synthetic comment -- @@ -536,9 +595,12 @@
* @param selected true to enable range, false to disable range
* @return true if successful, false otherwise
*/
    protected boolean tryAddRanges(int startId, int endId, boolean selected) {

startUpdate();
        populateAllRanges();
        // This is the new range to be enabled
        addRange(startId, endId, selected); // adds to mConfigList
return finishUpdate();
}

//Synthetic comment -- @@ -551,6 +613,36 @@
}

/**
     * Called when attempting to add a single range of message identifiers
     * Populate all ranges of message identifiers.
     */
    private void populateAllRanges() {
        Iterator<IntRange> itr = mRanges.iterator();
        // Populate all ranges from mRanges
        while (itr.hasNext()) {
            IntRange currRange = (IntRange) itr.next();
            addRange(currRange.startId, currRange.endId, true);
        }
    }

    /**
     * Called when attempting to add a single range of message identifiers
     * Populate all ranges of message identifiers using clients' ranges.
     */
    private void populateAllClientRanges() {
        int len = mRanges.size();
        for (int i = 0; i < len; i++) {
            IntRange range = mRanges.get(i);

            int clientLen = range.clients.size();
            for (int j=0; j < clientLen; j++) {
                ClientRange nextRange = range.clients.get(j);
                addRange(nextRange.startId, nextRange.endId, true);
            }
        }
    }

    /**
* Called when the list of enabled ranges has changed. This will be
* followed by zero or more calls to {@link #addRange} followed by
* a call to {@link #finishUpdate}.








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/IntRangeManagerTest.java b/tests/telephonytests/src/com/android/internal/telephony/IntRangeManagerTest.java
//Synthetic comment -- index 79dca39..b491a4e 100644

//Synthetic comment -- @@ -81,6 +81,18 @@
flags = 0;
mConfigList.clear();
}

        /** overwrite to not call populateAllRanges, so tryAddRanges called by enable
         * do not populate previously added ranges
         */
        protected boolean tryAddRanges(int startId, int endId, boolean selected) {

            startUpdate();
            //populateAllRanges();
            // This is the new range to be enabled
            addRange(startId, endId, selected); // adds to mConfigList
            return finishUpdate();
        }
}

public void testEmptyRangeManager() {
//Synthetic comment -- @@ -120,10 +132,9 @@
assertEquals("configlist size", 1, testManager.mConfigList.size());
testManager.reset();
assertTrue("disabling range", testManager.disableRange(123, 123, "client1"));
        assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
                testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
//Synthetic comment -- @@ -186,14 +197,13 @@
assertTrue("disabling range 1", testManager.disableRange(100, 200, "client1"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 150, 250, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
testManager.reset();
assertTrue("disabling range 2", testManager.disableRange(150, 250, "client2"));
        assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
                testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
//Synthetic comment -- @@ -219,8 +229,8 @@
assertTrue("disabling range 2", testManager.disableRange(150, 250, "client2"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 100, 200, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
//Synthetic comment -- @@ -229,10 +239,49 @@
SMS_CB_CODE_SCHEME_MAX, true);
testManager.reset();
assertTrue("disabling range 1", testManager.disableRange(100, 200, "client1"));
        assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED, testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
    }

    // new [3, 6]  existing [1, 2] [5, 7]
    public void testOverlappingChannels3() {
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(1, 2, "client1"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 2, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 2", testManager.enableRange(5, 7, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 5, 7, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 3", testManager.enableRange(3, 6, "client3"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 3, 4, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 7, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();

        // test disable
        testManager.reset();
        assertTrue("disabling range", testManager.disableRange(3, 6, "client3"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 2, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 2, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 5, 7, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);

}

public void testMultipleOverlappingChannels() {
//Synthetic comment -- @@ -280,11 +329,13 @@
testManager.reset();
assertTrue("disabling range 1", testManager.disableRange(67, 9999, "client1"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 3, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 12, 500, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 8000, 9998, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(2), 50000, 65535, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
//Synthetic comment -- @@ -298,13 +349,15 @@
testManager.reset();
assertTrue("disabling range 4", testManager.disableRange(12, 500, "client4"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 4, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 25, 75, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 150, 250, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(2), 8000, 9998, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(3), 50000, 65535, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
//Synthetic comment -- @@ -320,9 +373,13 @@
testManager.reset();
assertTrue("disabling range 5", testManager.disableRange(8000, 9998, "client5"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 3, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 25, 75, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 150, 250, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(2), 50000, 65535, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
//Synthetic comment -- @@ -336,9 +393,11 @@
testManager.reset();
assertTrue("disabling range 6", testManager.disableRange(50000, 65535, "client6"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 2, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 25, 75, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 150, 250, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
//Synthetic comment -- @@ -351,8 +410,8 @@
assertTrue("disabling range 2", testManager.disableRange(150, 250, "client2"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 25, 75, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
//Synthetic comment -- @@ -361,10 +420,713 @@
SMS_CB_CODE_SCHEME_MAX, true);
testManager.reset();
assertTrue("disabling range 3", testManager.disableRange(25, 75, "client3"));
        assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
                testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
                testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
    }

    public void testAddSingleDigitChannels() {
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(3, 5, "client1"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 3, 5, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 2", testManager.enableRange(7, 8, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 7, 8, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 3", testManager.enableRange(6, 6, "client3"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 6, 6, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 3, 8, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        // test disable
        testManager.reset();
        assertTrue("disable range 3", testManager.disableRange(6, 6, "client3"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 2, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 3, 5, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 7, 8, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        // enable
        testManager.reset();
        assertTrue("enabling range 4", testManager.enableRange(6, 12, "client3"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 6, 12, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 3, 12, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        // test disable
        testManager.reset();
        assertTrue("disable range 2", testManager.disableRange(7, 8, "client2"));
        assertEquals("flags after test", 0, testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 3, 12, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("disable range 1", testManager.disableRange(3, 5, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 6, 12, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("disable range 4", testManager.disableRange(6, 12, "client3"));
        assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
                testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
    }

    public void testAddSingleDigitChannels2() {
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(2, 2, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 2, 2, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 2", testManager.enableRange(1, 1, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 1, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 2, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
    }

    public void testAddSingleDigitChannels3() {
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(1, 1, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 1, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 2", testManager.enableRange(3, 5, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 3, 5, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 3", testManager.enableRange(2, 2, "client3"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 2, 2, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 5, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        // test disable
        testManager.reset();
        assertTrue("disabling range 1", testManager.disableRange(1, 1, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 2, 5, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
    }

    public void testAddSingleDigitChannels4() {
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(1, 1, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 1, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 2", testManager.enableRange(2, 2, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 2, 2, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 2, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        // test disable
        testManager.reset();
        assertFalse("disabling range 1", testManager.disableRange(1, 2, "client1"));
        assertEquals("flags after test", 0, testManager.flags);
        assertEquals("configlist size",0, testManager.mConfigList.size());
    }

    private void verifyAddChannel(
            TestIntRangeManager testManager, int startId, int endId, String client) {
        testManager.reset();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range", testManager.enableRange(startId, endId, client));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), startId, endId, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
    }

    public void testAddSingleDigitChannels5() {
        TestIntRangeManager testManager = new TestIntRangeManager();
        for (int i = 0; i <= 10; i++) {
            verifyAddChannel(testManager, i, i, "client1");
        }
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 0, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);

        // test disable
        testManager.reset();
        assertTrue("disabling range", testManager.disableRange(2, 2, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 2, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 0, 1, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 3, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("disabling range", testManager.disableRange(9, 9, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 3, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 0, 1, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 3, 8, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(2), 10, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);

        testManager.reset();
        assertTrue("enabling range 2", testManager.enableRange(2, 2, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 2, 2, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 2, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 0, 8, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 10, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
    }

    // new [1, 5] existing [1, 2] [1, 4] [1, 7] [2, 5]
    public void testClientInsert() {
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(1, 2, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 2, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 2", testManager.enableRange(1, 4, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 3, 4, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 3", testManager.enableRange(2, 5, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 5, 5, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 4", testManager.enableRange(1, 7, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 6, 7, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 5", testManager.enableRange(1, 5, "client2"));
        assertEquals("flags after test", 0, testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 7, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        // test disable
        testManager.reset();
        assertTrue("disabling range 5", testManager.disableRange(1, 5, "client2"));
        assertEquals("flags after test", 0, testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
        testManager.reset();
    }

    public void testAddTwoSameChannelsDifferentClient() {
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(1, 5, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 5, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 2", testManager.enableRange(1, 5, "client2"));
        assertEquals("flags after test", 0, testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());

        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 5, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        // now try disabling/deleting "client2" only
        testManager.reset();
        assertTrue("disabling range 1", testManager.disableRange(1, 5, "client2"));
        assertEquals("flags after test", 0, testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 5, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
    }

    public void testAddTwoSameChannelsSameClient() {
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(1, 1, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 1, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 2", testManager.enableRange(1, 1, "client1"));
        assertEquals("flags after test", 0, testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());

        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 1, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        // now try disabling/deleting
        testManager.reset();
        // trying to delete non-existing "client2"
        assertFalse("disabling range 2", testManager.disableRange(1, 1, "client2"));
        assertEquals("flags after test", 0, testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
        testManager.reset();
        // delete "client1"
        assertTrue("disabling range 1", testManager.disableRange(1, 1, "client1"));
        assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
                testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
                testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
    }

    public void testAddTwoChannels2() {
        // test non contiguous case where 2nd range precedes 2nd range
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(200, 250, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 200, 250, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 2", testManager.enableRange(100, 120, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 100, 120, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 2, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 100, 120, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 200, 250, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
    }

    // new [2, 10] existing [1, 4] [7, 15]
    public void testAddThreeChannels() {
        // 3rd range can include first two ranges.  Test combine case.
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(1, 4, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 4, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 2", testManager.enableRange(7, 15, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 7, 15, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 3", testManager.enableRange(2, 10, "client3"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 5, 6, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 15, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);

        //test disable
        testManager.reset();
        assertTrue("disabling range 2", testManager.disableRange(7, 15, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("disabling range 1", testManager.disableRange(1, 4, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 2, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("disabling range 3", testManager.disableRange(2, 10, "client3"));
        assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
                testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
    }

    public void testAddThreeChannels2() {
        // 3rd range can include first two ranges.  Test combine case.
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(2, 3, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 2, 3, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 2", testManager.enableRange(5, 6, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 5, 6, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 3", testManager.enableRange(1, 10, "client3"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);

        //test disable:
        testManager.reset();
        assertTrue("disabling range 3", testManager.disableRange(1, 10, "client3"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 2, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 2, 3, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 5, 6, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);

        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 2, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 2, 3, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 5, 6, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);

    }

    public void testAddChannels() {
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(2, 3, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 2, 3, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 2", testManager.enableRange(5, 6, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 5, 6, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 3", testManager.enableRange(14, 15, "client3"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 14, 15, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 4", testManager.enableRange(1, 10, "client4"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 2, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 14, 15, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        // test disable
        testManager.reset();
        assertTrue("disabling range 4", testManager.disableRange(1, 10, "client4"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 3, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 2, 3, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 5, 6, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(2), 14, 15, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
    }

    public void testAddChannels2() {
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(2, 3, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 2, 3, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 2", testManager.enableRange(5, 15, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 5, 15, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 3", testManager.enableRange(1, 10, "client3"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 4, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 15, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        // test disable
        testManager.reset();
        assertTrue("disabling range 2", testManager.disableRange(5, 15, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("disabling range 1", testManager.disableRange(2, 3, "client1"));
        assertEquals("flags after test", 0, testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
        testManager.reset();
        assertTrue("disabling range 3", testManager.disableRange(1, 10, "client3"));
        assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
                testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
    }


    public void testAddChannels3() {
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(1, 4, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 4, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 2", testManager.enableRange(7, 8, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 7, 8, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 3", testManager.enableRange(2, 10, "client3"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 5, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        // test disable
        testManager.reset();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("disabling range 2", testManager.disableRange(7, 8, "client2"));
        assertEquals("flags after test", 0, testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
    }

    public void testAddChannels4() {
        // new [2, 10] existing [1, 4] [6, 6] [8, 9] [12, 14]
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(1, 4, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 4, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 2", testManager.enableRange(6, 6, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 6, 6, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 3", testManager.enableRange(8, 9, "client3"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 8, 9, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 4", testManager.enableRange(12, 14, "client4"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 12, 14, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("enabling range 5", testManager.enableRange(2, 10, "client5"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 5, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 2, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 12, 14, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);

        // test disable
        testManager.reset();
        assertTrue("disabling range", testManager.disableRange(8, 9, "client3"));
        assertEquals("flags after test", 0, testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 2, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 10, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 12, 14, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("disabling range", testManager.disableRange(2, 10, "client5"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 3, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 4, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 6, 6, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(2), 12, 14, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 3, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 4, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(1), 6, 6, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        checkConfigInfo(testManager.mConfigList.get(2), 12, 14, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
    }

    public void testAddChannels5() {
        // range already enclosed in existing: new [3, 3], [1,3]
        TestIntRangeManager testManager = new TestIntRangeManager();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 1", testManager.enableRange(1, 3, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 3, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertEquals("flags before test", 0, testManager.flags);
        assertTrue("enabling range 2", testManager.enableRange(3, 3, "client1"));
        assertEquals("flags after test", 0, testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 3, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);

        // test disable
        testManager.reset();
        assertTrue("disabling range", testManager.disableRange(3, 3, "client1"));
        assertEquals("flags after test", 0, testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
        testManager.reset();
        assertTrue("updating ranges", testManager.updateRanges());
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 1, 3, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, true);
        testManager.reset();
        assertTrue("disabling range", testManager.disableRange(1, 3, "client1"));
        assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
                testManager.flags);
        assertEquals("configlist size", 0, testManager.mConfigList.size());
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,








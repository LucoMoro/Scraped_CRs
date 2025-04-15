/*Telephony: Fix IntRangeManager

-Fix to populate SmsBroadcastConfigInfo list with all the possible
 range lists upon enable and disable to match previous implementation.
-Fix ClientRange to be ordered by start id, then by end id.
-Fix enableRange to handle missing cases:
 -enable exact same range, i.e. new [x, y], existing [x, y]
 -enable (startId -1) == range.endId, i.e. new [3, y] existing [1, 2]
-Fix where range.endId is not updated correctly
-Fix disableRange to first update mRange then call updateRange.
-Add more unit test cases.

Change-Id:I130899b27cde8ccd574360588904d7ca3b63cd69*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IntRangeManager.java b/telephony/java/com/android/internal/telephony/IntRangeManager.java
//Synthetic comment -- index cc7774d..2d202d1 100644

//Synthetic comment -- @@ -101,7 +101,7 @@
}

/**
         * Insert new ClientRange in order by start id.
* <p>If the new ClientRange is known to be sorted before or after the
* existing ClientRanges, or at a particular index, it can be added
* to the clients array list directly, instead of via this method.
//Synthetic comment -- @@ -111,16 +111,31 @@
*/
void insert(ClientRange range) {
int len = clients.size();
for (int i=0; i < len; i++) {
ClientRange nextRange = clients.get(i);
if (range.startId <= nextRange.startId) {
// ignore duplicate ranges from the same client
if (!range.equals(nextRange)) {
clients.add(i, range);
}
return;
}
}
clients.add(range);    // append to end of list
}
}
//Synthetic comment -- @@ -179,7 +194,7 @@

// empty range list: add the initial IntRange
if (len == 0) {
            if (tryAddSingleRange(startId, endId, true)) {
mRanges.add(new IntRange(startId, endId, client));
return true;
} else {
//Synthetic comment -- @@ -189,20 +204,64 @@

for (int startIndex = 0; startIndex < len; startIndex++) {
IntRange range = mRanges.get(startIndex);
            if (startId < range.startId) {
// test if new range completely precedes this range
// note that [1, 4] and [5, 6] coalesce to [1, 6]
if ((endId + 1) < range.startId) {
// insert new int range before previous first range
                    if (tryAddSingleRange(startId, endId, true)) {
mRanges.add(startIndex, new IntRange(startId, endId, client));
return true;
} else {
return false;   // failed to update radio
}
} else if (endId <= range.endId) {
// extend the start of this range
                    if (tryAddSingleRange(startId, range.startId - 1, true)) {
range.startId = startId;
range.clients.add(0, new ClientRange(startId, endId, client));
return true;
//Synthetic comment -- @@ -214,8 +273,9 @@
for (int endIndex = startIndex+1; endIndex < len; endIndex++) {
IntRange endRange = mRanges.get(endIndex);
if ((endId + 1) < endRange.startId) {
// try to add entire new range
                            if (tryAddSingleRange(startId, endId, true)) {
range.startId = startId;
range.endId = endId;
// insert new ClientRange before existing ranges
//Synthetic comment -- @@ -227,6 +287,7 @@
// about the number of loop iterations, not the value of i.
int joinIndex = startIndex + 1;
for (int i = joinIndex; i < endIndex; i++) {
IntRange joinRange = mRanges.get(joinIndex);
range.clients.addAll(joinRange.clients);
mRanges.remove(joinRange);
//Synthetic comment -- @@ -236,9 +297,10 @@
return false;   // failed to update radio
}
} else if (endId <= endRange.endId) {
// add range from start id to start of last overlapping range,
// values from endRange.startId to endId are already enabled
                            if (tryAddSingleRange(startId, endRange.startId - 1, true)) {
range.startId = startId;
range.endId = endRange.endId;
// insert new ClientRange before existing ranges
//Synthetic comment -- @@ -261,8 +323,9 @@
}
}

// endId extends past all existing IntRanges: combine them all together
                    if (tryAddSingleRange(startId, endId, true)) {
range.startId = startId;
range.endId = endId;
// insert new ClientRange before existing ranges
//Synthetic comment -- @@ -274,6 +337,7 @@
// about the number of loop iterations, not the value of i.
int joinIndex = startIndex + 1;
for (int i = joinIndex; i < len; i++) {
IntRange joinRange = mRanges.get(joinIndex);
range.clients.addAll(joinRange.clients);
mRanges.remove(joinRange);
//Synthetic comment -- @@ -284,11 +348,14 @@
}
}
} else if ((startId + 1) <= range.endId) {
if (endId <= range.endId) {
// completely contained in existing range; no radio changes
range.insert(new ClientRange(startId, endId, client));
return true;
} else {
// find last range that can coalesce into the new combined range
int endIndex = startIndex;
for (int testIndex = startIndex+1; testIndex < len; testIndex++) {
//Synthetic comment -- @@ -301,9 +368,10 @@
}
// no adjacent IntRanges to combine
if (endIndex == startIndex) {
// add range from range.endId+1 to endId,
// values from startId to range.endId are already enabled
                        if (tryAddSingleRange(range.endId + 1, endId, true)) {
range.endId = endId;
range.insert(new ClientRange(startId, endId, client));
return true;
//Synthetic comment -- @@ -318,17 +386,20 @@
// else enable range from range.endId+1 to endRange.startId-1, because
// values from endRange.startId to endId have already been added.
int newRangeEndId = (endId <= endRange.endId) ? endRange.startId - 1 : endId;
                    if (tryAddSingleRange(range.endId + 1, newRangeEndId, true)) {
                        range.endId = endId;
// insert new ClientRange in place
range.insert(new ClientRange(startId, endId, client));
                        // coalesce range with following ranges up to endIndex-1
// remove each range after adding its elements, so the index
// of the next range to join is always startIndex+1 (joinIndex).
// i is the index if no elements had been removed: we only care
// about the number of loop iterations, not the value of i.
int joinIndex = startIndex + 1;
                        for (int i = joinIndex; i < endIndex; i++) {
IntRange joinRange = mRanges.get(joinIndex);
range.clients.addAll(joinRange.clients);
mRanges.remove(joinRange);
//Synthetic comment -- @@ -341,8 +412,9 @@
}
}

// append new range after existing IntRanges
        if (tryAddSingleRange(startId, endId, true)) {
mRanges.add(new IntRange(startId, endId, client));
return true;
} else {
//Synthetic comment -- @@ -377,12 +449,15 @@
if (crLength == 1) {
ClientRange cr = clients.get(0);
if (cr.startId == startId && cr.endId == endId && cr.client.equals(client)) {
                        // disable range in radio then remove the entire IntRange
                        if (tryAddSingleRange(startId, endId, false)) {
                            mRanges.remove(i);
return true;
} else {
                            return false;   // failed to update radio
}
} else {
return false;   // not found
//Synthetic comment -- @@ -393,25 +468,31 @@
// Save the original start and end id for the original IntRange
// in case the radio update fails and we have to revert it. If the
// update succeeds, we remove the client range and insert the new IntRanges.
int largestEndId = Integer.MIN_VALUE;  // largest end identifier found
boolean updateStarted = false;

for (int crIndex=0; crIndex < crLength; crIndex++) {
ClientRange cr = clients.get(crIndex);
if (cr.startId == startId && cr.endId == endId && cr.client.equals(client)) {
// found the ClientRange to remove, check if it's the last in the list
if (crIndex == crLength - 1) {
if (range.endId == largestEndId) {
// no channels to remove from radio; return success
clients.remove(crIndex);
return true;
} else {
// disable the channels at the end and lower the end id
                                if (tryAddSingleRange(largestEndId + 1, range.endId, false)) {
                                    clients.remove(crIndex);
                                    range.endId = largestEndId;
return true;
} else {
return false;
}
}
//Synthetic comment -- @@ -426,12 +507,11 @@
// removing the first ClientRange, so we may need to increase
// the start id of the IntRange.
// We know there are at least two ClientRanges in the list,
// so clients.get(1) should always succeed.
int nextStartId = clients.get(1).startId;
if (nextStartId != range.startId) {
                                startUpdate();
updateStarted = true;
                                addRange(range.startId, nextStartId - 1, false);
rangeCopy.startId = nextStartId;
}
// init largestEndId
//Synthetic comment -- @@ -448,15 +528,14 @@
for (int nextIndex = crIndex + 1; nextIndex < crLength; nextIndex++) {
ClientRange nextCr = clients.get(nextIndex);
if (nextCr.startId > largestEndId + 1) {
                                if (!updateStarted) {
                                    startUpdate();
                                    updateStarted = true;
                                }
                                addRange(largestEndId + 1, nextCr.startId - 1, false);
currentRange.endId = largestEndId;
newRanges.add(currentRange);
currentRange = new IntRange(nextCr);
} else {
currentRange.clients.add(nextCr);
}
if (nextCr.endId > largestEndId) {
//Synthetic comment -- @@ -466,22 +545,21 @@

// remove any channels between largestEndId and endId
if (largestEndId < endId) {
                            if (!updateStarted) {
                                startUpdate();
                                updateStarted = true;
                            }
                            addRange(largestEndId + 1, endId, false);
currentRange.endId = largestEndId;
}
newRanges.add(currentRange);

                        if (updateStarted && !finishUpdate()) {
                            return false;   // failed to update radio
                        }

// replace the original IntRange with newRanges
mRanges.remove(i);
mRanges.addAll(i, newRanges);
return true;
} else {
// not the ClientRange to remove; save highest end ID seen so far
//Synthetic comment -- @@ -504,28 +582,8 @@
*/
public boolean updateRanges() {
startUpdate();
        Iterator<IntRange> iterator = mRanges.iterator();
        if (iterator.hasNext()) {
            IntRange range = iterator.next();
            int start = range.startId;
            int end = range.endId;
            // accumulate ranges of [startId, endId]
            while (iterator.hasNext()) {
                IntRange nextNode = iterator.next();
                // [startIdA, endIdA], [endIdA + 1, endIdB] -> [startIdA, endIdB]
                if (nextNode.startId <= (end + 1)) {
                    if (nextNode.endId > end) {
                        end = nextNode.endId;
                    }
                } else {
                    addRange(start, end, true);
                    start = nextNode.startId;
                    end = nextNode.endId;
                }
            }
            // add final range
            addRange(start, end, true);
        }
return finishUpdate();
}

//Synthetic comment -- @@ -536,9 +594,12 @@
* @param selected true to enable range, false to disable range
* @return true if successful, false otherwise
*/
    private boolean tryAddSingleRange(int startId, int endId, boolean selected) {
startUpdate();
        addRange(startId, endId, selected);
return finishUpdate();
}

//Synthetic comment -- @@ -551,6 +612,36 @@
}

/**
* Called when the list of enabled ranges has changed. This will be
* followed by zero or more calls to {@link #addRange} followed by
* a call to {@link #finishUpdate}.








//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/com/android/internal/telephony/IntRangeManagerTest.java b/telephony/tests/telephonytests/src/com/android/internal/telephony/IntRangeManagerTest.java
//Synthetic comment -- index 79dca39..9901236 100644

//Synthetic comment -- @@ -81,6 +81,18 @@
flags = 0;
mConfigList.clear();
}
}

public void testEmptyRangeManager() {
//Synthetic comment -- @@ -120,10 +132,9 @@
assertEquals("configlist size", 1, testManager.mConfigList.size());
testManager.reset();
assertTrue("disabling range", testManager.disableRange(123, 123, "client1"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 123, 123, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, false);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
//Synthetic comment -- @@ -186,14 +197,13 @@
assertTrue("disabling range 1", testManager.disableRange(100, 200, "client1"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 100, 149, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, false);
testManager.reset();
assertTrue("disabling range 2", testManager.disableRange(150, 250, "client2"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 150, 250, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, false);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
//Synthetic comment -- @@ -219,8 +229,8 @@
assertTrue("disabling range 2", testManager.disableRange(150, 250, "client2"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 201, 250, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, false);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
//Synthetic comment -- @@ -229,10 +239,49 @@
SMS_CB_CODE_SCHEME_MAX, true);
testManager.reset();
assertTrue("disabling range 1", testManager.disableRange(100, 200, "client1"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 100, 200, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, false);
}

public void testMultipleOverlappingChannels() {
//Synthetic comment -- @@ -280,11 +329,13 @@
testManager.reset();
assertTrue("disabling range 1", testManager.disableRange(67, 9999, "client1"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 2, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 501, 7999, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, false);
        checkConfigInfo(testManager.mConfigList.get(1), 9999, 9999, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, false);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
//Synthetic comment -- @@ -298,13 +349,15 @@
testManager.reset();
assertTrue("disabling range 4", testManager.disableRange(12, 500, "client4"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 3, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 12, 24, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, false);
        checkConfigInfo(testManager.mConfigList.get(1), 76, 149, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, false);
        checkConfigInfo(testManager.mConfigList.get(2), 251, 500, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, false);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
//Synthetic comment -- @@ -320,9 +373,13 @@
testManager.reset();
assertTrue("disabling range 5", testManager.disableRange(8000, 9998, "client5"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 8000, 9998, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, false);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
//Synthetic comment -- @@ -336,9 +393,11 @@
testManager.reset();
assertTrue("disabling range 6", testManager.disableRange(50000, 65535, "client6"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 50000, 65535, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, false);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
//Synthetic comment -- @@ -351,8 +410,8 @@
assertTrue("disabling range 2", testManager.disableRange(150, 250, "client2"));
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 150, 250, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, false);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
//Synthetic comment -- @@ -361,14 +420,673 @@
SMS_CB_CODE_SCHEME_MAX, true);
testManager.reset();
assertTrue("disabling range 3", testManager.disableRange(25, 75, "client3"));
        assertEquals("flags after test", ALL_FLAGS_SET, testManager.flags);
        assertEquals("configlist size", 1, testManager.mConfigList.size());
        checkConfigInfo(testManager.mConfigList.get(0), 25, 75, SMS_CB_CODE_SCHEME_MIN,
                SMS_CB_CODE_SCHEME_MAX, false);
testManager.reset();
assertTrue("updating ranges", testManager.updateRanges());
assertEquals("flags after test", FLAG_START_UPDATE_CALLED | FLAG_FINISH_UPDATE_CALLED,
testManager.flags);
assertEquals("configlist size", 0, testManager.mConfigList.size());
}
}








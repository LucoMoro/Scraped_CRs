/*Fix issue with invisible recurring appointments

Fixes that recurring appointments goes invisible
when hit by the failsafe limit.

There is still a hard limit in RecurrenceProcessor,
but at least this limit is applied on a per-event
basis, meaning that it's always possible to create
new recurring appointments without them going invisible.

Change-Id:I6c9e1e10db78e1b73ea9f2b7104621fbbd906452*/




//Synthetic comment -- diff --git a/src/com/android/providers/calendar/RecurrenceProcessor.java b/src/com/android/providers/calendar/RecurrenceProcessor.java
//Synthetic comment -- index ecac106..68c0be1 100644

//Synthetic comment -- @@ -748,7 +748,9 @@
while (true) {
int monthIndex = 0;
if (failsafe++ > MAX_ALLOWED_ITERATIONS) { // Give up after about 1 second of processing
                        Log.w(TAG, "Recurrence processing stuck with r=" + r + " rangeStart="
                                + rangeStartDateValue + " rangeEnd=" + rangeEndDateValue);
                        break;
}

unsafeNormalize(iterator);








//Synthetic comment -- diff --git a/tests/src/com/android/providers/calendar/RRuleTest.java b/tests/src/com/android/providers/calendar/RRuleTest.java
//Synthetic comment -- index 45d1db2..2044d25 100644

//Synthetic comment -- @@ -131,11 +131,14 @@
// Infinite loop, bug 1662110
@MediumTest
public void testFrequencyLimits() throws Exception {
        // Check that finish in a reasonable time
        final long tenSeconds = 10000;
        long start = System.currentTimeMillis();
        runRecurrenceIteratorTest("RRULE:FREQ=SECONDLY;BYSECOND=0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,"
            + "15,16,17,18,19,20,21,22,23,24,25,26,27,28,29," + "30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,"
            + "45,46,47,48,49,50,51,52,53,54,55,56,57,58,59", "20000101", 1, "20000101");
        if (System.currentTimeMillis() - start > tenSeconds) {
fail("Don't do that");
}
}









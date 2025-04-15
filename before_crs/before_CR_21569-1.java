/*Fix EventLogTest Flakiness

Issue 14896

The test was getting the current time and collecting events since
that time. However, the times in the event logs are usually recorded
faster, so events could get dropped. As a result, there were sleep
calls that needed to be increased and adjusted for various devices.

Fix these tests to gather events by looking for an event with a
certain payload instead. This should be more reliable, since the
payload is the current time from the test. This doesn't test the
event times as much though.

Change-Id:I4f6c9ad3ced39f2aa03c8c01b01b27f59948f8d0*/
//Synthetic comment -- diff --git a/tests/tests/util/src/android/util/cts/EventLogTest.java b/tests/tests/util/src/android/util/cts/EventLogTest.java
//Synthetic comment -- index c803174..318b010 100644

//Synthetic comment -- @@ -18,9 +18,12 @@

import android.os.Process;
import android.util.EventLog;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;

//Synthetic comment -- @@ -30,13 +33,14 @@
private static final int E_TAG = 2718;

public void testWriteEvent() throws Exception {
        long t0 = getTime();
EventLog.writeEvent(ANSWER_TAG, 12345);
EventLog.writeEvent(ANSWER_TAG, 23456L);
EventLog.writeEvent(ANSWER_TAG, "Test");
EventLog.writeEvent(ANSWER_TAG, 12345, 23456L, "Test");

        ArrayList<EventLog.Event> events = getEventsSince(t0, new int[] {ANSWER_TAG});
assertEquals(4, events.size());
assertEquals(ANSWER_TAG, events.get(0).getTag());
assertEquals(12345, events.get(0).getData());
//Synthetic comment -- @@ -57,7 +61,8 @@
Object[] longArray = new Object[1000];
for (int i = 0; i < 1000; i++) longArray[i] = 12345;

        long t0 = getTime();
EventLog.writeEvent(ANSWER_TAG, longString.toString());
EventLog.writeEvent(ANSWER_TAG, "hi", longString.toString());
EventLog.writeEvent(ANSWER_TAG, 12345, longString.toString());
//Synthetic comment -- @@ -65,7 +70,7 @@
EventLog.writeEvent(ANSWER_TAG, longString.toString(), longString.toString());
EventLog.writeEvent(ANSWER_TAG, longArray);

        ArrayList<EventLog.Event> events = getEventsSince(t0, new int[] {ANSWER_TAG});
assertEquals(6, events.size());

// subtract: log header, type byte, final newline
//Synthetic comment -- @@ -105,11 +110,12 @@
}

public void testWriteNullEvent() throws Exception {
        long t0 = getTime();
EventLog.writeEvent(ANSWER_TAG, (String) null);
EventLog.writeEvent(ANSWER_TAG, 12345, (String) null);

        ArrayList<EventLog.Event> events = getEventsSince(t0, new int[] {ANSWER_TAG});
assertEquals(2, events.size());
assertEquals("NULL", events.get(0).getData());

//Synthetic comment -- @@ -120,31 +126,65 @@
}

public void testReadEvents() throws Exception {
        long t0 = getTime();
        EventLog.writeEvent(ANSWER_TAG, 0);
        long t1 = getTime();
        EventLog.writeEvent(PI_TAG, "1");
        long t2 = getTime();
        EventLog.writeEvent(E_TAG, 2);
        long t3 = getTime();

        // Exclude E_TAG
        ArrayList<EventLog.Event> events = getEventsSince(t0, new int[] {ANSWER_TAG, PI_TAG});
assertEquals(2, events.size());

        assertEquals(Process.myPid(), events.get(0).getProcessId());
        assertEquals(Process.myTid(), events.get(0).getThreadId());
        assertTrue(events.get(0).getTimeNanos() >= t0 * 1000000L);
        assertTrue(events.get(0).getTimeNanos() <= t1 * 1000000L);
        assertEquals(ANSWER_TAG, events.get(0).getTag());
        assertEquals(0, events.get(0).getData());

        assertEquals(Process.myPid(), events.get(1).getProcessId());
        assertEquals(Process.myTid(), events.get(1).getThreadId());
        assertTrue(events.get(1).getTimeNanos() >= t1 * 1000000L);
        assertTrue(events.get(1).getTimeNanos() <= t2 * 1000000L);
        assertEquals(PI_TAG, events.get(1).getTag());
        assertEquals("1", events.get(1).getData());
}

public void testGetTagName() throws Exception {
//Synthetic comment -- @@ -160,23 +200,4 @@
assertEquals(E_TAG, EventLog.getTagCode("e"));
assertEquals(-1, EventLog.getTagCode("does_not_exist"));
}

    private long getTime() throws InterruptedException {
        // The precision of currentTimeMillis is poor compared to event timestamps
        Thread.sleep(20);
        return System.currentTimeMillis() - 10;
    }

    private ArrayList<EventLog.Event> getEventsSince(long since, int[] tags) throws IOException {
        ArrayList<EventLog.Event> tmp = new ArrayList<EventLog.Event>();
        EventLog.readEvents(tags, tmp);

        ArrayList<EventLog.Event> out = new ArrayList<EventLog.Event>();
        for (EventLog.Event event : tmp) {
            if (event.getTimeNanos() / 1000000 >= since) {
                out.add(event);
            }
        }
        return out;
    }
}








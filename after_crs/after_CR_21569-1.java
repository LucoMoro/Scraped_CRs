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
import android.util.EventLog.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

//Synthetic comment -- @@ -30,13 +33,14 @@
private static final int E_TAG = 2718;

public void testWriteEvent() throws Exception {
        long markerData = System.currentTimeMillis();
        EventLog.writeEvent(ANSWER_TAG, markerData);
EventLog.writeEvent(ANSWER_TAG, 12345);
EventLog.writeEvent(ANSWER_TAG, 23456L);
EventLog.writeEvent(ANSWER_TAG, "Test");
EventLog.writeEvent(ANSWER_TAG, 12345, 23456L, "Test");

        List<EventLog.Event> events = getEventsAfterMarker(markerData, ANSWER_TAG);
assertEquals(4, events.size());
assertEquals(ANSWER_TAG, events.get(0).getTag());
assertEquals(12345, events.get(0).getData());
//Synthetic comment -- @@ -57,7 +61,8 @@
Object[] longArray = new Object[1000];
for (int i = 0; i < 1000; i++) longArray[i] = 12345;

        Long markerData = System.currentTimeMillis();
        EventLog.writeEvent(ANSWER_TAG, markerData);
EventLog.writeEvent(ANSWER_TAG, longString.toString());
EventLog.writeEvent(ANSWER_TAG, "hi", longString.toString());
EventLog.writeEvent(ANSWER_TAG, 12345, longString.toString());
//Synthetic comment -- @@ -65,7 +70,7 @@
EventLog.writeEvent(ANSWER_TAG, longString.toString(), longString.toString());
EventLog.writeEvent(ANSWER_TAG, longArray);

        List<Event> events = getEventsAfterMarker(markerData, ANSWER_TAG);
assertEquals(6, events.size());

// subtract: log header, type byte, final newline
//Synthetic comment -- @@ -105,11 +110,12 @@
}

public void testWriteNullEvent() throws Exception {
        Long markerData = System.currentTimeMillis();
        EventLog.writeEvent(ANSWER_TAG, markerData);
EventLog.writeEvent(ANSWER_TAG, (String) null);
EventLog.writeEvent(ANSWER_TAG, 12345, (String) null);

        List<EventLog.Event> events = getEventsAfterMarker(markerData, ANSWER_TAG);
assertEquals(2, events.size());
assertEquals("NULL", events.get(0).getData());

//Synthetic comment -- @@ -120,31 +126,65 @@
}

public void testReadEvents() throws Exception {
        Long markerData = System.currentTimeMillis();
        EventLog.writeEvent(ANSWER_TAG, markerData);

        Long data0 = markerData + 1;
        EventLog.writeEvent(ANSWER_TAG, data0);

        Long data1 = data0 + 1;
        EventLog.writeEvent(PI_TAG, data1);

        Long data2 = data1 + 1;
        EventLog.writeEvent(E_TAG, data2);

        List<Event> events = getEventsAfterMarker(markerData, ANSWER_TAG, PI_TAG, E_TAG);
        assertEquals(3, events.size());
        assertEvent(events.get(0), ANSWER_TAG, data0);
        assertEvent(events.get(1), PI_TAG, data1);
        assertEvent(events.get(2), E_TAG, data2);

        events = getEventsAfterMarker(markerData, ANSWER_TAG, E_TAG);
assertEquals(2, events.size());
        assertEvent(events.get(0), ANSWER_TAG, data0);
        assertEvent(events.get(1), E_TAG, data2);

        events = getEventsAfterMarker(markerData, ANSWER_TAG);
        assertEquals(1, events.size());
        assertEvent(events.get(0), ANSWER_TAG, data0);
    }

    /** Return elements after and the event that has the marker data and matching tag. */
    private List<Event> getEventsAfterMarker(Object marker, int... tags) throws IOException {
        List<Event> events = new ArrayList<Event>();
        EventLog.readEvents(tags, events);

        for (Iterator<Event> itr = events.iterator(); itr.hasNext(); ) {
            Event event = itr.next();
            itr.remove();
            if (marker.equals(event.getData())) {
                break;
            }
        }

        assertEventTimes(events);

        return events;
    }

    private void assertEvent(Event event, int expectedTag, Object expectedData) {
        assertEquals(Process.myPid(), event.getProcessId());
        assertEquals(Process.myTid(), event.getThreadId());
        assertEquals(expectedTag, event.getTag());
        assertEquals(expectedData, event.getData());
    }

    private void assertEventTimes(List<Event> events) {
        for (int i = 0; i + 1 < events.size(); i++) {
            long time = events.get(i).getTimeNanos();
            long nextTime = events.get(i).getTimeNanos();
            assertTrue(time <= nextTime);
        }
}

public void testGetTagName() throws Exception {
//Synthetic comment -- @@ -160,23 +200,4 @@
assertEquals(E_TAG, EventLog.getTagCode("e"));
assertEquals(-1, EventLog.getTagCode("does_not_exist"));
}
}








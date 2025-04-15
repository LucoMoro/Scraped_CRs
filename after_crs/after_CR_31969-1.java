/*Fix testSetToNow0 test

setToNow returns the current time with a precision of one second.
currentTimeMillis returns the current time with a precsion of 1 milli-second
These values may be up to 999ms apart if currentTimeMillis is called before
setToNow

Signed-off-by: Chris Dearman <chris@mips.com>*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/format/cts/TimeTest.java b/tests/tests/text/src/android/text/format/cts/TimeTest.java
//Synthetic comment -- index 0fb3f2a..50c51da 100644

//Synthetic comment -- @@ -627,10 +627,10 @@
)
public void testSetToNow0() throws Exception {
Time t = new Time(Time.TIMEZONE_UTC);
long currentTime = System.currentTimeMillis();
        t.setToNow();
long time = t.toMillis(false);
        assertEquals(currentTime, time, 999);
}

@TestTargetNew(








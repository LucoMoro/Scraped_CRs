/*Improve the assertion detail in flaky ThreadsTest.

Bug: 6595290
Change-Id:I2a0df0e5cf44ad7d468b1957694720a49a481459*/




//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/org/apache/harmony/kernel/dalvik/ThreadsTest.java b/luni/src/test/java/tests/api/org/apache/harmony/kernel/dalvik/ThreadsTest.java
//Synthetic comment -- index 286d4ab..bdc580d 100644

//Synthetic comment -- @@ -187,11 +187,10 @@
if (! completed) {
try {
wait(maxWaitMillis);
                    } catch (InterruptedException ignored) {
}
if (! completed) {
                        Assert.fail("parker hung for more than " + maxWaitMillis + " ms");
}
}









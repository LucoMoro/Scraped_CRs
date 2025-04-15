/*Fix AnimationUtilsTest failure due to the timing dependency

testCurrentAnimationTimeMillis tests that the animation timestamp
is incremented during at most 1000 iterations, which is not guaranteed
to happen.

Loosen the condition by adding a 1 ms sleep between the iterations.  This
extends the wait from a very small unspecified number to 1000 ms.

Change-Id:I8aedd6ac92005de310bc7a58042fe1822e6104dc*/
//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/animation/cts/AnimationUtilsTest.java b/tests/tests/view/src/android/view/animation/cts/AnimationUtilsTest.java
//Synthetic comment -- index e2fe6f35..cddbfdf 100644

//Synthetic comment -- @@ -91,6 +91,10 @@
long time2 = 0L;
for (int i = 0; i < 1000 && time1 >= time2; i++) {
time2 = AnimationUtils.currentAnimationTimeMillis();
assertTrue(time2 > 0);
}
assertTrue(time2 > time1);








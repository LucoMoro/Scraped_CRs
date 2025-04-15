/*Fix some TimerTest flakiness.

There's more flakiness around here, but this is a start. I'm not sure
what we should do with the tests that test how many times a task
has been executed in a given period, so I've left them for now.

Bug: 7766472
Bug: 7745085
Bug: 7740706
Bug: 6896554
Change-Id:I84d8aa036c075024d5cefdad050884762eab50a3*/




//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/util/TimerTest.java b/luni/src/test/java/tests/api/java/util/TimerTest.java
//Synthetic comment -- index 26d88ba..b09fa45 100644

//Synthetic comment -- @@ -91,6 +91,15 @@
}
}

    private void awaitRun(TimerTestTask task) {
        while (task.wasRun() == 0) {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
            }
        }
    }

/**
* java.util.Timer#Timer(boolean)
*/
//Synthetic comment -- @@ -101,14 +110,7 @@
t = new Timer(true);
TimerTestTask testTask = new TimerTestTask();
t.schedule(testTask, 200);
            awaitRun(testTask);
t.cancel();
} finally {
if (t != null)
//Synthetic comment -- @@ -127,14 +129,7 @@
t = new Timer();
TimerTestTask testTask = new TimerTestTask();
t.schedule(testTask, 200);
            awaitRun(testTask);
t.cancel();
} finally {
if (t != null)
//Synthetic comment -- @@ -153,13 +148,7 @@
t = new Timer("test_ConstructorSZThread", true);
TimerTestTask testTask = new TimerTestTask();
t.schedule(testTask, 200);
            awaitRun(testTask);
t.cancel();
} finally {
if (t != null)
//Synthetic comment -- @@ -191,13 +180,7 @@
t = new Timer("test_ConstructorSThread");
TimerTestTask testTask = new TimerTestTask();
t.schedule(testTask, 200);
            awaitRun(testTask);
t.cancel();
} finally {
if (t != null)
//Synthetic comment -- @@ -236,14 +219,7 @@
t = new Timer();
testTask = new TimerTestTask();
t.schedule(testTask, 100, 500);
            awaitRun(testTask);
t.cancel();
synchronized (sync) {
try {
//Synthetic comment -- @@ -258,14 +234,7 @@
t = new Timer();
testTask = new TimerTestTask();
t.schedule(testTask, 100, 500);
            awaitRun(testTask);
t.cancel();
t.cancel();
t.cancel();
//Synthetic comment -- @@ -446,12 +415,7 @@
testTask = new TimerTestTask();
d = new Date(System.currentTimeMillis() + 200);
t.schedule(testTask, d);
            awaitRun(testTask);
t.cancel();

// Ensure multiple tasks are run
//Synthetic comment -- @@ -568,12 +532,7 @@
t = new Timer();
testTask = new TimerTestTask();
t.schedule(testTask, 200);
            awaitRun(testTask);
t.cancel();

// Ensure multiple tasks are run








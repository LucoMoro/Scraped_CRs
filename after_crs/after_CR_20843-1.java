/*Fix Broken MessageQueueTests

Rewrite the addIdleHandler and removeIdleHandler tests to be more
clear.

Change-Id:I19547149d4cd85c535099ff5ca08703045ef73eb*/




//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/MessageQueueTest.java b/tests/tests/os/src/android/os/cts/MessageQueueTest.java
//Synthetic comment -- index 95441ae..c7b549a 100644

//Synthetic comment -- @@ -16,10 +16,7 @@

package android.os.cts;

import dalvik.annotation.TestTargetClass;

import android.os.Handler;
import android.os.HandlerThread;
//Synthetic comment -- @@ -30,77 +27,92 @@
import android.os.MessageQueue.IdleHandler;
import android.test.AndroidTestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestTargetClass(MessageQueue.class)
public class MessageQueueTest extends AndroidTestCase {

private static final long TIMEOUT = 1000;

    public void testAddIdleHandler() throws InterruptedException {
        TestLooperThread looperThread = new TestLooperThread(Test.ADD_IDLE_HANDLER);
        looperThread.start();

try {
            if (!looperThread.hasIdleHandlerBeenCalled()) {
                fail("IdleHandler#queueIdle was NOT called: " + looperThread.getTestProgress());
            }
        } finally {
            assertTrue("The looper should have been running.", looperThread.quit());
}
}

    public void testRemoveIdleHandler() throws InterruptedException {
        TestLooperThread looperThread = new TestLooperThread(Test.REMOVE_IDLE_HANDLER);
        looperThread.start();

        try {
            if (looperThread.hasIdleHandlerBeenCalled()) {
                fail("IdleHandler#queueIdle was called: " + looperThread.getTestProgress());
            }
        } finally {
            assertTrue("The looper should have been running.", looperThread.quit());
        }
    }

    private enum Test {ADD_IDLE_HANDLER, REMOVE_IDLE_HANDLER};

    /**
     * {@link HandlerThread} that adds or removes an idle handler depending on the {@link Test}
     * given. It uses a {@link CountDownLatch} with an initial count of 2. The first count down
     * occurs right before the looper's run thread had started running. The final count down
     * occurs when the idle handler was executed. Tests can call {@link #hasIdleHandlerBeenCalled()}
     * to see if the countdown reached to 0 or not.
     */
    private static class TestLooperThread extends HandlerThread {

        private final Test mTestMode;

        private final CountDownLatch mIdleLatch = new CountDownLatch(2);

        TestLooperThread(Test testMode) {
            super("TestLooperThread");
            mTestMode = testMode;
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();

            IdleHandler idleHandler = new IdleHandler() {
                public boolean queueIdle() {
                    mIdleLatch.countDown();
                    return false;
                }
            };

            if (mTestMode == Test.ADD_IDLE_HANDLER) {
                Looper.myQueue().addIdleHandler(idleHandler);
            } else {
                Looper.myQueue().addIdleHandler(idleHandler);
                Looper.myQueue().removeIdleHandler(idleHandler);
            }
        }

        @Override
        public void run() {
            mIdleLatch.countDown();
            super.run();
        }

        public boolean hasIdleHandlerBeenCalled() throws InterruptedException {
            return mIdleLatch.await(TIMEOUT, TimeUnit.MILLISECONDS);
        }

        public long getTestProgress() {
            return mIdleLatch.getCount();
        }
}

/**
//Synthetic comment -- @@ -155,68 +167,6 @@
}

/**
* Helper class used to test sending message to message queue.
*/
private class OrderTestHelper {








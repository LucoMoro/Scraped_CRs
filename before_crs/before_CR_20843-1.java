/*Fix Broken MessageQueueTests

Rewrite the addIdleHandler and removeIdleHandler tests to be more
clear.

Change-Id:I19547149d4cd85c535099ff5ca08703045ef73eb*/
//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/MessageQueueTest.java b/tests/tests/os/src/android/os/cts/MessageQueueTest.java
//Synthetic comment -- index 95441ae..c7b549a 100644

//Synthetic comment -- @@ -16,10 +16,7 @@

package android.os.cts;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;

import android.os.Handler;
import android.os.HandlerThread;
//Synthetic comment -- @@ -30,77 +27,92 @@
import android.os.MessageQueue.IdleHandler;
import android.test.AndroidTestCase;

@TestTargetClass(MessageQueue.class)
public class MessageQueueTest extends AndroidTestCase {

    private boolean mResult;
    // Action flag: true means addIdleHanlder, false means removeIdleHanlder
    private boolean mActionFlag;
private static final long TIMEOUT = 1000;
    private static final long INTERVAL = 50;
    private IdleHandler mIdleHandler = new IdleHandler() {
        public boolean queueIdle() {
            MessageQueueTest.this.mResult = true;
            return true;
        }
    };

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mResult = false;
    }

    /**
     * After calling addIdleHandler (called by MessageQueueTestHelper#doTest), the size of
     * idleHanlder list is not 0 (before calling addIdleHandler, there is no idleHanlder in
     * the test looper we started, that means no idleHanlder with flag mResult), and in doTest,
     * we start a looper, which will queueIdle (Looper.loop()) if idleHanlder list has element,
     * then mResult will be set true. It can make sure addIdleHandler works. If no idleHanlder
     * with flag mResult, mResult will be false.
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "addIdleHandler",
        args = {android.os.MessageQueue.IdleHandler.class}
    )
    @BrokenTest("needs investigation")
    public void testAddIdleHandler() throws RuntimeException, InterruptedException {
try {
            Looper.myQueue().addIdleHandler(null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // expected
}
        // If mActionFlag is true, doTest will call addIdleHandler
        mActionFlag = true;
        mResult = false;
        MessageQueueTestHelper tester = new MessageQueueTestHelper();
        tester.doTest(TIMEOUT, INTERVAL);

        tester.quit();
        assertTrue(mResult);
}

    /**
     * In this test method, at the beginning of the LooperThread, we call addIdleHandler then
     * removeIdleHandler, there should be no element in idleHanlder list. So the Looper.loop()
     * will not call queueIdle(), mResult will not be set true.
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "removeIdleHandler",
        args = {android.os.MessageQueue.IdleHandler.class}
    )
    @BrokenTest("needs investigation")
    public void testRemoveIdleHandler() throws RuntimeException, InterruptedException {
        mActionFlag = false;
        mResult = false;
        MessageQueueTestHelper tester = new MessageQueueTestHelper();
        tester.doTest(TIMEOUT, INTERVAL);

        tester.quit();
        assertFalse(mResult);
}

/**
//Synthetic comment -- @@ -155,68 +167,6 @@
}

/**
     * Helper class used to test addIdleHandler, removeIdleHandler
     */
    private class MessageQueueTestHelper {

        private boolean mDone;
        private Looper mLooper;

        public void doTest(long timeout, long interval) throws InterruptedException {
            (new LooperThread()).start();
            synchronized (this) {
                long now = System.currentTimeMillis();
                long endTime = now + timeout;
                // Wait and frequently check if mDone is set.
                while (!mDone && now < endTime) {
                    wait(interval);
                    now = System.currentTimeMillis();
                }
            }
            mLooper.quit();
            if (!mDone) {
                throw new RuntimeException("test timed out");
            }
        }

        private class LooperThread extends HandlerThread {
            public LooperThread() {
                super("MessengeQueueLooperThread");
            }

            public void onLooperPrepared() {
                mLooper = getLooper();
                if (mActionFlag) {
                    // If mActionFlag is true, just addIdleHandler, and
                    // Looper.loop() will set mResult true.
                    Looper.myQueue().addIdleHandler(mIdleHandler);
                } else {
                    // If mActionFlag is false, addIdleHandler and remove it, then Looper.loop()
                    // will not set mResult true because the idleHandler list is empty.
                    Looper.myQueue().addIdleHandler(mIdleHandler);
                    Looper.myQueue().removeIdleHandler(mIdleHandler);
                }
            }

            @Override
            public void run() {
                super.run();
                synchronized (MessageQueueTestHelper.this) {
                    mDone = true;
                    MessageQueueTestHelper.this.notifyAll();
                }
            }
        }

        public void quit() {
            synchronized (this) {
                mDone = true;
                notifyAll();
            }
        }
    }

    /**
* Helper class used to test sending message to message queue.
*/
private class OrderTestHelper {








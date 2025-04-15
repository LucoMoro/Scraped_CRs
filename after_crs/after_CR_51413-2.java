/*Move core logcat functionality from ddmuilib to ddmlib

Requires Change-IdIe120f978e5c5646e086ec999c9ef5027b724cc7ain tools/base

Change-Id:Ib436fa6b70ba49e48b2e7c974094c27b77c9fbb9*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/ILogCatBufferChangeListener.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/ILogCatBufferChangeListener.java
//Synthetic comment -- index 1a547c7..2804629 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ddmuilib.logcat;

import com.android.ddmlib.logcat.LogCatMessage;

import java.util.List;

/**








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/ILogCatMessageSelectionListener.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/ILogCatMessageSelectionListener.java
//Synthetic comment -- index 6e814b0..728b518 100644

//Synthetic comment -- @@ -15,6 +15,8 @@
*/
package com.android.ddmuilib.logcat;

import com.android.ddmlib.logcat.LogCatMessage;

/**
* Classes interested in listening to user selection of logcat
* messages should implement this interface.








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilter.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilter.java
//Synthetic comment -- index 7bdd98a..3024978 100644

//Synthetic comment -- @@ -16,6 +16,7 @@
package com.android.ddmuilib.logcat;

import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.logcat.LogCatMessage;

import java.util.ArrayList;
import java.util.List;








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessage.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessage.java
deleted file mode 100644
//Synthetic comment -- index aea4ead..0000000

//Synthetic comment -- @@ -1,96 +0,0 @@








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageList.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageList.java
//Synthetic comment -- index 080dbc1..c5cd548 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ddmuilib.logcat;

import com.android.ddmlib.logcat.LogCatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageParser.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageParser.java
deleted file mode 100644
//Synthetic comment -- index f1a5816..0000000

//Synthetic comment -- @@ -1,100 +0,0 @@








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index 4f27f02..c978de7 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ddmlib.DdmConstants;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.logcat.LogCatMessage;
import com.android.ddmuilib.AbstractBufferFindTarget;
import com.android.ddmuilib.FindDialog;
import com.android.ddmuilib.ITableFocusListener;








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatReceiver.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatReceiver.java
//Synthetic comment -- index 8d4e7fe..a85cd03 100644

//Synthetic comment -- @@ -17,10 +17,10 @@
package com.android.ddmuilib.logcat;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.logcat.LogCatListener;
import com.android.ddmlib.logcat.LogCatMessage;
import com.android.ddmlib.logcat.LogCatReceiverTask;

import org.eclipse.jface.preference.IPreferenceStore;

//Synthetic comment -- @@ -33,18 +33,15 @@
* A class to monitor a device for logcat messages. It stores the received
* log messages in a circular buffer.
*/
public final class LogCatReceiver implements LogCatListener {
private static LogCatMessage DEVICE_DISCONNECTED_MESSAGE =
new LogCatMessage(LogLevel.ERROR, "", "", "",
"", "", "Device disconnected");

private LogCatMessageList mLogMessages;
private IDevice mCurrentDevice;
    private LogCatReceiverTask mLogCatReceiverTask;
private Set<ILogCatBufferChangeListener> mLogCatMessageListeners;
private IPreferenceStore mPrefStore;

/**
//Synthetic comment -- @@ -60,7 +57,6 @@
mPrefStore = prefStore;

mLogCatMessageListeners = new HashSet<ILogCatBufferChangeListener>();
mLogMessages = new LogCatMessageList(getFifoSize());

startReceiverThread();
//Synthetic comment -- @@ -70,13 +66,14 @@
* Stop receiving messages from currently active device.
*/
public void stop() {
        if (mLogCatReceiverTask != null) {
/* stop the current logcat command */
            mLogCatReceiverTask.removeLogCatListener(this);
            mLogCatReceiverTask.stop();
            mLogCatReceiverTask = null;

// add a message to the log indicating that the device has been disconnected.
            log(Collections.singletonList(DEVICE_DISCONNECTED_MESSAGE));
}

mCurrentDevice = null;
//Synthetic comment -- @@ -88,85 +85,26 @@
}

private void startReceiverThread() {
        if (mCurrentDevice == null) {
            return;
        }

        mLogCatReceiverTask = new LogCatReceiverTask(mCurrentDevice);
        mLogCatReceiverTask.addLogCatListener(this);

        Thread t = new Thread(mLogCatReceiverTask);
t.setName("LogCat output receiver for " + mCurrentDevice.getSerialNumber());
t.start();
}

    @Override
    public void log(List<LogCatMessage> newMessages) {
        List<LogCatMessage> deletedMessages;
        synchronized (mLogMessages) {
            deletedMessages = mLogMessages.ensureSpace(newMessages.size());
            mLogMessages.appendMessages(newMessages);
}
        sendLogChangedEvent(newMessages, deletedMessages);
}

/**








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatFilterTest.java b/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatFilterTest.java
//Synthetic comment -- index 7fedb08..98b186e 100644

//Synthetic comment -- @@ -16,6 +16,7 @@
package com.android.ddmuilib.logcat;

import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.logcat.LogCatMessage;

import java.util.List;









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatMessageParserTest.java b/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatMessageParserTest.java
deleted file mode 100644
//Synthetic comment -- index 3b5029c..0000000

//Synthetic comment -- @@ -1,99 +0,0 @@








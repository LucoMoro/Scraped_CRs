/*Create new logging class in the common library.

The goal is to later migrate all existing code to this
new logger and get rid of all our duplicates.

Also did a misc fix in AndroidLocation.

Change-Id:Ia33a782b57c91b4e3d5fd2c0660e040be11b9cbb*/




//Synthetic comment -- diff --git a/common/src/com/android/prefs/AndroidLocation.java b/common/src/com/android/prefs/AndroidLocation.java
//Synthetic comment -- index c36048a..66c0248 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.prefs;

import com.android.annotations.NonNull;

import java.io.File;

/**
//Synthetic comment -- @@ -45,7 +47,7 @@
* @return an OS specific path, terminated by a separator.
* @throws AndroidLocationException
*/
    @NonNull public final static String getFolder() throws AndroidLocationException {
if (sPrefsLocation == null) {
String home = findValidPath("ANDROID_SDK_HOME", "user.home", "HOME");









//Synthetic comment -- diff --git a/common/src/com/android/utils/ILogger.java b/common/src/com/android/utils/ILogger.java
new file mode 100644
//Synthetic comment -- index 0000000..df3a636

//Synthetic comment -- @@ -0,0 +1,77 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.utils;

import com.android.annotations.NonNull;

import java.util.Formatter;

/**
 * Interface used to display warnings/errors while parsing the SDK content.
 * <p/>
 * There are a few default implementations available:
 * <ul>
 * <li> {@link NullLogger} is an implementation that does <em>nothing</em> with the log.
 *  Useful for limited cases where you need to call a class that requires a non-null logging
 *  yet the calling code does not have any mean of reporting logs itself. It can be
 *  acceptable for use as a temporary implementation but most of the time that means the caller
 *  code needs to be reworked to take a logger object from its own caller.
 * </li>
 * <li> {@link StdLogger} is an implementation that dumps the log to {@link System#out} or
 *  {@link System#err}. This is useful for unit tests or code that does not have any GUI.
 *  GUI based apps based should not use it and should provide a better way to report to the user.
 * </li>
 * </ul>
 */
public interface ILogger {

    /**
     * Prints an error message.
     *
     * @param t is an optional {@link Throwable} or {@link Exception}. If non-null, its
     *          message will be printed out.
     * @param msgFormat is an optional error format. If non-null, it will be printed
     *          using a {@link Formatter} with the provided arguments.
     * @param args provides the arguments for errorFormat.
     */
    void error(Throwable t, String msgFormat, Object... args);

    /**
     * Prints a warning message.
     *
     * @param msgFormat is a string format to be used with a {@link Formatter}. Cannot be null.
     * @param args provides the arguments for warningFormat.
     */
    void warning(@NonNull String msgFormat, Object... args);

    /**
     * Prints an information message.
     *
     * @param msgFormat is a string format to be used with a {@link Formatter}. Cannot be null.
     * @param args provides the arguments for msgFormat.
     */
    void info(@NonNull String msgFormat, Object... args);

    /**
     * Prints a verbose message.
     *
     * @param msgFormat is a string format to be used with a {@link Formatter}. Cannot be null.
     * @param args provides the arguments for msgFormat.
     */
    void verbose(String msgFormat, Object... args);

}








//Synthetic comment -- diff --git a/common/src/com/android/utils/NullLogger.java b/common/src/com/android/utils/NullLogger.java
new file mode 100644
//Synthetic comment -- index 0000000..77f1ad5

//Synthetic comment -- @@ -0,0 +1,52 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.utils;

/**
 * Dummy implementation of an {@link ILogger}.
 * <p/>
 * Use {@link #getLogger()} to get a default instance of this {@link NullLogger}.
 */
public class NullLogger implements ILogger {

    private static final ILogger sThis = new NullLogger();

    public static ILogger getLogger() {
        return sThis;
    }

    @Override
    public void error(Throwable t, String errorFormat, Object... args) {
        // ignore
    }

    @Override
    public void warning(String warningFormat, Object... args) {
        // ignore
    }

    @Override
    public void info(String msgFormat, Object... args) {
        // ignore
    }

    @Override
    public void verbose(String msgFormat, Object... args) {
        // ignore
    }

}








//Synthetic comment -- diff --git a/common/src/com/android/utils/StdLogger.java b/common/src/com/android/utils/StdLogger.java
new file mode 100644
//Synthetic comment -- index 0000000..c0de06f

//Synthetic comment -- @@ -0,0 +1,177 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.utils;

import com.android.SdkConstants;
import com.android.annotations.NonNull;

import java.io.PrintStream;
import java.util.Formatter;


/**
 * An implementation of {@link ILogger} that prints to {@link System#out} and {@link System#err}.
 * <p/>
 *
 */
public class StdLogger implements ILogger {

    private final Level mLevel;

    public enum Level {
        VERBOSE(0),
        INFO(1),
        WARNING(2),
        ERROR(3);

        private final int mLevel;

        Level(int level) {
            mLevel = level;
        }
    }

    /**
     * Creates the {@link StdLogger} with a given log {@link Level}.
     * @param level the log Level.
     */
    public StdLogger(@NonNull Level level) {
        if (level == null) {
            throw new IllegalArgumentException("level cannot be null");
        }

        mLevel = level;
    }

    /**
     * Returns the logger's log {@link Level}.
     * @return the log level.
     */
    public Level getLevel() {
        return mLevel;
    }

    /**
     * Prints an error message.
     * <p/>
     * The message will be tagged with "Error" on the output so the caller does not
     * need to put such a prefix in the format string.
     * <p/>
     * The output is done on {@link System#err}.
     * <p/>
     * This is always displayed, independent of the logging {@link Level}.
     *
     * @param t is an optional {@link Throwable} or {@link Exception}. If non-null, it's
     *          message will be printed out.
     * @param errorFormat is an optional error format. If non-null, it will be printed
     *          using a {@link Formatter} with the provided arguments.
     * @param args provides the arguments for errorFormat.
     */
    @Override
    public void error(Throwable t, String errorFormat, Object... args) {
        if (errorFormat != null) {
            String msg = String.format("Error: " + errorFormat, args);

            printMessage(msg, System.err);
        }
        if (t != null) {
            System.err.println(String.format("Error: %1$s", t.getMessage()));
        }
    }

    /**
     * Prints a warning message.
     * <p/>
     * The message will be tagged with "Warning" on the output so the caller does not
     * need to put such a prefix in the format string.
     * <p/>
     * The output is done on {@link System#out}.
     * <p/>
     * This is displayed only if the logging {@link Level} is {@link Level#WARNING} or higher.
     *
     * @param msgFormat is a string format to be used with a {@link Formatter}. Cannot be null.
     * @param args provides the arguments for warningFormat.
     */
    @Override
    public void warning(@NonNull String warningFormat, Object... args) {
        if (mLevel.mLevel > Level.WARNING.mLevel) {
            return;
        }

        String msg = String.format("Warning: " + warningFormat, args);

        printMessage(msg, System.out);
    }

    /**
     * Prints an info message.
     * <p/>
     * The output is done on {@link System#out}.
     * <p/>
     * This is displayed only if the logging {@link Level} is {@link Level#INFO} or higher.
     *
     * @param msgFormat is a string format to be used with a {@link Formatter}. Cannot be null.
     * @param args provides the arguments for msgFormat.
     */
    @Override
    public void info(@NonNull String msgFormat, Object... args) {
        if (mLevel.mLevel > Level.INFO.mLevel) {
            return;
        }

        String msg = String.format(msgFormat, args);

        printMessage(msg, System.out);
    }

    /**
     * Prints a verbose message.
     * <p/>
     * The output is done on {@link System#out}.
     * <p/>
     * This is displayed only if the logging {@link Level} is {@link Level#VERBOSE} or higher.
     *
     * @param msgFormat is a string format to be used with a {@link Formatter}. Cannot be null.
     * @param args provides the arguments for msgFormat.
     */
    @Override
    public void verbose(@NonNull String msgFormat, Object... args) {
        if (mLevel.mLevel > Level.VERBOSE.mLevel) {
            return;
        }

        String msg = String.format(msgFormat, args);

        printMessage(msg, System.out);
    }

    private void printMessage(String msg, PrintStream stream) {
        if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS &&
                !msg.endsWith("\r\n") &&
                msg.endsWith("\n")) {
            // remove last \n so that println can use \r\n as needed.
            msg = msg.substring(0, msg.length() - 1);
        }

        stream.print(msg);

        if (!msg.endsWith("\n")) {
            stream.println();
        }
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/StdSdkLog.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/StdSdkLog.java
//Synthetic comment -- index 914f6ea..84bc212 100644

//Synthetic comment -- @@ -47,7 +47,7 @@
}
}
if (t != null) {
            System.err.println(String.format("Error: %1$s", t.getMessage()));
}
}









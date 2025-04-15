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









//Synthetic comment -- diff --git a/common/src/com/android/utils/Logger.java b/common/src/com/android/utils/Logger.java
new file mode 100644
//Synthetic comment -- index 0000000..85c21de

//Synthetic comment -- @@ -0,0 +1,78 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

import java.util.Formatter;

/**
 * Interface used to display warnings/errors while parsing the SDK content.
 * <p/>
 * There are a few default implementations available:
 * <ul>
 * <li> {@link NullLogger} is an implementation that does <em>nothing</em> with the log.
 *  Useful for limited cases where you need to call a class that requires a non-null logging
 *  yet the calling code does not have any mean of reporting logs itself. It can be
 *  acceptable for use a temporary implementation but most of the time that means the caller
 *  code needs to be reworked to take a logger object from its own caller.
 * </li>
 * <li> {@link StdLogger} is an implementation that dumps the log to {@link System#out} or
 *  {@link System#err}. This is useful for unit tests or code that does not have any GUI.
 *  GUI based apps based should not use it and should provide a better way to report to the user.
 * </li>
 * </ul>
 */
public interface Logger {

    /**
     * Prints an error message.
     *
     * @param t is an optional {@link Throwable} or {@link Exception}. If non-null, it's
     *          message will be printed out.
     * @param errorFormat is an optional error format. If non-null, it will be printed
     *          using a {@link Formatter} with the provided arguments.
     * @param args provides the arguments for errorFormat.
     */
    void error(Throwable t, String errorFormat, Object... args);

    /**
     * Prints a warning message.
     *
     * @param warningFormat is an optional error format. If non-null, it will be printed
     *          using a {@link Formatter} with the provided arguments.
     * @param args provides the arguments for warningFormat.
     */
    void warning(String warningFormat, Object... args);

    /**
     * Prints an info message.
     *
     * @param msgFormat is an optional error format. If non-null, it will be printed
     *          using a {@link Formatter} with the provided arguments.
     * @param args provides the arguments for msgFormat.
     */
    void info(String msgFormat, Object... args);

    /**
     * Prints a verbose message.
     *
     * @param msgFormat is an optional error format. If non-null, it will be printed
     *          using a {@link Formatter} with the provided arguments.
     * @param args provides the arguments for msgFormat.
     */
    void verbose(String msgFormat, Object... args);

}








//Synthetic comment -- diff --git a/common/src/com/android/utils/NullLogger.java b/common/src/com/android/utils/NullLogger.java
new file mode 100644
//Synthetic comment -- index 0000000..21c6277

//Synthetic comment -- @@ -0,0 +1,52 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
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
 * Dummy implementation of an {@link Logger}.
 * <p/>
 * Use {@link #getLogger()} to get a default instance of this {@link NullLogger}.
 */
public class NullLogger implements Logger {

    private static final Logger sThis = new NullLogger();

    public static Logger getLogger() {
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
//Synthetic comment -- index 0000000..2e566c3

//Synthetic comment -- @@ -0,0 +1,208 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
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

import java.util.Formatter;


/**
 * An implementation of {@link Logger} that prints to {@link System#out} and {@link System#err}.
 * <p/>
 *
 */
public class StdLogger implements Logger {

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

            if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS &&
                    !msg.endsWith("\r\n") &&
                    msg.endsWith("\n")) {
                // remove last \n so that println can use \r\n as needed.
                msg = msg.substring(0, msg.length() - 1);
            }

            System.err.print(msg);

            if (!msg.endsWith("\n")) {
                System.err.println();
            }
        }
        if (t != null) {
            System.err.println(String.format("Error: %1$s%2$s", t.getMessage()));
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
     * @param warningFormat is an optional error format. If non-null, it will be printed
     *          using a {@link Formatter} with the provided arguments.
     * @param args provides the arguments for warningFormat.
     */
    @Override
    public void warning(String warningFormat, Object... args) {
        if (mLevel.mLevel > Level.WARNING.mLevel) {
            return;
        }

        String msg = String.format("Warning: " + warningFormat, args);

        if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS &&
                !msg.endsWith("\r\n") &&
                msg.endsWith("\n")) {
            // remove last \n so that println can use \r\n as needed.
            msg = msg.substring(0, msg.length() - 1);
        }

        System.out.print(msg);

        if (!msg.endsWith("\n")) {
            System.out.println();
        }
    }

    /**
     * Prints an info message.
     * <p/>
     * The output is done on {@link System#out}.
     * <p/>
     * This is displayed only if the logging {@link Level} is {@link Level#INFO} or higher.
     *
     * @param msgFormat is an optional error format. If non-null, it will be printed
     *          using a {@link Formatter} with the provided arguments.
     * @param args provides the arguments for msgFormat.
     */
    @Override
    public void info(String msgFormat, Object... args) {
        if (mLevel.mLevel > Level.INFO.mLevel) {
            return;
        }

        String msg = String.format(msgFormat, args);

        if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS &&
                !msg.endsWith("\r\n") &&
                msg.endsWith("\n")) {
            // remove last \n so that println can use \r\n as needed.
            msg = msg.substring(0, msg.length() - 1);
        }

        System.out.print(msg);

        if (!msg.endsWith("\n")) {
            System.out.println();
        }
    }

    /**
     * Prints a verbose message.
     * <p/>
     * The output is done on {@link System#out}.
     * <p/>
     * This is displayed only if the logging {@link Level} is {@link Level#VERBOSE} or higher.
     *
     * @param msgFormat is an optional error format. If non-null, it will be printed
     *          using a {@link Formatter} with the provided arguments.
     * @param args provides the arguments for msgFormat.
     */
    @Override
    public void verbose(String msgFormat, Object... args) {
        if (mLevel.mLevel > Level.VERBOSE.mLevel) {
            return;
        }

        String msg = String.format(msgFormat, args);

        if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS &&
                !msg.endsWith("\r\n") &&
                msg.endsWith("\n")) {
            // remove last \n so that println can use \r\n as needed.
            msg = msg.substring(0, msg.length() - 1);
        }

        System.out.print(msg);

        if (!msg.endsWith("\n")) {
            System.out.println();
        }
    }
}








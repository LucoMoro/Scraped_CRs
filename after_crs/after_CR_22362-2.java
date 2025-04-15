/*Use enum for converting TouchPressType values

Change-Id:I072425caf7da7c1c01bf757243005ba0ebd97014*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index 649e33c..100da68 100644

//Synthetic comment -- @@ -15,28 +15,27 @@
*/
package com.android.monkeyrunner;

import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;

import com.android.monkeyrunner.core.IMonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyImage;
import com.android.monkeyrunner.core.TouchPressType;
import com.android.monkeyrunner.doc.MonkeyRunnerExported;
import com.android.monkeyrunner.easy.HierarchyViewer;

import org.python.core.ArgParser;
import org.python.core.ClassDictInit;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyObject;
import org.python.core.PyTuple;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

/*
* Abstract base class that represents a single connected Android
//Synthetic comment -- @@ -46,28 +45,20 @@
*/
@MonkeyRunnerExported(doc = "Represents a device attached to the system.")
public class MonkeyDevice extends PyObject implements ClassDictInit {
    private static final Logger LOG = Logger.getLogger(MonkeyDevice.class.getName());

public static void classDictInit(PyObject dict) {
JythonUtils.convertDocAnnotationsForClass(MonkeyDevice.class, dict);
}

@MonkeyRunnerExported(doc = "Sends a DOWN event when used with touch() or press().")
    public static final String DOWN = TouchPressType.DOWN.getIdentifier();

@MonkeyRunnerExported(doc = "Sends an UP event when used with touch() or press().")
    public static final String UP = TouchPressType.UP.getIdentifier();

@MonkeyRunnerExported(doc = "Sends a DOWN event, immediately followed by an UP event when used with touch() or press()")
    public static final String DOWN_AND_UP = TouchPressType.DOWN_AND_UP.getIdentifier();

private IMonkeyDevice impl;

//Synthetic comment -- @@ -129,21 +120,14 @@
int x = ap.getInt(0);
int y = ap.getInt(1);

        TouchPressType type = TouchPressType.fromIdentifier(ap.getString(2));
        if (type == null) {
            LOG.warning(String.format("Invalid TouchPressType specified (%s) default used instead",
                    ap.getString(2)));
            type = TouchPressType.DOWN_AND_UP;
}

        impl.touch(x, y, type);
}

@MonkeyRunnerExported(doc = "Simulates dragging (touch, hold, and move) on the device screen.",
//Synthetic comment -- @@ -191,22 +175,14 @@
Preconditions.checkNotNull(ap);

String name = ap.getString(0);
        TouchPressType type = TouchPressType.fromIdentifier(ap.getString(1));
        if (type == null) {
            LOG.warning(String.format("Invalid TouchPressType specified (%s) default used instead",
                    ap.getString(2)));
            type = TouchPressType.DOWN_AND_UP;
}

        impl.press(name, type);
}

@MonkeyRunnerExported(doc = "Types the specified string on the keyboard. This is " +








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java
//Synthetic comment -- index 60eaba9..050292a 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.monkeyrunner.adb.LinearInterpolator.Point;
import com.android.monkeyrunner.core.IMonkeyImage;
import com.android.monkeyrunner.core.IMonkeyDevice;
import com.android.monkeyrunner.core.TouchPressType;
import com.android.monkeyrunner.easy.HierarchyViewer;

import java.io.IOException;








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java
//Synthetic comment -- index c081a56..9c06ec4 100644

//Synthetic comment -- @@ -27,10 +27,6 @@
* MonkeyDevice interface.
*/
public interface IMonkeyDevice {
/**
* Create a MonkeyMananger for talking to this device.
*








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/TouchPressType.java b/monkeyrunner/src/com/android/monkeyrunner/core/TouchPressType.java
new file mode 100644
//Synthetic comment -- index 0000000..cfa878a

//Synthetic comment -- @@ -0,0 +1,49 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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
package com.android.monkeyrunner.core;

import java.util.HashMap;
import java.util.Map;

/**
 * TouchPressType enum contains valid input for the "touch" Monkey command.
 * When passed as a string, the "identifier" value is used.
 */
public enum TouchPressType {
    DOWN("down"), UP("up"), DOWN_AND_UP("downAndUp");

    private static final Map<String,TouchPressType> identifierToEnum =
        new HashMap<String,TouchPressType>();
    static {
        for (TouchPressType type : values()) {
            identifierToEnum.put(type.identifier, type);
        }
    }

    private String identifier;

    TouchPressType(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static TouchPressType fromIdentifier(String name) {
        return identifierToEnum.get(name);
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java
//Synthetic comment -- index e72e462..1c6c71b 100644

//Synthetic comment -- @@ -19,10 +19,9 @@
import com.google.common.base.Preconditions;

import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.monkeyrunner.JythonUtils;
import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.core.TouchPressType;
import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.eclipse.swt.graphics.Point;
//Synthetic comment -- @@ -32,7 +31,6 @@
import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyTuple;

import java.util.Set;
//Synthetic comment -- @@ -76,8 +74,8 @@

By selector = getSelector(ap, 0);
String tmpType = ap.getString(1);
        TouchPressType type = TouchPressType.fromIdentifier(tmpType);
        Preconditions.checkNotNull(type, "Invalid touch type: " + tmpType);
// TODO: try catch rethrow PyExc
touch(selector, type);
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/MonkeyRecorder.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/MonkeyRecorder.java
//Synthetic comment -- index 914a5b9..bde882e 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.monkeyrunner.recorder;

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.adb.AdbBackend;
import com.android.monkeyrunner.core.IMonkeyBackend;
import com.android.monkeyrunner.core.IMonkeyDevice;
//Synthetic comment -- @@ -45,7 +46,11 @@
*
* @param device
*/
    public static void start(final MonkeyDevice device) {
        start(device.getImpl());
    }

    /* package */static void start(final IMonkeyDevice device) {
MonkeyRecorderFrame frame = new MonkeyRecorderFrame(device);
// TODO: this is a hack until the window listener works.
frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/PressAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/PressAction.java
//Synthetic comment -- index 66a933a..88c3fa7 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyDevice;
import com.android.monkeyrunner.core.TouchPressType;

/**
* Action to press a certain button.
//Synthetic comment -- @@ -62,7 +63,6 @@

@Override
public void execute(IMonkeyDevice device) {
        device.press(key, TouchPressType.fromIdentifier(downUpFlag));
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/TouchAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/TouchAction.java
//Synthetic comment -- index 4e0ae2d..1001bef 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyDevice;
import com.android.monkeyrunner.core.TouchPressType;

/**
* Action to touch the touchscreen at a certain location.
//Synthetic comment -- @@ -48,8 +49,7 @@

@Override
public void execute(IMonkeyDevice device) throws Exception {
        device.touch(x, y, TouchPressType.fromIdentifier(direction));
}

@Override








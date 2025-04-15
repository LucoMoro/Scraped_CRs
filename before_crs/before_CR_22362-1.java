/*Use enum for converting TouchPressType values

Change-Id:I072425caf7da7c1c01bf757243005ba0ebd97014*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index 649e33c..31ade07 100644

//Synthetic comment -- @@ -15,28 +15,26 @@
*/
package com.android.monkeyrunner;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.python.core.ArgParser;
import org.python.core.ClassDictInit;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PyTuple;

import com.android.monkeyrunner.core.IMonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyImage;
import com.android.monkeyrunner.doc.MonkeyRunnerExported;
import com.android.monkeyrunner.easy.HierarchyViewer;

import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;

/*
* Abstract base class that represents a single connected Android
//Synthetic comment -- @@ -51,23 +49,13 @@
}

@MonkeyRunnerExported(doc = "Sends a DOWN event when used with touch() or press().")
    public static final String DOWN = "down";
@MonkeyRunnerExported(doc = "Sends an UP event when used with touch() or press().")
    public static final String UP = "up";
@MonkeyRunnerExported(doc = "Sends a DOWN event, immediately followed by an UP event when used with touch() or press()")
    public static final String DOWN_AND_UP = "downAndUp";

    // TODO: This may not be accessible from jython; if so, remove it.
    public enum TouchPressType {
        DOWN, UP, DOWN_AND_UP,
    }

    public static final Map<String, IMonkeyDevice.TouchPressType> TOUCH_NAME_TO_ENUM =
        ImmutableMap.of(DOWN, IMonkeyDevice.TouchPressType.DOWN,
                UP, IMonkeyDevice.TouchPressType.UP,
                DOWN_AND_UP, IMonkeyDevice.TouchPressType.DOWN_AND_UP);

    private static final Set<String> VALID_DOWN_UP_TYPES = TOUCH_NAME_TO_ENUM.keySet();

private IMonkeyDevice impl;

//Synthetic comment -- @@ -129,21 +117,10 @@
int x = ap.getInt(0);
int y = ap.getInt(1);

        // Default
        String type = MonkeyDevice.DOWN_AND_UP;
        try {
            String tmpType = ap.getString(1);
            if (VALID_DOWN_UP_TYPES.contains(tmpType)) {
                type = tmpType;
            } else {
                // not a valid type
                type = MonkeyDevice.DOWN_AND_UP;
            }
        } catch (PyException e) {
            // bad stuff was passed in, just use the already specified default value
            type = MonkeyDevice.DOWN_AND_UP;
        }
        impl.touch(x, y, TOUCH_NAME_TO_ENUM.get(type));
}

@MonkeyRunnerExported(doc = "Simulates dragging (touch, hold, and move) on the device screen.",
//Synthetic comment -- @@ -191,22 +168,10 @@
Preconditions.checkNotNull(ap);

String name = ap.getString(0);

        // Default
        String type = MonkeyDevice.DOWN_AND_UP;
        try {
            String tmpType = ap.getString(1);
            if (VALID_DOWN_UP_TYPES.contains(tmpType)) {
                type = tmpType;
            } else {
                // not a valid type
                type = MonkeyDevice.DOWN_AND_UP;
            }
        } catch (PyException e) {
            // bad stuff was passed in, just use the already specified default value
            type = MonkeyDevice.DOWN_AND_UP;
        }
        impl.press(name, TOUCH_NAME_TO_ENUM.get(type));
}

@MonkeyRunnerExported(doc = "Types the specified string on the keyboard. This is " +








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java
//Synthetic comment -- index c081a56..2f1ac94 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.monkeyrunner.easy.HierarchyViewer;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;
//Synthetic comment -- @@ -27,8 +28,34 @@
* MonkeyDevice interface.
*/
public interface IMonkeyDevice {
enum TouchPressType {
        DOWN, UP, DOWN_AND_UP,
}

/**








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java
//Synthetic comment -- index e72e462..cc8fadf 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import com.google.common.base.Preconditions;

import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.hierarchyviewerlib.device.ViewNode.Property;
import com.android.monkeyrunner.JythonUtils;
import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyDevice.TouchPressType;
//Synthetic comment -- @@ -32,7 +31,6 @@
import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyTuple;

import java.util.Set;
//Synthetic comment -- @@ -76,8 +74,8 @@

By selector = getSelector(ap, 0);
String tmpType = ap.getString(1);
        TouchPressType type = MonkeyDevice.TOUCH_NAME_TO_ENUM.get(tmpType);
        if (type == null) type = TouchPressType.DOWN_AND_UP;
// TODO: try catch rethrow PyExc
touch(selector, type);
}








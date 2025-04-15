/*Use enum for converting TouchPressType values

Change-Id:I072425caf7da7c1c01bf757243005ba0ebd97014*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index 649e33c..31ade07 100644

//Synthetic comment -- @@ -15,28 +15,26 @@
*/
package com.android.monkeyrunner;

import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;

import com.android.monkeyrunner.core.IMonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyDevice.TouchPressType;
import com.android.monkeyrunner.core.IMonkeyImage;
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

/*
* Abstract base class that represents a single connected Android
//Synthetic comment -- @@ -51,23 +49,13 @@
}

@MonkeyRunnerExported(doc = "Sends a DOWN event when used with touch() or press().")
    public static final String DOWN = TouchPressType.DOWN.getIdentifier();

@MonkeyRunnerExported(doc = "Sends an UP event when used with touch() or press().")
    public static final String UP = TouchPressType.UP.getIdentifier();

@MonkeyRunnerExported(doc = "Sends a DOWN event, immediately followed by an UP event when used with touch() or press()")
    public static final String DOWN_AND_UP = TouchPressType.DOWN_AND_UP.getIdentifier();

private IMonkeyDevice impl;

//Synthetic comment -- @@ -129,21 +117,10 @@
int x = ap.getInt(0);
int y = ap.getInt(1);

        TouchPressType type = TouchPressType.fromIdentifier(ap.getString(2));
        if (type == null) type = TouchPressType.DOWN_AND_UP;

        impl.touch(x, y, type);
}

@MonkeyRunnerExported(doc = "Simulates dragging (touch, hold, and move) on the device screen.",
//Synthetic comment -- @@ -191,22 +168,10 @@
Preconditions.checkNotNull(ap);

String name = ap.getString(0);
        TouchPressType type = TouchPressType.fromIdentifier(ap.getString(1));
        if (type == null) type = TouchPressType.DOWN_AND_UP;

        impl.press(name, type);
}

@MonkeyRunnerExported(doc = "Types the specified string on the keyboard. This is " +








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java
//Synthetic comment -- index c081a56..2f1ac94 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.monkeyrunner.easy.HierarchyViewer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
//Synthetic comment -- @@ -27,8 +28,34 @@
* MonkeyDevice interface.
*/
public interface IMonkeyDevice {
    /**
     * TouchPressType enum contains valid input for the "touch" Monkey command.
     * When passed as a string, the "identifier" value is used.
     */
enum TouchPressType {
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

/**








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java
//Synthetic comment -- index e72e462..cc8fadf 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import com.google.common.base.Preconditions;

import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.monkeyrunner.JythonUtils;
import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyDevice.TouchPressType;
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








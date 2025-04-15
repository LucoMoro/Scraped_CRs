/*Separate MonkeyRunner core logic

Refactored the MonkeyRunner code to separate core logic
from the jython wrapper. The core logic is now usable
directly from Java w/o the pollution from jython.
The existing MonkeyRunner classes are now just a thin
and dumb wrapper atop the core.

Change-Id:I549a935133844fafee670b556ef288be6f0fd507*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index 654e49a..4b8a2fc 100644

//Synthetic comment -- @@ -57,10 +57,11 @@
@MonkeyRunnerExported(doc = "Sends a DOWN event, immediately followed by an UP event when used with touch() or press()")
public static final String DOWN_AND_UP = "downAndUp";

    // TODO: is this really necessary; remove if not accessible from jython
    /*public enum TouchPressType {
DOWN, UP, DOWN_AND_UP,
    }*/

public static final Map<String, IMonkeyDevice.TouchPressType> TOUCH_NAME_TO_ENUM =
ImmutableMap.of(DOWN, IMonkeyDevice.TouchPressType.DOWN,








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java
//Synthetic comment -- index 6850953..a8be5a0 100644

//Synthetic comment -- @@ -1,4 +1,18 @@
// Copyright 2011 Google Inc. All Rights Reserved.
package com.android.monkeyrunner.core;

import com.android.monkeyrunner.MonkeyManager;
//Synthetic comment -- @@ -10,7 +24,7 @@
import javax.annotation.Nullable;

/**
 * @author adrianz@google.com (Adrian Zakrzewski)
*/
public interface IMonkeyDevice {
enum TouchPressType {








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyImage.java
//Synthetic comment -- index 312ae51..c9bbf7f 100644

//Synthetic comment -- @@ -1,4 +1,18 @@
// Copyright 2011 Google Inc. All Rights Reserved.
package com.android.monkeyrunner.core;

import java.awt.*;
//Synthetic comment -- @@ -14,7 +28,7 @@
import javax.imageio.stream.ImageOutputStream;

/**
 * @author adrianz@google.com (Adrian Zakrzewski)
*/
public interface IMonkeyImage {
BufferedImage createBufferedImage();








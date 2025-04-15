/*added copyright

Change-Id:I94a30212fb06b9faa249fa5e0422a4b0e1900d79*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index 654e49a..4b8a2fc 100644

//Synthetic comment -- @@ -57,10 +57,11 @@
@MonkeyRunnerExported(doc = "Sends a DOWN event, immediately followed by an UP event when used with touch() or press()")
public static final String DOWN_AND_UP = "downAndUp";

    // TODO: Remove the enum if not used by the scripts. I think the scripts
    // cannot access it, but I'm not 100% sure.
    public enum TouchPressType {
DOWN, UP, DOWN_AND_UP,
    }

public static final Map<String, IMonkeyDevice.TouchPressType> TOUCH_NAME_TO_ENUM =
ImmutableMap.of(DOWN, IMonkeyDevice.TouchPressType.DOWN,








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java
//Synthetic comment -- index 6850953..a8be5a0 100644

//Synthetic comment -- @@ -1,4 +1,18 @@
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

import com.android.monkeyrunner.MonkeyManager;
//Synthetic comment -- @@ -10,7 +24,7 @@
import javax.annotation.Nullable;

/**
 * MonkeyDevice interface.
*/
public interface IMonkeyDevice {
enum TouchPressType {








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyImage.java
//Synthetic comment -- index 312ae51..1e55348 100644

//Synthetic comment -- @@ -1,4 +1,18 @@
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
package com.android.monkeyrunner.core;

import java.awt.*;
//Synthetic comment -- @@ -14,7 +28,7 @@
import javax.imageio.stream.ImageOutputStream;

/**
 * MonkeyImage interface.
*/
public interface IMonkeyImage {
BufferedImage createBufferedImage();








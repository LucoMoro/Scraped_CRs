/*Add @GuardedBy and @Immutable annotations.

Change-Id:Ieb5517ca4f80d3ab4a06259c32d9509f25e97c28*/




//Synthetic comment -- diff --git a/common/src/main/java/com/android/annotations/concurrency/GuardedBy.java b/common/src/main/java/com/android/annotations/concurrency/GuardedBy.java
new file mode 100644
//Synthetic comment -- index 0000000..9489bb1

//Synthetic comment -- @@ -0,0 +1,34 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.android.annotations.concurrency;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the target field or method should only be accessed
 * with the specified lock being held.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface GuardedBy {
    String value();
}








//Synthetic comment -- diff --git a/common/src/main/java/com/android/annotations/concurrency/Immutable.java b/common/src/main/java/com/android/annotations/concurrency/Immutable.java
new file mode 100644
//Synthetic comment -- index 0000000..d6c9a4a

//Synthetic comment -- @@ -0,0 +1,33 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.android.annotations.concurrency;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the target class to which this annotation is applied
 * is immutable.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Immutable {
}








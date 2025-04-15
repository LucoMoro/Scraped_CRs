/*Fix opcode tests for check_cast and filled_new_array_range.

Updated test to verify froyo vm behaviur of throwing exceptions at first
access.

Bug 3147582

Change-Id:Ic0ffc1b7e058ed8c5f8643b8ac39ce95f96ec124*/




//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/check_cast/Test_check_cast.java b/tools/vm-tests/src/dot/junit/opcodes/check_cast/Test_check_cast.java
//Synthetic comment -- index ea9e38c..5c783a6 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.check_cast.d.T_check_cast_1;
import dot.junit.opcodes.check_cast.d.T_check_cast_2;
import dot.junit.opcodes.check_cast.d.T_check_cast_3;
import dot.junit.opcodes.check_cast.d.T_check_cast_7;


public class Test_check_cast extends DxTestCase {
//Synthetic comment -- @@ -118,34 +120,35 @@
}
}

/**
* @constraint n/a
     * @title Attempt to access inaccessible class, expect throws NoClassDefFoundError
     * on first access
     */
public void testVFE5() {
//@uses dot.junit.opcodes.check_cast.TestStubs
//@uses dot.junit.opcodes.check_cast.d.T_check_cast_3
        T_check_cast_3 t = new T_check_cast_3();
try {
            t.run();
            fail("expected NoClassDefFoundError");
        } catch (NoClassDefFoundError iae) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to access undefined class, expect throws NoClassDefFoundError on
     * first access
     */
public void testVFE6() {
        T_check_cast_7 t = new T_check_cast_7();
try {
            t.run();
            fail("expected NoClassDefFoundError");
        } catch (NoClassDefFoundError iae) {
            // expected
}
}









//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/filled_new_array_range/Test_filled_new_array_range.java b/tools/vm-tests/src/dot/junit/opcodes/filled_new_array_range/Test_filled_new_array_range.java
//Synthetic comment -- index b69d5b4..1f1fb19 100644

//Synthetic comment -- @@ -19,8 +19,9 @@
import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.filled_new_array_range.d.T_filled_new_array_range_1;
import dot.junit.opcodes.filled_new_array_range.d.T_filled_new_array_range_10;
import dot.junit.opcodes.filled_new_array_range.d.T_filled_new_array_range_11;
import dot.junit.opcodes.filled_new_array_range.d.T_filled_new_array_range_2;

public class Test_filled_new_array_range extends DxTestCase {
/**
//Synthetic comment -- @@ -140,31 +141,32 @@
}

/**
     * @constraint n/a
* @title attempt to instantiate array of non-existent class
*/
public void testVFE8() {
        T_filled_new_array_range_10 t = new T_filled_new_array_range_10();
try {
            t.run();
            fail("expected NoClassDefFoundError exception");
        } catch (NoClassDefFoundError e) {
            // expected
}
}

/**
     * @constraint n/a
* @title attempt to instantiate array of inaccessible class
*/
public void testVFE9() {
        //@uses dot.junit.opcodes.filled_new_array_range.d.T_filled_new_array_range_11
//@uses dot.junit.opcodes.filled_new_array_range.TestStubs
        T_filled_new_array_range_11 t = new T_filled_new_array_range_11();
try {
            t.run();
            fail("expected NoClassDefFoundError exception");
        } catch (NoClassDefFoundError e) {
            // expected
}
}
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/filled_new_array_range/d/T_filled_new_array_range_10.java b/tools/vm-tests/src/dot/junit/opcodes/filled_new_array_range/d/T_filled_new_array_range_10.java
new file mode 100644
//Synthetic comment -- index 0000000..57075bd

//Synthetic comment -- @@ -0,0 +1,23 @@
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

package dot.junit.opcodes.filled_new_array_range.d;

public class T_filled_new_array_range_10 {
    public Object[] run() {
        return null;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/filled_new_array_range/d/T_filled_new_array_range_11.java b/tools/vm-tests/src/dot/junit/opcodes/filled_new_array_range/d/T_filled_new_array_range_11.java
new file mode 100644
//Synthetic comment -- index 0000000..f4ca611

//Synthetic comment -- @@ -0,0 +1,23 @@
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

package dot.junit.opcodes.filled_new_array_range.d;

public class T_filled_new_array_range_11 {
    public Object[] run() {
        return null;
    }
}








/*Fix opcode tests for sput_boolean

Bug 3147582

Change-Id:Id21109723e175393d34a3458c1765c11b4898577*/




//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_boolean/Test_sput_boolean.java b/tools/vm-tests/src/dot/junit/opcodes/sput_boolean/Test_sput_boolean.java
//Synthetic comment -- index 61a3e54..f503ec6 100644

//Synthetic comment -- @@ -19,10 +19,16 @@
import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.sput_boolean.d.T_sput_boolean_1;
import dot.junit.opcodes.sput_boolean.d.T_sput_boolean_10;
import dot.junit.opcodes.sput_boolean.d.T_sput_boolean_11;
import dot.junit.opcodes.sput_boolean.d.T_sput_boolean_12;
import dot.junit.opcodes.sput_boolean.d.T_sput_boolean_13;
import dot.junit.opcodes.sput_boolean.d.T_sput_boolean_14;
import dot.junit.opcodes.sput_boolean.d.T_sput_boolean_15;
import dot.junit.opcodes.sput_boolean.d.T_sput_boolean_17;
import dot.junit.opcodes.sput_boolean.d.T_sput_boolean_7;
import dot.junit.opcodes.sput_boolean.d.T_sput_boolean_8;
import dot.junit.opcodes.sput_boolean.d.T_sput_boolean_9;


public class Test_sput_boolean extends DxTestCase {
//Synthetic comment -- @@ -37,7 +43,7 @@
assertEquals(true, T_sput_boolean_1.st_i1);
}


/**
* @title modification of final field
*/
//Synthetic comment -- @@ -59,7 +65,7 @@
t.run();
assertEquals(true, T_sput_boolean_14.getProtectedField());
}


/**
* @title initialization of referenced class throws exception
//Synthetic comment -- @@ -75,7 +81,7 @@
}

/**
     * @constraint A12
* @title constant pool index
*/
public void testVFE1() {
//Synthetic comment -- @@ -88,8 +94,8 @@
}

/**
     *
     * @constraint A23
* @title number of registers
*/
public void testVFE2() {
//Synthetic comment -- @@ -103,23 +109,22 @@


/**
     *
     * @constraint B13
     * @title put boolean into long field - only field with same name but
* different type exists
*/
public void testVFE5() {
try {
            new T_sput_boolean_17().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
     *
     * @constraint B13
* @title put value '2' into boolean field
*/
public void testVFE6() {
//Synthetic comment -- @@ -132,9 +137,9 @@
}

/**
     *
     * @constraint B13
     * @title type of field doesn't match opcode - attempt to modify double
* field with single-width register
*/
public void testVFE7() {
//Synthetic comment -- @@ -145,87 +150,77 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint A12
     * @title Attempt to set non-static field.
*/
public void testVFE8() {
try {
             new T_sput_boolean_7().run();
             fail("expected IncompatibleClassChangeError");
         } catch (IncompatibleClassChangeError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to modify inaccessible field.
*/
public void testVFE9() {
//@uses dot.junit.opcodes.sput_boolean.TestStubs
//@uses dot.junit.opcodes.sput_boolean.d.T_sput_boolean_8
try {
            new T_sput_boolean_8().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to modify field of undefined class.
*/
public void testVFE10() {
try {
            new T_sput_boolean_9().run();
            fail("expected NoClassDefFoundError");
        } catch (NoClassDefFoundError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to modify undefined field.
*/
public void testVFE11() {
try {
            new T_sput_boolean_10().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}



/**
* @constraint n/a
     * @title Attempt to modify superclass' private field from subclass.
*/
public void testVFE12() {
//@uses dot.junit.opcodes.sput_boolean.d.T_sput_boolean_1
//@uses dot.junit.opcodes.sput_boolean.d.T_sput_boolean_15
try {
            new T_sput_boolean_15().run();
fail("expected a verification exception");
        } catch (IllegalAccessError t) {
}
}


/**
     * @constraint B1
* @title sput_boolean shall not work for wide numbers
*/
public void testVFE13() {
//Synthetic comment -- @@ -236,10 +231,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput_boolean shall not work for reference fields
*/
public void testVFE14() {
//Synthetic comment -- @@ -250,10 +245,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput_boolean shall not work for short fields
*/
public void testVFE15() {
//Synthetic comment -- @@ -264,10 +259,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput_boolean shall not work for int fields
*/
public void testVFE16() {
//Synthetic comment -- @@ -278,10 +273,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput_boolean shall not work for char fields
*/
public void testVFE17() {
//Synthetic comment -- @@ -292,10 +287,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput_boolean shall not work for byte fields
*/
public void testVFE18() {
//Synthetic comment -- @@ -306,19 +301,19 @@
DxUtil.checkVerifyException(t);
}
}

/**
     * @constraint B1
* @title Modification of final field in other class.
*/
public void testVFE19() {
//@uses dot.junit.opcodes.sput_boolean.TestStubs
//@uses dot.junit.opcodes.sput_boolean.d.T_sput_boolean_11

	try {
    		new T_sput_boolean_11().run();
    		fail("expected IllegalAccessError");
    	} catch (IllegalAccessError t) {
}
}
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_boolean/d/T_sput_boolean_10.java b/tools/vm-tests/src/dot/junit/opcodes/sput_boolean/d/T_sput_boolean_10.java
new file mode 100644
//Synthetic comment -- index 0000000..1bb1f7a3

//Synthetic comment -- @@ -0,0 +1,22 @@
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

package dot.junit.opcodes.sput_boolean.d;

public class T_sput_boolean_10 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_boolean/d/T_sput_boolean_15.java b/tools/vm-tests/src/dot/junit/opcodes/sput_boolean/d/T_sput_boolean_15.java
new file mode 100644
//Synthetic comment -- index 0000000..467fc55

//Synthetic comment -- @@ -0,0 +1,22 @@
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

package dot.junit.opcodes.sput_boolean.d;

public class T_sput_boolean_15 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_boolean/d/T_sput_boolean_17.java b/tools/vm-tests/src/dot/junit/opcodes/sput_boolean/d/T_sput_boolean_17.java
new file mode 100644
//Synthetic comment -- index 0000000..5e126b5

//Synthetic comment -- @@ -0,0 +1,22 @@
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

package dot.junit.opcodes.sput_boolean.d;

public class T_sput_boolean_17 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_boolean/d/T_sput_boolean_7.java b/tools/vm-tests/src/dot/junit/opcodes/sput_boolean/d/T_sput_boolean_7.java
new file mode 100644
//Synthetic comment -- index 0000000..7a97328

//Synthetic comment -- @@ -0,0 +1,22 @@
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

package dot.junit.opcodes.sput_boolean.d;

public class T_sput_boolean_7 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_boolean/d/T_sput_boolean_8.java b/tools/vm-tests/src/dot/junit/opcodes/sput_boolean/d/T_sput_boolean_8.java
new file mode 100644
//Synthetic comment -- index 0000000..8d738ae1

//Synthetic comment -- @@ -0,0 +1,22 @@
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

package dot.junit.opcodes.sput_boolean.d;

public class T_sput_boolean_8 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_boolean/d/T_sput_boolean_9.java b/tools/vm-tests/src/dot/junit/opcodes/sput_boolean/d/T_sput_boolean_9.java
new file mode 100644
//Synthetic comment -- index 0000000..6028d28

//Synthetic comment -- @@ -0,0 +1,22 @@
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

package dot.junit.opcodes.sput_boolean.d;

public class T_sput_boolean_9 {
    public void run() {
    }
}








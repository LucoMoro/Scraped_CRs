/*Fix opcode tests for sput_char

Bug 3147582

Change-Id:I5b9d06d8469f5d7bb2be0970b3fdb4bce21b127a*/




//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_char/Test_sput_char.java b/tools/vm-tests/src/dot/junit/opcodes/sput_char/Test_sput_char.java
//Synthetic comment -- index 92ec064..120b45a 100644

//Synthetic comment -- @@ -19,10 +19,16 @@
import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.sput_char.d.T_sput_char_1;
import dot.junit.opcodes.sput_char.d.T_sput_char_10;
import dot.junit.opcodes.sput_char.d.T_sput_char_11;
import dot.junit.opcodes.sput_char.d.T_sput_char_12;
import dot.junit.opcodes.sput_char.d.T_sput_char_13;
import dot.junit.opcodes.sput_char.d.T_sput_char_14;
import dot.junit.opcodes.sput_char.d.T_sput_char_15;
import dot.junit.opcodes.sput_char.d.T_sput_char_17;
import dot.junit.opcodes.sput_char.d.T_sput_char_7;
import dot.junit.opcodes.sput_char.d.T_sput_char_8;
import dot.junit.opcodes.sput_char.d.T_sput_char_9;

public class Test_sput_char extends DxTestCase {
/**
//Synthetic comment -- @@ -35,7 +41,7 @@
assertEquals(77, T_sput_char_1.st_i1);
}


/**
* @title modification of final field
*/
//Synthetic comment -- @@ -57,7 +63,7 @@
t.run();
assertEquals(77, T_sput_char_14.getProtectedField());
}


/**
* @title initialization of referenced class throws exception
//Synthetic comment -- @@ -73,7 +79,7 @@
}

/**
     * @constraint A12
* @title constant pool index
*/
public void testVFE1() {
//Synthetic comment -- @@ -86,8 +92,8 @@
}

/**
     *
     * @constraint A23
* @title number of registers
*/
public void testVFE2() {
//Synthetic comment -- @@ -101,23 +107,22 @@


/**
     *
     * @constraint B13
     * @title put char into long field - only field with same name but
* different type exists
*/
public void testVFE5() {
try {
            new T_sput_char_17().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
     *
     * @constraint B13
* @title put value '66000' into byte field
*/
public void testVFE6() {
//Synthetic comment -- @@ -130,9 +135,9 @@
}

/**
     *
     * @constraint B13
     * @title type of field doesn't match opcode - attempt to modify double
* field with single-width register
*/
public void testVFE7() {
//Synthetic comment -- @@ -143,87 +148,77 @@
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
             new T_sput_char_7().run();
             fail("expected IncompatibleClassChangeError");
         } catch (IncompatibleClassChangeError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to modify inaccessible field.
*/
public void testVFE9() {
//@uses dot.junit.opcodes.sput_char.TestStubs
//@uses dot.junit.opcodes.sput_char.d.T_sput_char_8
try {
            new T_sput_char_8().run();
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
            new T_sput_char_9().run();
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
            new T_sput_char_10().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}



/**
* @constraint n/a
     * @title Attempt to modify superclass' private field from subclass.
*/
public void testVFE12() {
//@uses dot.junit.opcodes.sput_char.d.T_sput_char_1
//@uses dot.junit.opcodes.sput_char.d.T_sput_char_15
try {
            new T_sput_char_15().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}


/**
     * @constraint B1
* @title sput-char shall not work for wide numbers
*/
public void testVFE13() {
//Synthetic comment -- @@ -234,10 +229,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput-char shall not work for reference fields
*/
public void testVFE14() {
//Synthetic comment -- @@ -248,10 +243,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput-char shall not work for short fields
*/
public void testVFE15() {
//Synthetic comment -- @@ -262,10 +257,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput-char shall not work for int fields
*/
public void testVFE16() {
//Synthetic comment -- @@ -276,10 +271,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput-char shall not work for byte fields
*/
public void testVFE17() {
//Synthetic comment -- @@ -290,10 +285,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput-char shall not work for boolean fields
*/
public void testVFE18() {
//Synthetic comment -- @@ -313,10 +308,9 @@
//@uses dot.junit.opcodes.sput_char.TestStubs
//@uses dot.junit.opcodes.sput_char.d.T_sput_char_11
	try {
            new T_sput_char_11().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_char/d/T_sput_char_10.java b/tools/vm-tests/src/dot/junit/opcodes/sput_char/d/T_sput_char_10.java
new file mode 100644
//Synthetic comment -- index 0000000..10f88f3

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

package dot.junit.opcodes.sput_char.d;

public class T_sput_char_10 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_char/d/T_sput_char_15.java b/tools/vm-tests/src/dot/junit/opcodes/sput_char/d/T_sput_char_15.java
new file mode 100644
//Synthetic comment -- index 0000000..2b4d7d2

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

package dot.junit.opcodes.sput_char.d;

public class T_sput_char_15 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_char/d/T_sput_char_17.java b/tools/vm-tests/src/dot/junit/opcodes/sput_char/d/T_sput_char_17.java
new file mode 100644
//Synthetic comment -- index 0000000..349eaa2

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

package dot.junit.opcodes.sput_char.d;

public class T_sput_char_17 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_char/d/T_sput_char_7.java b/tools/vm-tests/src/dot/junit/opcodes/sput_char/d/T_sput_char_7.java
new file mode 100644
//Synthetic comment -- index 0000000..7a496f2

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

package dot.junit.opcodes.sput_char.d;

public class T_sput_char_7 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_char/d/T_sput_char_8.java b/tools/vm-tests/src/dot/junit/opcodes/sput_char/d/T_sput_char_8.java
new file mode 100644
//Synthetic comment -- index 0000000..c4a3e38

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

package dot.junit.opcodes.sput_char.d;

public class T_sput_char_8 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_char/d/T_sput_char_9.java b/tools/vm-tests/src/dot/junit/opcodes/sput_char/d/T_sput_char_9.java
new file mode 100644
//Synthetic comment -- index 0000000..c38e18c

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

package dot.junit.opcodes.sput_char.d;

public class T_sput_char_9 {
    public void run() {
    }
}








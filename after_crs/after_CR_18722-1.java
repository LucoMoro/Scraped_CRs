/*Fix opcode tests for sget_*

Bug 3147582

Change-Id:I954004d96a411c4781b4011e53082f581f623c9a*/




//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget/Test_sget.java b/tools/vm-tests/src/dot/junit/opcodes/sget/Test_sget.java
//Synthetic comment -- index de7e2dd..981b19a 100644

//Synthetic comment -- @@ -20,12 +20,17 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.sget.d.T_sget_1;
import dot.junit.opcodes.sget.d.T_sget_11;
import dot.junit.opcodes.sget.d.T_sget_12;
import dot.junit.opcodes.sget.d.T_sget_13;
import dot.junit.opcodes.sget.d.T_sget_2;
import dot.junit.opcodes.sget.d.T_sget_5;
import dot.junit.opcodes.sget.d.T_sget_6;
import dot.junit.opcodes.sget.d.T_sget_7;
import dot.junit.opcodes.sget.d.T_sget_8;
import dot.junit.opcodes.sget.d.T_sget_9;

public class Test_sget extends DxTestCase {

/**
* @title type - int
*/
//Synthetic comment -- @@ -65,7 +70,7 @@
// expected
}
}

/**
* @title initialization of referenced class throws exception
*/
//Synthetic comment -- @@ -80,7 +85,7 @@
}

/**
     * @constraint A12
* @title constant pool index
*/
public void testVFE1() {
//Synthetic comment -- @@ -93,8 +98,8 @@
}

/**
     *
     * @constraint A23
* @title number of registers
*/
public void testVFE2() {
//Synthetic comment -- @@ -105,84 +110,75 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B13
     * @title read integer from long field - only field with same name but
* different type exists
*/
public void testVFE3() {
try {
            new T_sget_13().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.sget.d.T_sget_6
//@uses dot.junit.opcodes.sget.TestStubs
try {
            new T_sget_6().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class.
*/
public void testVFE5() {
try {
            new T_sget_7().run();
            fail("expected NoClassDefFoundError");
        } catch (NoClassDefFoundError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field.
*/
public void testVFE6() {
try {
            new T_sget_8().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.sget.d.T_sget_12
//@uses dot.junit.opcodes.sget.d.T_sget_1
try {
            new T_sget_12().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}

/**
     * @constraint B1
* @title sget shall not work for reference fields
*/
public void testVFE8() {
//Synthetic comment -- @@ -193,10 +189,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget shall not work for short fields
*/
public void testVFE9() {
//Synthetic comment -- @@ -207,10 +203,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget shall not work for boolean fields
*/
public void testVFE10() {
//Synthetic comment -- @@ -221,10 +217,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget shall not work for char fields
*/
public void testVFE11() {
//Synthetic comment -- @@ -235,10 +231,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget shall not work for byte fields
*/
public void testVFE12() {
//Synthetic comment -- @@ -248,11 +244,11 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B1
* @title sget shall not work for double fields
*/
public void testVFE13() {
//Synthetic comment -- @@ -262,11 +258,11 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B1
* @title sget shall not work for long fields
*/
public void testVFE14() {
//Synthetic comment -- @@ -276,5 +272,5 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget/d/T_sget_12.java b/tools/vm-tests/src/dot/junit/opcodes/sget/d/T_sget_12.java
new file mode 100644
//Synthetic comment -- index 0000000..f4232d3

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

package dot.junit.opcodes.sget.d;

public class T_sget_12 {
    public int run(){
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget/d/T_sget_13.java b/tools/vm-tests/src/dot/junit/opcodes/sget/d/T_sget_13.java
new file mode 100644
//Synthetic comment -- index 0000000..a1d862f

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

package dot.junit.opcodes.sget.d;

public class T_sget_13 {
    public void run(){
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget/d/T_sget_6.java b/tools/vm-tests/src/dot/junit/opcodes/sget/d/T_sget_6.java
new file mode 100644
//Synthetic comment -- index 0000000..8f81780

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

package dot.junit.opcodes.sget.d;

public class T_sget_6 {
    public int run(){
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget/d/T_sget_7.java b/tools/vm-tests/src/dot/junit/opcodes/sget/d/T_sget_7.java
new file mode 100644
//Synthetic comment -- index 0000000..8960240

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

package dot.junit.opcodes.sget.d;

public class T_sget_7 {
    public int run(){
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget/d/T_sget_8.java b/tools/vm-tests/src/dot/junit/opcodes/sget/d/T_sget_8.java
new file mode 100644
//Synthetic comment -- index 0000000..17e16f0

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

package dot.junit.opcodes.sget.d;

public class T_sget_8 {
    public int run(){
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_boolean/Test_sget_boolean.java b/tools/vm-tests/src/dot/junit/opcodes/sget_boolean/Test_sget_boolean.java
//Synthetic comment -- index 85ef065..ccbeb09 100644

//Synthetic comment -- @@ -20,11 +20,16 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.sget_boolean.d.T_sget_boolean_1;
import dot.junit.opcodes.sget_boolean.d.T_sget_boolean_11;
import dot.junit.opcodes.sget_boolean.d.T_sget_boolean_12;
import dot.junit.opcodes.sget_boolean.d.T_sget_boolean_13;
import dot.junit.opcodes.sget_boolean.d.T_sget_boolean_5;
import dot.junit.opcodes.sget_boolean.d.T_sget_boolean_6;
import dot.junit.opcodes.sget_boolean.d.T_sget_boolean_7;
import dot.junit.opcodes.sget_boolean.d.T_sget_boolean_8;
import dot.junit.opcodes.sget_boolean.d.T_sget_boolean_9;

public class Test_sget_boolean extends DxTestCase {

/**
* @title get boolean from static field
*/
//Synthetic comment -- @@ -49,7 +54,7 @@
* @title attempt to access non-static field
*/
public void testE1() {

T_sget_boolean_5 t = new T_sget_boolean_5();
try {
t.run();
//Synthetic comment -- @@ -58,7 +63,7 @@
// expected
}
}

/**
* @title initialization of referenced class throws exception
*/
//Synthetic comment -- @@ -72,10 +77,10 @@
}
}



/**
     * @constraint A12
* @title constant pool index
*/
public void testVFE1() {
//Synthetic comment -- @@ -88,8 +93,8 @@
}

/**
     *
     * @constraint A23
* @title number of registers
*/
public void testVFE2() {
//Synthetic comment -- @@ -100,84 +105,74 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B13
     * @title read boolean from long field - only field with same name but
* different type exists
*/
public void testVFE3() {
try {
            new T_sget_boolean_13().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.sget_boolean.d.T_sget_boolean_6
//@uses dot.junit.opcodes.sget_boolean.TestStubs
try {
            new T_sget_boolean_6().run();
        } catch (IllegalAccessError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class.
*/
public void testVFE5() {
try {
            new T_sget_boolean_7().run();
            fail("expected NoClassDefFoundError");
        } catch (NoClassDefFoundError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field.
*/
public void testVFE6() {
try {
            new T_sget_boolean_8().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.sget_boolean.d.T_sget_boolean_12
//@uses dot.junit.opcodes.sget_boolean.d.T_sget_boolean_1
try {
            new T_sget_boolean_12().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}

/**
     * @constraint B1
* @title sget_boolean shall not work for reference fields
*/
public void testVFE8() {
//Synthetic comment -- @@ -188,10 +183,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_boolean shall not work for short fields
*/
public void testVFE9() {
//Synthetic comment -- @@ -202,10 +197,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_boolean shall not work for int fields
*/
public void testVFE10() {
//Synthetic comment -- @@ -216,10 +211,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_boolean shall not work for char fields
*/
public void testVFE11() {
//Synthetic comment -- @@ -230,10 +225,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_boolean shall not work for byte fields
*/
public void testVFE12() {
//Synthetic comment -- @@ -243,11 +238,11 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B1
* @title sget_boolean shall not work for double fields
*/
public void testVFE13() {
//Synthetic comment -- @@ -257,11 +252,11 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B1
* @title sget_boolean shall not work for long fields
*/
public void testVFE14() {
//Synthetic comment -- @@ -271,5 +266,5 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_boolean/d/T_sget_boolean_12.java b/tools/vm-tests/src/dot/junit/opcodes/sget_boolean/d/T_sget_boolean_12.java
new file mode 100644
//Synthetic comment -- index 0000000..74566c4

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

package dot.junit.opcodes.sget_boolean.d;

public class T_sget_boolean_12 {
    public boolean run(){
        return true;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_boolean/d/T_sget_boolean_13.java b/tools/vm-tests/src/dot/junit/opcodes/sget_boolean/d/T_sget_boolean_13.java
new file mode 100644
//Synthetic comment -- index 0000000..f69d590

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

package dot.junit.opcodes.sget_boolean.d;

public class T_sget_boolean_13 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_boolean/d/T_sget_boolean_6.java b/tools/vm-tests/src/dot/junit/opcodes/sget_boolean/d/T_sget_boolean_6.java
new file mode 100644
//Synthetic comment -- index 0000000..ecf8943

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

package dot.junit.opcodes.sget_boolean.d;

public class T_sget_boolean_6 {
    public boolean run(){
        return true;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_boolean/d/T_sget_boolean_7.java b/tools/vm-tests/src/dot/junit/opcodes/sget_boolean/d/T_sget_boolean_7.java
new file mode 100644
//Synthetic comment -- index 0000000..4db0e16

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

package dot.junit.opcodes.sget_boolean.d;

public class T_sget_boolean_7 {
    public boolean run(){
        return true;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_boolean/d/T_sget_boolean_8.java b/tools/vm-tests/src/dot/junit/opcodes/sget_boolean/d/T_sget_boolean_8.java
new file mode 100644
//Synthetic comment -- index 0000000..42cf804

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

package dot.junit.opcodes.sget_boolean.d;

public class T_sget_boolean_8 {
    public boolean run(){
        return true;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_byte/Test_sget_byte.java b/tools/vm-tests/src/dot/junit/opcodes/sget_byte/Test_sget_byte.java
//Synthetic comment -- index 87781ac..9a19f98 100644

//Synthetic comment -- @@ -20,11 +20,16 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.sget_byte.d.T_sget_byte_1;
import dot.junit.opcodes.sget_byte.d.T_sget_byte_11;
import dot.junit.opcodes.sget_byte.d.T_sget_byte_12;
import dot.junit.opcodes.sget_byte.d.T_sget_byte_13;
import dot.junit.opcodes.sget_byte.d.T_sget_byte_5;
import dot.junit.opcodes.sget_byte.d.T_sget_byte_6;
import dot.junit.opcodes.sget_byte.d.T_sget_byte_7;
import dot.junit.opcodes.sget_byte.d.T_sget_byte_8;
import dot.junit.opcodes.sget_byte.d.T_sget_byte_9;

public class Test_sget_byte extends DxTestCase {

/**
* @title get byte from static field
*/
//Synthetic comment -- @@ -49,7 +54,7 @@
* @title attempt to access non-static field
*/
public void testE1() {

T_sget_byte_5 t = new T_sget_byte_5();
try {
t.run();
//Synthetic comment -- @@ -58,7 +63,7 @@
// expected
}
}

/**
* @title initialization of referenced class throws exception
*/
//Synthetic comment -- @@ -72,10 +77,10 @@
}
}



/**
     * @constraint A12
* @title constant pool index
*/
public void testVFE1() {
//Synthetic comment -- @@ -88,8 +93,8 @@
}

/**
     *
     * @constraint A23
* @title number of registers
*/
public void testVFE2() {
//Synthetic comment -- @@ -100,84 +105,75 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B13
     * @title read byte from long field - only field with same name but
* different type exists
*/
public void testVFE3() {
try {
            new T_sget_byte_13().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.sget_byte.d.T_sget_byte_6
//@uses dot.junit.opcodes.sget_byte.TestStubs
try {
            new T_sget_byte_6().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class.
*/
public void testVFE5() {
try {
            new T_sget_byte_7().run();
            fail("expected NoClassDefFoundError");
        } catch (NoClassDefFoundError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field.
*/
public void testVFE6() {
try {
            new T_sget_byte_8().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.sget_byte.d.T_sget_byte_12
//@uses dot.junit.opcodes.sget_byte.d.T_sget_byte_1
try {
            new T_sget_byte_12().run();
fail("expected a verification exception");
        } catch (IllegalAccessError t) {
}
}

/**
     * @constraint B1
* @title sget_byte shall not work for reference fields
*/
public void testVFE8() {
//Synthetic comment -- @@ -188,10 +184,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_byte shall not work for short fields
*/
public void testVFE9() {
//Synthetic comment -- @@ -202,10 +198,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_byte shall not work for int fields
*/
public void testVFE10() {
//Synthetic comment -- @@ -216,10 +212,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_byte shall not work for char fields
*/
public void testVFE11() {
//Synthetic comment -- @@ -230,10 +226,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_byte shall not work for boolean fields
*/
public void testVFE12() {
//Synthetic comment -- @@ -243,11 +239,11 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B1
* @title sget_byte shall not work for double fields
*/
public void testVFE13() {
//Synthetic comment -- @@ -257,11 +253,11 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B1
* @title sget_byte shall not work for long fields
*/
public void testVFE14() {
//Synthetic comment -- @@ -271,5 +267,5 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_byte/d/T_sget_byte_12.java b/tools/vm-tests/src/dot/junit/opcodes/sget_byte/d/T_sget_byte_12.java
new file mode 100644
//Synthetic comment -- index 0000000..cbfb2c0

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

package dot.junit.opcodes.sget_byte.d;

public class T_sget_byte_12 {
    public byte run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_byte/d/T_sget_byte_13.java b/tools/vm-tests/src/dot/junit/opcodes/sget_byte/d/T_sget_byte_13.java
new file mode 100644
//Synthetic comment -- index 0000000..b2723ae

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

package dot.junit.opcodes.sget_byte.d;

public class T_sget_byte_13 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_byte/d/T_sget_byte_6.java b/tools/vm-tests/src/dot/junit/opcodes/sget_byte/d/T_sget_byte_6.java
new file mode 100644
//Synthetic comment -- index 0000000..df4e3f9

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

package dot.junit.opcodes.sget_byte.d;

public class T_sget_byte_6 {
    public byte run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_byte/d/T_sget_byte_7.java b/tools/vm-tests/src/dot/junit/opcodes/sget_byte/d/T_sget_byte_7.java
new file mode 100644
//Synthetic comment -- index 0000000..b865dde8

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

package dot.junit.opcodes.sget_byte.d;

public class T_sget_byte_7 {
    public byte run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_byte/d/T_sget_byte_8.java b/tools/vm-tests/src/dot/junit/opcodes/sget_byte/d/T_sget_byte_8.java
new file mode 100644
//Synthetic comment -- index 0000000..605616f

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

package dot.junit.opcodes.sget_byte.d;

public class T_sget_byte_8 {
    public byte run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_char/Test_sget_char.java b/tools/vm-tests/src/dot/junit/opcodes/sget_char/Test_sget_char.java
//Synthetic comment -- index 1cf81b7..10324a5 100644

//Synthetic comment -- @@ -20,11 +20,16 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.sget_char.d.T_sget_char_1;
import dot.junit.opcodes.sget_char.d.T_sget_char_11;
import dot.junit.opcodes.sget_char.d.T_sget_char_12;
import dot.junit.opcodes.sget_char.d.T_sget_char_13;
import dot.junit.opcodes.sget_char.d.T_sget_char_5;
import dot.junit.opcodes.sget_char.d.T_sget_char_6;
import dot.junit.opcodes.sget_char.d.T_sget_char_7;
import dot.junit.opcodes.sget_char.d.T_sget_char_8;
import dot.junit.opcodes.sget_char.d.T_sget_char_9;

public class Test_sget_char extends DxTestCase {

/**
* @title get char from static field
*/
//Synthetic comment -- @@ -49,7 +54,7 @@
* @title attempt to access non-static field
*/
public void testE1() {

T_sget_char_5 t = new T_sget_char_5();
try {
t.run();
//Synthetic comment -- @@ -58,7 +63,7 @@
// expected
}
}

/**
* @title initialization of referenced class throws exception
*/
//Synthetic comment -- @@ -72,10 +77,10 @@
}
}



/**
     * @constraint A12
* @title constant pool index
*/
public void testVFE1() {
//Synthetic comment -- @@ -88,8 +93,8 @@
}

/**
     *
     * @constraint A23
* @title number of registers
*/
public void testVFE2() {
//Synthetic comment -- @@ -100,84 +105,75 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B13
     * @title read char from long field - only field with same name but
* different type exists
*/
public void testVFE3() {
try {
            new T_sget_char_13().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.sget_char.d.T_sget_char_6
//@uses dot.junit.opcodes.sget_char.TestStubs
try {
            new T_sget_char_6().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class.
*/
public void testVFE5() {
try {
            new T_sget_char_7().run();
            fail("expected NoClassDefFoundError");
        } catch (NoClassDefFoundError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field.
*/
public void testVFE6() {
try {
            new T_sget_char_8().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.sget_char.d.T_sget_char_12
//@uses dot.junit.opcodes.sget_char.d.T_sget_char_1
try {
            new T_sget_char_12().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}

/**
     * @constraint B1
* @title sget_char shall not work for reference fields
*/
public void testVFE8() {
//Synthetic comment -- @@ -188,10 +184,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_char shall not work for short fields
*/
public void testVFE9() {
//Synthetic comment -- @@ -202,10 +198,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_char shall not work for int fields
*/
public void testVFE10() {
//Synthetic comment -- @@ -216,10 +212,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_char shall not work for byte fields
*/
public void testVFE11() {
//Synthetic comment -- @@ -230,10 +226,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_char shall not work for boolean fields
*/
public void testVFE12() {
//Synthetic comment -- @@ -243,11 +239,11 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B1
* @title sget_char shall not work for double fields
*/
public void testVFE13() {
//Synthetic comment -- @@ -257,11 +253,11 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B1
* @title sget_char shall not work for long fields
*/
public void testVFE14() {
//Synthetic comment -- @@ -271,5 +267,5 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_char/d/T_sget_char_12.java b/tools/vm-tests/src/dot/junit/opcodes/sget_char/d/T_sget_char_12.java
new file mode 100644
//Synthetic comment -- index 0000000..f7affdf

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

package dot.junit.opcodes.sget_char.d;

public class T_sget_char_12 {
    public char run() {
        return ' ';
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_char/d/T_sget_char_13.java b/tools/vm-tests/src/dot/junit/opcodes/sget_char/d/T_sget_char_13.java
new file mode 100644
//Synthetic comment -- index 0000000..b347b53

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

package dot.junit.opcodes.sget_char.d;

public class T_sget_char_13 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_char/d/T_sget_char_6.java b/tools/vm-tests/src/dot/junit/opcodes/sget_char/d/T_sget_char_6.java
new file mode 100644
//Synthetic comment -- index 0000000..7fc0f38

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

package dot.junit.opcodes.sget_char.d;

public class T_sget_char_6 {
    public char run() {
        return ' ';
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_char/d/T_sget_char_7.java b/tools/vm-tests/src/dot/junit/opcodes/sget_char/d/T_sget_char_7.java
new file mode 100644
//Synthetic comment -- index 0000000..616d9f1

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

package dot.junit.opcodes.sget_char.d;

public class T_sget_char_7 {
    public char run() {
        return ' ';
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_char/d/T_sget_char_8.java b/tools/vm-tests/src/dot/junit/opcodes/sget_char/d/T_sget_char_8.java
new file mode 100644
//Synthetic comment -- index 0000000..78e63e9

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

package dot.junit.opcodes.sget_char.d;

public class T_sget_char_8 {
    public char run() {
        return ' ';
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_object/Test_sget_object.java b/tools/vm-tests/src/dot/junit/opcodes/sget_object/Test_sget_object.java
//Synthetic comment -- index 51d6c91..88aa4cb 100644

//Synthetic comment -- @@ -20,11 +20,17 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.sget_object.d.T_sget_object_1;
import dot.junit.opcodes.sget_object.d.T_sget_object_11;
import dot.junit.opcodes.sget_object.d.T_sget_object_12;
import dot.junit.opcodes.sget_object.d.T_sget_object_13;
import dot.junit.opcodes.sget_object.d.T_sget_object_21;
import dot.junit.opcodes.sget_object.d.T_sget_object_5;
import dot.junit.opcodes.sget_object.d.T_sget_object_6;
import dot.junit.opcodes.sget_object.d.T_sget_object_7;
import dot.junit.opcodes.sget_object.d.T_sget_object_8;
import dot.junit.opcodes.sget_object.d.T_sget_object_9;

public class Test_sget_object extends DxTestCase {

/**
* @title get object from static field
*/
//Synthetic comment -- @@ -49,7 +55,7 @@
* @title attempt to access non-static field
*/
public void testE1() {

T_sget_object_5 t = new T_sget_object_5();
try {
t.run();
//Synthetic comment -- @@ -58,7 +64,7 @@
// expected
}
}

/**
* @title initialization of referenced class throws exception
*/
//Synthetic comment -- @@ -72,10 +78,10 @@
}
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
//Synthetic comment -- @@ -100,84 +106,75 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B13
     * @title read object from long field - only field with same name but
* different type exists
*/
public void testVFE3() {
try {
            new T_sget_object_13().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.sget_object.d.T_sget_object_6
//@uses dot.junit.opcodes.sget_object.TestStubs
try {
            new T_sget_object_6().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class.
*/
public void testVFE5() {
try {
            new T_sget_object_7().run();
            fail("expected NoClassDefFoundError");
        } catch (NoClassDefFoundError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field.
*/
public void testVFE6() {
try {
            new T_sget_object_8().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.sget_object.d.T_sget_object_12
//@uses dot.junit.opcodes.sget_object.d.T_sget_object_1
try {
            new T_sget_object_12().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}

/**
     * @constraint B1
* @title sget_object shall not work for short fields
*/
public void testVFE8() {
//Synthetic comment -- @@ -188,10 +185,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_object shall not work for char fields
*/
public void testVFE9() {
//Synthetic comment -- @@ -202,10 +199,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_object shall not work for int fields
*/
public void testVFE10() {
//Synthetic comment -- @@ -216,10 +213,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_object shall not work for byte fields
*/
public void testVFE11() {
//Synthetic comment -- @@ -230,10 +227,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_object shall not work for boolean fields
*/
public void testVFE12() {
//Synthetic comment -- @@ -243,11 +240,11 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B1
* @title sget_object shall not work for double fields
*/
public void testVFE13() {
//Synthetic comment -- @@ -257,11 +254,11 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B1
* @title sget_object shall not work for long fields
*/
public void testVFE14() {
//Synthetic comment -- @@ -271,19 +268,18 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B13
* @title only field of different type exists)
*/
public void testVFE15() {
try {
            new T_sget_object_21().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_object/d/T_sget_object_12.java b/tools/vm-tests/src/dot/junit/opcodes/sget_object/d/T_sget_object_12.java
new file mode 100644
//Synthetic comment -- index 0000000..0978a26

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

package dot.junit.opcodes.sget_object.d;

public class T_sget_object_12 {
    public Object run() {
        return null;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_object/d/T_sget_object_13.java b/tools/vm-tests/src/dot/junit/opcodes/sget_object/d/T_sget_object_13.java
new file mode 100644
//Synthetic comment -- index 0000000..7fe275e

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

package dot.junit.opcodes.sget_object.d;

public class T_sget_object_13 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_object/d/T_sget_object_21.java b/tools/vm-tests/src/dot/junit/opcodes/sget_object/d/T_sget_object_21.java
new file mode 100644
//Synthetic comment -- index 0000000..0343390

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

package dot.junit.opcodes.sget_object.d;

public class T_sget_object_21 {
    public String run() {
        return null;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_object/d/T_sget_object_6.java b/tools/vm-tests/src/dot/junit/opcodes/sget_object/d/T_sget_object_6.java
new file mode 100644
//Synthetic comment -- index 0000000..84714b2

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

package dot.junit.opcodes.sget_object.d;

public class T_sget_object_6 {
    public Object run() {
        return null;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_object/d/T_sget_object_7.java b/tools/vm-tests/src/dot/junit/opcodes/sget_object/d/T_sget_object_7.java
new file mode 100644
//Synthetic comment -- index 0000000..56fbd35

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

package dot.junit.opcodes.sget_object.d;

public class T_sget_object_7 {
    public Object run() {
        return null;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_object/d/T_sget_object_8.java b/tools/vm-tests/src/dot/junit/opcodes/sget_object/d/T_sget_object_8.java
new file mode 100644
//Synthetic comment -- index 0000000..87afe69

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

package dot.junit.opcodes.sget_object.d;

public class T_sget_object_8 {
    public Object run() {
        return null;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_short/Test_sget_short.java b/tools/vm-tests/src/dot/junit/opcodes/sget_short/Test_sget_short.java
//Synthetic comment -- index 33a3c06..b38ef29 100644

//Synthetic comment -- @@ -20,11 +20,16 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.sget_short.d.T_sget_short_1;
import dot.junit.opcodes.sget_short.d.T_sget_short_11;
import dot.junit.opcodes.sget_short.d.T_sget_short_12;
import dot.junit.opcodes.sget_short.d.T_sget_short_13;
import dot.junit.opcodes.sget_short.d.T_sget_short_5;
import dot.junit.opcodes.sget_short.d.T_sget_short_6;
import dot.junit.opcodes.sget_short.d.T_sget_short_7;
import dot.junit.opcodes.sget_short.d.T_sget_short_8;
import dot.junit.opcodes.sget_short.d.T_sget_short_9;

public class Test_sget_short extends DxTestCase {

/**
* @title get short from static field
*/
//Synthetic comment -- @@ -49,7 +54,7 @@
* @title attempt to access non-static field
*/
public void testE1() {

T_sget_short_5 t = new T_sget_short_5();
try {
t.run();
//Synthetic comment -- @@ -58,7 +63,7 @@
// expected
}
}

/**
* @title initialization of referenced class throws exception
*/
//Synthetic comment -- @@ -72,10 +77,10 @@
}
}



/**
     * @constraint A12
* @title constant pool index
*/
public void testVFE1() {
//Synthetic comment -- @@ -88,8 +93,8 @@
}

/**
     *
     * @constraint A23
* @title number of registers
*/
public void testVFE2() {
//Synthetic comment -- @@ -100,84 +105,75 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B13
     * @title read short from long field - only field with same name but
* different type exists
*/
public void testVFE3() {
try {
            new T_sget_short_13().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.sget_short.d.T_sget_short_6
//@uses dot.junit.opcodes.sget_short.TestStubs
try {
            new T_sget_short_6().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class.
*/
public void testVFE5() {
try {
            new T_sget_short_7().run();
            fail("expected NoClassDefFoundError");
        } catch (NoClassDefFoundError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field.
*/
public void testVFE6() {
try {
            new T_sget_short_8().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.sget_short.d.T_sget_short_12
//@uses dot.junit.opcodes.sget_short.d.T_sget_short_1
try {
            new T_sget_short_12().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}

/**
     * @constraint B1
* @title sget_short shall not work for reference fields
*/
public void testVFE8() {
//Synthetic comment -- @@ -188,10 +184,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_short shall not work for char fields
*/
public void testVFE9() {
//Synthetic comment -- @@ -202,10 +198,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_short shall not work for int fields
*/
public void testVFE10() {
//Synthetic comment -- @@ -216,10 +212,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_short shall not work for byte fields
*/
public void testVFE11() {
//Synthetic comment -- @@ -230,10 +226,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget_short shall not work for boolean fields
*/
public void testVFE12() {
//Synthetic comment -- @@ -243,11 +239,11 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B1
* @title sget_short shall not work for double fields
*/
public void testVFE13() {
//Synthetic comment -- @@ -257,11 +253,11 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B1
* @title sget_short shall not work for long fields
*/
public void testVFE14() {
//Synthetic comment -- @@ -271,5 +267,5 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_short/d/T_sget_short_12.java b/tools/vm-tests/src/dot/junit/opcodes/sget_short/d/T_sget_short_12.java
new file mode 100644
//Synthetic comment -- index 0000000..ea41518

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

package dot.junit.opcodes.sget_short.d;

public class T_sget_short_12 {
    public short run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_short/d/T_sget_short_13.java b/tools/vm-tests/src/dot/junit/opcodes/sget_short/d/T_sget_short_13.java
new file mode 100644
//Synthetic comment -- index 0000000..4bc3105

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

package dot.junit.opcodes.sget_short.d;

public class T_sget_short_13 {
    public void run(){
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_short/d/T_sget_short_6.java b/tools/vm-tests/src/dot/junit/opcodes/sget_short/d/T_sget_short_6.java
new file mode 100644
//Synthetic comment -- index 0000000..1499a3c

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

package dot.junit.opcodes.sget_short.d;

public class T_sget_short_6 {
    public short run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_short/d/T_sget_short_7.java b/tools/vm-tests/src/dot/junit/opcodes/sget_short/d/T_sget_short_7.java
new file mode 100644
//Synthetic comment -- index 0000000..823e90d

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

package dot.junit.opcodes.sget_short.d;

public class T_sget_short_7 {
    public short run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_short/d/T_sget_short_8.java b/tools/vm-tests/src/dot/junit/opcodes/sget_short/d/T_sget_short_8.java
new file mode 100644
//Synthetic comment -- index 0000000..2c3ec32

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

package dot.junit.opcodes.sget_short.d;

public class T_sget_short_8 {
    public short run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_wide/Test_sget_wide.java b/tools/vm-tests/src/dot/junit/opcodes/sget_wide/Test_sget_wide.java
//Synthetic comment -- index b107ea8..8ecbeb7 100644

//Synthetic comment -- @@ -20,12 +20,17 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.sget_wide.d.T_sget_wide_1;
import dot.junit.opcodes.sget_wide.d.T_sget_wide_11;
import dot.junit.opcodes.sget_wide.d.T_sget_wide_12;
import dot.junit.opcodes.sget_wide.d.T_sget_wide_13;
import dot.junit.opcodes.sget_wide.d.T_sget_wide_2;
import dot.junit.opcodes.sget_wide.d.T_sget_wide_5;
import dot.junit.opcodes.sget_wide.d.T_sget_wide_6;
import dot.junit.opcodes.sget_wide.d.T_sget_wide_7;
import dot.junit.opcodes.sget_wide.d.T_sget_wide_8;
import dot.junit.opcodes.sget_wide.d.T_sget_wide_9;

public class Test_sget_wide extends DxTestCase {

/**
* @title type - long
*/
//Synthetic comment -- @@ -57,7 +62,7 @@
* @title attempt to access non-static field
*/
public void testE1() {

T_sget_wide_5 t = new T_sget_wide_5();
try {
t.run();
//Synthetic comment -- @@ -66,12 +71,12 @@
// expected
}
}

/**
* @title initialization of referenced class throws exception
*/
public void testE6() {

T_sget_wide_9 t = new T_sget_wide_9();
try {
t.run();
//Synthetic comment -- @@ -81,10 +86,10 @@
}
}



/**
     * @constraint A12
* @title constant pool index
*/
public void testVFE1() {
//Synthetic comment -- @@ -97,8 +102,8 @@
}

/**
     *
     * @constraint A23
* @title number of registers
*/
public void testVFE2() {
//Synthetic comment -- @@ -109,84 +114,75 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B13
     * @title read long from integer field - only field with same name but
* different type exists
*/
public void testVFE3() {
try {
            new T_sget_wide_13().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.sget_wide.d.T_sget_wide_6
//@uses dot.junit.opcodes.sget_wide.TestStubs
try {
            new T_sget_wide_6().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class.
*/
public void testVFE5() {
try {
            new T_sget_wide_7().run();
            fail("expected NoClassDefFoundError");
        } catch (NoClassDefFoundError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field.
*/
public void testVFE6() {
try {
            new T_sget_wide_8().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.sget_wide.d.T_sget_wide_12
//@uses dot.junit.opcodes.sget_wide.d.T_sget_wide_1
try {
            new T_sget_wide_12().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}

/**
     * @constraint B1
* @title sget-wide shall not work for reference fields
*/
public void testVFE8() {
//Synthetic comment -- @@ -197,10 +193,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget-wide shall not work for short fields
*/
public void testVFE9() {
//Synthetic comment -- @@ -211,10 +207,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget-wide shall not work for boolean fields
*/
public void testVFE10() {
//Synthetic comment -- @@ -225,10 +221,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget-wide shall not work for char fields
*/
public void testVFE11() {
//Synthetic comment -- @@ -239,10 +235,10 @@
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sget-wide shall not work for byte fields
*/
public void testVFE12() {
//Synthetic comment -- @@ -252,11 +248,11 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B1
* @title sget-wide shall not work for float fields
*/
public void testVFE13() {
//Synthetic comment -- @@ -266,11 +262,11 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }

/**
     *
     * @constraint B1
* @title sget-wide shall not work for int fields
*/
public void testVFE14() {
//Synthetic comment -- @@ -280,5 +276,5 @@
} catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_wide/d/T_sget_wide_12.java b/tools/vm-tests/src/dot/junit/opcodes/sget_wide/d/T_sget_wide_12.java
new file mode 100644
//Synthetic comment -- index 0000000..9dc55c6

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

package dot.junit.opcodes.sget_wide.d;

public class T_sget_wide_12 {
    public long run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_wide/d/T_sget_wide_13.java b/tools/vm-tests/src/dot/junit/opcodes/sget_wide/d/T_sget_wide_13.java
new file mode 100644
//Synthetic comment -- index 0000000..21fc969

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

package dot.junit.opcodes.sget_wide.d;

public class T_sget_wide_13 {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_wide/d/T_sget_wide_6.java b/tools/vm-tests/src/dot/junit/opcodes/sget_wide/d/T_sget_wide_6.java
new file mode 100644
//Synthetic comment -- index 0000000..f2744cf

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

package dot.junit.opcodes.sget_wide.d;

public class T_sget_wide_6 {
    public long run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_wide/d/T_sget_wide_7.java b/tools/vm-tests/src/dot/junit/opcodes/sget_wide/d/T_sget_wide_7.java
new file mode 100644
//Synthetic comment -- index 0000000..e97e416

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

package dot.junit.opcodes.sget_wide.d;

public class T_sget_wide_7 {
    public long run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sget_wide/d/T_sget_wide_8.java b/tools/vm-tests/src/dot/junit/opcodes/sget_wide/d/T_sget_wide_8.java
new file mode 100644
//Synthetic comment -- index 0000000..95b2dbb

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

package dot.junit.opcodes.sget_wide.d;

public class T_sget_wide_8 {
    public long run() {
        return 0;
    }
}








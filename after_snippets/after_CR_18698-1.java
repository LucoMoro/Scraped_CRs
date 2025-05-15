
//<Beginning of snippet n. 0>


import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.sput_short.d.T_sput_short_1;
import dot.junit.opcodes.sput_short.d.T_sput_short_10;
import dot.junit.opcodes.sput_short.d.T_sput_short_11;
import dot.junit.opcodes.sput_short.d.T_sput_short_12;
import dot.junit.opcodes.sput_short.d.T_sput_short_13;
import dot.junit.opcodes.sput_short.d.T_sput_short_14;
import dot.junit.opcodes.sput_short.d.T_sput_short_15;
import dot.junit.opcodes.sput_short.d.T_sput_short_17;
import dot.junit.opcodes.sput_short.d.T_sput_short_7;
import dot.junit.opcodes.sput_short.d.T_sput_short_8;
import dot.junit.opcodes.sput_short.d.T_sput_short_9;

public class Test_sput_short extends DxTestCase {
/**
assertEquals(77, T_sput_short_1.st_i1);
}


/**
* @title modification of final field
*/
}

/**
     * @constraint A12
* @title constant pool index
*/
public void testVFE1() {
}

/**
     *
     * @constraint A23
* @title number of registers
*/
public void testVFE2() {


/**
     *
     * @constraint B13
     * @title put short into long field - only field with same name but
* different type exists
*/
public void testVFE5() {
try {
            new T_sput_short_17().run();
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
}

/**
     *
     * @constraint B13
     * @title type of field doesn't match opcode - attempt to modify double
* field with single-width register
*/
public void testVFE7() {
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
             new T_sput_short_7().run();
             fail("expected IncompatibleClassChangeError");
         } catch (IncompatibleClassChangeError t) {
}
}

/**
* @constraint n/a
     * @title Attempt to modify inaccessible field.
*/
public void testVFE9() {
//@uses dot.junit.opcodes.sput_short.TestStubs
//@uses dot.junit.opcodes.sput_short.d.T_sput_short_8
try {
            new T_sput_short_8().run();
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
            new T_sput_short_9().run();
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
            new T_sput_short_10().run();
            fail("expected NoSuchFieldError");
        } catch (NoSuchFieldError t) {
}
}



/**
* @constraint n/a
     * @title Attempt to modify superclass' private field from subclass.
*/
public void testVFE12() {
//@uses dot.junit.opcodes.sput_short.d.T_sput_short_1
//@uses dot.junit.opcodes.sput_short.d.T_sput_short_15
try {
            new T_sput_short_15().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}


/**
     * @constraint B1
* @title sput-short shall not work for wide numbers
*/
public void testVFE13() {
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput-short shall not work for reference fields
*/
public void testVFE14() {
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput-short shall not work for char fields
*/
public void testVFE15() {
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput-short shall not work for int fields
*/
public void testVFE16() {
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput-short shall not work for byte fields
*/
public void testVFE17() {
DxUtil.checkVerifyException(t);
}
}

/**
     *
     * @constraint B1
* @title sput-short shall not work for boolean fields
*/
public void testVFE18() {
//@uses dot.junit.opcodes.sput_short.TestStubs
//@uses dot.junit.opcodes.sput_short.d.T_sput_short_11
	try {
            new T_sput_short_11().run();
            fail("expected IllegalAccessError");
        } catch (IllegalAccessError t) {
}
}
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>

new file mode 100644

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

package dot.junit.opcodes.sput_short.d;

public class T_sput_short_10 {
    public void run() {
    }
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>

new file mode 100644

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

package dot.junit.opcodes.sput_short.d;

public class T_sput_short_15 {
    public void run() {
    }
}

//<End of snippet n. 2>










//<Beginning of snippet n. 3>

new file mode 100644

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

package dot.junit.opcodes.sput_short.d;

public class T_sput_short_17 {
    public void run() {
    }
}

//<End of snippet n. 3>










//<Beginning of snippet n. 4>

new file mode 100644

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

package dot.junit.opcodes.sput_short.d;

public class T_sput_short_7 {
    public void run() {
    }
}

//<End of snippet n. 4>










//<Beginning of snippet n. 5>

new file mode 100644

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

package dot.junit.opcodes.sput_short.d;

public class T_sput_short_8 {
    public void run() {
    }
}

//<End of snippet n. 5>










//<Beginning of snippet n. 6>

new file mode 100644

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

package dot.junit.opcodes.sput_short.d;

public class T_sput_short_9 {
    public void run() {
    }
}

//<End of snippet n. 6>









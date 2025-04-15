/*Fix iget* opcode tests.

Bug 3147582

Change-Id:I59b57f02c5ec5a7005751d44ba6317606d23cb61*/




//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/Test_iget.java b/tools/vm-tests/src/dot/junit/opcodes/iget/Test_iget.java
//Synthetic comment -- index b5fcb67..50658ce 100644

//Synthetic comment -- @@ -20,7 +20,14 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.iget.d.T_iget_1;
import dot.junit.opcodes.iget.d.T_iget_11;
import dot.junit.opcodes.iget.d.T_iget_12;
import dot.junit.opcodes.iget.d.T_iget_13;
import dot.junit.opcodes.iget.d.T_iget_2;
import dot.junit.opcodes.iget.d.T_iget_21;
import dot.junit.opcodes.iget.d.T_iget_5;
import dot.junit.opcodes.iget.d.T_iget_6;
import dot.junit.opcodes.iget.d.T_iget_7;
import dot.junit.opcodes.iget.d.T_iget_8;
import dot.junit.opcodes.iget.d.T_iget_9;

public class Test_iget extends DxTestCase {
//Synthetic comment -- @@ -98,70 +105,66 @@
*/
public void testVFE3() {
try {
            new T_iget_13().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible private field.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.iget.d.T_iget_6
//@uses dot.junit.opcodes.iget.TestStubs
try {
            new T_iget_6().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class.
*/
public void testVFE5() {
try {
            new T_iget_7().run();
            fail("expected a NoClassDefFoundError exception");
        } catch (NoClassDefFoundError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field.
*/
public void testVFE6() {
try {
            new T_iget_8().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.iget.d.T_iget_12
//@uses dot.junit.opcodes.iget.d.T_iget_1
try {
            new T_iget_12().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}

//Synthetic comment -- @@ -264,37 +267,33 @@

/**
* @constraint B12
     * @title Attempt to read protected field of unrelated class.
*/
public void testVFE15() {
//@uses dot.junit.opcodes.iget.d.T_iget_21
//@uses dot.junit.opcodes.iget.TestStubs
try {
            new T_iget_21().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}

/**
* @constraint A11
     * @title Attempt to read static field.
*/
public void testVFE16() {
//@uses dot.junit.opcodes.iget.d.T_iget_5
        //@uses dot.junit.opcodes.iget.TestStubs
try {
            new T_iget_5().run();
            fail("expected an IncompatibleClassChangeError exception");
        } catch (IncompatibleClassChangeError e) {
            // expected
}
}

/**
* @constraint B6 








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_12.java b/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_12.java
new file mode 100644
//Synthetic comment -- index 0000000..b9f40ae

//Synthetic comment -- @@ -0,0 +1,25 @@
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

package dot.junit.opcodes.iget.d;

public class T_iget_12 extends T_iget_1 {

    @Override
    public int run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_13.java b/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_13.java
new file mode 100644
//Synthetic comment -- index 0000000..1038f56

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

package dot.junit.opcodes.iget.d;

public class T_iget_13 {

    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_21.java b/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_21.java
new file mode 100644
//Synthetic comment -- index 0000000..31db5f1

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget.d;

public class T_iget_21 {

    public int run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_5.java b/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_5.java
new file mode 100644
//Synthetic comment -- index 0000000..053f013

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget.d;

public class T_iget_5 {

    public int run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_6.java b/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_6.java
new file mode 100644
//Synthetic comment -- index 0000000..efb61d2

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget.d;

public class T_iget_6 {

    public int run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_7.java b/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_7.java
new file mode 100644
//Synthetic comment -- index 0000000..54b848f

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget.d;

public class T_iget_7 {

    public int run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_8.java b/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_8.java
new file mode 100644
//Synthetic comment -- index 0000000..249b4d0

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget.d;

public class T_iget_8 {

    public int run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/Test_iget_boolean.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/Test_iget_boolean.java
//Synthetic comment -- index 2b24d6d..b23f330 100644

//Synthetic comment -- @@ -20,6 +20,13 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.iget_boolean.d.T_iget_boolean_1;
import dot.junit.opcodes.iget_boolean.d.T_iget_boolean_11;
import dot.junit.opcodes.iget_boolean.d.T_iget_boolean_12;
import dot.junit.opcodes.iget_boolean.d.T_iget_boolean_13;
import dot.junit.opcodes.iget_boolean.d.T_iget_boolean_21;
import dot.junit.opcodes.iget_boolean.d.T_iget_boolean_5;
import dot.junit.opcodes.iget_boolean.d.T_iget_boolean_6;
import dot.junit.opcodes.iget_boolean.d.T_iget_boolean_7;
import dot.junit.opcodes.iget_boolean.d.T_iget_boolean_8;
import dot.junit.opcodes.iget_boolean.d.T_iget_boolean_9;

public class Test_iget_boolean extends DxTestCase {
//Synthetic comment -- @@ -91,70 +98,66 @@
*/
public void testVFE3() {
try {
            new T_iget_boolean_13().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field
*/
public void testVFE4() {
//@uses dot.junit.opcodes.iget_boolean.d.T_iget_boolean_6
//@uses dot.junit.opcodes.iget_boolean.TestStubs
try {
            new T_iget_boolean_6().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class.
*/
public void testVFE5() {
try {
            new T_iget_boolean_7().run();
            fail("expected a NoClassDefFoundError exception");
        } catch (NoClassDefFoundError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field.
*/
public void testVFE6() {
try {
            new T_iget_boolean_8().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.iget_boolean.d.T_iget_boolean_12
//@uses dot.junit.opcodes.iget_boolean.d.T_iget_boolean_1
try {
            new T_iget_boolean_12().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}

//Synthetic comment -- @@ -252,35 +255,32 @@

/**
* @constraint B12
     * @title Attempt to read inaccessible protected field.
*/
public void testVFE15() {
//@uses dot.junit.opcodes.iget_boolean.d.T_iget_boolean_21
//@uses dot.junit.opcodes.iget_boolean.TestStubs
try {
            new T_iget_boolean_21().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}


/**
* @constraint A11
     * @title Attempt to read static field.
*/
public void testVFE16() {
//@uses dot.junit.opcodes.iget_boolean.d.T_iget_boolean_5
//@uses dot.junit.opcodes.iget_boolean.TestStubs
try {
            new T_iget_boolean_5().run();
            fail("expected an IncompatibleClassChangeError exception");
        } catch (IncompatibleClassChangeError e) {
            // expected
}
}

//Synthetic comment -- @@ -297,4 +297,3 @@
}
}
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_12.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_12.java
new file mode 100644
//Synthetic comment -- index 0000000..5af996f

//Synthetic comment -- @@ -0,0 +1,25 @@
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

package dot.junit.opcodes.iget_boolean.d;

public class T_iget_boolean_12 extends T_iget_boolean_1 {

    @Override
    public boolean run(){
        return false;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_13.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_13.java
new file mode 100644
//Synthetic comment -- index 0000000..b652090

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

package dot.junit.opcodes.iget_boolean.d;

public class T_iget_boolean_13 {

    public void run(){
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_21.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_21.java
new file mode 100644
//Synthetic comment -- index 0000000..74e2da1

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_boolean.d;

public class T_iget_boolean_21 {

    public boolean run(){
        return false;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_5.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_5.java
new file mode 100644
//Synthetic comment -- index 0000000..5b275aa

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_boolean.d;

public class T_iget_boolean_5 {

    public boolean run(){
        return false;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_6.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_6.java
new file mode 100644
//Synthetic comment -- index 0000000..0d91cab

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_boolean.d;

public class T_iget_boolean_6 {

    public boolean run(){
        return false;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_7.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_7.java
new file mode 100644
//Synthetic comment -- index 0000000..3ec333b

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_boolean.d;

public class T_iget_boolean_7 {

    public boolean run(){
        return false;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_8.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_8.java
new file mode 100644
//Synthetic comment -- index 0000000..b87cf8d

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_boolean.d;

public class T_iget_boolean_8 {

    public boolean run(){
        return false;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/Test_iget_byte.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/Test_iget_byte.java
//Synthetic comment -- index 5d8630b..de48192 100644

//Synthetic comment -- @@ -20,6 +20,13 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.iget_byte.d.T_iget_byte_1;
import dot.junit.opcodes.iget_byte.d.T_iget_byte_11;
import dot.junit.opcodes.iget_byte.d.T_iget_byte_12;
import dot.junit.opcodes.iget_byte.d.T_iget_byte_13;
import dot.junit.opcodes.iget_byte.d.T_iget_byte_21;
import dot.junit.opcodes.iget_byte.d.T_iget_byte_5;
import dot.junit.opcodes.iget_byte.d.T_iget_byte_6;
import dot.junit.opcodes.iget_byte.d.T_iget_byte_7;
import dot.junit.opcodes.iget_byte.d.T_iget_byte_8;
import dot.junit.opcodes.iget_byte.d.T_iget_byte_9;

public class Test_iget_byte extends DxTestCase {
//Synthetic comment -- @@ -89,70 +96,66 @@
*/
public void testVFE3() {
try {
            new T_iget_byte_13().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.iget_byte.d.T_iget_byte_6
//@uses dot.junit.opcodes.iget_byte.TestStubs
try {
            new T_iget_byte_6().run();
            fail("expected an IllegalAccessError exception");
        }  catch (IllegalAccessError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class.
*/
public void testVFE5() {
try {
            new T_iget_byte_7().run();
            fail("expected a NoClassDefFoundError exception");
        } catch (NoClassDefFoundError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field.
*/
public void testVFE6() {
try {
            new T_iget_byte_8().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.iget_byte.d.T_iget_byte_12
//@uses dot.junit.opcodes.iget_byte.d.T_iget_byte_1
try {
            new T_iget_byte_12().run();
            fail("expected an IllegalAccessError exception");
        }  catch (IllegalAccessError e) {
            // expected
}
}

//Synthetic comment -- @@ -249,34 +252,32 @@

/**
* @constraint B12
     * @title Attempt to read inaccessible protected field.
*/
public void testVFE15() {
//@uses dot.junit.opcodes.iget_byte.d.T_iget_byte_21
//@uses dot.junit.opcodes.iget_byte.TestStubs
try {
            new T_iget_byte_21().run();
            fail("expected an IllegalAccessError exception");
        }  catch (IllegalAccessError e) {
            // expected
}
}


/**
* @constraint A11
     * @title Attempt to read static  field.
*/
public void testVFE16() {
//@uses dot.junit.opcodes.iget_byte.d.T_iget_byte_5
//@uses dot.junit.opcodes.iget_byte.TestStubs        
try {
            new T_iget_byte_5().run();
            fail("expected an IncompatibleClassChangeError exception");
        }  catch (IncompatibleClassChangeError e) {
            // expected
}
}

//Synthetic comment -- @@ -293,4 +294,3 @@
}
}
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_12.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_12.java
new file mode 100644
//Synthetic comment -- index 0000000..3927a5b

//Synthetic comment -- @@ -0,0 +1,25 @@
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

package dot.junit.opcodes.iget_byte.d;

public class T_iget_byte_12  extends T_iget_byte_1 {

    @Override
    public byte run() {
        return p1;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_13.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_13.java
new file mode 100644
//Synthetic comment -- index 0000000..755b896

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

package dot.junit.opcodes.iget_byte.d;

public class T_iget_byte_13  {

    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_21.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_21.java
new file mode 100644
//Synthetic comment -- index 0000000..10deb39

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_byte.d;

public class T_iget_byte_21 {

    public byte run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_5.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_5.java
new file mode 100644
//Synthetic comment -- index 0000000..75f7a5c

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_byte.d;

public class T_iget_byte_5 {

    public byte run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_6.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_6.java
new file mode 100644
//Synthetic comment -- index 0000000..20af80f

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_byte.d;

public class T_iget_byte_6  {

    public byte run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_7.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_7.java
new file mode 100644
//Synthetic comment -- index 0000000..f13071d

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_byte.d;

public class T_iget_byte_7  {

    public byte run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_8.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_8.java
new file mode 100644
//Synthetic comment -- index 0000000..6f89bf3

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_byte.d;

public class T_iget_byte_8  {

    public byte run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/Test_iget_char.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/Test_iget_char.java
//Synthetic comment -- index 1805534..c7516e8 100644

//Synthetic comment -- @@ -20,6 +20,13 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.iget_char.d.T_iget_char_1;
import dot.junit.opcodes.iget_char.d.T_iget_char_11;
import dot.junit.opcodes.iget_char.d.T_iget_char_12;
import dot.junit.opcodes.iget_char.d.T_iget_char_13;
import dot.junit.opcodes.iget_char.d.T_iget_char_21;
import dot.junit.opcodes.iget_char.d.T_iget_char_5;
import dot.junit.opcodes.iget_char.d.T_iget_char_6;
import dot.junit.opcodes.iget_char.d.T_iget_char_7;
import dot.junit.opcodes.iget_char.d.T_iget_char_8;
import dot.junit.opcodes.iget_char.d.T_iget_char_9;

public class Test_iget_char extends DxTestCase {
//Synthetic comment -- @@ -92,70 +99,66 @@
*/
public void testVFE3() {
try {
            new T_iget_char_13().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError t) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.iget_char.d.T_iget_char_6
//@uses dot.junit.opcodes.iget_char.TestStubs
try {
            new T_iget_char_6().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError t) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class.
*/
public void testVFE5() {
try {
            new T_iget_char_7().run();
            fail("expected a NoClassDefFoundError exception");
        } catch (NoClassDefFoundError t) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field.
*/
public void testVFE6() {
try {
            new T_iget_char_8().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError t) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.iget_char.d.T_iget_char_12
//@uses dot.junit.opcodes.iget_char.d.T_iget_char_1
try {
            new T_iget_char_12().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError t) {
            // expected
}
}

//Synthetic comment -- @@ -256,34 +259,32 @@

/**
* @constraint B12
     * @title Attempt to read inaccessible protected field.
*/
public void testVFE15() {
//@uses dot.junit.opcodes.iget_char.d.T_iget_char_21
//@uses dot.junit.opcodes.iget_char.TestStubs
try {
            new T_iget_char_21().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError t) {
            // expected
}
}


/**
* @constraint A11
     * @title Attempt to read static  field.
*/
public void testVFE16() {
//@uses dot.junit.opcodes.iget_char.d.T_iget_char_5
//@uses dot.junit.opcodes.iget_char.TestStubs
try {
            new T_iget_char_5().run();
            fail("expected an IncompatibleClassChangeError exception");
        } catch (IncompatibleClassChangeError t) {
            // expected
}
}

//Synthetic comment -- @@ -301,4 +302,3 @@
}
}
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_12.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_12.java
new file mode 100644
//Synthetic comment -- index 0000000..611b24b

//Synthetic comment -- @@ -0,0 +1,25 @@
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

package dot.junit.opcodes.iget_char.d;

public class T_iget_char_12 extends T_iget_char_1 {

    @Override
    public char run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_13.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_13.java
new file mode 100644
//Synthetic comment -- index 0000000..33cae33

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

package dot.junit.opcodes.iget_char.d;

public class T_iget_char_13 {

    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_21.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_21.java
new file mode 100644
//Synthetic comment -- index 0000000..6517824

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_char.d;

public class T_iget_char_21 {

    public char run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_5.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_5.java
new file mode 100644
//Synthetic comment -- index 0000000..4975e76

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_char.d;

public class T_iget_char_5 {

    public char run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_6.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_6.java
new file mode 100644
//Synthetic comment -- index 0000000..8be8c2b

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_char.d;

public class T_iget_char_6 {

    public char run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_7.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_7.java
new file mode 100644
//Synthetic comment -- index 0000000..4ca80d7

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_char.d;

public class T_iget_char_7 {

    public char run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_8.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_8.java
new file mode 100644
//Synthetic comment -- index 0000000..49d35c6

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_char.d;

public class T_iget_char_8 {

    public char run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/Test_iget_object.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/Test_iget_object.java
//Synthetic comment -- index 7c6e03c..3a735b6 100644

//Synthetic comment -- @@ -20,6 +20,14 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.iget_object.d.T_iget_object_1;
import dot.junit.opcodes.iget_object.d.T_iget_object_11;
import dot.junit.opcodes.iget_object.d.T_iget_object_12;
import dot.junit.opcodes.iget_object.d.T_iget_object_13;
import dot.junit.opcodes.iget_object.d.T_iget_object_21;
import dot.junit.opcodes.iget_object.d.T_iget_object_22;
import dot.junit.opcodes.iget_object.d.T_iget_object_5;
import dot.junit.opcodes.iget_object.d.T_iget_object_6;
import dot.junit.opcodes.iget_object.d.T_iget_object_7;
import dot.junit.opcodes.iget_object.d.T_iget_object_8;
import dot.junit.opcodes.iget_object.d.T_iget_object_9;

public class Test_iget_object extends DxTestCase {
//Synthetic comment -- @@ -91,70 +99,66 @@
*/
public void testVFE3() {
try {
            new T_iget_object_13().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.iget_object.d.T_iget_object_6
//@uses dot.junit.opcodes.iget_object.TestStubs
try {
            new T_iget_object_6().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class.
*/
public void testVFE5() {
try {
            new T_iget_object_7().run();
            fail("expected a NoClassDefFoundError exception");
        } catch (NoClassDefFoundError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field.
*/
public void testVFE6() {
try {
            new T_iget_object_8().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.iget_object.d.T_iget_object_12
//@uses dot.junit.opcodes.iget_object.d.T_iget_object_1
try {
            new T_iget_object_12().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}

//Synthetic comment -- @@ -262,43 +266,40 @@
*/
public void testVFE15() {
try {
            new T_iget_object_21().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError e) {
            // expected
}
}

/**
* @constraint B12
     * @title Attempt to read inaccessible protected field.
*/
public void testVFE16() {
//@uses dot.junit.opcodes.iget_object.d.T_iget_object_22
//@uses dot.junit.opcodes.iget_object.TestStubs
try {
            new T_iget_object_22().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}

/**
* @constraint A11
     * @title Attempt to read static field.
*/
public void testVFE17() {
//@uses dot.junit.opcodes.iget_object.d.T_iget_object_5
        //@uses dot.junit.opcodes.iget_object.TestStubs
try {
            new T_iget_object_5().run();
            fail("expected an IncompatibleClassChangeError exception");
        } catch (IncompatibleClassChangeError e) {
            // expected
}
}

//Synthetic comment -- @@ -315,4 +316,3 @@
}
}
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_12.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_12.java
new file mode 100644
//Synthetic comment -- index 0000000..f98c36a

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_object.d;

public class T_iget_object_12  extends T_iget_object_1 {

    public Object run() {
        return null;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_13.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_13.java
new file mode 100644
//Synthetic comment -- index 0000000..85a8761

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

package dot.junit.opcodes.iget_object.d;

public class T_iget_object_13  {

    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_21.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_21.java
new file mode 100644
//Synthetic comment -- index 0000000..e301893

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_object.d;

public class T_iget_object_21  {

    public String run() {
        return null;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_22.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_22.java
new file mode 100644
//Synthetic comment -- index 0000000..d9ad933

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_object.d;

public class T_iget_object_22  {

    public Object run() {
        return null;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_5.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_5.java
new file mode 100644
//Synthetic comment -- index 0000000..3626499

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_object.d;

public class T_iget_object_5  {

    public Object run() {
        return null;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_6.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_6.java
new file mode 100644
//Synthetic comment -- index 0000000..a4ba515

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_object.d;

public class T_iget_object_6  {

    public Object run() {
        return null;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_7.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_7.java
new file mode 100644
//Synthetic comment -- index 0000000..2940ca4

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_object.d;

public class T_iget_object_7  {

    public Object run() {
        return null;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_8.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_8.java
new file mode 100644
//Synthetic comment -- index 0000000..3b5c4d7

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_object.d;

public class T_iget_object_8  {

    public Object run() {
        return null;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/Test_iget_short.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/Test_iget_short.java
//Synthetic comment -- index aaa8270..a7d3658 100644

//Synthetic comment -- @@ -20,6 +20,13 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.iget_short.d.T_iget_short_1;
import dot.junit.opcodes.iget_short.d.T_iget_short_11;
import dot.junit.opcodes.iget_short.d.T_iget_short_12;
import dot.junit.opcodes.iget_short.d.T_iget_short_13;
import dot.junit.opcodes.iget_short.d.T_iget_short_21;
import dot.junit.opcodes.iget_short.d.T_iget_short_5;
import dot.junit.opcodes.iget_short.d.T_iget_short_6;
import dot.junit.opcodes.iget_short.d.T_iget_short_7;
import dot.junit.opcodes.iget_short.d.T_iget_short_8;
import dot.junit.opcodes.iget_short.d.T_iget_short_9;

public class Test_iget_short extends DxTestCase {
//Synthetic comment -- @@ -93,70 +100,66 @@
*/
public void testVFE3() {
try {
            new T_iget_short_13().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.iget_short.d.T_iget_short_6
//@uses dot.junit.opcodes.iget_short.TestStubs
try {
            new T_iget_short_6().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class.
*/
public void testVFE5() {
try {
            new T_iget_short_7().run();
            fail("expected a NoClassDefFoundError exception");
        } catch (NoClassDefFoundError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field.
*/
public void testVFE6() {
try {
            new T_iget_short_8().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.iget_short.d.T_iget_short_12
//@uses dot.junit.opcodes.iget_short.d.T_iget_short_1
try {
            new T_iget_short_12().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}

//Synthetic comment -- @@ -259,34 +262,32 @@

/**
* @constraint B12
     * @title Attempt to read inaccessible protected field.
*/
public void testVFE15() {
//@uses dot.junit.opcodes.iget_short.d.T_iget_short_21
//@uses dot.junit.opcodes.iget_short.TestStubs
try {
            new T_iget_short_21().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}


/**
* @constraint A11
     * @title Attempt to read static  field.
*/
public void testVFE16() {
//@uses dot.junit.opcodes.iget_short.d.T_iget_short_5
        //@uses dot.junit.opcodes.iget_short.TestStubs
try {
            new T_iget_short_5().run();
            fail("expected an IncompatibleClassChangeError exception");
        } catch (IncompatibleClassChangeError e) {
            // expected
}
}

//Synthetic comment -- @@ -303,4 +304,3 @@
}
}
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_12.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_12.java
new file mode 100644
//Synthetic comment -- index 0000000..be322f3

//Synthetic comment -- @@ -0,0 +1,25 @@
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

package dot.junit.opcodes.iget_short.d;

public class T_iget_short_12 extends T_iget_short_1 {

    @Override
    public short run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_13.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_13.java
new file mode 100644
//Synthetic comment -- index 0000000..3b27d6c

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

package dot.junit.opcodes.iget_short.d;

public class T_iget_short_13 {

    public void run(){
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_21.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_21.java
//Synthetic comment -- index 2c7fd32..5bb7f6e 100644

//Synthetic comment -- @@ -18,4 +18,6 @@

public class T_iget_short_21 {

    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_5.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_5.java
new file mode 100644
//Synthetic comment -- index 0000000..37c43519

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_short.d;

public class T_iget_short_5 {

    public short run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_6.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_6.java
new file mode 100644
//Synthetic comment -- index 0000000..5ac127b

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_short.d;

public class T_iget_short_6 {

    public short run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_7.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_7.java
new file mode 100644
//Synthetic comment -- index 0000000..7c81be5

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_short.d;

public class T_iget_short_7 {

    public short run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_8.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_8.java
new file mode 100644
//Synthetic comment -- index 0000000..f22a81e

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_short.d;

public class T_iget_short_8 {

    public short run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/Test_iget_wide.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/Test_iget_wide.java
//Synthetic comment -- index 2260220..7e42d32 100644

//Synthetic comment -- @@ -20,7 +20,14 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_1;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_11;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_12;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_13;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_2;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_21;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_5;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_6;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_7;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_8;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_9;

public class Test_iget_wide extends DxTestCase {
//Synthetic comment -- @@ -99,70 +106,66 @@
*/
public void testVFE3() {
try {
            new T_iget_wide_13().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.iget_wide.d.T_iget_wide_6
//@uses dot.junit.opcodes.iget_wide.TestStubs
try {
            new T_iget_wide_6().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class. 
*/
public void testVFE5() {
try {
            new T_iget_wide_7().run();
            fail("expected a NoClassDefFoundError exception");
        } catch (NoClassDefFoundError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field.
*/
public void testVFE6() {
try {
            new T_iget_wide_8().run();
            fail("expected a NoSuchFieldError exception");
        } catch (NoSuchFieldError e) {
            // expected
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.iget_wide.d.T_iget_wide_12
//@uses dot.junit.opcodes.iget_wide.d.T_iget_wide_1
try {
            new T_iget_wide_12().run();
            fail("expected a IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}

//Synthetic comment -- @@ -265,34 +268,31 @@

/**
* @constraint B12
     * @title Attempt to read inaccessible protected field.
*/
public void testVFE15() {
//@uses dot.junit.opcodes.iget_wide.d.T_iget_wide_21
        //@uses dot.junit.opcodes.iget_wide.TestStubs
try {
            new T_iget_wide_21().run();
            fail("expected an IllegalAccessError exception");
        } catch (IllegalAccessError e) {
            // expected
}
}

/**
* @constraint A11
     * @title Attempt to read static  field.
*/
public void testVFE16() {
//@uses dot.junit.opcodes.iget_wide.d.T_iget_wide_5
        //@uses dot.junit.opcodes.iget_wide.TestStubs
try {
            new T_iget_wide_5().run();
            fail("expected a IncompatibleClassChangeError exception");
        } catch (IncompatibleClassChangeError e) {
            // expected
}
}

//Synthetic comment -- @@ -309,4 +309,3 @@
}
}
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_12.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_12.java
new file mode 100644
//Synthetic comment -- index 0000000..af3cbda

//Synthetic comment -- @@ -0,0 +1,25 @@
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

package dot.junit.opcodes.iget_wide.d;

public class T_iget_wide_12 extends T_iget_wide_1 {

    @Override
    public long run() {
        return -99;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_13.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_13.java
new file mode 100644
//Synthetic comment -- index 0000000..98086a6

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

package dot.junit.opcodes.iget_wide.d;

public class T_iget_wide_13 {

    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_21.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_21.java
new file mode 100644
//Synthetic comment -- index 0000000..affeadf

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_wide.d;

public class T_iget_wide_21 {

    public long run() {
        return -99;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_5.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_5.java
new file mode 100644
//Synthetic comment -- index 0000000..c4c130e

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_wide.d;

public class T_iget_wide_5 {

    public long run() {
        return -99;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_6.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_6.java
new file mode 100644
//Synthetic comment -- index 0000000..649795f

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_wide.d;

public class T_iget_wide_6 {

    public long run() {
        return -99;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_7.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_7.java
new file mode 100644
//Synthetic comment -- index 0000000..0866645

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_wide.d;

public class T_iget_wide_7 {

    public long run() {
        return -99;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_8.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_8.java
new file mode 100644
//Synthetic comment -- index 0000000..570764d

//Synthetic comment -- @@ -0,0 +1,24 @@
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

package dot.junit.opcodes.iget_wide.d;

public class T_iget_wide_8 {

    public long run() {
        return -99;
    }
}








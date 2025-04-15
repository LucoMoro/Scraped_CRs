/*Fix more opcode tests for invoke_*

...this is not ready yet.

Bug 3147582

Change-Id:I98200ee239d0bd52cbb1b2a3a8a093c0b248aa40*/




//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_super/Test_invoke_super.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_super/Test_invoke_super.java
//Synthetic comment -- index 6efac8b..904c72d 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.invoke_super.d.T_invoke_super_1;
import dot.junit.opcodes.invoke_super.d.T_invoke_super_10;
import dot.junit.opcodes.invoke_super.d.T_invoke_super_13;
import dot.junit.opcodes.invoke_super.d.T_invoke_super_14;
import dot.junit.opcodes.invoke_super.d.T_invoke_super_15;
import dot.junit.opcodes.invoke_super.d.T_invoke_super_17;
//Synthetic comment -- @@ -299,10 +300,9 @@
//@uses dot.junit.opcodes.invoke_super.d.T_invoke_super_13
//@uses dot.junit.opcodes.invoke_super.d.TSuper
try {
             new T_invoke_super_13().run();
             fail("expected IllegalAccessError");
         } catch (IllegalAccessError t) {
}
}









//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_super/d/T_invoke_super_13.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_super/d/T_invoke_super_13.java
new file mode 100644
//Synthetic comment -- index 0000000..f0860bd

//Synthetic comment -- @@ -0,0 +1,26 @@
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

package dot.junit.opcodes.invoke_super.d;

public class T_invoke_super_13 extends TSuper {
    private int toIntPvt() {
        return 0;
    }
    public int run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_super_range/Test_invoke_super_range.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_super_range/Test_invoke_super_range.java
//Synthetic comment -- index d65b8d4..e7eb2e1 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_1;
import dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_10;
import dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_13;
import dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_14;
import dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_15;
import dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_17;
//Synthetic comment -- @@ -299,10 +300,9 @@
//@uses dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_13
//@uses dot.junit.opcodes.invoke_super_range.d.TSuper
try {
             new T_invoke_super_range_13().run();
             fail("expected NoSuchMethodError");
         } catch (NoSuchMethodError t) {
}
}









//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_super_range/d/T_invoke_super_range_13.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_super_range/d/T_invoke_super_range_13.java
new file mode 100644
//Synthetic comment -- index 0000000..95b62e8

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

package dot.junit.opcodes.invoke_super_range.d;

public class T_invoke_super_range_13 extends TSuper {
    public void run() {
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual/Test_invoke_virtual.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual/Test_invoke_virtual.java
//Synthetic comment -- index 6e41ec1..4129694 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.invoke_virtual.d.T_invoke_virtual_1;
import dot.junit.opcodes.invoke_virtual.d.T_invoke_virtual_10;
import dot.junit.opcodes.invoke_virtual.d.T_invoke_virtual_13;
import dot.junit.opcodes.invoke_virtual.d.T_invoke_virtual_14;
import dot.junit.opcodes.invoke_virtual.d.T_invoke_virtual_15;
import dot.junit.opcodes.invoke_virtual.d.T_invoke_virtual_17;
//Synthetic comment -- @@ -290,12 +291,12 @@
* @title invoke-virtual shall be used to invoke private methods
*/
public void testVFE16() {
//         try {
             new T_invoke_virtual_13().run();
//             fail("expected a verification exception");
//         } catch (Throwable t) {
//             DxUtil.checkVerifyException(t);
//         }
}

/**








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual/d/T_invoke_virtual_13.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual/d/T_invoke_virtual_13.java
new file mode 100644
//Synthetic comment -- index 0000000..501b161

//Synthetic comment -- @@ -0,0 +1,26 @@
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

package dot.junit.opcodes.invoke_virtual.d;

public class T_invoke_virtual_13 {
    private int getInt() {
        return 0;
    }
    public int run() {
        return 0;
    }
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual_range/Test_invoke_virtual_range.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual_range/Test_invoke_virtual_range.java
//Synthetic comment -- index 2368dc3..f537f37 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.invoke_virtual_range.d.T_invoke_virtual_range_1;
import dot.junit.opcodes.invoke_virtual_range.d.T_invoke_virtual_range_10;
import dot.junit.opcodes.invoke_virtual_range.d.T_invoke_virtual_range_13;
import dot.junit.opcodes.invoke_virtual_range.d.T_invoke_virtual_range_14;
import dot.junit.opcodes.invoke_virtual_range.d.T_invoke_virtual_range_15;
import dot.junit.opcodes.invoke_virtual_range.d.T_invoke_virtual_range_17;
//Synthetic comment -- @@ -298,12 +299,12 @@
* @title invoke-virtual/range shall be used to invoke private methods
*/
public void testVFE16() {
//         try {
             new T_invoke_virtual_range_13().run();
//             fail("expected a verification exception");
//         } catch (Throwable t) {
//             DxUtil.checkVerifyException(t);
//         }
}

/**








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual_range/d/T_invoke_virtual_range_13.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual_range/d/T_invoke_virtual_range_13.java
new file mode 100644
//Synthetic comment -- index 0000000..907b34c

//Synthetic comment -- @@ -0,0 +1,26 @@
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

package dot.junit.opcodes.invoke_virtual_range.d;

public class T_invoke_virtual_range_13 {
    private int getInt() {
        return 0;
    }
    public int run() {
        return 0;
    }
}








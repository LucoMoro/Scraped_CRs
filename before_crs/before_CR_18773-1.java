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
import dot.junit.opcodes.invoke_super.d.T_invoke_super_14;
import dot.junit.opcodes.invoke_super.d.T_invoke_super_15;
import dot.junit.opcodes.invoke_super.d.T_invoke_super_17;
//Synthetic comment -- @@ -299,10 +300,9 @@
//@uses dot.junit.opcodes.invoke_super.d.T_invoke_super_13
//@uses dot.junit.opcodes.invoke_super.d.TSuper
try {
             Class.forName("dot.junit.opcodes.invoke_super.d.T_invoke_super_13");
             fail("expected a verification exception");
         } catch (Throwable t) {
             DxUtil.checkVerifyException(t);
}
}









//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_super/d/T_invoke_super_13.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_super/d/T_invoke_super_13.java
new file mode 100644
//Synthetic comment -- index 0000000..f0860bd

//Synthetic comment -- @@ -0,0 +1,26 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_super_range/Test_invoke_super_range.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_super_range/Test_invoke_super_range.java
//Synthetic comment -- index d65b8d4..e7eb2e1 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_1;
import dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_10;
import dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_14;
import dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_15;
import dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_17;
//Synthetic comment -- @@ -299,10 +300,9 @@
//@uses dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_13
//@uses dot.junit.opcodes.invoke_super_range.d.TSuper
try {
             Class.forName("dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_13");
             fail("expected a verification exception");
         } catch (Throwable t) {
             DxUtil.checkVerifyException(t);
}
}









//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_super_range/d/T_invoke_super_range_13.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_super_range/d/T_invoke_super_range_13.java
new file mode 100644
//Synthetic comment -- index 0000000..95b62e8

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual/Test_invoke_virtual.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual/Test_invoke_virtual.java
//Synthetic comment -- index 6e41ec1..4129694 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.invoke_virtual.d.T_invoke_virtual_1;
import dot.junit.opcodes.invoke_virtual.d.T_invoke_virtual_10;
import dot.junit.opcodes.invoke_virtual.d.T_invoke_virtual_14;
import dot.junit.opcodes.invoke_virtual.d.T_invoke_virtual_15;
import dot.junit.opcodes.invoke_virtual.d.T_invoke_virtual_17;
//Synthetic comment -- @@ -290,12 +291,12 @@
* @title invoke-virtual shall be used to invoke private methods
*/
public void testVFE16() {
         try {
             Class.forName("dot.junit.opcodes.invoke_virtual.d.T_invoke_virtual_13");
             fail("expected a verification exception");
         } catch (Throwable t) {
             DxUtil.checkVerifyException(t);
         }
}

/**








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual/d/T_invoke_virtual_13.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual/d/T_invoke_virtual_13.java
new file mode 100644
//Synthetic comment -- index 0000000..501b161

//Synthetic comment -- @@ -0,0 +1,26 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual_range/Test_invoke_virtual_range.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual_range/Test_invoke_virtual_range.java
//Synthetic comment -- index 2368dc3..f537f37 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.invoke_virtual_range.d.T_invoke_virtual_range_1;
import dot.junit.opcodes.invoke_virtual_range.d.T_invoke_virtual_range_10;
import dot.junit.opcodes.invoke_virtual_range.d.T_invoke_virtual_range_14;
import dot.junit.opcodes.invoke_virtual_range.d.T_invoke_virtual_range_15;
import dot.junit.opcodes.invoke_virtual_range.d.T_invoke_virtual_range_17;
//Synthetic comment -- @@ -298,12 +299,12 @@
* @title invoke-virtual/range shall be used to invoke private methods
*/
public void testVFE16() {
         try {
             Class.forName("dot.junit.opcodes.invoke_virtual_range.d.T_invoke_virtual_range_13");
             fail("expected a verification exception");
         } catch (Throwable t) {
             DxUtil.checkVerifyException(t);
         }
}

/**








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual_range/d/T_invoke_virtual_range_13.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual_range/d/T_invoke_virtual_range_13.java
new file mode 100644
//Synthetic comment -- index 0000000..907b34c

//Synthetic comment -- @@ -0,0 +1,26 @@








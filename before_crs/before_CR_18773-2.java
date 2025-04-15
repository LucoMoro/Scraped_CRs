/*Delete some opcode tests for invoke_*

These will be added back in a future release!

Bug 3147582

Change-Id:I98200ee239d0bd52cbb1b2a3a8a093c0b248aa40*/
//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_super/Test_invoke_super.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_super/Test_invoke_super.java
//Synthetic comment -- index 6efac8b..a2fd2d8 100644

//Synthetic comment -- @@ -292,21 +292,6 @@
}

/**
     * @constraint n/a
     * @title invoke-super shall be used to invoke private methods
     */
    public void testVFE16() {
        //@uses dot.junit.opcodes.invoke_super.d.T_invoke_super_13
        //@uses dot.junit.opcodes.invoke_super.d.TSuper
         try {
             Class.forName("dot.junit.opcodes.invoke_super.d.T_invoke_super_13");
             fail("expected a verification exception");
         } catch (Throwable t) {
             DxUtil.checkVerifyException(t);
         }
    }

    /**
* @constraint A23
* @title number of registers
*/








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_super_range/Test_invoke_super_range.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_super_range/Test_invoke_super_range.java
//Synthetic comment -- index d65b8d4..7bee936 100644

//Synthetic comment -- @@ -292,21 +292,6 @@
}

/**
     * @constraint n/a
     * @title invoke-super/range shall be used to invoke private methods
     */
    public void testVFE16() {
        //@uses dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_13
        //@uses dot.junit.opcodes.invoke_super_range.d.TSuper
         try {
             Class.forName("dot.junit.opcodes.invoke_super_range.d.T_invoke_super_range_13");
             fail("expected a verification exception");
         } catch (Throwable t) {
             DxUtil.checkVerifyException(t);
         }
    }

    /**
* @constraint A23
* @title number of registers
*/








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual/Test_invoke_virtual.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual/Test_invoke_virtual.java
//Synthetic comment -- index 6e41ec1..70e9f9e 100644

//Synthetic comment -- @@ -286,19 +286,6 @@
}

/**
     * @constraint n/a
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
* @constraint A23
* @title number of registers
*/








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual_range/Test_invoke_virtual_range.java b/tools/vm-tests/src/dot/junit/opcodes/invoke_virtual_range/Test_invoke_virtual_range.java
//Synthetic comment -- index 2368dc3..4f91ba8 100644

//Synthetic comment -- @@ -294,19 +294,6 @@
}

/**
     * @constraint n/a
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
* @constraint A23
* @title number of registers
*/








/*Add a test for a dalvik interface cache bug.

Bug:http://code.google.com/p/android/issues/detail?id=29358Change-Id:Ia4d6be890da3a22199ad5a5183562f593a969496*/




//Synthetic comment -- diff --git a/tools/vm-tests-tf/src/dot/junit/opcodes/invoke_interface/Test_invoke_interface.java b/tools/vm-tests-tf/src/dot/junit/opcodes/invoke_interface/Test_invoke_interface.java
//Synthetic comment -- index 377cc58..ae06cda 100644

//Synthetic comment -- @@ -90,6 +90,29 @@
}

/**
     * @title dvmInterpFindInterfaceMethod failures were putting NULL Method*s
     * in the interface cache, leading to a null pointer deference the second
     * time you made the same bad call, with no exception thrown.
     * See http://code.google.com/p/android/issues/detail?id=29358 for details.
     */
    public void testE4_2() {
        //@uses dot.junit.opcodes.invoke_interface.d.T_invoke_interface_11
        //@uses dot.junit.opcodes.invoke_interface.ITest
        //@uses dot.junit.opcodes.invoke_interface.ITestImpl
        T_invoke_interface_11 t = new T_invoke_interface_11();
        try {
            t.run();
            fail("expected IncompatibleClassChangeError");
        } catch (IncompatibleClassChangeError expected) {
        }
        try {
            t.run();
            fail("expected IncompatibleClassChangeError");
        } catch (IncompatibleClassChangeError expected) {
        }
    }

    /**
* @title Native method can't be linked
*/
public void testE5() {








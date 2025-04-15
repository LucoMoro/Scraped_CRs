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


public class Test_check_cast extends DxTestCase {
//Synthetic comment -- @@ -118,34 +120,35 @@
}
}

   
/**
* @constraint n/a
     * @title Attempt to access inaccessible class. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
     */    
public void testVFE5() {
//@uses dot.junit.opcodes.check_cast.TestStubs
//@uses dot.junit.opcodes.check_cast.d.T_check_cast_3
try {
            Class.forName("dot.junit.opcodes.check_cast.d.T_check_cast_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to access undefined class. Java throws NoClassDefFoundError on 
     * first access but Dalvik throws VerifyError on class loading.
     */    
public void testVFE6() {
try {
            Class.forName("dot.junit.opcodes.check_cast.d.T_check_cast_7");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}









//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/filled_new_array_range/Test_filled_new_array_range.java b/tools/vm-tests/src/dot/junit/opcodes/filled_new_array_range/Test_filled_new_array_range.java
//Synthetic comment -- index b69d5b4..1f1fb19 100644

//Synthetic comment -- @@ -19,8 +19,9 @@
import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.filled_new_array_range.d.T_filled_new_array_range_1;
import dot.junit.opcodes.filled_new_array_range.d.T_filled_new_array_range_2;
import dot.junit.opcodes.filled_new_array_range.d.T_filled_new_array_range_9;

public class Test_filled_new_array_range extends DxTestCase {
/**
//Synthetic comment -- @@ -140,31 +141,32 @@
}

/**
     * @constraint n/a 
* @title attempt to instantiate array of non-existent class
*/
public void testVFE8() {
try {
        	Class.forName("dot.junit.opcodes.filled_new_array_range.d.T_filled_new_array_range_10");
        	fail("expected a verification exception");
        } catch(Throwable t) {
        	DxUtil.checkVerifyException(t);	
}
}
    
/**
     * @constraint n/a 
* @title attempt to instantiate array of inaccessible class
*/
public void testVFE9() {
    	//@uses dot.junit.opcodes.filled_new_array_range.d.T_filled_new_array_range_11
//@uses dot.junit.opcodes.filled_new_array_range.TestStubs
try {
        	Class.forName("dot.junit.opcodes.filled_new_array_range.d.T_filled_new_array_range_11");
        	fail("expected a verification exception");
        } catch(Throwable t) {
        	DxUtil.checkVerifyException(t);	
}
}
    
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/filled_new_array_range/d/T_filled_new_array_range_10.java b/tools/vm-tests/src/dot/junit/opcodes/filled_new_array_range/d/T_filled_new_array_range_10.java
new file mode 100644
//Synthetic comment -- index 0000000..57075bd

//Synthetic comment -- @@ -0,0 +1,23 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/filled_new_array_range/d/T_filled_new_array_range_11.java b/tools/vm-tests/src/dot/junit/opcodes/filled_new_array_range/d/T_filled_new_array_range_11.java
new file mode 100644
//Synthetic comment -- index 0000000..f4ca611

//Synthetic comment -- @@ -0,0 +1,23 @@








/*Fix opcode tests for sput

Bug 3147582

Change-Id:Iaa57bc9345f45a565eea6de4cd676d9d1b481769*/
//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput/Test_sput.java b/tools/vm-tests/src/dot/junit/opcodes/sput/Test_sput.java
//Synthetic comment -- index 7733001..8b73bb5 100644

//Synthetic comment -- @@ -19,12 +19,18 @@
import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.sput.d.T_sput_1;
import dot.junit.opcodes.sput.d.T_sput_11;
import dot.junit.opcodes.sput.d.T_sput_12;
import dot.junit.opcodes.sput.d.T_sput_13;
import dot.junit.opcodes.sput.d.T_sput_14;
import dot.junit.opcodes.sput.d.T_sput_19;
import dot.junit.opcodes.sput.d.T_sput_5;


public class Test_sput extends DxTestCase {
//Synthetic comment -- @@ -49,7 +55,7 @@
assertEquals(3.14f, T_sput_19.st_f1);
}

 
/**
* @title modification of final field
*/
//Synthetic comment -- @@ -73,8 +79,8 @@
}

/**
     * @title Trying to put float into integer field. Dalvik doens't distinguish 32-bits types 
     * internally, so this operation makes no sense but shall not crash the VM.  
*/
public void testN6() {
T_sput_5 t = new  T_sput_5();
//Synthetic comment -- @@ -84,7 +90,7 @@
}
}

   

/**
* @title initialization of referenced class throws exception
//Synthetic comment -- @@ -100,7 +106,7 @@
}

/**
     * @constraint A12 
* @title  constant pool index
*/
public void testVFE1() {
//Synthetic comment -- @@ -113,8 +119,8 @@
}

/**
     * 
     * @constraint A23 
* @title  number of registers
*/
public void testVFE2() {
//Synthetic comment -- @@ -128,24 +134,23 @@


/**
     * 
     * @constraint B13 
     * @title  put integer into long field - only field with same name but 
* different type exists
*/
public void testVFE5() {
try {
            Class.forName("dot.junit.opcodes.sput.d.T_sput_17");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
     * 
     * @constraint B13 
     * @title type of field doesn't match opcode - attempt to modify double field 
* with single-width register
*/
public void testVFE7() {
//Synthetic comment -- @@ -156,87 +161,77 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint A12 
     * @title Attempt to set non-static field. Java throws IncompatibleClassChangeError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE8() {
try {
             Class.forName("dot.junit.opcodes.sput.d.T_sput_7");
             fail("expected a verification exception");
         } catch (Throwable t) {
             DxUtil.checkVerifyException(t);
}
}
    
/**
* @constraint n/a
     * @title Attempt to modify inaccessible field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE9() {
//@uses dot.junit.opcodes.sput.TestStubs
//@uses dot.junit.opcodes.sput.d.T_sput_8
try {
            Class.forName("dot.junit.opcodes.sput.d.T_sput_8");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to modify field of undefined class. Java throws NoClassDefFoundError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE10() {
try {
            Class.forName("dot.junit.opcodes.sput.d.T_sput_9");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to modify undefined field. Java throws NoSuchFieldError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE11() {
try {
            Class.forName("dot.junit.opcodes.sput.d.T_sput_10");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}
    
    
    
/**
* @constraint n/a
     * @title Attempt to modify superclass' private field from subclass. Java 
     * throws IllegalAccessError on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE12() {
//@uses dot.junit.opcodes.sput.d.T_sput_1
//@uses dot.junit.opcodes.sput.d.T_sput_15
try {
            Class.forName("dot.junit.opcodes.sput.d.T_sput_15");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}
    
    
/**
     * @constraint B1 
* @title sput shall not work for wide numbers
*/
public void testVFE13() {
//Synthetic comment -- @@ -247,10 +242,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput shall not work for reference fields
*/
public void testVFE14() {
//Synthetic comment -- @@ -261,10 +256,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput shall not work for short fields
*/
public void testVFE15() {
//Synthetic comment -- @@ -275,10 +270,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput shall not work for boolean fields
*/
public void testVFE16() {
//Synthetic comment -- @@ -289,10 +284,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput shall not work for char fields
*/
public void testVFE17() {
//Synthetic comment -- @@ -303,10 +298,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput shall not work for byte fields
*/
public void testVFE18() {
//Synthetic comment -- @@ -317,7 +312,7 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
* @constraint n/a
* @title Modification of final field in other class
//Synthetic comment -- @@ -326,11 +321,10 @@
//@uses dot.junit.opcodes.sput.TestStubs
//@uses dot.junit.opcodes.sput.d.T_sput_11
	try {
            Class.forName("dot.junit.opcodes.sput.d.T_sput_11");
fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}
    
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput/d/T_sput_10.java b/tools/vm-tests/src/dot/junit/opcodes/sput/d/T_sput_10.java
new file mode 100644
//Synthetic comment -- index 0000000..01a9d6b

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput/d/T_sput_15.java b/tools/vm-tests/src/dot/junit/opcodes/sput/d/T_sput_15.java
new file mode 100644
//Synthetic comment -- index 0000000..25944f3

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput/d/T_sput_17.java b/tools/vm-tests/src/dot/junit/opcodes/sput/d/T_sput_17.java
new file mode 100644
//Synthetic comment -- index 0000000..cc068c8

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput/d/T_sput_7.java b/tools/vm-tests/src/dot/junit/opcodes/sput/d/T_sput_7.java
new file mode 100644
//Synthetic comment -- index 0000000..8b22c00

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput/d/T_sput_8.java b/tools/vm-tests/src/dot/junit/opcodes/sput/d/T_sput_8.java
new file mode 100644
//Synthetic comment -- index 0000000..5ccae38

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput/d/T_sput_9.java b/tools/vm-tests/src/dot/junit/opcodes/sput/d/T_sput_9.java
new file mode 100644
//Synthetic comment -- index 0000000..b8f71e7

//Synthetic comment -- @@ -0,0 +1,22 @@








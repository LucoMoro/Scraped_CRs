/*Fix opcode tests for sput_wide

Bug 3147582

Change-Id:Ia9cce524d95549fb9b0cef661654fe5ac3aaaad6*/
//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_wide/Test_sput_wide.java b/tools/vm-tests/src/dot/junit/opcodes/sput_wide/Test_sput_wide.java
//Synthetic comment -- index edca9d9..29ff21b 100644

//Synthetic comment -- @@ -19,11 +19,17 @@
import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.sput_wide.d.T_sput_wide_1;
import dot.junit.opcodes.sput_wide.d.T_sput_wide_11;
import dot.junit.opcodes.sput_wide.d.T_sput_wide_12;
import dot.junit.opcodes.sput_wide.d.T_sput_wide_13;
import dot.junit.opcodes.sput_wide.d.T_sput_wide_14;
import dot.junit.opcodes.sput_wide.d.T_sput_wide_5;

public class Test_sput_wide extends DxTestCase {
/**
//Synthetic comment -- @@ -35,7 +41,7 @@
t.run();
assertEquals(778899112233l, T_sput_wide_1.st_i1);
}
    
/**
* @title put double into static field
*/
//Synthetic comment -- @@ -46,7 +52,7 @@
assertEquals(0.5d, T_sput_wide_5.st_i1);
}

 
/**
* @title modification of final field
*/
//Synthetic comment -- @@ -68,7 +74,7 @@
t.run();
assertEquals(77, T_sput_wide_14.getProtectedField());
}
  
/**
* @title initialization of referenced class throws exception
*/
//Synthetic comment -- @@ -83,7 +89,7 @@
}

/**
     * @constraint A12 
* @title constant pool index
*/
public void testVFE1() {
//Synthetic comment -- @@ -96,8 +102,8 @@
}

/**
     * 
     * @constraint A23 
* @title number of registers
*/
public void testVFE2() {
//Synthetic comment -- @@ -111,26 +117,25 @@


/**
     * 
     * @constraint B13 
     * @title put int into long field - only field with same name but 
* different type exists
*/
public void testVFE5() {
try {
            Class.forName("dot.junit.opcodes.sput_wide.d.T_sput_wide_17");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}
    
 

/**
     * 
     * @constraint B13 
     * @title type of field doesn't match opcode - attempt to modify float 
* field with double-width register
*/
public void testVFE7() {
//Synthetic comment -- @@ -141,87 +146,85 @@
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
             Class.forName("dot.junit.opcodes.sput_wide.d.T_sput_wide_7");
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
//@uses dot.junit.opcodes.sput_wide.TestStubs
//@uses dot.junit.opcodes.sput_wide.d.T_sput_wide_8
try {
            Class.forName("dot.junit.opcodes.sput_wide.d.T_sput_wide_8");
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
            Class.forName("dot.junit.opcodes.sput_wide.d.T_sput_wide_9");
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
            Class.forName("dot.junit.opcodes.sput_wide.d.T_sput_wide_10");
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
//@uses dot.junit.opcodes.sput_wide.d.T_sput_wide_1
//@uses dot.junit.opcodes.sput_wide.d.T_sput_wide_15
try {
            Class.forName("dot.junit.opcodes.sput_wide.d.T_sput_wide_15");
            fail("expected a verification exception");
        } catch (Throwable t) {
DxUtil.checkVerifyException(t);
}
}
    
    
/**
     * @constraint B1 
* @title sput-wide shall not work for single-width numbers
*/
public void testVFE13() {
//Synthetic comment -- @@ -232,10 +235,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput-wide shall not work for reference fields
*/
public void testVFE14() {
//Synthetic comment -- @@ -246,10 +249,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput-wide shall not work for char fields
*/
public void testVFE15() {
//Synthetic comment -- @@ -260,10 +263,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput-wide shall not work for int fields
*/
public void testVFE16() {
//Synthetic comment -- @@ -274,10 +277,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput-wide shall not work for byte fields
*/
public void testVFE17() {
//Synthetic comment -- @@ -288,10 +291,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput-wide shall not work for boolean fields
*/
public void testVFE18() {
//Synthetic comment -- @@ -304,8 +307,8 @@
}

/**
     * 
     * @constraint B1 
* @title sput-wide shall not work for short fields
*/
public void testVFE6() {
//Synthetic comment -- @@ -316,7 +319,7 @@
DxUtil.checkVerifyException(t);
}
}
  
/**
* @constraint n/a
* @title Modification of final field in other class
//Synthetic comment -- @@ -325,12 +328,11 @@
//@uses dot.junit.opcodes.sput_wide.TestStubs
//@uses dot.junit.opcodes.sput_wide.d.T_sput_wide_11
	try {
            Class.forName("dot.junit.opcodes.sput_wide.d.T_sput_wide_11");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

    
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_wide/d/T_sput_wide_10.java b/tools/vm-tests/src/dot/junit/opcodes/sput_wide/d/T_sput_wide_10.java
new file mode 100644
//Synthetic comment -- index 0000000..b275744

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_wide/d/T_sput_wide_15.java b/tools/vm-tests/src/dot/junit/opcodes/sput_wide/d/T_sput_wide_15.java
new file mode 100644
//Synthetic comment -- index 0000000..376cdd8

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_wide/d/T_sput_wide_17.java b/tools/vm-tests/src/dot/junit/opcodes/sput_wide/d/T_sput_wide_17.java
new file mode 100644
//Synthetic comment -- index 0000000..244fa5e

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_wide/d/T_sput_wide_7.java b/tools/vm-tests/src/dot/junit/opcodes/sput_wide/d/T_sput_wide_7.java
new file mode 100644
//Synthetic comment -- index 0000000..4608227

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_wide/d/T_sput_wide_8.java b/tools/vm-tests/src/dot/junit/opcodes/sput_wide/d/T_sput_wide_8.java
new file mode 100644
//Synthetic comment -- index 0000000..17cf144

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_wide/d/T_sput_wide_9.java b/tools/vm-tests/src/dot/junit/opcodes/sput_wide/d/T_sput_wide_9.java
new file mode 100644
//Synthetic comment -- index 0000000..00355dd

//Synthetic comment -- @@ -0,0 +1,22 @@








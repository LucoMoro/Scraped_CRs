/*Fix opcode tests for sput_short

Bug 3147582

Change-Id:Icde18c91b5d34557fdb4fdf7e388c3c28f6eb82c*/
//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_short/Test_sput_short.java b/tools/vm-tests/src/dot/junit/opcodes/sput_short/Test_sput_short.java
//Synthetic comment -- index c566401..f14c4a7 100644

//Synthetic comment -- @@ -19,10 +19,16 @@
import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.sput_short.d.T_sput_short_1;
import dot.junit.opcodes.sput_short.d.T_sput_short_11;
import dot.junit.opcodes.sput_short.d.T_sput_short_12;
import dot.junit.opcodes.sput_short.d.T_sput_short_13;
import dot.junit.opcodes.sput_short.d.T_sput_short_14;

public class Test_sput_short extends DxTestCase {
/**
//Synthetic comment -- @@ -35,7 +41,7 @@
assertEquals(77, T_sput_short_1.st_i1);
}

 
/**
* @title modification of final field
*/
//Synthetic comment -- @@ -73,7 +79,7 @@
}

/**
     * @constraint A12 
* @title constant pool index
*/
public void testVFE1() {
//Synthetic comment -- @@ -86,8 +92,8 @@
}

/**
     * 
     * @constraint A23 
* @title number of registers
*/
public void testVFE2() {
//Synthetic comment -- @@ -101,23 +107,22 @@


/**
     * 
     * @constraint B13 
     * @title put short into long field - only field with same name but 
* different type exists
*/
public void testVFE5() {
try {
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_17");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B13 
* @title put value '66000' into byte field
*/
public void testVFE6() {
//Synthetic comment -- @@ -130,9 +135,9 @@
}

/**
     * 
     * @constraint B13 
     * @title type of field doesn't match opcode - attempt to modify double 
* field with single-width register
*/
public void testVFE7() {
//Synthetic comment -- @@ -143,87 +148,77 @@
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
             Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_7");
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
//@uses dot.junit.opcodes.sput_short.TestStubs
//@uses dot.junit.opcodes.sput_short.d.T_sput_short_8
try {
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_8");
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
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_9");
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
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_10");
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
//@uses dot.junit.opcodes.sput_short.d.T_sput_short_1
//@uses dot.junit.opcodes.sput_short.d.T_sput_short_15
try {
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_15");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}
    
    
/**
     * @constraint B1 
* @title sput-short shall not work for wide numbers
*/
public void testVFE13() {
//Synthetic comment -- @@ -234,10 +229,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput-short shall not work for reference fields
*/
public void testVFE14() {
//Synthetic comment -- @@ -248,10 +243,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput-short shall not work for char fields
*/
public void testVFE15() {
//Synthetic comment -- @@ -262,10 +257,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput-short shall not work for int fields
*/
public void testVFE16() {
//Synthetic comment -- @@ -276,10 +271,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput-short shall not work for byte fields
*/
public void testVFE17() {
//Synthetic comment -- @@ -290,10 +285,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput-short shall not work for boolean fields
*/
public void testVFE18() {
//Synthetic comment -- @@ -313,10 +308,9 @@
//@uses dot.junit.opcodes.sput_short.TestStubs
//@uses dot.junit.opcodes.sput_short.d.T_sput_short_11
	try {
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_11");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_short/d/T_sput_short_10.java b/tools/vm-tests/src/dot/junit/opcodes/sput_short/d/T_sput_short_10.java
new file mode 100644
//Synthetic comment -- index 0000000..3af3f04

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_short/d/T_sput_short_15.java b/tools/vm-tests/src/dot/junit/opcodes/sput_short/d/T_sput_short_15.java
new file mode 100644
//Synthetic comment -- index 0000000..04f6a7c

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_short/d/T_sput_short_17.java b/tools/vm-tests/src/dot/junit/opcodes/sput_short/d/T_sput_short_17.java
new file mode 100644
//Synthetic comment -- index 0000000..fa56416

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_short/d/T_sput_short_7.java b/tools/vm-tests/src/dot/junit/opcodes/sput_short/d/T_sput_short_7.java
new file mode 100644
//Synthetic comment -- index 0000000..cbecc44

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_short/d/T_sput_short_8.java b/tools/vm-tests/src/dot/junit/opcodes/sput_short/d/T_sput_short_8.java
new file mode 100644
//Synthetic comment -- index 0000000..852263e

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_short/d/T_sput_short_9.java b/tools/vm-tests/src/dot/junit/opcodes/sput_short/d/T_sput_short_9.java
new file mode 100644
//Synthetic comment -- index 0000000..e950c1d

//Synthetic comment -- @@ -0,0 +1,22 @@








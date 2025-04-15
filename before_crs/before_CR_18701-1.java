/*Fix opcode tests for sput_object

Bug 3147582

Change-Id:I7437599a75161b419f82852ed857c52efaf19dc5*/
//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_object/Test_sput_object.java b/tools/vm-tests/src/dot/junit/opcodes/sput_object/Test_sput_object.java
//Synthetic comment -- index 0607225..cd070a7 100644

//Synthetic comment -- @@ -19,10 +19,16 @@
import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.sput_object.d.T_sput_object_1;
import dot.junit.opcodes.sput_object.d.T_sput_object_11;
import dot.junit.opcodes.sput_object.d.T_sput_object_12;
import dot.junit.opcodes.sput_object.d.T_sput_object_13;
import dot.junit.opcodes.sput_object.d.T_sput_object_14;

public class Test_sput_object extends DxTestCase {
/**
//Synthetic comment -- @@ -35,7 +41,7 @@
assertEquals(t, T_sput_object_1.st_i1);
}

 
/**
* @title modification of final field
*/
//Synthetic comment -- @@ -57,7 +63,7 @@
t.run();
assertEquals(t, T_sput_object_14.getProtectedField());
}
  

/**
* @title initialization of referenced class throws exception
//Synthetic comment -- @@ -86,8 +92,8 @@
}

/**
     * 
     * @constraint A23 
* @title number of registers
*/
public void testVFE2() {
//Synthetic comment -- @@ -101,25 +107,24 @@


/**
     * 
     * @constraint B13 
     * @title put object into long field - only field with same name but 
* different type exists
*/
public void testVFE5() {
try {
            Class.forName("dot.junit.opcodes.sput_object.d.T_sput_object_17");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}
    

/**
     * 
     * @constraint B13 
     * @title type of field doesn't match opcode - attempt to modify double 
* field with single-width register
*/
public void testVFE7() {
//Synthetic comment -- @@ -130,87 +135,77 @@
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
             Class.forName("dot.junit.opcodes.sput_object.d.T_sput_object_7");
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
//@uses dot.junit.opcodes.sput_object.TestStubs
//@uses dot.junit.opcodes.sput_object.d.T_sput_object_8
try {
            Class.forName("dot.junit.opcodes.sput_object.d.T_sput_object_8");
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
            Class.forName("dot.junit.opcodes.sput_object.d.T_sput_object_9");
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
            Class.forName("dot.junit.opcodes.sput_object.d.T_sput_object_10");
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
//@uses dot.junit.opcodes.sput_object.d.T_sput_object_1
//@uses dot.junit.opcodes.sput_object.d.T_sput_object_15
try {
            Class.forName("dot.junit.opcodes.sput_object.d.T_sput_object_15");
fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}
    
    
/**
     * @constraint B1 
* @title sput-object shall not work for wide numbers
*/
public void testVFE13() {
//Synthetic comment -- @@ -221,10 +216,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B13 
* @title assignment incompatible references
*/
public void testVFE14() {
//Synthetic comment -- @@ -235,10 +230,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput-object shall not work for char fields
*/
public void testVFE15() {
//Synthetic comment -- @@ -249,10 +244,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput-object shall not work for int fields
*/
public void testVFE16() {
//Synthetic comment -- @@ -263,10 +258,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput-object shall not work for byte fields
*/
public void testVFE17() {
//Synthetic comment -- @@ -277,10 +272,10 @@
DxUtil.checkVerifyException(t);
}
}
    
/**
     * 
     * @constraint B1 
* @title sput-object shall not work for boolean fields
*/
public void testVFE18() {
//Synthetic comment -- @@ -293,8 +288,8 @@
}

/**
     * 
     * @constraint B1 
* @title sput-object shall not work for short fields
*/
public void testVFE6() {
//Synthetic comment -- @@ -308,17 +303,16 @@


/**
     * @constraint B1 
* @title Modification of final field in other class
*/
public void testVFE19() {
//@uses dot.junit.opcodes.sput_object.TestStubs
//@uses dot.junit.opcodes.sput_object.d.T_sput_object_11
	try {
            Class.forName("dot.junit.opcodes.sput_object.d.T_sput_object_11");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}
}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_object/d/T_sput_object_10.java b/tools/vm-tests/src/dot/junit/opcodes/sput_object/d/T_sput_object_10.java
new file mode 100644
//Synthetic comment -- index 0000000..bbc15c5

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_object/d/T_sput_object_15.java b/tools/vm-tests/src/dot/junit/opcodes/sput_object/d/T_sput_object_15.java
new file mode 100644
//Synthetic comment -- index 0000000..676e4c3

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_object/d/T_sput_object_17.java b/tools/vm-tests/src/dot/junit/opcodes/sput_object/d/T_sput_object_17.java
new file mode 100644
//Synthetic comment -- index 0000000..6f7ad7c

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_object/d/T_sput_object_7.java b/tools/vm-tests/src/dot/junit/opcodes/sput_object/d/T_sput_object_7.java
new file mode 100644
//Synthetic comment -- index 0000000..0e5f873

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_object/d/T_sput_object_8.java b/tools/vm-tests/src/dot/junit/opcodes/sput_object/d/T_sput_object_8.java
new file mode 100644
//Synthetic comment -- index 0000000..d3d7408

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/sput_object/d/T_sput_object_9.java b/tools/vm-tests/src/dot/junit/opcodes/sput_object/d/T_sput_object_9.java
new file mode 100644
//Synthetic comment -- index 0000000..bef9d16

//Synthetic comment -- @@ -0,0 +1,22 @@








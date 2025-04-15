/*Fix iget* opcode tests.

Bug 3147582

Change-Id:I59b57f02c5ec5a7005751d44ba6317606d23cb61*/
//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/Test_iget.java b/tools/vm-tests/src/dot/junit/opcodes/iget/Test_iget.java
//Synthetic comment -- index b5fcb67..76dbccd 100644

//Synthetic comment -- @@ -20,7 +20,14 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.iget.d.T_iget_1;
import dot.junit.opcodes.iget.d.T_iget_11;
import dot.junit.opcodes.iget.d.T_iget_2;
import dot.junit.opcodes.iget.d.T_iget_9;

public class Test_iget extends DxTestCase {
//Synthetic comment -- @@ -98,70 +105,71 @@
*/
public void testVFE3() {
try {
            Class.forName("dot.junit.opcodes.iget.d.T_iget_13");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible private field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.iget.d.T_iget_6
//@uses dot.junit.opcodes.iget.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget.d.T_iget_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class. Java throws NoClassDefFoundError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE5() {
try {
            Class.forName("dot.junit.opcodes.iget.d.T_iget_7");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field. Java throws NoSuchFieldError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE6() {
try {
            Class.forName("dot.junit.opcodes.iget.d.T_iget_8");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass. Java 
     * throws IllegalAccessError on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.iget.d.T_iget_12
//@uses dot.junit.opcodes.iget.d.T_iget_1
try {
            Class.forName("dot.junit.opcodes.iget.d.T_iget_12");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

//Synthetic comment -- @@ -264,37 +272,35 @@

/**
* @constraint B12
     * @title Attempt to read protected field of unrelated class. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE15() {
//@uses dot.junit.opcodes.iget.d.T_iget_21
//@uses dot.junit.opcodes.iget.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget.d.T_iget_21");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

    
/**
* @constraint A11
     * @title Attempt to read static field. Java throws IncompatibleClassChangeError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE16() {
//@uses dot.junit.opcodes.iget.d.T_iget_5
        //@uses dot.junit.opcodes.iget.TestStubs        
try {
            Class.forName("dot.junit.opcodes.iget.d.T_iget_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}
    

/**
* @constraint B6 








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_12.java b/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_12.java
new file mode 100644
//Synthetic comment -- index 0000000..b9f40ae

//Synthetic comment -- @@ -0,0 +1,25 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_13.java b/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_13.java
new file mode 100644
//Synthetic comment -- index 0000000..1038f56

//Synthetic comment -- @@ -0,0 +1,23 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_21.java b/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_21.java
new file mode 100644
//Synthetic comment -- index 0000000..31db5f1

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_5.java b/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_5.java
new file mode 100644
//Synthetic comment -- index 0000000..053f013

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_6.java b/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_6.java
new file mode 100644
//Synthetic comment -- index 0000000..efb61d2

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_7.java b/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_7.java
new file mode 100644
//Synthetic comment -- index 0000000..54b848f

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_8.java b/tools/vm-tests/src/dot/junit/opcodes/iget/d/T_iget_8.java
new file mode 100644
//Synthetic comment -- index 0000000..249b4d0

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/Test_iget_boolean.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/Test_iget_boolean.java
//Synthetic comment -- index 2b24d6d..66dd888 100644

//Synthetic comment -- @@ -20,6 +20,13 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.iget_boolean.d.T_iget_boolean_1;
import dot.junit.opcodes.iget_boolean.d.T_iget_boolean_11;
import dot.junit.opcodes.iget_boolean.d.T_iget_boolean_9;

public class Test_iget_boolean extends DxTestCase {
//Synthetic comment -- @@ -91,70 +98,71 @@
*/
public void testVFE3() {
try {
            Class.forName("dot.junit.opcodes.iget_boolean.d.T_iget_boolean_13");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.iget_boolean.d.T_iget_boolean_6
//@uses dot.junit.opcodes.iget_boolean.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget_boolean.d.T_iget_boolean_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class. Java throws NoClassDefFoundError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE5() {
try {
            Class.forName("dot.junit.opcodes.iget_boolean.d.T_iget_boolean_7");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field. Java throws NoSuchFieldError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE6() {
try {
            Class.forName("dot.junit.opcodes.iget_boolean.d.T_iget_boolean_8");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass. Java 
     * throws IllegalAccessError on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.iget_boolean.d.T_iget_boolean_12
//@uses dot.junit.opcodes.iget_boolean.d.T_iget_boolean_1
try {
            Class.forName("dot.junit.opcodes.iget_boolean.d.T_iget_boolean_12");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

//Synthetic comment -- @@ -252,35 +260,34 @@

/**
* @constraint B12
     * @title Attempt to read inaccessible protected field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE15() {
//@uses dot.junit.opcodes.iget_boolean.d.T_iget_boolean_21
//@uses dot.junit.opcodes.iget_boolean.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget_boolean.d.T_iget_boolean_21");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}


/**
* @constraint A11
     * @title Attempt to read static field. Java throws IncompatibleClassChangeError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE16() {
//@uses dot.junit.opcodes.iget_boolean.d.T_iget_boolean_5
//@uses dot.junit.opcodes.iget_boolean.TestStubs

try {
            Class.forName("dot.junit.opcodes.iget_boolean.d.T_iget_boolean_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}









//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_12.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_12.java
new file mode 100644
//Synthetic comment -- index 0000000..5af996f

//Synthetic comment -- @@ -0,0 +1,25 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_13.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_13.java
new file mode 100644
//Synthetic comment -- index 0000000..b652090

//Synthetic comment -- @@ -0,0 +1,23 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_21.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_21.java
new file mode 100644
//Synthetic comment -- index 0000000..74e2da1

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_5.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_5.java
new file mode 100644
//Synthetic comment -- index 0000000..5b275aa

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_6.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_6.java
new file mode 100644
//Synthetic comment -- index 0000000..0d91cab

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_7.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_7.java
new file mode 100644
//Synthetic comment -- index 0000000..3ec333b

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_8.java b/tools/vm-tests/src/dot/junit/opcodes/iget_boolean/d/T_iget_boolean_8.java
new file mode 100644
//Synthetic comment -- index 0000000..b87cf8d

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/Test_iget_byte.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/Test_iget_byte.java
//Synthetic comment -- index 5d8630b..ee5fd08 100644

//Synthetic comment -- @@ -20,6 +20,13 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.iget_byte.d.T_iget_byte_1;
import dot.junit.opcodes.iget_byte.d.T_iget_byte_11;
import dot.junit.opcodes.iget_byte.d.T_iget_byte_9;

public class Test_iget_byte extends DxTestCase {
//Synthetic comment -- @@ -89,70 +96,71 @@
*/
public void testVFE3() {
try {
            Class.forName("dot.junit.opcodes.iget_byte.d.T_iget_byte_13");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.iget_byte.d.T_iget_byte_6
//@uses dot.junit.opcodes.iget_byte.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget_byte.d.T_iget_byte_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class. Java throws NoClassDefFoundError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE5() {
try {
            Class.forName("dot.junit.opcodes.iget_byte.d.T_iget_byte_7");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field. Java throws NoSuchFieldError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE6() {
try {
            Class.forName("dot.junit.opcodes.iget_byte.d.T_iget_byte_8");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass. Java 
     * throws IllegalAccessError on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.iget_byte.d.T_iget_byte_12
//@uses dot.junit.opcodes.iget_byte.d.T_iget_byte_1
try {
            Class.forName("dot.junit.opcodes.iget_byte.d.T_iget_byte_12");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

//Synthetic comment -- @@ -249,34 +257,34 @@

/**
* @constraint B12
     * @title Attempt to read inaccessible protected field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE15() {
//@uses dot.junit.opcodes.iget_byte.d.T_iget_byte_21
//@uses dot.junit.opcodes.iget_byte.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget_byte.d.T_iget_byte_21");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}


/**
* @constraint A11
     * @title Attempt to read static  field. Java throws IncompatibleClassChangeError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE16() {
//@uses dot.junit.opcodes.iget_byte.d.T_iget_byte_5
//@uses dot.junit.opcodes.iget_byte.TestStubs        
try {
            Class.forName("dot.junit.opcodes.iget_byte.d.T_iget_byte_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}









//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_12.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_12.java
new file mode 100644
//Synthetic comment -- index 0000000..3927a5b

//Synthetic comment -- @@ -0,0 +1,25 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_13.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_13.java
new file mode 100644
//Synthetic comment -- index 0000000..755b896

//Synthetic comment -- @@ -0,0 +1,23 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_21.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_21.java
new file mode 100644
//Synthetic comment -- index 0000000..10deb39

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_5.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_5.java
new file mode 100644
//Synthetic comment -- index 0000000..75f7a5c

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_6.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_6.java
new file mode 100644
//Synthetic comment -- index 0000000..20af80f

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_7.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_7.java
new file mode 100644
//Synthetic comment -- index 0000000..f13071d

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_8.java b/tools/vm-tests/src/dot/junit/opcodes/iget_byte/d/T_iget_byte_8.java
new file mode 100644
//Synthetic comment -- index 0000000..6f89bf3

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/Test_iget_char.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/Test_iget_char.java
//Synthetic comment -- index 1805534..03025fd 100644

//Synthetic comment -- @@ -20,6 +20,13 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.iget_char.d.T_iget_char_1;
import dot.junit.opcodes.iget_char.d.T_iget_char_11;
import dot.junit.opcodes.iget_char.d.T_iget_char_9;

public class Test_iget_char extends DxTestCase {
//Synthetic comment -- @@ -92,70 +99,71 @@
*/
public void testVFE3() {
try {
            Class.forName("dot.junit.opcodes.iget_char.d.T_iget_char_13");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.iget_char.d.T_iget_char_6
//@uses dot.junit.opcodes.iget_char.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget_char.d.T_iget_char_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class. Java throws NoClassDefFoundError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE5() {
try {
            Class.forName("dot.junit.opcodes.iget_char.d.T_iget_char_7");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field. Java throws NoSuchFieldError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE6() {
try {
            Class.forName("dot.junit.opcodes.iget_char.d.T_iget_char_8");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass. Java 
     * throws IllegalAccessError on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.iget_char.d.T_iget_char_12
//@uses dot.junit.opcodes.iget_char.d.T_iget_char_1
try {
            Class.forName("dot.junit.opcodes.iget_char.d.T_iget_char_12");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

//Synthetic comment -- @@ -256,34 +264,34 @@

/**
* @constraint B12
     * @title Attempt to read inaccessible protected field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE15() {
//@uses dot.junit.opcodes.iget_char.d.T_iget_char_21
//@uses dot.junit.opcodes.iget_char.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget_char.d.T_iget_char_21");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}


/**
* @constraint A11
     * @title Attempt to read static  field. Java throws IncompatibleClassChangeError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE16() {
//@uses dot.junit.opcodes.iget_char.d.T_iget_char_5
//@uses dot.junit.opcodes.iget_char.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget_char.d.T_iget_char_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}









//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_12.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_12.java
new file mode 100644
//Synthetic comment -- index 0000000..611b24b

//Synthetic comment -- @@ -0,0 +1,25 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_13.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_13.java
new file mode 100644
//Synthetic comment -- index 0000000..33cae33

//Synthetic comment -- @@ -0,0 +1,23 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_21.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_21.java
new file mode 100644
//Synthetic comment -- index 0000000..6517824

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_5.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_5.java
new file mode 100644
//Synthetic comment -- index 0000000..4975e76

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_6.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_6.java
new file mode 100644
//Synthetic comment -- index 0000000..8be8c2b

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_7.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_7.java
new file mode 100644
//Synthetic comment -- index 0000000..4ca80d7

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_8.java b/tools/vm-tests/src/dot/junit/opcodes/iget_char/d/T_iget_char_8.java
new file mode 100644
//Synthetic comment -- index 0000000..49d35c6

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/Test_iget_object.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/Test_iget_object.java
//Synthetic comment -- index 7c6e03c..5357b17 100644

//Synthetic comment -- @@ -20,6 +20,14 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.iget_object.d.T_iget_object_1;
import dot.junit.opcodes.iget_object.d.T_iget_object_11;
import dot.junit.opcodes.iget_object.d.T_iget_object_9;

public class Test_iget_object extends DxTestCase {
//Synthetic comment -- @@ -91,40 +99,41 @@
*/
public void testVFE3() {
try {
            Class.forName("dot.junit.opcodes.iget_object.d.T_iget_object_13");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.iget_object.d.T_iget_object_6
//@uses dot.junit.opcodes.iget_object.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget_object.d.T_iget_object_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class. Java throws NoClassDefFoundError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE5() {
try {
            Class.forName("dot.junit.opcodes.iget_object.d.T_iget_object_7");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

//Synthetic comment -- @@ -135,26 +144,27 @@
*/
public void testVFE6() {
try {
            Class.forName("dot.junit.opcodes.iget_object.d.T_iget_object_8");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass. Java 
     * throws IllegalAccessError on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.iget_object.d.T_iget_object_12
//@uses dot.junit.opcodes.iget_object.d.T_iget_object_1
try {
            Class.forName("dot.junit.opcodes.iget_object.d.T_iget_object_12");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

//Synthetic comment -- @@ -262,10 +272,11 @@
*/
public void testVFE15() {
try {
            Class.forName("dot.junit.opcodes.iget_object.d.T_iget_object_21");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

//Synthetic comment -- @@ -278,10 +289,11 @@
//@uses dot.junit.opcodes.iget_object.d.T_iget_object_22
//@uses dot.junit.opcodes.iget_object.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget_object.d.T_iget_object_22");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

//Synthetic comment -- @@ -293,12 +305,13 @@
*/
public void testVFE17() {
//@uses dot.junit.opcodes.iget_object.d.T_iget_object_5
        //@uses dot.junit.opcodes.iget_object.TestStubs  
try {
            Class.forName("dot.junit.opcodes.iget_object.d.T_iget_object_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}









//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_12.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_12.java
new file mode 100644
//Synthetic comment -- index 0000000..f98c36a

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_13.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_13.java
new file mode 100644
//Synthetic comment -- index 0000000..85a8761

//Synthetic comment -- @@ -0,0 +1,23 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_21.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_21.java
new file mode 100644
//Synthetic comment -- index 0000000..e301893

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_22.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_22.java
new file mode 100644
//Synthetic comment -- index 0000000..d9ad933

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_5.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_5.java
new file mode 100644
//Synthetic comment -- index 0000000..3626499

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_6.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_6.java
new file mode 100644
//Synthetic comment -- index 0000000..a4ba515

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_7.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_7.java
new file mode 100644
//Synthetic comment -- index 0000000..2940ca4

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_8.java b/tools/vm-tests/src/dot/junit/opcodes/iget_object/d/T_iget_object_8.java
new file mode 100644
//Synthetic comment -- index 0000000..3b5c4d7

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/Test_iget_short.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/Test_iget_short.java
//Synthetic comment -- index aaa8270..dd9dc1c 100644

//Synthetic comment -- @@ -20,6 +20,13 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.iget_short.d.T_iget_short_1;
import dot.junit.opcodes.iget_short.d.T_iget_short_11;
import dot.junit.opcodes.iget_short.d.T_iget_short_9;

public class Test_iget_short extends DxTestCase {
//Synthetic comment -- @@ -93,70 +100,71 @@
*/
public void testVFE3() {
try {
            Class.forName("dot.junit.opcodes.iget_short.d.T_iget_short_13");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.iget_short.d.T_iget_short_6
//@uses dot.junit.opcodes.iget_short.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget_short.d.T_iget_short_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class. Java throws NoClassDefFoundError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE5() {
try {
            Class.forName("dot.junit.opcodes.iget_short.d.T_iget_short_7");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field. Java throws NoSuchFieldError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE6() {
try {
            Class.forName("dot.junit.opcodes.iget_short.d.T_iget_short_8");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass. Java 
     * throws IllegalAccessError on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.iget_short.d.T_iget_short_12
//@uses dot.junit.opcodes.iget_short.d.T_iget_short_1
try {
            Class.forName("dot.junit.opcodes.iget_short.d.T_iget_short_12");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

//Synthetic comment -- @@ -259,34 +267,34 @@

/**
* @constraint B12
     * @title Attempt to read inaccessible protected field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE15() {
//@uses dot.junit.opcodes.iget_short.d.T_iget_short_21
//@uses dot.junit.opcodes.iget_short.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget_short.d.T_iget_short_21");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}


/**
* @constraint A11
     * @title Attempt to read static  field. Java throws IncompatibleClassChangeError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE16() {
//@uses dot.junit.opcodes.iget_short.d.T_iget_short_5
        //@uses dot.junit.opcodes.iget_short.TestStubs  
try {
            Class.forName("dot.junit.opcodes.iget_short.d.T_iget_short_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

//Synthetic comment -- @@ -303,4 +311,3 @@
}
}
}









//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_12.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_12.java
new file mode 100644
//Synthetic comment -- index 0000000..be322f3

//Synthetic comment -- @@ -0,0 +1,25 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_13.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_13.java
new file mode 100644
//Synthetic comment -- index 0000000..3b27d6c

//Synthetic comment -- @@ -0,0 +1,23 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_21.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_21.java
//Synthetic comment -- index 2c7fd32..5bb7f6e 100644

//Synthetic comment -- @@ -18,4 +18,6 @@

public class T_iget_short_21 {

}








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_5.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_5.java
new file mode 100644
//Synthetic comment -- index 0000000..37c43519

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_6.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_6.java
new file mode 100644
//Synthetic comment -- index 0000000..5ac127b

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_7.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_7.java
new file mode 100644
//Synthetic comment -- index 0000000..7c81be5

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_8.java b/tools/vm-tests/src/dot/junit/opcodes/iget_short/d/T_iget_short_8.java
new file mode 100644
//Synthetic comment -- index 0000000..f22a81e

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/Test_iget_wide.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/Test_iget_wide.java
//Synthetic comment -- index 2260220..9294a71 100644

//Synthetic comment -- @@ -20,7 +20,14 @@
import dot.junit.DxUtil;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_1;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_11;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_2;
import dot.junit.opcodes.iget_wide.d.T_iget_wide_9;

public class Test_iget_wide extends DxTestCase {
//Synthetic comment -- @@ -99,70 +106,71 @@
*/
public void testVFE3() {
try {
            Class.forName("dot.junit.opcodes.iget_wide.d.T_iget_wide_13");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read inaccessible field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE4() {
//@uses dot.junit.opcodes.iget_wide.d.T_iget_wide_6
//@uses dot.junit.opcodes.iget_wide.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget_wide.d.T_iget_wide_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read field of undefined class. Java throws NoClassDefFoundError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE5() {
try {
            Class.forName("dot.junit.opcodes.iget_wide.d.T_iget_wide_7");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

/**
* @constraint n/a
     * @title Attempt to read undefined field. Java throws NoSuchFieldError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE6() {
try {
            Class.forName("dot.junit.opcodes.iget_wide.d.T_iget_wide_8");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}
    
/**
* @constraint n/a
     * @title Attempt to read superclass' private field from subclass. Java 
     * throws IllegalAccessError on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE7() {
//@uses dot.junit.opcodes.iget_wide.d.T_iget_wide_12
//@uses dot.junit.opcodes.iget_wide.d.T_iget_wide_1
try {
            Class.forName("dot.junit.opcodes.iget_wide.d.T_iget_wide_12");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}

//Synthetic comment -- @@ -265,34 +273,33 @@

/**
* @constraint B12
     * @title Attempt to read inaccessible protected field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE15() {
//@uses dot.junit.opcodes.iget_wide.d.T_iget_wide_21
        //@uses dot.junit.opcodes.iget.TestStubs
try {
            Class.forName("dot.junit.opcodes.iget_wide.d.T_iget_wide_21");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}


/**
* @constraint A11
     * @title Attempt to read static  field. Java throws IncompatibleClassChangeError 
     * on first access but Dalvik throws VerifyError on class loading.
*/
public void testVFE16() {
//@uses dot.junit.opcodes.iget_wide.d.T_iget_wide_5
        //@uses dot.junit.opcodes.iget_wide.TestStubs  
try {
            Class.forName("dot.junit.opcodes.iget_wide.d.T_iget_wide_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
}
}









//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_12.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_12.java
new file mode 100644
//Synthetic comment -- index 0000000..af3cbda

//Synthetic comment -- @@ -0,0 +1,25 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_13.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_13.java
new file mode 100644
//Synthetic comment -- index 0000000..98086a6

//Synthetic comment -- @@ -0,0 +1,23 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_21.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_21.java
new file mode 100644
//Synthetic comment -- index 0000000..affeadf

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_5.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_5.java
new file mode 100644
//Synthetic comment -- index 0000000..c4c130e

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_6.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_6.java
new file mode 100644
//Synthetic comment -- index 0000000..649795f

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_7.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_7.java
new file mode 100644
//Synthetic comment -- index 0000000..0866645

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_8.java b/tools/vm-tests/src/dot/junit/opcodes/iget_wide/d/T_iget_wide_8.java
new file mode 100644
//Synthetic comment -- index 0000000..570764d

//Synthetic comment -- @@ -0,0 +1,24 @@








//<Beginning of snippet n. 0>


import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.sput_short.d.T_sput_short_1;
import dot.junit.opcodes.sput_short.d.T_sput_short_11;
import dot.junit.opcodes.sput_short.d.T_sput_short_12;
import dot.junit.opcodes.sput_short.d.T_sput_short_13;
import dot.junit.opcodes.sput_short.d.T_sput_short_14;

public class Test_sput_short extends DxTestCase {
    /**
     * @title modification of final field
     */
    public void testModificationOfFinalField() {
        assertEquals(77, T_sput_short_1.st_i1);
    }

    /**
    * @constraint A12 
    * @title constant pool index
    */
    public void testVFE1() {
        try {
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_2");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint A23 
     * @title number of registers
     */
    public void testVFE2() {
        try {
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

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
        try {
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_4");
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
        try {
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
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
        try {
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_13");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint B1 
     * @title sput-short shall not work for reference fields
     */
    public void testVFE14() {
        try {
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_14");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint B1 
     * @title sput-short shall not work for char fields
     */
    public void testVFE15() {
        try {
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_16");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint B1 
     * @title sput-short shall not work for int fields
     */
    public void testVFE16() {
        try {
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_18");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint B1 
     * @title sput-short shall not work for byte fields
     */
    public void testVFE17() {
        try {
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_19");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint B1 
     * @title sput-short shall not work for boolean fields
     */
    public void testVFE18() {
        try {
            Class.forName("dot.junit.opcodes.sput_short.d.T_sput_short_11");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}

//<End of snippet n. 0>
/*Layoutlib_create: Unittest for ClassHasNativeVisitor.

Change-Id:Id101bb3fc2644e450847e73c933cb6f616982f24*/




//Synthetic comment -- diff --git a/tools/layoutlib/create/src/com/android/tools/layoutlib/annotations/Nullable.java b/tools/layoutlib/create/src/com/android/tools/layoutlib/annotations/Nullable.java
new file mode 100755
//Synthetic comment -- index 0000000..0689c92

//Synthetic comment -- @@ -0,0 +1,35 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tools.layoutlib.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes a parameter or field can be null.
 * <p/>
 * When decorating a method call parameter, this denotes the parameter can
 * legitimately be null and the method will gracefully deal with it. Typically used
 * on optional parameters.
 * <p/>
 * When decorating a method, this denotes the method might legitimately return null.
 * <p/>
 * This is a marker annotation and it has no specific attributes.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Nullable {
}








//Synthetic comment -- diff --git a/tools/layoutlib/create/src/com/android/tools/layoutlib/annotations/VisibleForTesting.java b/tools/layoutlib/create/src/com/android/tools/layoutlib/annotations/VisibleForTesting.java
new file mode 100755
//Synthetic comment -- index 0000000..e4e016b

//Synthetic comment -- @@ -0,0 +1,50 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tools.layoutlib.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes that the class, method or field has its visibility relaxed so
 * that unit tests can access it.
 * <p/>
 * The <code>visibility</code> argument can be used to specific what the original
 * visibility should have been if it had not been made public or package-private for testing.
 * The default is to consider the element private.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface VisibleForTesting {
    /**
     * Intended visibility if the element had not been made public or package-private for
     * testing.
     */
    enum Visibility {
        /** The element should be considered protected. */
        PROTECTED,
        /** The element should be considered package-private. */
        PACKAGE,
        /** The element should be considered private. */
        PRIVATE
    }

    /**
     * Intended visibility if the element had not been made public or package-private for testing.
     * If not specified, one should assume the element originally intended to be private.
     */
    Visibility visibility() default Visibility.PRIVATE;
}








//Synthetic comment -- diff --git a/tools/layoutlib/create/src/com/android/tools/layoutlib/create/ClassHasNativeVisitor.java b/tools/layoutlib/create/src/com/android/tools/layoutlib/create/ClassHasNativeVisitor.java
//Synthetic comment -- index 5424efa..722dce2 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.tools.layoutlib.create;

import com.android.tools.layoutlib.annotations.VisibleForTesting;
import com.android.tools.layoutlib.annotations.VisibleForTesting.Visibility;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
//Synthetic comment -- @@ -27,13 +30,18 @@
* Indicates if a class contains any native methods.
*/
public class ClassHasNativeVisitor implements ClassVisitor {

private boolean mHasNativeMethods = false;

public boolean hasNativeMethods() {
return mHasNativeMethods;
}

    @VisibleForTesting(visibility=Visibility.PRIVATE)
    protected void setHasNativeMethods(boolean hasNativeMethods, String methodName) {
        mHasNativeMethods = hasNativeMethods;
    }

public void visit(int version, int access, String name, String signature,
String superName, String[] interfaces) {
// pass
//Synthetic comment -- @@ -65,7 +73,9 @@

public MethodVisitor visitMethod(int access, String name, String desc,
String signature, String[] exceptions) {
        if ((access & Opcodes.ACC_NATIVE) != 0) {
            setHasNativeMethods(true, name);
        }
return null;
}









//Synthetic comment -- diff --git a/tools/layoutlib/create/tests/com/android/tools/layoutlib/create/ClassHasNativeVisitorTest.java b/tools/layoutlib/create/tests/com/android/tools/layoutlib/create/ClassHasNativeVisitorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..d6916ae

//Synthetic comment -- @@ -0,0 +1,98 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tools.layoutlib.create;

import static org.junit.Assert.*;

import org.junit.Test;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Tests {@link ClassHasNativeVisitor}.
 */
public class ClassHasNativeVisitorTest {

    @Test
    public void testHasNative() throws IOException {
        MockClassHasNativeVisitor cv = new MockClassHasNativeVisitor();
        ClassReader cr = new ClassReader(
                "com.android.tools.layoutlib.create.ClassHasNativeVisitorTest$ClassWithNative");

        cr.accept(cv, 0 /* flags */);
        assertArrayEquals(new String[] { "native_method" }, cv.getMethodsFound());
        assertTrue(cv.hasNativeMethods());
    }

    @Test
    public void testHasNoNative() throws IOException {
        MockClassHasNativeVisitor cv = new MockClassHasNativeVisitor();
        ClassReader cr = new ClassReader(
                "com.android.tools.layoutlib.create.ClassHasNativeVisitorTest$ClassWithoutNative");

        cr.accept(cv, 0 /* flags */);
        assertArrayEquals(new String[0], cv.getMethodsFound());
        assertFalse(cv.hasNativeMethods());
    }

    /**
     * Overrides {@link ClassHasNativeVisitor} to collec the name of the native methods found.
     */
    private static class MockClassHasNativeVisitor extends ClassHasNativeVisitor {
        private ArrayList<String> mMethodsFound = new ArrayList<String>();

        public String[] getMethodsFound() {
            return mMethodsFound.toArray(new String[mMethodsFound.size()]);
        }

        @Override
        protected void setHasNativeMethods(boolean hasNativeMethods, String methodName) {
            if (hasNativeMethods) {
                mMethodsFound.add(methodName);
            }
            super.setHasNativeMethods(hasNativeMethods, methodName);
        }
    }

    /**
     * Dummy test class with a native method.
     */
    public static class ClassWithNative {
        public ClassWithNative() {
        }

        public void callTheNativeMethod() {
            native_method();
        }

        private native void native_method();
    }

    /**
     * Dummy test class with no native method.
     */
    public static class ClassWithoutNative {
        public ClassWithoutNative() {
        }

        public void someMethod() {
        }
    }
}








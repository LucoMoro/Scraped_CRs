/*Layoutlib_create: Unittest for ClassHasNativeVisitor.

Change-Id:Id101bb3fc2644e450847e73c933cb6f616982f24*/
//Synthetic comment -- diff --git a/tools/layoutlib/create/src/com/android/tools/layoutlib/annotations/Nullable.java b/tools/layoutlib/create/src/com/android/tools/layoutlib/annotations/Nullable.java
new file mode 100755
//Synthetic comment -- index 0000000..0689c92

//Synthetic comment -- @@ -0,0 +1,35 @@








//Synthetic comment -- diff --git a/tools/layoutlib/create/src/com/android/tools/layoutlib/annotations/VisibleForTesting.java b/tools/layoutlib/create/src/com/android/tools/layoutlib/annotations/VisibleForTesting.java
new file mode 100755
//Synthetic comment -- index 0000000..e4e016b

//Synthetic comment -- @@ -0,0 +1,50 @@








//Synthetic comment -- diff --git a/tools/layoutlib/create/src/com/android/tools/layoutlib/create/ClassHasNativeVisitor.java b/tools/layoutlib/create/src/com/android/tools/layoutlib/create/ClassHasNativeVisitor.java
//Synthetic comment -- index 5424efa..722dce2 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.tools.layoutlib.create;

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

public void visit(int version, int access, String name, String signature,
String superName, String[] interfaces) {
// pass
//Synthetic comment -- @@ -65,7 +73,9 @@

public MethodVisitor visitMethod(int access, String name, String desc,
String signature, String[] exceptions) {
        mHasNativeMethods |= ((access & Opcodes.ACC_NATIVE) != 0);
return null;
}









//Synthetic comment -- diff --git a/tools/layoutlib/create/tests/com/android/tools/layoutlib/create/ClassHasNativeVisitorTest.java b/tools/layoutlib/create/tests/com/android/tools/layoutlib/create/ClassHasNativeVisitorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..d6916ae

//Synthetic comment -- @@ -0,0 +1,98 @@








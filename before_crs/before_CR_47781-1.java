/*Add a test for a dalvik interface cache bug.

Bug:http://code.google.com/p/android/issues/detail?id=29358Change-Id:Ia4d6be890da3a22199ad5a5183562f593a969496*/
//Synthetic comment -- diff --git a/tools/vm-tests-tf/src/dot/junit/opcodes/invoke_interface/Test_invoke_interface.java b/tools/vm-tests-tf/src/dot/junit/opcodes/invoke_interface/Test_invoke_interface.java
//Synthetic comment -- index 377cc58..ae06cda 100644

//Synthetic comment -- @@ -90,6 +90,29 @@
}

/**
* @title Native method can't be linked
*/
public void testE5() {








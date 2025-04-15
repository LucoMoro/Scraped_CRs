/*Fix Debug_InstructionCountTest

Bug 2988161

Change the assertion to allow for additional instructions
to be executed besides the ones caused by the "addTest"
calls. The additional instructions could come during
context switches.

Change-Id:Id2b2f5860fd110b9148c27255d2332eb67b83726*/




//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/Debug_InstructionCountTest.java b/tests/tests/os/src/android/os/cts/Debug_InstructionCountTest.java
//Synthetic comment -- index d6e9a68..ac8d5d8 100644

//Synthetic comment -- @@ -79,8 +79,10 @@
int insCountsThird = instructionCount.globalTotal();
int methodInvThird = instructionCount.globalMethodInvocations();

        // Additional instructions may be executed if there are context switches between
        // the resetAndStart and collect calls.
        assertTrue(insCountsThird - insCountsFirst >= (insCountsSecond - insCountsFirst) * 2);
        assertTrue(methodInvThird - methodInvFirst >= (methodInvSecond - methodInvFirst) * 2);
}

// must not be private, otherwise javac may inline the code








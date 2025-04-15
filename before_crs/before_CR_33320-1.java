/*Prevent to happen ArithmeticException "divide by zero"

If the printCurrentState() would be called without calling update()
method in advance, then sampleRealTime would be zero.

Change-Id:I46f3aaa40a59e3dc4d1c8060ba451c47b95169c7*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/os/ProcessStats.java b/core/java/com/android/internal/os/ProcessStats.java
old mode 100644
new mode 100755
//Synthetic comment -- index e0e9a29..d780ff7

//Synthetic comment -- @@ -679,6 +679,11 @@
}

final public String printCurrentState(long now) {
buildWorkingProcs();

StringWriter sw = new StringWriter();








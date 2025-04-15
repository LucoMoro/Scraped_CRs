/*Include Omitted Tests in Total

I think its better to have a fixed total, so we can tell whether
tests were accidentally being left out due to moving tests.

Change-Id:Id4559c2b36fdafb82a5aa003274860f67eb8113e*/




//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestSession.java b/tools/host/src/com/android/cts/TestSession.java
//Synthetic comment -- index 18ace44..1179512 100644

//Synthetic comment -- @@ -490,7 +490,7 @@
int omittedNum = mSessionLog.getTestList(CtsTestResult.CODE_OMITTED).size();
int notExecutedNum = mSessionLog.getTestList(CtsTestResult.CODE_NOT_EXECUTED).size();
int timeOutNum = mSessionLog.getTestList(CtsTestResult.CODE_TIMEOUT).size();
            int total = passNum + failNum + omittedNum + notExecutedNum + timeOutNum;

println("Test summary:   pass=" + passNum
+ "   fail=" + failNum








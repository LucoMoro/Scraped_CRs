/*Fix build by updating adt-test to the new LayoutLog.

Change-Id:If6c972fdb379e67686dfaf5af0bcbd6cb4aa4b9c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLoggerTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLoggerTest.java
//Synthetic comment -- index c829004..2ca5eb2 100644

//Synthetic comment -- @@ -27,8 +27,8 @@
public void testLogger2() throws Exception {
RenderLogger l = new RenderLogger("foo");
assertFalse(l.hasProblems());
        l.fidelityWarning(null, "No perspective Transforms", null);
        l.fidelityWarning(null, "No GPS", null);
assertTrue(l.hasProblems());
assertEquals("The graphics preview may not be accurate:\n"
+ "* No perspective Transforms\n" + "* No GPS\n", l.getProblems());
//Synthetic comment -- @@ -40,7 +40,7 @@
RenderLogger l = new RenderLogger("foo");
assertFalse(l.hasProblems());
l.error("timeout", "Sample Error", new RuntimeException());
        l.warning("slow", "Sample warning");
assertTrue(l.hasProblems());
assertEquals("Sample Error\n" + "Sample warning\n"
+ "Exception details are logged in Window > Show View > Error Log", l.getProblems());








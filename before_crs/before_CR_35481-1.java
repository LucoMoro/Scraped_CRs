/*Bypassed two test cases for 4.0.3 builds

These tests are not compatible with 4.0.3.*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PaintTest.java b/tests/tests/graphics/src/android/graphics/cts/PaintTest.java
//Synthetic comment -- index 169fb56..baf2e42 100644

//Synthetic comment -- @@ -1248,6 +1248,11 @@
}

public void testMeasureTextWithLongText() {
final int MAX_COUNT = 65535;
char[] longText = new char[MAX_COUNT];
for (int n = 0; n < MAX_COUNT; n++) {








//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java b/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java
//Synthetic comment -- index 05e668b..2af4482 100644

//Synthetic comment -- @@ -215,6 +215,11 @@
)
})
public void testAccessAllowFileAccess() {
assertTrue(mSettings.getAllowFileAccess());

String fileUrl = TestHtmlConstants.getFileUrl(TestHtmlConstants.HELLO_WORLD_URL);








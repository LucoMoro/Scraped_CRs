/*Allow specifying a list of complementary features where only one is required

Change-Id:I73cb484a140d2fff551c24bedf18db98f975e1e8*/
//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/ManifestTestListAdapter.java b/apps/CtsVerifier/src/com/android/cts/verifier/ManifestTestListAdapter.java
//Synthetic comment -- index 1f19cbe..539408a 100644

//Synthetic comment -- @@ -67,6 +67,13 @@
*             <meta-data android:name="test_required_features" android:value="android.hardware.sensor.accelerometer" />
*         </pre>
*     </li>
*
* </ol>
*/
//Synthetic comment -- @@ -77,6 +84,7 @@
private static final String TEST_PARENT_META_DATA = "test_parent";

private static final String TEST_REQUIRED_FEATURES_META_DATA = "test_required_features";

private Context mContext;

//Synthetic comment -- @@ -151,8 +159,10 @@
String title = getTitle(mContext, info.activityInfo);
String testName = info.activityInfo.name;
Intent intent = getActivityIntent(info.activityInfo);
            String[] requiredFeatures = getRequiredFeatures(info.activityInfo.metaData);
            TestListItem item = TestListItem.newTest(title, testName, intent, requiredFeatures);

String testCategory = getTestCategory(mContext, info.activityInfo.metaData);
addTestToCategory(testsByCategory, testCategory, item);
//Synthetic comment -- @@ -177,11 +187,12 @@
return metaData != null ? metaData.getString(TEST_PARENT_META_DATA) : null;
}

    static String[] getRequiredFeatures(Bundle metaData) {
if (metaData == null) {
return null;
} else {
            String value = metaData.getString(TEST_REQUIRED_FEATURES_META_DATA);
if (value == null) {
return null;
} else {
//Synthetic comment -- @@ -223,14 +234,29 @@
while (iterator.hasNext()) {
TestListItem item = iterator.next();
String[] requiredFeatures = item.requiredFeatures;
if (requiredFeatures != null) {
for (int i = 0; i < requiredFeatures.length; i++) {
if (!packageManager.hasSystemFeature(requiredFeatures[i])) {
iterator.remove();
break;
}
}
}
}
return filteredTests;
}








//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/TestListAdapter.java b/apps/CtsVerifier/src/com/android/cts/verifier/TestListAdapter.java
//Synthetic comment -- index 2cc79fb..3bf1e1c 100644

//Synthetic comment -- @@ -83,14 +83,30 @@
/** Features necessary to run this test. */
final String[] requiredFeatures;

public static TestListItem newTest(Context context, int titleResId, String testName,
Intent intent, String[] requiredFeatures) {
            return newTest(context.getString(titleResId), testName, intent, requiredFeatures);
}

public static TestListItem newTest(String title, String testName, Intent intent,
String[] requiredFeatures) {
            return new TestListItem(title, testName, intent, requiredFeatures);
}

public static TestListItem newCategory(Context context, int titleResId) {
//Synthetic comment -- @@ -98,15 +114,16 @@
}

public static TestListItem newCategory(String title) {
            return new TestListItem(title, null, null, null);
}

private TestListItem(String title, String testName, Intent intent,
                String[] requiredFeatures) {
this.title = title;
this.testName = testName;
this.intent = intent;
this.requiredFeatures = requiredFeatures;
}

boolean isTest() {








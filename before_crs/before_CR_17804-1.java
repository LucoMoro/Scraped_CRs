/*Fix CTS host unit tests.

Change-Id:Ib4836478d58d9761e11074b906b07a0a312e29ed*/
//Synthetic comment -- diff --git a/tools/host/test/com/android/cts/TestSessionBuilderTests.java b/tools/host/test/com/android/cts/TestSessionBuilderTests.java
//Synthetic comment -- index 26eb561..d0f5230 100644

//Synthetic comment -- @@ -34,6 +34,8 @@
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.android.cts.TestDevice.DeviceParameterCollector;

/**
//Synthetic comment -- @@ -107,7 +109,7 @@
createTestPackage(descriptionConfigStr, mTestPackageBinaryName);
HostConfig.getInstance().loadTestPackages();

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
assertEquals(1, ts.getNumOfRequiredDevices());

TestSessionLog tsl = ts.getSessionLog();
//Synthetic comment -- @@ -288,7 +290,7 @@
createTestPackage(descriptionConfigStr, mTestPackageBinaryName);
HostConfig.getInstance().loadTestPackages();

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
assertEquals(1, ts.getNumOfRequiredDevices());

TestSessionLog tsl = ts.getSessionLog();
//Synthetic comment -- @@ -369,7 +371,7 @@
createTestPackage(descriptionConfigStr, mTestPackageBinaryName);
HostConfig.getInstance().loadTestPackages();

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
assertEquals(1, ts.getNumOfRequiredDevices());

TestSessionLog tsl = ts.getSessionLog();
//Synthetic comment -- @@ -474,7 +476,7 @@
createTestPackage(descriptionConfigStr, mTestPackageBinaryName);
HostConfig.getInstance().loadTestPackages();

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
assertEquals(1, ts.getNumOfRequiredDevices());

TestSessionLog tsl = ts.getSessionLog();
//Synthetic comment -- @@ -558,7 +560,7 @@
String planPath = HostConfig.getInstance().getPlanRepository().getPlanPath(planName);
TestSessionBuilder.getInstance().serialize(planName, packageNames, results);

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
assertEquals(1, ts.getNumOfRequiredDevices());

TestSessionLog tsl = ts.getSessionLog();
//Synthetic comment -- @@ -640,7 +642,7 @@
String planPath = HostConfig.getInstance().getPlanRepository().getPlanPath(planName);
TestSessionBuilder.getInstance().serialize(planName, packageNames, results);

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
assertEquals(1, ts.getNumOfRequiredDevices());

TestSessionLog tsl = ts.getSessionLog();
//Synthetic comment -- @@ -706,7 +708,7 @@
String planPath = HostConfig.getInstance().getPlanRepository().getPlanPath(planName);
TestSessionBuilder.getInstance().serialize(planName, packageNames, results);

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
assertEquals(1, ts.getNumOfRequiredDevices());

TestSessionLog tsl = ts.getSessionLog();
//Synthetic comment -- @@ -787,7 +789,7 @@
String planPath = HostConfig.getInstance().getPlanRepository().getPlanPath(planName);
TestSessionBuilder.getInstance().serialize(planName, packageNames, results);

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
assertEquals(1, ts.getNumOfRequiredDevices());

TestSessionLog tsl = ts.getSessionLog();
//Synthetic comment -- @@ -848,7 +850,7 @@
String planPath = HostConfig.getInstance().getPlanRepository().getPlanPath(planName);
TestSessionBuilder.getInstance().serialize(planName, packageNames, results);

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
assertEquals(1, ts.getNumOfRequiredDevices());

TestSessionLog tsl = ts.getSessionLog();
//Synthetic comment -- @@ -923,7 +925,7 @@
String planPath = HostConfig.getInstance().getPlanRepository().getPlanPath(planName);
TestSessionBuilder.getInstance().serialize(planName, packageNames, results);

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
assertEquals(1, ts.getNumOfRequiredDevices());

TestSessionLog tsl = ts.getSessionLog();
//Synthetic comment -- @@ -996,7 +998,7 @@
String planPath = HostConfig.getInstance().getPlanRepository().getPlanPath(planName);
TestSessionBuilder.getInstance().serialize(planName, packageNames, results);

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
assertEquals(1, ts.getNumOfRequiredDevices());

TestSessionLog tsl = ts.getSessionLog();
//Synthetic comment -- @@ -1091,7 +1093,7 @@
String planPath = HostConfig.getInstance().getPlanRepository().getPlanPath(planName);
TestSessionBuilder.getInstance().serialize(planName, packageNames, results);

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
assertEquals(1, ts.getNumOfRequiredDevices());

TestSessionLog tsl = ts.getSessionLog();
//Synthetic comment -- @@ -1172,7 +1174,7 @@
createTestPackage(descriptionConfigStr, mTestPackageBinaryName);
HostConfig.getInstance().loadTestPackages();

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
assertEquals(2, ts.getNumOfRequiredDevices());

TestSessionLog tsl = ts.getSessionLog();
//Synthetic comment -- @@ -1201,7 +1203,7 @@
* Test serializing the test plan.
*/
public void testSerialize() throws Exception {
        final String srcStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
+ "<TestPlan version=\"1.0\">\n"
+ "<PlanSettings/>\n"
+ "<Entry uri=\"com.google.android.cts.CtsTest\"/>\n"
//Synthetic comment -- @@ -1284,7 +1286,7 @@
String planPath = HostConfig.getInstance().getPlanRepository().getPlanPath(planName);
TestSessionBuilder.getInstance().serialize(planName, packageNames, results);

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
assertEquals(1, ts.getNumOfRequiredDevices());

ts.getSessionLog().setStartTime(System.currentTimeMillis());
//Synthetic comment -- @@ -1423,7 +1425,7 @@
String planPath = HostConfig.getInstance().getPlanRepository().getPlanPath(planName);
TestSessionBuilder.getInstance().serialize(planName, packageNames, results);

        TestSession ts = TestSessionBuilder.getInstance().build(planPath);
ts.getSessionLog().setStartTime(System.currentTimeMillis());
TestSessionLog tsl = ts.getSessionLog();
TestPackage testPackage = tsl.getTestPackages().iterator().next();








//Synthetic comment -- diff --git a/tools/host/test/com/android/cts/TestSessionLogBuilderTests.java b/tools/host/test/com/android/cts/TestSessionLogBuilderTests.java
//Synthetic comment -- index 7deb9af..a2b5274e 100644

//Synthetic comment -- @@ -40,7 +40,7 @@
"<?xml-stylesheet type=\"text/xsl\"  href=\"cts_result.xsl\"?>\n" +
"\n" +
"<TestResult endtime=\"Wed Apr 29 10:36:47 CST 2009\" " +
            "starttime=\"Wed Apr 29 10:36:30 CST 2009\" testPlan=\"location\" version=\"1.0\">\n" +
"  <DeviceInfo>\n" +
"    <Screen resolution=\"480x320\"/>\n" +
"    <PhoneSubInfo subscriberId=\"15555218135\"/>\n" +








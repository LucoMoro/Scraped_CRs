/*Fix various warnings

Change-Id:I4864b89347ed8756b1eae83d97990555144fb93a*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AndroidDebugBridge.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index 01cdf0a..30bbada 100644

//Synthetic comment -- @@ -620,6 +620,7 @@
* @param line The line to scan.
* @return True if a version number was found (whether it is acceptable or not).
*/
    @SuppressWarnings("all") // With Eclipse 3.6, replace by @SuppressWarnings("unused")
private boolean scanVersionLine(String line) {
if (line != null) {
Matcher matcher = sAdbVersion.matcher(line);








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/log/event/DisplaySync.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/log/event/DisplaySync.java
//Synthetic comment -- index 1c006bc..6122513 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ddmlib.log.EventContainer;
import com.android.ddmlib.log.EventLogParser;
import com.android.ddmlib.log.InvalidTypeException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jfree.chart.labels.CustomXYToolTipGenerator;
//Synthetic comment -- @@ -78,7 +79,11 @@

XYBarRenderer br = new XYBarRenderer();
mDatasetsSync = new TimePeriodValues[NUM_AUTHS];

        @SuppressWarnings("unchecked")
        List<String> mTooltipsSyncTmp[] = new List[NUM_AUTHS];
        mTooltipsSync = mTooltipsSyncTmp;

mTooltipGenerators = new CustomXYToolTipGenerator[NUM_AUTHS];

TimePeriodValuesCollection tpvc = new TimePeriodValuesCollection();
//Synthetic comment -- @@ -129,7 +134,7 @@
if (event.mTag == EVENT_TICKLE) {
int auth = getAuth(event.getValueAsString(0));
if (auth >= 0) {
                    long msec = event.sec * 1000L + (event.nsec / 1000000L);
mDatasetsSyncTickle[auth].addOrUpdate(new FixedMillisecond(msec), -1);
}
}
//Synthetic comment -- @@ -282,7 +287,7 @@
mTooltipsSync[auth].add(getTextFromDetails(auth, details, syncSource));
mTooltipGenerators[auth].addToolTipSeries(mTooltipsSync[auth]);
if (details.indexOf('x') >= 0 || details.indexOf('X') >= 0) {
            long msec = event.sec * 1000L + (event.nsec / 1000000L);
mDatasetError.addOrUpdate(new FixedMillisecond(msec), -1);
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/log/event/DisplaySyncHistogram.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/log/event/DisplaySyncHistogram.java
//Synthetic comment -- index 36d90ce..5bfc039 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.ddmlib.log.EventContainer;
import com.android.ddmlib.log.EventLogParser;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jfree.chart.plot.XYPlot;
//Synthetic comment -- @@ -69,7 +70,10 @@

AbstractXYItemRenderer br = new XYBarRenderer();
mDatasetsSyncHist = new TimePeriodValues[NUM_AUTHS+1];

        @SuppressWarnings("unchecked")
        Map<SimpleTimePeriod, Integer> mTimePeriodMapTmp[] = new HashMap[NUM_AUTHS + 1];
        mTimePeriodMap = mTimePeriodMapTmp;

TimePeriodValuesCollection tpvc = new TimePeriodValuesCollection();
xyPlot.setDataset(tpvc);








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/log/event/DisplaySyncPerf.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/log/event/DisplaySyncPerf.java
//Synthetic comment -- index b484f26..10176e3 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ddmlib.log.EventContainer;
import com.android.ddmlib.log.EventLogParser;
import com.android.ddmlib.log.InvalidTypeException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jfree.chart.labels.CustomXYToolTipGenerator;
//Synthetic comment -- @@ -35,7 +36,8 @@
public class DisplaySyncPerf extends SyncCommon {

CustomXYToolTipGenerator mTooltipGenerator;

    List<String> mTooltips[];

// The series number for each graphed item.
// sync authorities are 0-3
//Synthetic comment -- @@ -125,7 +127,10 @@
XYPlot xyPlot = mChart.getXYPlot();
xyPlot.getRangeAxis().setVisible(false);
mTooltipGenerator = new CustomXYToolTipGenerator();

        @SuppressWarnings("unchecked")
        List<String>[] mTooltipsTmp = new List[NUM_SERIES];
        mTooltips = mTooltipsTmp;

XYBarRenderer br = new XYBarRenderer();
br.setUseYInterval(true);
//Synthetic comment -- @@ -158,7 +163,7 @@
if (event.mTag == EVENT_DB_OPERATION) {
// 52000 db_operation (name|3),(op_type|1|5),(time|2|3)
String tip = event.getValueAsString(0);
                long endTime = event.sec * 1000L + (event.nsec / 1000000L);
int opType = Integer.parseInt(event.getValueAsString(1));
long duration = Long.parseLong(event.getValueAsString(2));

//Synthetic comment -- @@ -175,7 +180,7 @@
// 52001 http_stats (useragent|3),(response|2|3),(processing|2|3),(tx|1|2),(rx|1|2)
String tip = event.getValueAsString(0) + ", tx:" + event.getValueAsString(3) +
", rx: " + event.getValueAsString(4);
                long endTime = event.sec * 1000L + (event.nsec / 1000000L);
long netEndTime = endTime - Long.parseLong(event.getValueAsString(2));
long netStartTime = netEndTime - Long.parseLong(event.getValueAsString(1));
mDatasets[HTTP_NETWORK].add(new SimpleTimePeriod(netStartTime, netEndTime),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java
//Synthetic comment -- index 50df52a..9ee286e 100644

//Synthetic comment -- @@ -24,7 +24,6 @@
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -839,7 +838,7 @@

private static void rebindClasspathEntries(IJavaModel model, IPath containerPath)
throws JavaModelException {
        ArrayList<IJavaProject> affectedProjects = new ArrayList<IJavaProject>();

IJavaProject[] projects = model.getJavaProjects();
for (int i = 0; i < projects.length; i++) {
//Synthetic comment -- @@ -854,7 +853,7 @@
}
}
if (!affectedProjects.isEmpty()) {
            IJavaProject[] affected = affectedProjects
.toArray(new IJavaProject[affectedProjects.size()]);
updateProjects(affected);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index f41a240..a9b615f 100644

//Synthetic comment -- @@ -687,7 +687,6 @@
*
* @param selection The selection when the wizard was initiated.
*/
private void initializeFromSelection(IStructuredSelection selection) {
if (selection == null) {
return;








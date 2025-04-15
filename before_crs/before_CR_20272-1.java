/*Merge: Cleanup ArrayList usage in SDK Manager.

Change-Id:Id03b96aa420a0aa83771c60880887577fd8c020e*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateChooserDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateChooserDialog.java
//Synthetic comment -- index 277a9c2..81ca91e 100755

//Synthetic comment -- @@ -55,6 +55,7 @@
import org.eclipse.swt.widgets.TableColumn;

import java.util.ArrayList;


/**
//Synthetic comment -- @@ -84,15 +85,15 @@

/**
* List of all archives to be installed with dependency information.
     *
* Note: in a lot of cases, we need to find the archive info for a given archive. This
* is currently done using a simple linear search, which is fine since we only have a very
* limited number of archives to deal with (e.g. < 10 now). We might want to revisit
* this later if it becomes an issue. Right now just do the simple thing.
     *
* Typically we could add a map Archive=>ArchiveInfo later.
*/
    private final ArrayList<ArchiveInfo> mArchives;



//Synthetic comment -- @@ -104,7 +105,7 @@
*/
public UpdateChooserDialog(Shell parentShell,
UpdaterData updaterData,
            ArrayList<ArchiveInfo> archives) {
super(parentShell, 3, false/*makeColumnsEqual*/);
mUpdaterData = updaterData;
mArchives = archives;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 33d756a..e8d3103 100755

//Synthetic comment -- @@ -57,6 +57,7 @@
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
* Data shared between {@link UpdaterWindowImpl} and its pages.
//Synthetic comment -- @@ -399,7 +400,7 @@
*
* @param result The archives to install. Incompatible ones will be skipped.
*/
    public void installArchives(final ArrayList<ArchiveInfo> result) {
if (mTaskFactory == null) {
throw new IllegalArgumentException("Task Factory is null");
}
//Synthetic comment -- @@ -633,7 +634,7 @@
// resolve missing dependencies.

UpdaterLogic ul = new UpdaterLogic(this);
        ArrayList<ArchiveInfo> archives = ul.computeUpdates(
selectedArchives,
getSources(),
getLocalSdkParser().getPackages(),
//Synthetic comment -- @@ -681,7 +682,7 @@
refreshSources(true);

UpdaterLogic ul = new UpdaterLogic(this);
        ArrayList<ArchiveInfo> archives = ul.computeUpdates(
null /*selectedArchives*/,
getSources(),
getLocalSdkParser().getPackages(),








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index 505a613..824a89f 100755

//Synthetic comment -- @@ -42,6 +42,7 @@
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
* The logic to compute which packages to install, based on the choices
//Synthetic comment -- @@ -65,14 +66,14 @@
* When the user doesn't provide a selection, looks at local packages to find
* those that can be updated and compute dependencies too.
*/
    public ArrayList<ArchiveInfo> computeUpdates(
Collection<Archive> selectedArchives,
SdkSources sources,
Package[] localPkgs,
boolean includeObsoletes) {

        ArrayList<ArchiveInfo> archives = new ArrayList<ArchiveInfo>();
        ArrayList<Package> remotePkgs = new ArrayList<Package>();
SdkSource[] remoteSources = sources.getAllSources();

// Create ArchiveInfos out of local (installed) packages.
//Synthetic comment -- @@ -121,7 +122,7 @@
* and adds them to the list of archives to install.
*/
public void addNewPlatforms(
            ArrayList<ArchiveInfo> archives,
SdkSources sources,
Package[] localPkgs,
boolean includeObsoletes) {
//Synthetic comment -- @@ -280,7 +281,7 @@
*/
private Collection<Archive> findUpdates(
ArchiveInfo[] localArchives,
            ArrayList<Package> remotePkgs,
SdkSource[] remoteSources,
boolean includeObsoletes) {
ArrayList<Archive> updates = new ArrayList<Archive>();
//Synthetic comment -- @@ -320,9 +321,9 @@
* an appropriate package.
*/
private void fixMissingLocalDependencies(
            ArrayList<ArchiveInfo> outArchives,
Collection<Archive> selectedArchives,
            ArrayList<Package> remotePkgs,
SdkSource[] remoteSources,
ArchiveInfo[] localArchives) {

//Synthetic comment -- @@ -364,9 +365,9 @@
}

private ArchiveInfo insertArchive(Archive archive,
            ArrayList<ArchiveInfo> outArchives,
Collection<Archive> selectedArchives,
            ArrayList<Package> remotePkgs,
SdkSource[] remoteSources,
ArchiveInfo[] localArchives,
boolean automated) {
//Synthetic comment -- @@ -430,9 +431,9 @@
* at least size 1 and contain no null elements.
*/
private ArchiveInfo[] findDependency(Package pkg,
            ArrayList<ArchiveInfo> outArchives,
Collection<Archive> selectedArchives,
            ArrayList<Package> remotePkgs,
SdkSource[] remoteSources,
ArchiveInfo[] localArchives) {

//Synthetic comment -- @@ -534,9 +535,9 @@
*/
protected ArchiveInfo findToolsDependency(
IMinToolsDependency pkg,
            ArrayList<ArchiveInfo> outArchives,
Collection<Archive> selectedArchives,
            ArrayList<Package> remotePkgs,
SdkSource[] remoteSources,
ArchiveInfo[] localArchives) {
// This is the requirement to match.
//Synthetic comment -- @@ -632,9 +633,9 @@
*/
protected ArchiveInfo findPlatformToolsDependency(
IMinPlatformToolsDependency pkg,
            ArrayList<ArchiveInfo> outArchives,
Collection<Archive> selectedArchives,
            ArrayList<Package> remotePkgs,
SdkSource[] remoteSources,
ArchiveInfo[] localArchives) {
// This is the requirement to match.
//Synthetic comment -- @@ -773,9 +774,9 @@
*/
protected ArchiveInfo findPlatformDependency(
IPlatformDependency pkg,
            ArrayList<ArchiveInfo> outArchives,
Collection<Archive> selectedArchives,
            ArrayList<Package> remotePkgs,
SdkSource[] remoteSources,
ArchiveInfo[] localArchives) {
// This is the requirement to match.
//Synthetic comment -- @@ -872,9 +873,9 @@
*/
protected ArchiveInfo findMinApiLevelDependency(
IMinApiLevelDependency pkg,
            ArrayList<ArchiveInfo> outArchives,
Collection<Archive> selectedArchives,
            ArrayList<Package> remotePkgs,
SdkSource[] remoteSources,
ArchiveInfo[] localArchives) {

//Synthetic comment -- @@ -987,9 +988,9 @@
*/
protected ArchiveInfo findExactApiLevelDependency(
IExactApiLevelDependency pkg,
            ArrayList<ArchiveInfo> outArchives,
Collection<Archive> selectedArchives,
            ArrayList<Package> remotePkgs,
SdkSource[] remoteSources,
ArchiveInfo[] localArchives) {

//Synthetic comment -- @@ -1095,7 +1096,7 @@
* @param remoteSources A list of available remote sources to fetch from.
*/
protected void fetchRemotePackages(
            final ArrayList<Package> remotePkgs,
final SdkSource[] remoteSources) {
if (remotePkgs.size() > 0) {
return;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java
//Synthetic comment -- index dc65e14..fb956f2 100755

//Synthetic comment -- @@ -34,6 +34,7 @@

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

//Synthetic comment -- @@ -80,7 +81,7 @@
}

@Override
        protected void fetchRemotePackages(ArrayList<Package> remotePkgs,
SdkSource[] remoteSources) {
// Ignore remoteSources and instead uses the remotePackages list given to the
// constructor.








/*Add multi-apk export log.

The log is used to confirm that new export do not conflict
with previous build. The build file can also be used to set
per-apk minor version code.

Change-Id:Ic5ad98758aa327f6a5bc1d00e66cf5437ac098e4*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/MultiApkExportTask.java b/anttasks/src/com/android/ant/MultiApkExportTask.java
//Synthetic comment -- index 2c161d5..eb3f055 100644

//Synthetic comment -- @@ -33,9 +33,12 @@
import org.apache.tools.ant.types.FileSet;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
//Synthetic comment -- @@ -54,6 +57,11 @@
*/
public class MultiApkExportTask extends Task {

/**
* Class representing one apk that needs to be generated. This contains
* which project it must be created from, and which filters should be used.
//Synthetic comment -- @@ -61,8 +69,16 @@
* This class is meant to be sortable in a way that allows generation of the buildInfo
* value that goes in the composite versionCode.
*/
    private static class ExportData implements Comparable<ExportData> {

String relativePath;
File project;
int buildInfo;
//Synthetic comment -- @@ -74,11 +90,11 @@
int glVersion;
// screen size?

        ExportData() {
// do nothing.
}

        public ExportData(ExportData data) {
relativePath = data.relativePath;
project = data.project;
buildInfo = data.buildInfo;
//Synthetic comment -- @@ -88,7 +104,18 @@
glVersion = data.glVersion;
}

        public int compareTo(ExportData o) {
int minSdkDiff = minSdkVersion - o.minSdkVersion;
if (minSdkDiff != 0) {
return minSdkDiff;
//Synthetic comment -- @@ -116,6 +143,75 @@

return 0;
}
}

private static enum Target {
//Synthetic comment -- @@ -178,15 +274,47 @@
System.out.println("versionCode: " + version);

// checks whether the projects can be signed.
        String value = antProject.getProperty("key.store");
        String keyStore = value != null && value.length() > 0 ? value : null;
        value = antProject.getProperty("key.alias");
        String keyAlias = value != null && value.length() > 0 ? value : null;
        boolean canSign = keyStore != null && keyAlias != null;

        ExportData[] projects = getProjects(antProject, appPackage);
HashSet<String> compiledProject = new HashSet<String>();

XPathFactory xPathFactory = XPathFactory.newInstance();

File exportProjectOutput = new File(getValidatedProperty(antProject, "out.absolute.dir"));
//Synthetic comment -- @@ -216,16 +344,16 @@
keyAliasPassword = getValidatedProperty(antProject, "key.alias.password");
}

        for (ExportData projectData : projects) {
// this output is prepended by "[android-export] " (17 chars), so we put 61 stars
System.out.println("\n*************************************************************");
            System.out.println("Exporting project: " + projectData.relativePath);

SubAnt subAnt = new SubAnt();
subAnt.setTarget(mTarget.getTarget());
subAnt.setProject(antProject);

            File subProjectFolder = new File(antProject.getBaseDir(), projectData.relativePath);

FileSet fileSet = new FileSet();
fileSet.setProject(antProject);
//Synthetic comment -- @@ -240,19 +368,19 @@
// this project.
// (projects can be export multiple time if some properties are set up to
// generate more than one APK (for instance ABI split).
                if (compiledProject.contains(projectData.relativePath) == false) {
                    compiledProject.add(projectData.relativePath);
} else {
addProp(subAnt, "do.not.compile", "true");
}

// set the version code, and filtering
                String compositeVersionCode = getVersionCodeString(versionCode, projectData);
addProp(subAnt, "version.code", compositeVersionCode);
System.out.println("Composite versionCode: " + compositeVersionCode);
                if (projectData.abi != null) {
                    addProp(subAnt, "filter.abi", projectData.abi);
                    System.out.println("ABI Filter: " + projectData.abi);
}

// end of the output by this task. Everything that follows will be output
//Synthetic comment -- @@ -279,7 +407,7 @@

// override the resource pack file.
addProp(subAnt, "resource.package.file.name",
                        name + "-" + projectData.buildInfo + ".ap_");

if (canSign) {
// set the properties for the password.
//Synthetic comment -- @@ -288,17 +416,18 @@
addProp(subAnt, "key.store.password", keyStorePassword);
addProp(subAnt, "key.alias.password", keyAliasPassword);


// temporary file only get a filename change (still stored in the project
// bin folder).
addProp(subAnt, "out.unsigned.file.name",
                            name + "-" + projectData.buildInfo + "-unsigned.apk");
addProp(subAnt, "out.unaligned.file",
                            name + "-" + projectData.buildInfo + "-unaligned.apk");

// final file is stored locally.
addProp(subAnt, "out.release.file", new File(exportProjectOutput,
                            name + "-" + projectData.buildInfo + "-release.apk").getAbsolutePath());
} else {
// put some empty prop. This is to override possible ones defined in the
// project. The reason is that if there's more than one project, we don't
//Synthetic comment -- @@ -307,16 +436,18 @@
addProp(subAnt, "key.store", "");
addProp(subAnt, "key.alias", "");
// final file is the unsigned version. It gets stored locally.
addProp(subAnt, "out.unsigned.file", new File(exportProjectOutput,
                            name + "-" + projectData.buildInfo + "-unsigned.apk").getAbsolutePath());
}
}

subAnt.execute();
}

        // TODO: export build log.

}

/**
//Synthetic comment -- @@ -343,11 +474,11 @@
* @param antProject the Ant project.
* @param appPackage the application package. Projects' manifest must match this.
*/
    private ExportData[] getProjects(Project antProject, String appPackage) {
String projects = antProject.getProperty("projects");
String[] paths = projects.split("\\:");

        ArrayList<ExportData> datalist = new ArrayList<ExportData>();

for (String path : paths) {
File projectFolder = new File(path);
//Synthetic comment -- @@ -374,11 +505,11 @@
SdkConstants.FN_ANDROID_MANIFEST_XML));
}

                ArrayList<ExportData> datalist2 = checkManifest(androidManifest, appPackage);

// if the method returns without throwing, this is a good project to
// export.
                for (ExportData data : datalist2) {
data.relativePath = path;
data.project = projectFolder;
}
//Synthetic comment -- @@ -393,14 +524,22 @@
// sort the projects and assign buildInfo
Collections.sort(datalist);
int buildInfo = 0;
        for (ExportData data : datalist) {
data.buildInfo = buildInfo++;
}

        return datalist.toArray(new ExportData[datalist.size()]);
}

    private ArrayList<ExportData> checkManifest(FileWrapper androidManifest, String appPackage) {
try {
String manifestPackage = AndroidManifest.getPackage(androidManifest);
if (appPackage.equals(manifestPackage) == false) {
//Synthetic comment -- @@ -421,8 +560,8 @@
"Codename in minSdkVersion is not supported by multi-apk export.");
}

            ArrayList<ExportData> dataList = new ArrayList<ExportData>();
            ExportData data = new ExportData();
dataList.add(data);
data.minSdkVersion = minSdkVersion;

//Synthetic comment -- @@ -441,10 +580,10 @@
if (apkSettings.isSplitByAbi()) {
// need to find the available ABIs.
List<String> abis = findAbis(projectPath);
                    ExportData current = data;
for (String abi : abis) {
if (current == null) {
                            current = new ExportData(data);
dataList.add(current);
}

//Synthetic comment -- @@ -464,6 +603,11 @@
}
}

private List<String> findAbis(String projectPath) {
ArrayList<String> abiList = new ArrayList<String>();
File libs = new File(projectPath, SdkConstants.FD_NATIVE_LIBS);
//Synthetic comment -- @@ -488,6 +632,12 @@
return abiList;
}

private void addProp(SubAnt task, String name, String value) {
Property prop = new Property();
prop.setName(name);
//Synthetic comment -- @@ -495,12 +645,144 @@
task.addProperty(prop);
}

    private String getVersionCodeString(int versionCode, ExportData projectData) {
        int trueVersionCode = versionCode * 10000;
        trueVersionCode += projectData.buildInfo * 100;
        trueVersionCode += projectData.minor;

return Integer.toString(trueVersionCode);
}

}








/*Add multi-apk export log.

The log is used to confirm that new export do not conflict
with previous build. The build file can also be used to set
per-apk minor version code.

Change-Id:Ic5ad98758aa327f6a5bc1d00e66cf5437ac098e4*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/MultiApkExportTask.java b/anttasks/src/com/android/ant/MultiApkExportTask.java
//Synthetic comment -- index 2c161d5..e2b342f 100644

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
//Synthetic comment -- @@ -63,6 +71,13 @@
*/
private static class ExportData implements Comparable<ExportData> {

String relativePath;
File project;
int buildInfo;
//Synthetic comment -- @@ -73,6 +88,7 @@
String abi;
int glVersion;
// screen size?

ExportData() {
// do nothing.
//Synthetic comment -- @@ -88,6 +104,17 @@
glVersion = data.glVersion;
}

public int compareTo(ExportData o) {
int minSdkDiff = minSdkVersion - o.minSdkVersion;
if (minSdkDiff != 0) {
//Synthetic comment -- @@ -116,6 +143,60 @@

return 0;
}
}

private static enum Target {
//Synthetic comment -- @@ -178,15 +259,50 @@
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
//Synthetic comment -- @@ -288,7 +404,6 @@
addProp(subAnt, "key.store.password", keyStorePassword);
addProp(subAnt, "key.alias.password", keyAliasPassword);


// temporary file only get a filename change (still stored in the project
// bin folder).
addProp(subAnt, "out.unsigned.file.name",
//Synthetic comment -- @@ -297,8 +412,10 @@
name + "-" + projectData.buildInfo + "-unaligned.apk");

// final file is stored locally.
addProp(subAnt, "out.release.file", new File(exportProjectOutput,
                            name + "-" + projectData.buildInfo + "-release.apk").getAbsolutePath());
} else {
// put some empty prop. This is to override possible ones defined in the
// project. The reason is that if there's more than one project, we don't
//Synthetic comment -- @@ -307,16 +424,18 @@
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
//Synthetic comment -- @@ -496,11 +615,121 @@
}

private String getVersionCodeString(int versionCode, ExportData projectData) {
        int trueVersionCode = versionCode * 10000;
        trueVersionCode += projectData.buildInfo * 100;
trueVersionCode += projectData.minor;

return Integer.toString(trueVersionCode);
}

}








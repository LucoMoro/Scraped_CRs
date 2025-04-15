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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
//Synthetic comment -- @@ -54,6 +57,11 @@
*/
public class MultiApkExportTask extends Task {

    private final static int MAX_MINOR = 100;
    private final static int MAX_BUILDINFO = 100;
    private final static int OFFSET_BUILD_INFO = MAX_MINOR;
    private final static int OFFSET_VERSION_CODE = OFFSET_BUILD_INFO * MAX_BUILDINFO;

/**
* Class representing one apk that needs to be generated. This contains
* which project it must be created from, and which filters should be used.
//Synthetic comment -- @@ -61,8 +69,16 @@
* This class is meant to be sortable in a way that allows generation of the buildInfo
* value that goes in the composite versionCode.
*/
    public static class ApkData implements Comparable<ApkData> {

        private final static int INDEX_OUTPUTNAME = 0;
        private final static int INDEX_PROJECT    = 1;
        private final static int INDEX_MINOR      = 2;
        private final static int INDEX_MINSDK     = 3;
        private final static int INDEX_ABI        = 4;
        private final static int INDEX_MAX        = 5;

        String outputName;
String relativePath;
File project;
int buildInfo;
//Synthetic comment -- @@ -74,11 +90,11 @@
int glVersion;
// screen size?

        public ApkData() {
// do nothing.
}

        public ApkData(ApkData data) {
relativePath = data.relativePath;
project = data.project;
buildInfo = data.buildInfo;
//Synthetic comment -- @@ -88,7 +104,18 @@
glVersion = data.glVersion;
}

        public String toString() {
            StringBuilder sb = new StringBuilder(outputName);
            sb.append(" / ").append(relativePath);
            sb.append(" / ").append(buildInfo);
            sb.append(" / ").append(minor);
            sb.append(" / ").append(minSdkVersion);
            sb.append(" / ").append(abi);

            return sb.toString();
        }

        public int compareTo(ApkData o) {
int minSdkDiff = minSdkVersion - o.minSdkVersion;
if (minSdkDiff != 0) {
return minSdkDiff;
//Synthetic comment -- @@ -116,6 +143,75 @@

return 0;
}

        /**
         * Writes the apk description in the given writer. a single line is used to write
         * everything.
         * @param writer
         * @throws IOException
         *
         * @see {@link #read(String)}
         */
        public void write(FileWriter writer) throws IOException {
            for (int i = 0 ; i < ApkData.INDEX_MAX ; i++) {
                write(i, writer);
            }
        }

        /**
         * reads the apk description from a log line.
         * @param data
         *
         * @see #write(FileWriter)
         */
        public void read(String line) {
            String[] dataStrs = line.split(",");
            for (int i = 0 ; i < ApkData.INDEX_MAX ; i++) {
                read(i, dataStrs);
            }
        }

        private void write(int index, FileWriter writer) throws IOException {
            switch (index) {
                case INDEX_OUTPUTNAME:
                    writeValue(writer, outputName);
                    break;
                case INDEX_PROJECT:
                    writeValue(writer, relativePath);
                    break;
                case INDEX_MINOR:
                    writeValue(writer, minor);
                    break;
                case INDEX_MINSDK:
                    writeValue(writer, minSdkVersion);
                    break;
                case INDEX_ABI:
                    writeValue(writer, abi != null ? abi : "");
                    break;
            }
        }

        private void read(int index, String[] data) {
            switch (index) {
                case INDEX_OUTPUTNAME:
                    outputName = data[index];
                    break;
                case INDEX_PROJECT:
                    relativePath = data[index];
                    break;
                case INDEX_MINOR:
                    minor = Integer.parseInt(data[index]);
                    break;
                case INDEX_MINSDK:
                    minSdkVersion = Integer.parseInt(data[index]);
                    break;
                case INDEX_ABI:
                    if (index < data.length && data[index].length() > 0) {
                        abi = data[index];
                    }
                    break;
            }
        }
}

private static enum Target {
//Synthetic comment -- @@ -178,15 +274,47 @@
System.out.println("versionCode: " + version);

// checks whether the projects can be signed.
        boolean canSign = false;
        String keyStore = null, keyAlias = null;
        if (mTarget == Target.RELEASE) {
            String value = antProject.getProperty("key.store");
            keyStore = value != null && value.length() > 0 ? value : null;
            value = antProject.getProperty("key.alias");
            keyAlias = value != null && value.length() > 0 ? value : null;
            canSign = keyStore != null && keyAlias != null;
        }

        // get the list of apk to export and their configuration.
        ApkData[] apks = getProjects(antProject, appPackage);

        // look to see if there's an export log from a previous export
        File log = getBuildLog(appPackage, versionCode);
        if (mTarget == Target.RELEASE && log.isFile()) {
            // load the log and compare to current export list.
            // Any difference will force a new versionCode.
            ApkData[] previousApks = getProjects(log);

            if (previousApks.length != apks.length) {
                throw new BuildException(String.format(
                        "Project export is setup differently from previous export at versionCode %d.\n" +
                        "Any change in the multi-apk configuration require a increment of the versionCode.",
                        versionCode));
            }

            for (int i = 0 ; i < previousApks.length ; i++) {
                // update the minor value from what is in the log file.
                apks[i].minor = previousApks[i].minor;
                if (apks[i].compareTo(previousApks[i]) != 0) {
                    throw new BuildException(String.format(
                            "Project export is setup differently from previous export at versionCode %d.\n" +
                            "Any change in the multi-apk configuration require a increment of the versionCode.",
                            versionCode));
                }
            }
        }

        // some temp var used by the project loop
HashSet<String> compiledProject = new HashSet<String>();
XPathFactory xPathFactory = XPathFactory.newInstance();

File exportProjectOutput = new File(getValidatedProperty(antProject, "out.absolute.dir"));
//Synthetic comment -- @@ -216,16 +344,16 @@
keyAliasPassword = getValidatedProperty(antProject, "key.alias.password");
}

        for (ApkData apk : apks) {
// this output is prepended by "[android-export] " (17 chars), so we put 61 stars
System.out.println("\n*************************************************************");
            System.out.println("Exporting project: " + apk.relativePath);

SubAnt subAnt = new SubAnt();
subAnt.setTarget(mTarget.getTarget());
subAnt.setProject(antProject);

            File subProjectFolder = new File(antProject.getBaseDir(), apk.relativePath);

FileSet fileSet = new FileSet();
fileSet.setProject(antProject);
//Synthetic comment -- @@ -240,19 +368,19 @@
// this project.
// (projects can be export multiple time if some properties are set up to
// generate more than one APK (for instance ABI split).
                if (compiledProject.contains(apk.relativePath) == false) {
                    compiledProject.add(apk.relativePath);
} else {
addProp(subAnt, "do.not.compile", "true");
}

// set the version code, and filtering
                String compositeVersionCode = getVersionCodeString(versionCode, apk);
addProp(subAnt, "version.code", compositeVersionCode);
System.out.println("Composite versionCode: " + compositeVersionCode);
                if (apk.abi != null) {
                    addProp(subAnt, "filter.abi", apk.abi);
                    System.out.println("ABI Filter: " + apk.abi);
}

// end of the output by this task. Everything that follows will be output
//Synthetic comment -- @@ -279,7 +407,7 @@

// override the resource pack file.
addProp(subAnt, "resource.package.file.name",
                        name + "-" + apk.buildInfo + ".ap_");

if (canSign) {
// set the properties for the password.
//Synthetic comment -- @@ -288,17 +416,18 @@
addProp(subAnt, "key.store.password", keyStorePassword);
addProp(subAnt, "key.alias.password", keyAliasPassword);

// temporary file only get a filename change (still stored in the project
// bin folder).
addProp(subAnt, "out.unsigned.file.name",
                            name + "-" + apk.buildInfo + "-unsigned.apk");
addProp(subAnt, "out.unaligned.file",
                            name + "-" + apk.buildInfo + "-unaligned.apk");

// final file is stored locally.
                    apk.outputName = name + "-" + compositeVersionCode + "-release.apk";
addProp(subAnt, "out.release.file", new File(exportProjectOutput,
                            apk.outputName).getAbsolutePath());

} else {
// put some empty prop. This is to override possible ones defined in the
// project. The reason is that if there's more than one project, we don't
//Synthetic comment -- @@ -307,16 +436,18 @@
addProp(subAnt, "key.store", "");
addProp(subAnt, "key.alias", "");
// final file is the unsigned version. It gets stored locally.
                    apk.outputName = name + "-" + compositeVersionCode + "-unsigned.apk";
addProp(subAnt, "out.unsigned.file", new File(exportProjectOutput,
                            apk.outputName).getAbsolutePath());
}
}

subAnt.execute();
}

        if (mTarget == Target.RELEASE) {
            makeBuildLog(appPackage, versionCode, apks);
        }
}

/**
//Synthetic comment -- @@ -343,11 +474,11 @@
* @param antProject the Ant project.
* @param appPackage the application package. Projects' manifest must match this.
*/
    private ApkData[] getProjects(Project antProject, String appPackage) {
String projects = antProject.getProperty("projects");
String[] paths = projects.split("\\:");

        ArrayList<ApkData> datalist = new ArrayList<ApkData>();

for (String path : paths) {
File projectFolder = new File(path);
//Synthetic comment -- @@ -374,11 +505,11 @@
SdkConstants.FN_ANDROID_MANIFEST_XML));
}

                ArrayList<ApkData> datalist2 = checkManifest(androidManifest, appPackage);

// if the method returns without throwing, this is a good project to
// export.
                for (ApkData data : datalist2) {
data.relativePath = path;
data.project = projectFolder;
}
//Synthetic comment -- @@ -393,14 +524,22 @@
// sort the projects and assign buildInfo
Collections.sort(datalist);
int buildInfo = 0;
        for (ApkData data : datalist) {
data.buildInfo = buildInfo++;
}

        return datalist.toArray(new ApkData[datalist.size()]);
}

    /**
     * Checks a manifest of the project for inclusion in the list of exported APK.
     * If the manifest is correct, a list of apk to export is created and returned.
     * @param androidManifest the manifest to check
     * @param appPackage the package name of the application being exported, as read from
     * export.properties.
     * @return
     */
    private ArrayList<ApkData> checkManifest(FileWrapper androidManifest, String appPackage) {
try {
String manifestPackage = AndroidManifest.getPackage(androidManifest);
if (appPackage.equals(manifestPackage) == false) {
//Synthetic comment -- @@ -421,8 +560,8 @@
"Codename in minSdkVersion is not supported by multi-apk export.");
}

            ArrayList<ApkData> dataList = new ArrayList<ApkData>();
            ApkData data = new ApkData();
dataList.add(data);
data.minSdkVersion = minSdkVersion;

//Synthetic comment -- @@ -441,10 +580,10 @@
if (apkSettings.isSplitByAbi()) {
// need to find the available ABIs.
List<String> abis = findAbis(projectPath);
                    ApkData current = data;
for (String abi : abis) {
if (current == null) {
                            current = new ApkData(data);
dataList.add(current);
}

//Synthetic comment -- @@ -464,6 +603,11 @@
}
}

    /**
     * Finds ABIs in a project folder. This is based on the presence of libs/<abi>/ folder.
     * @param projectPath
     * @return
     */
private List<String> findAbis(String projectPath) {
ArrayList<String> abiList = new ArrayList<String>();
File libs = new File(projectPath, SdkConstants.FD_NATIVE_LIBS);
//Synthetic comment -- @@ -488,6 +632,12 @@
return abiList;
}

    /**
     * Adds a property to a {@link SubAnt} task.
     * @param task the task.
     * @param name the name of the property.
     * @param value the value of the property.
     */
private void addProp(SubAnt task, String name, String value) {
Property prop = new Property();
prop.setName(name);
//Synthetic comment -- @@ -495,12 +645,144 @@
task.addProperty(prop);
}

    /**
     * Computes and returns the composite version code
     * @param versionCode the major version code.
     * @param apkData the apk data.
     * @return the composite versionCode to be used in the manifest.
     */
    private String getVersionCodeString(int versionCode, ApkData apkData) {
        int trueVersionCode = versionCode * OFFSET_VERSION_CODE;
        trueVersionCode += apkData.buildInfo * OFFSET_BUILD_INFO;
        trueVersionCode += apkData.minor;

return Integer.toString(trueVersionCode);
}

    /**
     * Returns the {@link File} for the build log.
     * @param appPackage
     * @param versionCode
     * @return
     */
    private File getBuildLog(String appPackage, int versionCode) {
        return new File(appPackage + "." + versionCode + ".log");
    }

    /**
     * Loads and returns a list of {@link ApkData} from a build log.
     * @param log
     * @return
     */
    private ApkData[] getProjects(File log) {
        ArrayList<ApkData> datalist = new ArrayList<ApkData>();

        FileReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            reader = new FileReader(log);
            bufferedReader = new BufferedReader(reader);
            String line;
            int lineIndex = 0;
            int apkIndex = 0;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }

                switch (lineIndex) {
                    case 0:
                        // read package value
                        lineIndex++;
                        break;
                    case 1:
                        // read versionCode value
                        lineIndex++;
                        break;
                    default:
                        // read apk description
                        ApkData data = new ApkData();
                        data.buildInfo = apkIndex++;
                        datalist.add(data);
                        data.read(line);
                        if (data.minor >= MAX_MINOR) {
                            throw new BuildException(
                                    "Valid minor version code values are 0-" + (MAX_MINOR-1));
                        }
                        break;
                }
            }
        } catch (IOException e) {
            throw new BuildException("Failed to read existing build log", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                throw new BuildException("Failed to read existing build log", e);
            }
        }

        return datalist.toArray(new ApkData[datalist.size()]);
    }

    /**
     * Writes the build log for a given list of {@link ApkData}.
     * @param appPackage
     * @param versionCode
     * @param apks
     */
    private void makeBuildLog(String appPackage, int versionCode, ApkData[] apks) {
        File log = getBuildLog(appPackage, versionCode);
        FileWriter writer = null;
        try {
            writer = new FileWriter(log);

            writer.append("# Multi-APK BUILD log.\n");
            writer.append("# Only edit manually to change minor versions.\n");

            writeValue(writer, "package", appPackage);
            writeValue(writer, "versionCode", versionCode);

            writer.append("# what follow is one line per generated apk with its description.\n");
            writer.append("# the format is CSV in the following order:\n");
            writer.append("# apkname,project,minor, minsdkversion, abi filter,\n");

            for (ApkData apk : apks) {
                apk.write(writer);
                writer.append('\n');
            }

            writer.flush();
        } catch (IOException e) {
            throw new BuildException("Failed to write build log", e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                throw new BuildException("Failed to write build log", e);
            }
        }
    }

    private static void writeValue(FileWriter writer, String value) throws IOException {
        writer.append(value).append(',');
    }

    private static void writeValue(FileWriter writer, int value) throws IOException {
        writeValue(writer, Integer.toString(value));
    }

    private void writeValue(FileWriter writer, String name, String value) throws IOException {
        writer.append(name).append('=').append(value).append('\n');
    }

    private void writeValue(FileWriter writer, String name, int value) throws IOException {
        writeValue(writer, name, Integer.toString(value));
    }

}








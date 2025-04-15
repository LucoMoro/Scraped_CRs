/*Add multi-apk export log.

The log is used to confirm that new export do not conflict
with previous build. The build file can also be used to set
per-apk minor version code.

Change-Id:Ic5ad98758aa327f6a5bc1d00e66cf5437ac098e4*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/MultiApkExportTask.java b/anttasks/src/com/android/ant/MultiApkExportTask.java
//Synthetic comment -- index 2c161d5..3c70ed4 100644

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
    private final static int OFFSET_MINOR = MAX_MINOR;
    private final static int OFFSET_BUILDINFO = OFFSET_MINOR * MAX_BUILDINFO;

/**
* Class representing one apk that needs to be generated. This contains
* which project it must be created from, and which filters should be used.
//Synthetic comment -- @@ -63,6 +71,14 @@
*/
private static class ExportData implements Comparable<ExportData> {

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
//Synthetic comment -- @@ -88,6 +104,17 @@
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

public int compareTo(ExportData o) {
int minSdkDiff = minSdkVersion - o.minSdkVersion;
if (minSdkDiff != 0) {
//Synthetic comment -- @@ -116,6 +143,60 @@

return 0;
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

        private void write(FileWriter writer) throws IOException {
            for (int i = 0 ; i < ExportData.INDEX_MAX ; i++) {
                write(i, writer);
            }
        }

        private void read(String data[]) {
            for (int i = 0 ; i < ExportData.INDEX_MAX ; i++) {
                read(i, data);
            }
        }
}

private static enum Target {
//Synthetic comment -- @@ -178,15 +259,47 @@
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
ExportData[] projects = getProjects(antProject, appPackage);

        // look to see if there's an export log from a previous export
        File log = getBuildLog(appPackage, versionCode);
        if (mTarget == Target.RELEASE && log.isFile()) {
            // load the log and compare to current export list.
            // Any difference will force a new versionCode.
            ExportData[] projects2 = getProjects(log);

            if (projects2.length != projects.length) {
                throw new BuildException(String.format(
                        "Project export is setup differently from previous export at versionCode %d.\n" +
                        "Any change in the multi-apk configuration require a increment of the versionCode.",
                        versionCode));
            }

            for (int i = 0 ; i < projects2.length ; i++) {
                // update the minor value from what is in the log file.
                projects[i].minor = projects2[i].minor;
                if (projects[i].compareTo(projects2[i]) != 0) {
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
//Synthetic comment -- @@ -288,7 +401,6 @@
addProp(subAnt, "key.store.password", keyStorePassword);
addProp(subAnt, "key.alias.password", keyAliasPassword);

// temporary file only get a filename change (still stored in the project
// bin folder).
addProp(subAnt, "out.unsigned.file.name",
//Synthetic comment -- @@ -297,8 +409,10 @@
name + "-" + projectData.buildInfo + "-unaligned.apk");

// final file is stored locally.
                    projectData.outputName = name + "-" + compositeVersionCode + "-release.apk";
addProp(subAnt, "out.release.file", new File(exportProjectOutput,
                            projectData.outputName).getAbsolutePath());

} else {
// put some empty prop. This is to override possible ones defined in the
// project. The reason is that if there's more than one project, we don't
//Synthetic comment -- @@ -307,16 +421,18 @@
addProp(subAnt, "key.store", "");
addProp(subAnt, "key.alias", "");
// final file is the unsigned version. It gets stored locally.
                    projectData.outputName = name + "-" + compositeVersionCode + "-unsigned.apk";
addProp(subAnt, "out.unsigned.file", new File(exportProjectOutput,
                            projectData.outputName).getAbsolutePath());
}
}

subAnt.execute();
}

        if (mTarget == Target.RELEASE) {
            makeBuildLog(appPackage, versionCode, projects);
        }
}

/**
//Synthetic comment -- @@ -496,11 +612,121 @@
}

private String getVersionCodeString(int versionCode, ExportData projectData) {
        int trueVersionCode = versionCode * OFFSET_BUILDINFO;
        trueVersionCode += projectData.buildInfo * OFFSET_MINOR;
trueVersionCode += projectData.minor;

return Integer.toString(trueVersionCode);
}

    private File getBuildLog(String appPackage, int versionCode) {
        return new File(appPackage + "." + versionCode + ".log");
    }

    private ExportData[] getProjects(File log) {
        ArrayList<ExportData> datalist = new ArrayList<ExportData>();

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
                        String[] dataStrs = line.split(",");
                        ExportData data = new ExportData();
                        data.buildInfo = apkIndex++;
                        datalist.add(data);
                        data.read(dataStrs);
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

        return datalist.toArray(new ExportData[datalist.size()]);
    }

    private void makeBuildLog(String appPackage, int versionCode, ExportData[] projects) {
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

            for (ExportData data : projects) {
                data.write(writer);
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








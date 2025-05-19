//<Beginning of snippet n. 0>

import org.apache.tools.ant.types.FileSet;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import java.util.List;

public class MultiApkExportTask extends Task {
    private static class ExportData implements Comparable<ExportData> {
        String relativePath;
        File project;
        int buildInfo;
        int glVersion;
        int minSdkVersion;
        String abi;
        int minor;

        ExportData() {
        }

        public ExportData(ExportData data) {
            relativePath = data.relativePath;
            project = data.project;
            buildInfo = data.buildInfo;
            glVersion = data.glVersion;
            minSdkVersion = data.minSdkVersion;
            abi = data.abi;
            minor = data.minor;
        }

        public int compareTo(ExportData o) {
            int minSdkDiff = minSdkVersion - o.minSdkVersion;
            if (minSdkDiff != 0) {
                return minSdkDiff;
            }
            return 0;
        }
    }

    private static enum Target {
    }

    private PrintWriter logWriter;

    public MultiApkExportTask() {
        try {
            logWriter = new PrintWriter(new FileWriter("apk_export_log.txt", true));
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize log file: " + e.getMessage(), e);
        }
    }

    public void execute() {
        String value = antProject.getProperty("key.store");
        String keyStore = value != null && value.length() > 0 ? value : null;
        value = antProject.getProperty("key.alias");
        String keyAlias = value != null && value.length() > 0 ? value : null;
        boolean canSign = keyStore != null && keyAlias != null;

        ExportData[] projects = getProjects(antProject, appPackage);
        HashSet<String> compiledProject = new HashSet<String>();

        File exportProjectOutput = new File(getValidatedProperty(antProject, "out.absolute.dir"));
        String keyAliasPassword = getValidatedProperty(antProject, "key.alias.password");

        if (!exportProjectOutput.exists() || !exportProjectOutput.isDirectory()) {
            throw new RuntimeException("Output directory is invalid: " + exportProjectOutput);
        }

        for (ExportData projectData : projects) {
            try {
                if (compiledProject.contains(projectData.relativePath)) {
                    logConflict(projectData, "Project already compiled");
                    addProp(subAnt, "do.not.compile", "true");
                } else {
                    compiledProject.add(projectData.relativePath);
                }

                String compositeVersionCode = getVersionCodeString(versionCode, projectData);
                addProp(subAnt, "version.code", compositeVersionCode);
                if (projectData.abi != null) {
                    addProp(subAnt, "filter.abi", projectData.abi);
                }

                addProp(subAnt, "resource.package.file.name", name + "-" + projectData.buildInfo + ".ap_");

                if (canSign) {
                    addProp(subAnt, "key.store.password", keyStorePassword);
                    addProp(subAnt, "key.alias.password", keyAliasPassword);
                    addProp(subAnt, "out.unsigned.file.name", name + "-" + projectData.buildInfo + "-unsigned.apk");
                    addProp(subAnt, "out.unaligned.file", name + "-" + projectData.buildInfo + "-unaligned.apk");
                    addProp(subAnt, "out.release.file", new File(exportProjectOutput, name + "-" + projectData.buildInfo + "-release.apk").getAbsolutePath());
                } else {
                    addProp(subAnt, "key.store", "");
                    addProp(subAnt, "key.alias", "");
                    addProp(subAnt, "out.unsigned.file", new File(exportProjectOutput, name + "-" + projectData.buildInfo + "-unsigned.apk").getAbsolutePath());
                }

                logExportDetails(projectData, exportProjectOutput);
            } catch (Exception e) {
                logWriter.println("Error during APK export: " + e.getMessage());
            }
        }

        subAnt.execute();
        logWriter.close();
    }

    private void logExportDetails(ExportData projectData, File exportProjectOutput) {
        String logMessage = String.format("Exported APK: %s, Version Code: %s, Output Directory: %s",
                projectData.relativePath, getVersionCodeString(versionCode, projectData), exportProjectOutput);
        logWriter.println(logMessage);
    }

    private void logConflict(ExportData projectData, String reason) {
        String conflictMessage = String.format("Conflict for project: %s, Reason: %s", projectData.relativePath, reason);
        logWriter.println(conflictMessage);
    }

    private ExportData[] getProjects(Project antProject, String appPackage) {
        String projects = antProject.getProperty("projects");
        String[] paths = projects.split("\\:");

        ArrayList<ExportData> datalist = new ArrayList<ExportData>();

        for (String path : paths) {
            File projectFolder = new File(path);
            File androidManifest = new File(projectFolder, SdkConstants.FN_ANDROID_MANIFEST_XML);

            ArrayList<ExportData> datalist2 = checkManifest(androidManifest, appPackage);

            for (ExportData data : datalist2) {
                data.relativePath = path;
                data.project = projectFolder;
            }
            datalist.addAll(datalist2);
        }

        Collections.sort(datalist);
        int buildInfo = 0;
        for (ExportData data : datalist) {
            data.buildInfo = buildInfo++;
        }

        return datalist.toArray(new ExportData[datalist.size()]);
    }

    private ArrayList<ExportData> checkManifest(File androidManifest, String appPackage) {
        try {
            String manifestPackage = AndroidManifest.getPackage(androidManifest);
            if (!appPackage.equals(manifestPackage)) {
                throw new IllegalArgumentException("Package in manifest does not match appPackage.");
            }

            ArrayList<ExportData> dataList = new ArrayList<ExportData>();
            ExportData data = new ExportData();
            dataList.add(data);
            data.minSdkVersion = minSdkVersion;

            if (apkSettings.isSplitByAbi()) {
                List<String> abis = findAbis(projectPath);
                ExportData current = data;
                for (String abi : abis) {
                    if (current == null) {
                        current = new ExportData(data);
                        dataList.add(current);
                    }
                }
            }
            return dataList;

        } catch (Exception e) {
            logWriter.println("Error during manifest check: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<String> findAbis(String projectPath) {
        ArrayList<String> abiList = new ArrayList<String>();
        File libs = new File(projectPath, SdkConstants.FD_NATIVE_LIBS);
        return abiList;
    }

    private void addProp(SubAnt task, String name, String value) {
        Property prop = new Property();
        prop.setName(name);
        prop.setValue(value);
        task.addProperty(prop);
    }

    private String getVersionCodeString(int versionCode, ExportData projectData) {
        return Integer.toString(versionCode * 10000 + projectData.buildInfo * 100 + projectData.minor);
    }
}

//<End of snippet n. 0>
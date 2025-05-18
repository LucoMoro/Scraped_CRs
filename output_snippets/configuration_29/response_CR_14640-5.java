//<Beginning of snippet n. 0>


import org.apache.tools.ant.types.FileSet;
import org.xml.sax.InputSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Collections;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.Property;
import javax.xml.xpath.XPathFactory;
import org.json.JSONArray;
import org.json.JSONObject;

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
        // Assume Target is an enum with defined values.
    }

    private Project antProject;
    private String appPackage;
    private String exportPath;
    private String keyStorePassword;
    private String keyAliasPassword;
    private List<String> logEntries = new ArrayList<>();

    public void execute() {
        String keyStore = antProject.getProperty("key.store");
        String keyAlias = antProject.getProperty("key.alias");
        boolean canSign = keyStore != null && keyAlias != null;

        ExportData[] projects = getProjects(antProject, appPackage);
        HashSet<String> compiledProject = new HashSet<String>();
        File exportProjectOutput = new File(getValidatedProperty(antProject, "out.absolute.dir"));

        for (ExportData projectData : projects) {
            logBuildInfo("Exporting project: " + projectData.relativePath);

            String compositeVersionCode = getVersionCodeString(projectData.buildInfo, projectData);
            if (compiledProject.contains(projectData.relativePath + "|" + compositeVersionCode + "|" + projectData.abi)) {
                logBuildError("Conflict detected for project: " + projectData.relativePath + 
                              ", buildInfo: " + projectData.buildInfo + 
                              ", ABI: " + projectData.abi);
                continue; 
            }
            compiledProject.add(projectData.relativePath + "|" + compositeVersionCode + "|" + projectData.abi);

            SubAnt subAnt = new SubAnt();
            subAnt.setProject(antProject);
            addProp(subAnt, "version.code", compositeVersionCode);
            logBuildInfo("Composite versionCode: " + compositeVersionCode);
            if (projectData.abi != null) {
                addProp(subAnt, "filter.abi", projectData.abi);
                logBuildInfo("ABI Filter: " + projectData.abi);
            }

            addProp(subAnt, "resource.package.file.name", "name-" + projectData.buildInfo + ".ap_");
            if (canSign) {
                addProp(subAnt, "key.store.password", keyStorePassword);
                addProp(subAnt, "key.alias.password", keyAliasPassword);
                addProp(subAnt, "out.unsigned.file.name", "name-" + projectData.buildInfo + "-unsigned.apk");
                addProp(subAnt, "out.unaligned.file", "name-" + projectData.buildInfo + "-unaligned.apk");
                addProp(subAnt, "out.release.file", new File(exportProjectOutput, "name-" + projectData.buildInfo + "-release.apk").getAbsolutePath());
            } else {
                addProp(subAnt, "key.store", "");
                addProp(subAnt, "key.alias", "");
                addProp(subAnt, "out.unsigned.file", new File(exportProjectOutput, "name-" + projectData.buildInfo + "-unsigned.apk").getAbsolutePath());
            }

            subAnt.execute();
        }
        exportBuildLog();
    }

    private void logBuildInfo(String message) {
        logEntries.add("INFO: " + message);
        System.out.println("INFO: " + message);
    }

    private void logBuildError(String message) {
        logEntries.add("ERROR: " + message);
        System.err.println("ERROR: " + message);
    }

    private void exportBuildLog() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(exportPath, "build_log.json")))) {
            JSONArray logArray = new JSONArray();
            for (String entry : logEntries) {
                JSONObject logEntry = new JSONObject();
                logEntry.put("message", entry);
                logArray.put(logEntry);
            }
            writer.write(logArray.toString(4)); 
        } catch (IOException e) {
            logBuildError("Failed to export build log: " + e.getMessage());
        }
    }

    private ExportData[] getProjects(Project antProject, String appPackage) {
        String projects = antProject.getProperty("projects");
        String[] paths = projects.split("\\:");

        ArrayList<ExportData> datalist = new ArrayList<ExportData>();
        for (String path : paths) {
            File projectFolder = new File(path);
            File androidManifest = new File(projectFolder, "AndroidManifest.xml");
            ArrayList<ExportData> datalist2 = checkManifest(androidManifest, appPackage);
            for (ExportData data : datalist2) {
                data.relativePath = path;
                data.project = projectFolder;
            }
        }

        Collections.sort(datalist);
        int buildInfo = 0;
        for (ExportData data : datalist) {
            data.buildInfo = buildInfo++;
        }

        return datalist.toArray(new ExportData[datalist.size()]);
    }

    private ArrayList<ExportData> checkManifest(File androidManifest, String appPackage) {
        ArrayList<ExportData> dataList = new ArrayList<ExportData>();
        try {
            String manifestPackage = AndroidManifest.getPackage(androidManifest);
            if (!appPackage.equals(manifestPackage)) {
                throw new IllegalArgumentException("Codename in minSdkVersion is not supported by multi-apk export.");
            }
            ExportData data = new ExportData();
            dataList.add(data);
            data.minSdkVersion = minSdkVersion;
            if (apkSettings.isSplitByAbi()) {
                List<String> abis = findAbis(androidManifest.getParent());
                for (String abi : abis) {
                    ExportData current = new ExportData(data);
                    current.abi = abi;
                    dataList.add(current);
                }
            }
        } catch (Exception e) {
            logBuildError("Manifest check failed: " + e.getMessage());
        }
        return dataList;
    }

    private List<String> findAbis(String projectPath) {
        ArrayList<String> abiList = new ArrayList<String>();
        File libs = new File(projectPath, "libs");
        // Logic to populate abiList goes here
        return abiList;
    }

    private void addProp(SubAnt task, String name, String value) {
        Property prop = new Property();
        prop.setName(name);
        prop.setValue(value);
        task.addProperty(prop);
    }

    private String getVersionCodeString(int versionCode, ExportData projectData) {
        int trueVersionCode = versionCode * 10000 + projectData.buildInfo * 100 + projectData.minor;
        return Integer.toString(trueVersionCode);
    }

    private void validateExportPath(String exportPath) {
        File exportDir = new File(exportPath);
        if (!exportDir.exists() || !exportDir.canWrite()) {
            throw new IllegalArgumentException("Invalid export path: " + exportPath);
        }
    }

//<End of snippet n. 0>
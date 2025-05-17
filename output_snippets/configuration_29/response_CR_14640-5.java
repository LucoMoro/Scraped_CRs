//<Beginning of snippet n. 0>


import org.apache.tools.ant.types.FileSet;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiApkExportTask extends Task {

    private static final Logger LOGGER = Logger.getLogger(MultiApkExportTask.class.getName());

    private static class ExportData implements Comparable<ExportData> {
        String relativePath;
        File project;
        int buildInfo;
        int glVersion;
        int minSdkVersion;
        int minor;

        ExportData() {
            this.minor = 0; // Initialize minor version to prevent null pointer exceptions
        }

        public ExportData(ExportData data) {
            relativePath = data.relativePath;
            project = data.project;
            buildInfo = data.buildInfo;
            glVersion = data.glVersion;
            minSdkVersion = data.minSdkVersion;
            this.minor = data.minor; // Ensure minor is copied
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
        // Enum for target values, if needed later
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

        for (ExportData projectData : projects) {
            LOGGER.log(Level.INFO, "Exporting project: {0}", projectData.relativePath);

            SubAnt subAnt = new SubAnt();
            subAnt.setTarget(mTarget.getTarget());
            subAnt.setProject(antProject);

            File subProjectFolder = new File(antProject.getBaseDir(), projectData.relativePath);

            FileSet fileSet = new FileSet();
            fileSet.setProject(antProject);

            if (compiledProject.contains(projectData.relativePath) == false) {
                compiledProject.add(projectData.relativePath);
            } else {
                addProp(subAnt, "do.not.compile", "true");
                LOGGER.log(Level.WARNING, "Conflict detected for project: {0} - Already compiled.", projectData.relativePath);
            }

            String compositeVersionCode = getVersionCodeString(versionCode, projectData);
            addProp(subAnt, "version.code", compositeVersionCode);
            LOGGER.log(Level.INFO, "Composite versionCode: {0}", compositeVersionCode);
            if (projectData.abi != null) {
                addProp(subAnt, "filter.abi", projectData.abi);
                LOGGER.log(Level.INFO, "ABI Filter: {0}", projectData.abi);
            }

            String unsignedFileName = name + "-" + projectData.buildInfo + "-unsigned.apk";
            String releaseFileName = name + "-" + projectData.buildInfo + "-release.apk";

            String unsignedFilePath = new File(exportProjectOutput, unsignedFileName).getAbsolutePath();
            addProp(subAnt, "out.unsigned.file", unsignedFilePath);

            if (new File(unsignedFilePath).exists()) {
                LOGGER.log(Level.SEVERE, "Output file already exists: {0}", unsignedFilePath);
                continue; // Skip this project export as the file would be overwritten
            }

            if (canSign) {
                addProp(subAnt, "key.store.password", keyStorePassword);
                addProp(subAnt, "key.alias.password", keyAliasPassword);
                addProp(subAnt, "out.release.file", new File(exportProjectOutput, releaseFileName).getAbsolutePath());
            } else {
                addProp(subAnt, "key.store", "");
                addProp(subAnt, "key.alias", "");
            }
        }

        subAnt.execute();
        LOGGER.log(Level.INFO, "Export process completed.");
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
                LOGGER.log(Level.SEVERE, "Codename in minSdkVersion is not supported by multi-apk export.");
                return dataList; // return empty if there's an error
            }

            ExportData data = new ExportData();
            dataList.add(data);
            data.minSdkVersion = minSdkVersion;

            if (apkSettings.isSplitByAbi()) {
                List<String> abis = findAbis(projectPath);
                for (String abi : abis) {
                    ExportData current = new ExportData(data);
                    dataList.add(current);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking manifest: {0}", e.getMessage());
        }
        return dataList;
    }

    private List<String> findAbis(String projectPath) {
        ArrayList<String> abiList = new ArrayList<String>();
        File libs = new File(projectPath, SdkConstants.FD_NATIVE_LIBS);
        // logic to populate abiList
        return abiList;
    }

    private void addProp(SubAnt task, String name, String value) {
        Property prop = new Property();
        prop.setName(name);
        prop.setValue(value);
        task.addProperty(prop);
    }

    private String getVersionCodeString(int versionCode, ExportData projectData) {
        int trueVersionCode = versionCode * 10000;
        trueVersionCode += projectData.buildInfo * 100;
        trueVersionCode += projectData.minor;
        return Integer.toString(trueVersionCode);
    }
}

//<End of snippet n. 0>
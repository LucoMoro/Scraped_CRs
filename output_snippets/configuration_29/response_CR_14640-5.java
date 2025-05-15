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
    private static final Logger logger = Logger.getLogger(MultiApkExportTask.class.getName());

    private static class ExportData implements Comparable<ExportData> {
        String relativePath;
        File project;
        int buildInfo;
        int glVersion;
        int minor; // Added minor version code
        int minSdkVersion; // Added to handle minSdkVersion

        ExportData() {
            // do nothing.
        }

        public ExportData(ExportData data) {
            relativePath = data.relativePath;
            project = data.project;
            buildInfo = data.buildInfo;
            glVersion = data.glVersion;
            minor = data.minor; // Copy minor version code
            minSdkVersion = data.minSdkVersion; // Copy minSdkVersion
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
        // Enum definition here
    }

    public void execute() {
        // Existing code...

        for (ExportData projectData : projects) {
            System.out.println("\n*************************************************************");
            System.out.println("Exporting project: " + projectData.relativePath);

            SubAnt subAnt = new SubAnt();
            subAnt.setTarget(mTarget.getTarget());
            subAnt.setProject(antProject);

            File subProjectFolder = new File(antProject.getBaseDir(), projectData.relativePath);

            FileSet fileSet = new FileSet();
            fileSet.setProject(antProject);
            if (!compiledProject.contains(projectData.relativePath)) {
                compiledProject.add(projectData.relativePath);
            } else {
                addProp(subAnt, "do.not.compile", "true");
            }

            String compositeVersionCode = getVersionCodeString(versionCode, projectData);
            addProp(subAnt, "version.code", compositeVersionCode);
            System.out.println("Composite versionCode: " + compositeVersionCode);

            if (projectData.abi != null) {
                addProp(subAnt, "filter.abi", projectData.abi);
                System.out.println("ABI Filter: " + projectData.abi);
            }

            addProp(subAnt, "resource.package.file.name", name + "-" + projectData.buildInfo + ".ap_");

            if (canSign) {
                addProp(subAnt, "key.store.password", keyStorePassword);
                addProp(subAnt, "key.alias.password", keyAliasPassword);
                addProp(subAnt, "out.unsigned.file.name",
                        name + "-" + projectData.buildInfo + "-unsigned.apk");
                addProp(subAnt, "out.unaligned.file",
                        name + "-" + projectData.buildInfo + "-unaligned.apk");
                addProp(subAnt, "out.release.file", new File(exportProjectOutput,
                        name + "-" + projectData.buildInfo + "-release.apk").getAbsolutePath());
            } else {
                addProp(subAnt, "key.store", "");
                addProp(subAnt, "key.alias", "");
                addProp(subAnt, "out.unsigned.file", new File(exportProjectOutput,
                        name + "-" + projectData.buildInfo + "-unsigned.apk").getAbsolutePath());
            }

            // Log export success or failure
            try {
                subAnt.execute();
                logger.info("Export successful for project: " + projectData.relativePath);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Export failed for project: " + projectData.relativePath, e);
            }
        }

        // TODO: Implement additional export log
    }

    private ExportData[] getProjects(Project antProject, String appPackage) {
        String projects = antProject.getProperty("projects");
        String[] paths = projects.split("\\:");

        ArrayList<ExportData> datalist = new ArrayList<ExportData>();

        for (String path : paths) {
            File projectFolder = new File(path);
            FileWrapper androidManifest = new File(projectFolder, SdkConstants.FN_ANDROID_MANIFEST_XML);
            ArrayList<ExportData> datalist2 = checkManifest(androidManifest, appPackage);

            for (ExportData data : datalist2) {
                data.relativePath = path;
                data.project = projectFolder;
            }
            datalist.addAll(datalist2); // Collect the valid projects
        }

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
            if (!appPackage.equals(manifestPackage)) {
                logger.warning("Codename in minSdkVersion is not supported by multi-apk export.");
                return new ArrayList<>();

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
            logger.log(Level.SEVERE, "Error checking manifest", e);
            return new ArrayList<>();
        }
    }

    private List<String> findAbis(String projectPath) {
        ArrayList<String> abiList = new ArrayList<String>();
        File libs = new File(projectPath, SdkConstants.FD_NATIVE_LIBS);
        // Detection logic for ABIs can be added here
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
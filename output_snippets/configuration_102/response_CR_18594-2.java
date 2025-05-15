//<Beginning of snippet n. 0>

String TARGET_JAR_ROOT_PATH = "/data/local/tmp/";
String cp = String.format("%s:%s", targetCoreJarPath, mainJar);
for (String depFqcn : dependentTestClassNames) {
    int lastDotPos = depFqcn.lastIndexOf('.');
    String targetName = TARGET_JAR_ROOT_PATH + depFqcn.substring(lastDotPos + 1) + ".jar";
    String sourceName = depFqcn.replaceAll("\\.", "/") + ".jar";
    
    // Check if sourceName exists before pushing
    if (new File(sourceName).exists()) {
        curJunitFileData += "    " + getADBPushJavaLine(sourceName, targetName);
    }
}

//<End of snippet n. 0>
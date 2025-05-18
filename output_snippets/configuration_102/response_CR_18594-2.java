//<Beginning of snippet n. 0>
String cp = String.format("%s:%s", targetCoreJarPath, mainJar);
for (String depFqcn : dependentTestClassNames) {
    int lastDotPos = depFqcn.lastIndexOf('.');
    String jarFileName = depFqcn.substring(lastDotPos + 1) + ".jar";
    
    if (!TARGET_JAR_ROOT_PATH.endsWith("/")) {
        TARGET_JAR_ROOT_PATH += "/";
    }
    
    String targetName = TARGET_JAR_ROOT_PATH + jarFileName;
    String sourceName = depFqcn.replaceAll("\\.", "/") + ".jar";
    
    if (!sourceName.startsWith("/") || !targetName.startsWith("/")) {
        throw new IllegalArgumentException("Invalid path format for sourceName or targetName");
    }

    System.out.println("adb push " + sourceName + " " + targetName);
    
    curJunitFileData += "    " + getADBPushJavaLine(sourceName, targetName);
}
//<End of snippet n. 0>
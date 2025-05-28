//<Beginning of snippet n. 0>
String cp = String.format("%s:%s", targetCoreJarPath, mainJar);
for (String depFqcn : dependentTestClassNames) {
    int lastDotPos = depFqcn.lastIndexOf('.');
    String targetName = TARGET_JAR_ROOT_PATH + depFqcn.substring(lastDotPos + 1) + ".jar";
    String sourceName = depFqcn.replaceAll("\\.", "/") + ".jar";
    
    if (!sourceName.startsWith("/") || !targetName.startsWith("/") || !TARGET_JAR_ROOT_PATH.endsWith("/")) {
        throw new IllegalArgumentException("Invalid path format for sourceName or targetName");
    }

    System.out.println("adb push " + sourceName + " " + targetName);
    
    curJunitFileData += "    " + getADBPushJavaLine(sourceName, targetName);
}
//<End of snippet n. 0>
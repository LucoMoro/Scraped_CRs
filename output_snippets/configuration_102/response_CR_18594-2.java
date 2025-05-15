//<Beginning of snippet n. 0>

String cp = String.format("%s:%s", targetCoreJarPath, mainJar);
for (String depFqcn : dependentTestClassNames) {
    int lastDotPos = depFqcn.lastIndexOf('.');
    String targetName = "/data/local/tmp/" + depFqcn.substring(lastDotPos + 1) + ".jar";
    String sourceName = "/data/local/tmp/" + depFqcn.replaceAll("\\.", "/") + ".jar";
    //System.out.println("adb push " + sourceName + " " + targetName);
    
    // Error handling for adb push operation
    String adbPushCommand = getADBPushJavaLine(sourceName, targetName);
    boolean success = executeADBCommand(adbPushCommand);
    if (!success) {
        System.err.println("Failed to push " + sourceName + " to " + targetName);
    }
    
    curJunitFileData += "    " + adbPushCommand;
}

//<End of snippet n. 0>
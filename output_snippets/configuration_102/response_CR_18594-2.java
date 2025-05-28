//<Beginning of snippet n. 0>

String cp = String.format("%s:%s", targetCoreJarPath, mainJar);
for (String depFqcn : dependentTestClassNames) {
    if (depFqcn == null || !depFqcn.matches("^[a-zA-Z_$][\\w$]*$")) {
        continue;
    }
    int lastDotPos = depFqcn.lastIndexOf('.');
    String targetName = "/data/local/tmp/" + depFqcn.substring(lastDotPos + 1) + ".jar";
    String sourceName = "/data/local/tmp/" + depFqcn.replaceAll("\\.", "/") + ".jar";
    
    String adbPushCommand = getADBPushJavaLine(sourceName, targetName);
    curJunitFileData += "    " + adbPushCommand;
    System.out.println("Executing adb push command: " + adbPushCommand);
}

//<End of snippet n. 0>
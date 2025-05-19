//<Beginning of snippet n. 0>

String cp = String.format("%s:%s", targetCoreJarPath, mainJar);
for (String depFqcn : dependentTestClassNames) {
    if (depFqcn == null || !depFqcn.matches("^[a-zA-Z_$][\\w$]*$")) {
        continue;
    }
    int lastDotPos = depFqcn.lastIndexOf('.');
    String filename = depFqcn.substring(lastDotPos + 1) + ".jar";
    if (!filename.matches("^[\\w$]+\\.jar$")) {
        continue;
    }
    String targetName = String.format("/data/local/tmp/%s", filename);
    String sourceName = String.format("/data/local/tmp/%s.jar", depFqcn.replaceAll("\\.", "/"));
    
    String adbPushCommand = getADBPushJavaLine(sourceName, targetName);
    curJunitFileData += "    " + adbPushCommand;
    System.out.println(String.format("Executing adb push command: %s", adbPushCommand));
}

//<End of snippet n. 0>
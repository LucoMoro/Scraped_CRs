//<Beginning of snippet n. 0>

String targetName = "/data/local/tmp/" + depFqcn.substring(depFqcn.lastIndexOf('.') + 1) + ".jar";
String sourceName = "dot/junit/opcodes/add_double_2addr/" + depFqcn.replaceAll("\\.", "/") + ".jar";

if (isValidPath(sourceName) && isValidPath(targetName)) {
    curJunitFileData += "    " + getADBPushJavaLine(sourceName, targetName);
}

//<End of snippet n. 0>
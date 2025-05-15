
//<Beginning of snippet n. 0>



// for each dependency:
// adb push dot/junit/opcodes/add_double_2addr/Main_testN2.jar
        // /data/local/tmp/Main_testN2.jar
String cp = String.format("%s:%s", targetCoreJarPath, mainJar);
for (String depFqcn : dependentTestClassNames) {
int lastDotPos = depFqcn.lastIndexOf('.');
            String targetName= String.format("%s/%s.jar", TARGET_JAR_ROOT_PATH,
                    depFqcn.substring(lastDotPos +1));
String sourceName = depFqcn.replaceAll("\\.", "/")+".jar";
//System.out.println("adb push "+sourceName+" "+targetName);
curJunitFileData+= "    "+getADBPushJavaLine(sourceName, targetName);

//<End of snippet n. 0>









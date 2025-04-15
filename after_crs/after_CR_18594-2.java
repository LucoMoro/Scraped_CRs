/*Fix dependent vm-test jar path.

Push file to /data/local/tmp/*.jar, not /data/local/tmp*.jar

Bug 3130080

Change-Id:I0ea4ab648eea4eb9bc8b35dfdab2986a823a31f4*/




//Synthetic comment -- diff --git a/tools/vm-tests/src/util/build/BuildDalvikSuite.java b/tools/vm-tests/src/util/build/BuildDalvikSuite.java
//Synthetic comment -- index 48c8087..a3ff7e3 100644

//Synthetic comment -- @@ -290,11 +290,12 @@

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








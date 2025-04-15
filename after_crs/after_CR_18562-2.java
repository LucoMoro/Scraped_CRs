/*Make vm-tests push jars to a writable directory.

With this change, jars are pushed to /data/local/tmp, as opposed to /data/.

Bug 3130080

Change-Id:Ic268adb0f8e19c31dc28a20029216da250459da3*/




//Synthetic comment -- diff --git a/tools/vm-tests/src/util/build/BuildDalvikSuite.java b/tools/vm-tests/src/util/build/BuildDalvikSuite.java
//Synthetic comment -- index f28a71b..48c8087 100644

//Synthetic comment -- @@ -76,6 +76,7 @@
private static String restrictTo = null; // e.g. restrict to
// "opcodes.add_double"

    private static final String TARGET_JAR_ROOT_PATH = "/data/local/tmp";

private int testClassCnt = 0;
private int testMethodsCnt = 0;
//Synthetic comment -- @@ -275,13 +276,14 @@

private void addCTSHostMethod(String pName, String method, MethodData md,
Set<String> dependentTestClassNames) {
    	final String targetCoreJarPath = String.format("%s/dexcore.jar", TARGET_JAR_ROOT_PATH);
	curJunitFileData+="public void "+method+ "() throws Exception {\n";
curJunitFileData+= "    "+getADBPushJavaLine("dot/junit/dexcore.jar",
        		targetCoreJarPath);

// push class with Main jar.
String mjar = "Main_"+method+".jar";
        String mainJar = String.format("%s/%s", TARGET_JAR_ROOT_PATH, mjar);
String pPath = pName.replaceAll("\\.","/");
//System.out.println("adb push "+pPath+"/"+mjar +" "+mainJar);
curJunitFileData+= "    "+getADBPushJavaLine(pPath+"/"+mjar, mainJar);
//Synthetic comment -- @@ -289,10 +291,10 @@
// for each dependency:
// adb push dot/junit/opcodes/add_double_2addr/Main_testN2.jar
// /data/Main_testN2.jar
        String cp = String.format("%s:%s", targetCoreJarPath, mainJar);
for (String depFqcn : dependentTestClassNames) {
int lastDotPos = depFqcn.lastIndexOf('.');
            String targetName= TARGET_JAR_ROOT_PATH+depFqcn.substring(lastDotPos +1)+".jar";
String sourceName = depFqcn.replaceAll("\\.", "/")+".jar";
//System.out.println("adb push "+sourceName+" "+targetName);
curJunitFileData+= "    "+getADBPushJavaLine(sourceName, targetName);
//Synthetic comment -- @@ -307,10 +309,6 @@
curJunitFileData+= "}\n\n"; 
}    

private void handleTests() throws IOException {
System.out.println("collected "+testMethodsCnt+" test methods in " + 
testClassCnt+" junit test classes");








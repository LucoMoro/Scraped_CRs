/*Temporary Fix for CTS Build

The build does not set the $PATH, so dexdeps could not be found.
Hard code the path for now, but I will fix the tool to properly
use a flag to specify the path to dexdeps.

Change-Id:I058d5863ff994874e77d922dbb18320d2d27eda2*/




//Synthetic comment -- diff --git a/tools/cts-api-coverage/src/com/android/cts/apicoverage/CtsApiCoverage.java b/tools/cts-api-coverage/src/com/android/cts/apicoverage/CtsApiCoverage.java
//Synthetic comment -- index 112ba39..02c4371 100644

//Synthetic comment -- @@ -149,7 +149,9 @@
DexDepsXmlHandler dexDepsXmlHandler = new DexDepsXmlHandler(apiCoverage);
xmlReader.setContentHandler(dexDepsXmlHandler);

        // TODO: Take an argument to specify the location of dexdeps.
        Process process = new ProcessBuilder("out/host/linux-x86/bin/dexdeps",
                "--format=xml", testApk.getPath()).start();
xmlReader.parse(new InputSource(process.getInputStream()));
}









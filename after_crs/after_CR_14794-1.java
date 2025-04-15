/*Replaced if/elseif Construct with switch statement. Cached args.length variable, so it is not evaluated every time

Change-Id:Ia189769eb17f4a5196183396d6584d7ceb3174dd*/




//Synthetic comment -- diff --git a/apkbuilder/src/com/android/apkbuilder/internal/ApkBuilderImpl.java b/apkbuilder/src/com/android/apkbuilder/internal/ApkBuilderImpl.java
//Synthetic comment -- index 2bc7625..f0cd0de 100644

//Synthetic comment -- @@ -86,7 +86,8 @@

public void run(String[] args) throws WrongOptionException, FileNotFoundException,
ApkCreationException {
        int length = args.length;
        if (length < 1) {
throw new WrongOptionException("No options specified");
}

//Synthetic comment -- @@ -103,64 +104,74 @@
do {
String argument = args[index++];

            switch (argument) {
                case "-v":
                    mVerbose = true;
                    break;
                case "-d":
                    mDebugMode = true;
                    break;
                case "-u":
                    mSignedPackage = false;
                    break;
                case "-z":
                    // quick check on the next argument.
                    if (index == length)  {
                        throw new WrongOptionException("Missing value for -z");
                    }

                    try {
                        FileInputStream input = new FileInputStream(args[index++]);
                        zipArchives.add(input);
                    } catch (FileNotFoundException e) {
                        throw new ApkCreationException("-z file is not found");
                    }
                    break;
                case "-f":
                    // quick check on the next argument.
                    if (index == length) {
                        throw new WrongOptionException("Missing value for -f");
                    }

                    archiveFiles.add(getInputFile(args[index++]));
                    break;
                case "-rf":
                    // quick check on the next argument.
                    if (index == length) {
                        throw new WrongOptionException("Missing value for -rf");
                    }

                    processSourceFolderForResource(new File(args[index++]), javaResources);
                    break;
                case "-rj":
                    // quick check on the next argument.
                    if (index == length) {
                        throw new WrongOptionException("Missing value for -rj");
                    }

                    processJar(new File(args[index++]), resourcesJars);
                    break;
                case "-nf":
                    // quick check on the next argument.
                    if (index == length) {
                        throw new WrongOptionException("Missing value for -nf");
                    }

                    processNativeFolder(new File(args[index++]), mDebugMode, nativeLibraries,
                            mVerbose, null /*abiFilter*/);
                    break;
                case "-storetype":
                    // quick check on the next argument.
                    if (index == length) {
                        throw new WrongOptionException("Missing value for -storetype");
                    }

                    mStoreType  = args[index++];
                    break;
                default:
                    throw new WrongOptionException("Unknown argument: " + argument);
}
        } while (index < length);

createPackage(outFile, zipArchives, archiveFiles, javaResources, resourcesJars,
nativeLibraries);








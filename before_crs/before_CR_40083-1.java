/*Update error message shown when ddms does not find adb.

Change-Id:I7e949a943694a98faab5aa475808f4f071e1ade4*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AndroidDebugBridge.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index 15eb0dc..5407d7f 100644

//Synthetic comment -- @@ -560,68 +560,79 @@
return;
}

try {
            String[] command = new String[2];
            command[0] = mAdbOsLocation;
            command[1] = "version"; //$NON-NLS-1$
            Log.d(DDMS, String.format("Checking '%1$s version'", mAdbOsLocation)); //$NON-NLS-1$
            Process process = Runtime.getRuntime().exec(command);

            ArrayList<String> errorOutput = new ArrayList<String>();
            ArrayList<String> stdOutput = new ArrayList<String>();
            int status = grabProcessOutput(process, errorOutput, stdOutput,
                    true /* waitForReaders */);

            if (status != 0) {
                StringBuilder builder = new StringBuilder("'adb version' failed!"); //$NON-NLS-1$
                for (String error : errorOutput) {
                    builder.append('\n');
                    builder.append(error);
                }
                Log.logAndDisplay(LogLevel.ERROR, ADB, builder.toString());
}

            // check both stdout and stderr
            boolean versionFound = false;
            for (String line : stdOutput) {
versionFound = scanVersionLine(line);
if (versionFound) {
break;
}
}
            if (!versionFound) {
                for (String line : errorOutput) {
                    versionFound = scanVersionLine(line);
                    if (versionFound) {
                        break;
                    }
                }
            }

            if (!versionFound) {
                // if we get here, we failed to parse the output.
                StringBuilder builder = new StringBuilder(
                        "Failed to parse the output of 'adb version':\n"); //$NON-NLS-1$
                builder.append("Standard Output was:\n"); //$NON-NLS-1$
                for (String line : stdOutput) {
                    builder.append(line);
                    builder.append('\n');
                }
                builder.append("\nError Output was:\n"); //$NON-NLS-1$
                for (String line : errorOutput) {
                    builder.append(line);
                    builder.append('\n');
                }
                Log.logAndDisplay(LogLevel.ERROR, ADB, builder.toString());
}
        } catch (IOException e) {
            Log.logAndDisplay(LogLevel.ERROR, ADB,
                    "Failed to get the adb version: " + e.getMessage()   //$NON-NLS-1$
                    + " from '" + mAdbOsLocation + "' - exists="         //$NON-NLS-1$
                            + (new File(mAdbOsLocation).exists()));
        } catch (InterruptedException e) {
        } finally {

}
}









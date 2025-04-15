/*Close the dependency file after reading it.

The dependency file will be eventually closed by the finalize() method
at some point, but it is not known when.  In the meantime, the Ant
recipe continues execution and may try to move or delete this file.
At that point the build may fail because the file is still open.

The solution is to explicitly close the BufferedReader opened by
DependencyGraph.readFile(), so the underlying file is closed as soon
as reading is finished.

Change-Id:If25f0d430191f4265a73a0e6adc3d81764c63758Signed-off-by: Kaloian Doganov <doganov@projectoria.bg>*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/DependencyGraph.java b/anttasks/src/com/android/ant/DependencyGraph.java
//Synthetic comment -- index 8671359..ef059b9 100644

//Synthetic comment -- @@ -436,13 +436,17 @@
if (fStream != null) {
BufferedReader reader = new BufferedReader(new InputStreamReader(fStream));

                String line;
                StringBuilder total = new StringBuilder(reader.readLine());
                while ((line = reader.readLine()) != null) {
                    total.append('\n');
                    total.append(line);
}
                return total.toString();
}
} catch (IOException e) {
// we'll just return null








/*Make the run method of lint.Main public.

Subclasses may want to accept constructor arguments, override some of the
getters to return them, and then run lint.  But since run is private, even
subclasses are unable to invoke a customized lint.

Change-Id:I0a5db348e206ed3d9402a1b3b60bef3fa201aa15Signed-off-by: Jonathan Rockway <jrockway@google.com>*/




//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/Main.java b/lint/cli/src/main/java/com/android/tools/lint/Main.java
//Synthetic comment -- index 085a38c..673e4d8 100644

//Synthetic comment -- @@ -152,7 +152,7 @@
*
* @param args program arguments
*/
    public void run(String[] args) {
if (args.length < 1) {
printUsage(System.err);
System.exit(ERRNO_USAGE);








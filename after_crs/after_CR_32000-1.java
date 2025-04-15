/*Close files after reading them

avoids running out of file descriptors

Signed-off-by: Chris Dearman <chris@mips.com>
Change-Id:Id46c190474a0d440325a15f1182e578ce888b1ec*/




//Synthetic comment -- diff --git a/tools/vm-tests-tf/src/util/build/BuildDalvikSuite.java b/tools/vm-tests-tf/src/util/build/BuildDalvikSuite.java
//Synthetic comment -- index dae417f..bd688fe 100644

//Synthetic comment -- @@ -650,6 +650,7 @@
FileReader reader = new FileReader(file);
char[] charContents = new char[(int) file.length()];
reader.read(charContents);
                reader.close();
String contents = new String(charContents);
if (contents.equals(content)) {
// System.out.println("skipping identical: "








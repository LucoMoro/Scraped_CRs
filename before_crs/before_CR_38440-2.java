/*Fix Ant task for 1.8.3 which seems more strict than 1.8.2

Change-Id:Id88bc5d12d007c4c94ca11b7eddfc911fbd7bddb*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/GetLibraryPathTask.java b/anttasks/src/com/android/ant/GetLibraryPathTask.java
//Synthetic comment -- index a79f6e3..813574e 100644

//Synthetic comment -- @@ -83,7 +83,7 @@
element.setPath(sb.toString());
}

        public Path getPath() {
return mPath;
}
}
//Synthetic comment -- @@ -175,6 +175,8 @@

System.out.println("Library dependencies:");

if (helper.getLibraryCount() > 0) {
System.out.println("\n------------------\nOrdered libraries:");

//Synthetic comment -- @@ -182,27 +184,25 @@

if (mLibraryFolderPathOut != null) {
if (mLeaf == null) {
                    // Create a Path object of all the libraries in reverse order.
// This is important so that compilation of libraries happens
// in the reverse order.
                    Path rootPath = new Path(antProject);

List<File> libraries = helper.getLibraries();

for (int i = libraries.size() - 1 ; i >= 0; i--) {
File library = libraries.get(i);
                        PathElement element = rootPath.createPathElement();
element.setPath(library.getAbsolutePath());
}

                    antProject.addReference(mLibraryFolderPathOut, rootPath);
} else {
                    antProject.addReference(mLibraryFolderPathOut,
                            ((LeafProcessor) processor).getPath());
}
}
} else {
System.out.println("No Libraries");
}
}
}








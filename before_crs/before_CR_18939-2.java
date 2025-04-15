/*Close files to prevent resource leakage

These two files open files to parse them and do not explicitly close
them.  The result is that on machines with large amounts of memory
the heap just continues to grow without GC kicking in to close the
files.  This change explicitly closes the files such that the resource
leakage doesn't happen.

Change-Id:Ie2328816ec06ce090d0fa26aad00064b74f93e10*/
//Synthetic comment -- diff --git a/tools/dx-tests/src/util/CollectAllTests.java b/tools/dx-tests/src/util/CollectAllTests.java
//Synthetic comment -- index fa09662..028ec27 100644

//Synthetic comment -- @@ -417,6 +417,9 @@
}
}
}
} catch (Exception e) {
throw new RuntimeException("failed to parse", e);
}
//Synthetic comment -- @@ -474,6 +477,12 @@
md.methodBody = builder.toString();
md.constraint = constraint;
md.title = title;
return md;
}









//Synthetic comment -- diff --git a/tools/vm-tests/src/util/build/BuildDalvikSuite.java b/tools/vm-tests/src/util/build/BuildDalvikSuite.java
//Synthetic comment -- index ff89022a..eda6382 100644

//Synthetic comment -- @@ -681,7 +681,6 @@
+ ".java";
File f = new File(absPath);


Scanner scanner;
try {
scanner = new Scanner(f);
//Synthetic comment -- @@ -732,6 +731,9 @@
}
}
}
} catch (Exception e) {
throw new RuntimeException("failed to parse", e);
}
//Synthetic comment -- @@ -788,6 +790,12 @@
md.methodBody = builder.toString();
md.constraint = constraint;
md.title = title;
return md;
}









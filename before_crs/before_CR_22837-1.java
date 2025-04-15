/*Escape Strings extracted with the Extract String refactoring

Escape any single or double quotes inside the string
value definition in strings.xml:
  This'll work   =>  "This'll work"
  Escape '"      =>  Escape \'\"

Change-Id:I00df6491560c0ab3ca95f485ca508f9717be19f4*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java
//Synthetic comment -- index 955a0b2..dce8160 100644

//Synthetic comment -- @@ -1202,6 +1202,9 @@

IStructuredModel smodel = null;

try {
IStructuredDocument sdoc = null;
boolean checkTopElement = true;
//Synthetic comment -- @@ -1432,6 +1435,34 @@
}

/**
* Computes the changes to be made to the source Android XML file and
* returns a list of {@link Change}.
* <p/>








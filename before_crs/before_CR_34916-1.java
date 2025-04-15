/*Fix issue 28330: Gracefully handle corrupt .class files

Change-Id:Icf9c3cdd105f1edb653260e0a76419bcfc9ef83d*/
//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index ad736c6..e221faa 100644

//Synthetic comment -- @@ -950,9 +950,17 @@
if (classDetectors != null && classDetectors.size() > 0 && entries.size() > 0) {
mOuterClasses = new ArrayDeque<ClassNode>();
for (ClassEntry entry : entries) {
                    ClassReader reader = new ClassReader(entry.bytes);
                    ClassNode classNode = new ClassNode();
                    reader.accept(classNode, 0 /* flags */);

ClassNode peek;
while ((peek = mOuterClasses.peek()) != null) {
//Synthetic comment -- @@ -1019,9 +1027,13 @@

private void addSuperClasses(SuperclassVisitor visitor, List<ClassEntry> entries) {
for (ClassEntry entry : entries) {
            ClassReader reader = new ClassReader(entry.bytes);
            int flags = ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
            reader.accept(visitor, flags);
}
}

//Synthetic comment -- @@ -1764,6 +1776,14 @@
this.bytes = bytes;
}

@Override
public int compareTo(ClassEntry other) {
String p1 = file.getPath();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index 6a6c1cc..8a611a1 100644

//Synthetic comment -- @@ -345,7 +345,7 @@
if (exception != null) {
sb.append(exception.toString());
}
            fail(sb.toString());
}

@Override
//Synthetic comment -- @@ -387,7 +387,6 @@

return super.findResource(relativePath);
}

}

public class TestConfiguration extends Configuration {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/FieldGetterDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/FieldGetterDetectorTest.java
//Synthetic comment -- index ccbf26b..95d8b09 100644

//Synthetic comment -- @@ -71,4 +71,14 @@
"bytecode/GetterTest.jar.data=>bin/classes.jar"
));
}
}








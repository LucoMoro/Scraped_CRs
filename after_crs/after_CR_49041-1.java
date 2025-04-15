/*Bug fix: Use JVM signature for custom views

Without this the lint check for instantiatable classes
might incorrectly conclude that a custom view should
have a default constructor.

Change-Id:Iff9abf8e25619c170238c982d710fe671a78831a*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MissingClassDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MissingClassDetector.java
//Synthetic comment -- index d3dbe33..8002c40 100644

//Synthetic comment -- @@ -249,7 +249,7 @@
handle = context.parser.createLocationHandle(context, element);
mReferencedClasses.put(signature, handle);
if (folderType == ResourceFolderType.LAYOUT && !tag.equals(VIEW_FRAGMENT)) {
                mCustomViews.add(ClassContext.getInternalName(className));
}
}

//Synthetic comment -- @@ -393,7 +393,8 @@
}
}

            if (!hasDefaultConstructor && !isCustomView && !context.isFromClassLibrary()
                    && context.getProject().getReportIssues()) {
context.report(INSTANTIATABLE, context.getLocation(classNode), String.format(
"This class should provide a default constructor (a public " +
"constructor with no arguments) (%1$s)",








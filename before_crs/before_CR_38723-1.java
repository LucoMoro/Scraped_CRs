/*Fix lint attribute positions in the CLI

Change-Id:I795a93b5d4b3041ba9892d5d3eb226de1873a88c*/
//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/LintCliXmlParser.java b/lint/cli/src/com/android/tools/lint/LintCliXmlParser.java
//Synthetic comment -- index 24149b3..8611019 100644

//Synthetic comment -- @@ -72,7 +72,7 @@

@Override
public @NonNull Location getLocation(@NonNull XmlContext context, @NonNull Node node) {
        OffsetPosition pos = (OffsetPosition) getPosition(node, 0, 0);
if (pos != null) {
return Location.create(context.file, pos, (OffsetPosition) pos.getEnd());
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index cce3238..926d59f 100644

//Synthetic comment -- @@ -47,7 +47,7 @@

/**
* Manifest constant for declaring an issue provider. Example:
     * Lint-Issues: foo.bar.CustomIssueRegistry
*/
private static final String MF_LINT_REGISTRY = "Lint-Registry"; //$NON-NLS-1$









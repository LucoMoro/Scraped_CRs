/*Minor tweaks and typos

Change-Id:I46630c51a4f055222fb614d73419cb28272b87de*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/LocaleManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/LocaleManager.java
//Synthetic comment -- index 60120ee..e4da6e8 100644

//Synthetic comment -- @@ -758,7 +758,7 @@

// "pa": Panjabi, -> Pakistan, India
sLanguageToCountry.put("pa", "PK"); //$NON-NLS-1$ //$NON-NLS-2$
         sLanguageNames.put("pa", "Panjabi,"); //$NON-NLS-1$

// "pl": Polish -> Poland
sLanguageToCountry.put("pl", "PL"); //$NON-NLS-1$ //$NON-NLS-2$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFix.java
//Synthetic comment -- index 1ab02c3..c02c303 100644

//Synthetic comment -- @@ -106,7 +106,7 @@
public String getAdditionalProposalInfo() {
Issue issue = EclipseLintClient.getRegistry().getIssue(mId);
if (issue != null) {
            return issue.getExplanation().replace("\n", "<br>"); //$NON-NLS-1$ //$NON-NLS-2$
}

return null;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TranslationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TranslationDetector.java
//Synthetic comment -- index c796d26..a501e05 100644

//Synthetic comment -- @@ -74,7 +74,7 @@
"If an application has more than one locale, then all the strings declared in " +
"one language should also be translated in all other languages.\n" +
"\n" +
            "If the string should *not* be translated, you can add the attribute\n" +
"`translatable=\"false\"` on the `<string>` element, or you can define all " +
"your non-translatable strings in a resource file called `donottranslate.xml`. " +
"Or, you can ignore the issue with a `tools:ignore=\"MissingTranslation\"` " +








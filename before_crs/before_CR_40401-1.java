/*35875: Lint too strict about translating strings by default.

This changeset updates the issue explanation for the translation
detector to explain the translatable=false and donottranslate.xml
mechanisms to handle non-translatable strings.

It also bumps up the severity of the extra translations issue as
justified in issue 35875.

Change-Id:I7539464b234b0a4b444bf9f188ce5b819f962430*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TranslationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TranslationDetector.java
//Synthetic comment -- index f89fb81..7b81717 100644

//Synthetic comment -- @@ -73,6 +73,12 @@
"If an application has more than one locale, then all the strings declared in " +
"one language should also be translated in all other languages.\n" +
"\n" +
"By default this detector allows regions of a language to just provide a " +
"subset of the strings and fall back to the standard language strings. " +
"You can require all regions to provide a full translation by setting the " +
//Synthetic comment -- @@ -90,10 +96,13 @@
"If a string appears in a specific language translation file, but there is " +
"no corresponding string in the default locale, then this string is probably " +
"unused. (It's technically possible that your application is only intended to " +
            "run in a specific locale, but it's still a good idea to provide a fallback.)",
Category.MESSAGES,
6,
            Severity.WARNING,
TranslationDetector.class,
Scope.ALL_RESOURCES_SCOPE);









//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/TranslationDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/TranslationDetectorTest.java
//Synthetic comment -- index 6f1c2e6..6f9d1eb 100644

//Synthetic comment -- @@ -34,9 +34,9 @@
TranslationDetector.COMPLETE_REGIONS = false;
assertEquals(
// Sample files from the Home app
            "values-cs/arrays.xml:3: Warning: \"security_questions\" is translated here but not found in default locale\n" +
"=> values-es/strings.xml:12: Also translated here\n" +
            "values-de-rDE/strings.xml:11: Warning: \"continue_skip_label\" is translated here but not found in default locale\n" +
"values/strings.xml:20: Error: \"show_all_apps\" is not translated in nl-rNL\n" +
"values/strings.xml:23: Error: \"menu_wallpaper\" is not translated in nl-rNL\n" +
"values/strings.xml:25: Error: \"menu_settings\" is not translated in cs, de-rDE, es, es-rUS, nl-rNL",
//Synthetic comment -- @@ -57,7 +57,7 @@
TranslationDetector.COMPLETE_REGIONS = true;
assertEquals(
// Sample files from the Home app
            "values-de-rDE/strings.xml:11: Warning: \"continue_skip_label\" is translated here but not found in default locale\n" +
"values/strings.xml:19: Error: \"home_title\" is not translated in es-rUS\n" +
"values/strings.xml:20: Error: \"show_all_apps\" is not translated in es-rUS, nl-rNL\n" +
"values/strings.xml:23: Error: \"menu_wallpaper\" is not translated in es-rUS, nl-rNL\n" +








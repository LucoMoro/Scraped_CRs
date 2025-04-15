/*35875: Lint too strict about translating strings by default.

This changeset updates the issue explanation for the translation
detector to explain the translatable=false and donottranslate.xml
mechanisms to handle non-translatable strings.

It adds a quickfix for missing translation items to set the
translatable attribute to false.

And finally it also bumps up the severity of the extra translations
issue as justified in issue 35875.

Change-Id:I7539464b234b0a4b444bf9f188ce5b819f962430*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFix.java
//Synthetic comment -- index 9fa5018..1ab02c3 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.tools.lint.checks.ScrollViewChildDetector;
import com.android.tools.lint.checks.SecurityDetector;
import com.android.tools.lint.checks.TextFieldDetector;
import com.android.tools.lint.checks.TypoDetector;
import com.android.tools.lint.checks.TypographyDetector;
import com.android.tools.lint.checks.UseCompoundDrawableDetector;
//Synthetic comment -- @@ -153,6 +154,7 @@
sFixes.put(PxUsageDetector.PX_ISSUE.getId(), ConvertToDpFix.class);
sFixes.put(TextFieldDetector.ISSUE.getId(), SetAttributeFix.class);
sFixes.put(SecurityDetector.EXPORTED_SERVICE.getId(), SetAttributeFix.class);
sFixes.put(DetectMissingPrefix.MISSING_NAMESPACE.getId(), AddPrefixFix.class);
sFixes.put(ScrollViewChildDetector.ISSUE.getId(), SetScrollViewSizeFix.class);
sFixes.put(ObsoleteLayoutParamsDetector.ISSUE.getId(), ObsoleteLayoutParamsFix.class);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java
//Synthetic comment -- index a07101f..5d38df2 100644

//Synthetic comment -- @@ -98,6 +98,7 @@
* in the Problems view; perhaps we should use a custom view for these. That would also
* make marker management more obvious.
*/
public class LintFixGenerator implements IMarkerResolutionGenerator2, IQuickAssistProcessor {
/** Constructs a new {@link LintFixGenerator} */
public LintFixGenerator() {
//Synthetic comment -- @@ -248,7 +249,6 @@
*
* @param marker the marker pointing to the error to be suppressed
*/
    @SuppressWarnings("restriction") // XML model
public static void addSuppressAnnotation(IMarker marker) {
String id = EclipseLintClient.getId(marker);
if (id != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/SetAttributeFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/SetAttributeFix.java
//Synthetic comment -- index 896966e..a860c69 100644

//Synthetic comment -- @@ -18,17 +18,18 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_CONTENT_DESCRIPTION;
import static com.android.ide.common.layout.LayoutConstants.ATTR_INPUT_TYPE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FALSE;

import com.android.tools.lint.checks.AccessibilityDetector;
import com.android.tools.lint.checks.InefficientWeightDetector;
import com.android.tools.lint.checks.SecurityDetector;
import com.android.tools.lint.checks.TextFieldDetector;
import com.android.tools.lint.detector.api.LintConstants;

import org.eclipse.core.resources.IMarker;

/** Shared fix class for various builtin attributes */
@SuppressWarnings("restriction") // DOM model
final class SetAttributeFix extends SetPropertyFix {
private SetAttributeFix(String id, IMarker marker) {
super(id, marker);
//Synthetic comment -- @@ -44,6 +45,8 @@
return LintConstants.ATTR_PERMISSION;
} else if (mId.equals(TextFieldDetector.ISSUE.getId())) {
return ATTR_INPUT_TYPE;
} else {
assert false : mId;
return "";
//Synthetic comment -- @@ -51,6 +54,15 @@
}

@Override
public String getDisplayString() {
if (mId.equals(AccessibilityDetector.ISSUE.getId())) {
return "Add content description attribute";
//Synthetic comment -- @@ -60,6 +72,8 @@
return "Set input type";
} else if (mId.equals(SecurityDetector.EXPORTED_SERVICE.getId())) {
return "Add permission attribute";
} else {
assert false : mId;
return "";
//Synthetic comment -- @@ -67,15 +81,37 @@
}

@Override
protected boolean invokeCodeCompletion() {
return mId.equals(SecurityDetector.EXPORTED_SERVICE.getId())
|| mId.equals(TextFieldDetector.ISSUE.getId());
}

@Override
protected String getProposal() {
if (mId.equals(InefficientWeightDetector.BASELINE_WEIGHTS.getId())) {
return VALUE_FALSE;
}

return super.getProposal();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/SetPropertyFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/SetPropertyFix.java
//Synthetic comment -- index 2bfe5e8..8b32734 100644

//Synthetic comment -- @@ -48,6 +48,9 @@
/** Attribute to be added */
protected abstract String getAttribute();

protected String getProposal() {
return invokeCodeCompletion() ? "" : "TODO"; //$NON-NLS-1$
}
//Synthetic comment -- @@ -70,7 +73,10 @@
Element element = (Element) node;
String proposal = getProposal();
String localAttribute = getAttribute();
            String prefix = XmlUtils.lookupNamespacePrefix(node, ANDROID_URI);
String attribute = prefix != null ? prefix + ':' + localAttribute : localAttribute;

// This does not work even though it should: it does not include the prefix
//Synthetic comment -- @@ -78,18 +84,29 @@
// So workaround instead:
element.setAttribute(attribute, proposal);

            Attr attr = element.getAttributeNodeNS(ANDROID_URI, localAttribute);
if (attr instanceof IndexedRegion) {
IndexedRegion region = (IndexedRegion) attr;
int offset = region.getStartOffset();
// We only want to select the value part inside the quotes,
// so skip the attribute and =" parts added by WST:
offset += attribute.length() + 2;
                mSelect = new Region(offset, proposal.length());
}
}
}

@Override
public void apply(IDocument document) {
try {








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








/*Compute completion documentation lazily, and add docs for ?-attrs

This changeset changes the content assist code to compute the
documentation popup text lazily (by providing our own
ICompletionProposal subclass which has enough data to compute it
lazily). This avoids the cost of formatting all the doc strings for
all the matches (there could be hundreds) and only doing it for the
currently selected item.

Second, this is used to look up lazily the documentation for attribute
strings such as ?android:attr/dividerHeight, which makes the recently
added code completion for these more useful.

Also fixes a typo in a field name in an unrelated class.

Change-Id:I12c37a5c91418d866d1d08f1401efdb07c1243de*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index 87e207f..58403c8 100644

//Synthetic comment -- @@ -24,11 +24,9 @@
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.descriptors.SeparatorAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextValueDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
//Synthetic comment -- @@ -44,7 +42,6 @@
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
//Synthetic comment -- @@ -555,7 +552,7 @@
if (choice instanceof ElementDescriptor) {
keyword = ((ElementDescriptor)choice).getXmlName();
icon    = ((ElementDescriptor)choice).getGenericIcon();
                tooltip = DescriptorsUtils.formatTooltip(((ElementDescriptor)choice).getTooltip());
} else if (choice instanceof TextValueDescriptor) {
continue; // Value nodes are not part of the completion choices
} else if (choice instanceof SeparatorAttributeDescriptor) {
//Synthetic comment -- @@ -563,9 +560,7 @@
} else if (choice instanceof AttributeDescriptor) {
keyword = ((AttributeDescriptor)choice).getXmlLocalName();
icon    = ((AttributeDescriptor)choice).getGenericIcon();
                if (choice instanceof TextAttributeDescriptor) {
                    tooltip = ((TextAttributeDescriptor) choice).getTooltip();
                }

// Get the namespace URI for the attribute. Note that some attributes
// do not have a namespace and thus return null here.
//Synthetic comment -- @@ -649,6 +644,8 @@
// inside the quotes.
// Special case for attributes: insert ="" stuff and locate caret inside ""
proposals.add(new CompletionProposal(
keyword + suffix,                   // String replacementString
offset - wordPrefix.length(),       // int replacementOffset
wordPrefix.length() + replaceLength,// int replacementLength
//Synthetic comment -- @@ -687,6 +684,11 @@
return true;
}

/**
* This method performs a prefix match for the given word and prefix, with a couple of
* Android code completion specific twists:








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/CompletionProposal.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/CompletionProposal.java
new file mode 100644
//Synthetic comment -- index 0000000..74b7dd8

//Synthetic comment -- @@ -0,0 +1,159 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 79c9d62..bcca729 100644

//Synthetic comment -- @@ -125,7 +125,7 @@
*/
private final static class TargetLoadBundle {
LoadStatus status;
        final HashSet<IJavaProject> projecsToReload = new HashSet<IJavaProject>();
}

private final SdkManager mManager;
//Synthetic comment -- @@ -534,7 +534,7 @@

// add project to bundle
if (project != null) {
                    bundle.projecsToReload.add(project);
}

// and set the flag to start the loading below
//Synthetic comment -- @@ -542,7 +542,7 @@
} else if (bundle.status == LoadStatus.LOADING) {
// add project to bundle
if (project != null) {
                    bundle.projecsToReload.add(project);
}

return bundle.status;
//Synthetic comment -- @@ -566,14 +566,14 @@

if (status.getCode() != IStatus.OK) {
bundle.status = LoadStatus.FAILED;
                                bundle.projecsToReload.clear();
} else {
bundle.status = LoadStatus.LOADED;

// Prepare the array of project to recompile.
// The call is done outside of the synchronized block.
                                javaProjectArray = bundle.projecsToReload.toArray(
                                        new IJavaProject[bundle.projecsToReload.size()]);

// and update the UI of the editors that depend on the target data.
plugin.updateTargetListeners(target);








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
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.descriptors.SeparatorAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextValueDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
//Synthetic comment -- @@ -44,7 +42,6 @@
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
//Synthetic comment -- @@ -555,7 +552,7 @@
if (choice instanceof ElementDescriptor) {
keyword = ((ElementDescriptor)choice).getXmlName();
icon    = ((ElementDescriptor)choice).getGenericIcon();
                // Tooltip computed lazily in {@link CompletionProposal}
} else if (choice instanceof TextValueDescriptor) {
continue; // Value nodes are not part of the completion choices
} else if (choice instanceof SeparatorAttributeDescriptor) {
//Synthetic comment -- @@ -563,9 +560,7 @@
} else if (choice instanceof AttributeDescriptor) {
keyword = ((AttributeDescriptor)choice).getXmlLocalName();
icon    = ((AttributeDescriptor)choice).getGenericIcon();
                // Tooltip computed lazily in {@link CompletionProposal}

// Get the namespace URI for the attribute. Note that some attributes
// do not have a namespace and thus return null here.
//Synthetic comment -- @@ -649,6 +644,8 @@
// inside the quotes.
// Special case for attributes: insert ="" stuff and locate caret inside ""
proposals.add(new CompletionProposal(
                    this,
                    choice,
keyword + suffix,                   // String replacementString
offset - wordPrefix.length(),       // int replacementOffset
wordPrefix.length() + replaceLength,// int replacementLength
//Synthetic comment -- @@ -687,6 +684,11 @@
return true;
}

    /** @return the editor associated with this content assist */
    AndroidXmlEditor getEditor() {
        return mEditor;
    }

/**
* This method performs a prefix match for the given word and prefix, with a couple of
* Android code completion specific twists:








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/CompletionProposal.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/CompletionProposal.java
new file mode 100644
//Synthetic comment -- index 0000000..74b7dd8

//Synthetic comment -- @@ -0,0 +1,159 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ide.eclipse.adt.internal.editors;

import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Just like {@link org.eclipse.jface.text.contentassist.CompletionProposal},
 * but computes the documentation string lazily since they are typically only
 * displayed for a small subset (the currently focused item) of the available
 * proposals, and producing the strings requires some computation.
 * <p>
 * It also attempts to compute documentation for value strings like
 * ?android:attr/dividerHeight.
 * <p>
 * TODO: Enhance this to compute documentation for additional values, such as
 * the various enum values (which are available in the attrs.xml file, but not
 * in the AttributeInfo objects for each enum value). To do this, I should
 * basically keep around the maps computed by the attrs.xml parser.
 */
class CompletionProposal implements ICompletionProposal {
    private static final Pattern ATTRIBUTE_PATTERN =
            Pattern.compile("[@?]android:attr/(.*)"); //$NON-NLS-1$

    private final AndroidContentAssist mAssist;
    private final Object mChoice;
    private final int mCursorPosition;
    private final int mReplacementOffset;
    private final int mReplacementLength;
    private final String mReplacementString;
    private final Image mImage;
    private final String mDisplayString;
    private final IContextInformation mContextInformation;
    private String mAdditionalProposalInfo;

    CompletionProposal(AndroidContentAssist assist,
            Object choice, String replacementString, int replacementOffset,
            int replacementLength, int cursorPosition, Image image, String displayString,
            IContextInformation contextInformation, String additionalProposalInfo) {
        assert replacementString != null;
        assert replacementOffset >= 0;
        assert replacementLength >= 0;
        assert cursorPosition >= 0;

        mAssist = assist;
        mChoice = choice;
        mCursorPosition = cursorPosition;
        mReplacementOffset = replacementOffset;
        mReplacementLength = replacementLength;
        mReplacementString = replacementString;
        mImage = image;
        mDisplayString = displayString;
        mContextInformation = contextInformation;
        mAdditionalProposalInfo = additionalProposalInfo;
    }

    @Override
    public Point getSelection(IDocument document) {
        return new Point(mReplacementOffset + mCursorPosition, 0);
    }

    @Override
    public IContextInformation getContextInformation() {
        return mContextInformation;
    }

    @Override
    public Image getImage() {
        return mImage;
    }

    @Override
    public String getDisplayString() {
        if (mDisplayString != null) {
            return mDisplayString;
        }
        return mReplacementString;
    }

    @Override
    public String getAdditionalProposalInfo() {
        if (mAdditionalProposalInfo == null) {
            if (mChoice instanceof ElementDescriptor) {
                String tooltip = ((ElementDescriptor)mChoice).getTooltip();
                mAdditionalProposalInfo = DescriptorsUtils.formatTooltip(tooltip);
            } else if (mChoice instanceof TextAttributeDescriptor) {
                mAdditionalProposalInfo = ((TextAttributeDescriptor) mChoice).getTooltip();
            } else if (mChoice instanceof String) {
                // Try to produce it lazily for strings like @android
                String value = (String) mChoice;
                Matcher matcher = ATTRIBUTE_PATTERN.matcher(value);
                if (matcher.matches()) {
                    String attrName = matcher.group(1);
                    AndroidTargetData data = mAssist.getEditor().getTargetData();
                    if (data != null) {
                        IDescriptorProvider descriptorProvider =
                            data.getDescriptorProvider(mAssist.getRootDescriptorId());
                        if (descriptorProvider != null) {
                            ElementDescriptor[] rootElementDescriptors =
                                descriptorProvider.getRootElementDescriptors();
                            for (ElementDescriptor elementDesc : rootElementDescriptors) {
                                for (AttributeDescriptor desc : elementDesc.getAttributes()) {
                                    String name = desc.getXmlLocalName();
                                    if (attrName.equals(name)) {
                                        IAttributeInfo attributeInfo = desc.getAttributeInfo();
                                        if (attributeInfo != null) {
                                            return attributeInfo.getJavaDoc();
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

        return mAdditionalProposalInfo;
    }

    @Override
    public void apply(IDocument document) {
        try {
            document.replace(mReplacementOffset, mReplacementLength, mReplacementString);
        } catch (BadLocationException x) {
            // ignore
        }
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 79c9d62..bcca729 100644

//Synthetic comment -- @@ -125,7 +125,7 @@
*/
private final static class TargetLoadBundle {
LoadStatus status;
        final HashSet<IJavaProject> projectsToReload = new HashSet<IJavaProject>();
}

private final SdkManager mManager;
//Synthetic comment -- @@ -534,7 +534,7 @@

// add project to bundle
if (project != null) {
                    bundle.projectsToReload.add(project);
}

// and set the flag to start the loading below
//Synthetic comment -- @@ -542,7 +542,7 @@
} else if (bundle.status == LoadStatus.LOADING) {
// add project to bundle
if (project != null) {
                    bundle.projectsToReload.add(project);
}

return bundle.status;
//Synthetic comment -- @@ -566,14 +566,14 @@

if (status.getCode() != IStatus.OK) {
bundle.status = LoadStatus.FAILED;
                                bundle.projectsToReload.clear();
} else {
bundle.status = LoadStatus.LOADED;

// Prepare the array of project to recompile.
// The call is done outside of the synchronized block.
                                javaProjectArray = bundle.projectsToReload.toArray(
                                        new IJavaProject[bundle.projectsToReload.size()]);

// and update the UI of the editors that depend on the target data.
plugin.updateTargetListeners(target);








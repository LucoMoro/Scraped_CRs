/*Remove check to UiElementNode.hasError() in format call

The XML editor would check node.hasError() and only format if that
method returned false.  The idea is that it shouldn't attempt to
prettyprint a DOM that might contain errors.

However, turns out that method is unrelated to the DOM model; it's
simply an error state which can be *set* on nodes. This is used by for
example the manifest editor to put error icons on certain
fields. However, it is never set on nodes in the layout editor, and
calling hasError() isn't cheap (because it iterates over all
attributes and recursively over all the children checking the hasError
state on all of them). Since this will never return true, we might as
well remove the call.

Change-Id:Iae176c8de1bb28460835a41faad75324acf97ebf*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 803478b..396e172 100644

//Synthetic comment -- @@ -1018,23 +1018,21 @@
model.changedModel();

if (AdtPrefs.getPrefs().getFormatGuiXml() && mFormatNode != null) {
                            if (mFormatNode == getUiRootNode()) {
                                reformatDocument();
                            } else {
                                Node node = mFormatNode.getXmlNode();
                                if (node instanceof IndexedRegion) {
                                    IndexedRegion region = (IndexedRegion) node;
                                    int begin = region.getStartOffset();
                                    int end = region.getEndOffset();

                                    if (!mFormatChildren) {
                                        // This will format just the attribute list
                                        end = begin + 1;
}

                                    reformatRegion(begin, end);
}
}
mFormatNode = null;








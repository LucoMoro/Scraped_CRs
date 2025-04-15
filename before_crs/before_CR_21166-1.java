/*Add Show Included In safeguard, and work around Eclipse DOM bug

First, add a check in the view hierarchy code to ensure that when we
are attempting to render a view included within another that the
included view's model is actually included in the render. If it is
not, then clear the inclusion context and re-render. This shouldn't be
necessary since we only offer Show Included In for views known to be
including the current view, but if something should go wrong somewhere
(such as a stale include list, or the current bug we have with parser
nesting) this is a useful fallback to prevent major confusion.

Second, add in null checks for a couple of places where we were
calling DOM Element.getAttributeNS. This method is according to the
DOM API not supposed to ever return null, so we had code assuming that
it would not, yet I've just run into it returning null in some cases
(and found an Eclipse bug report from 2009 stating the same thing -https://bugs.eclipse.org/bugs/show_bug.cgi?id=272378) so add
workaround safeguards. We call getAttributeNS in a LOT of places, and
we do not see this problem crop up everywhere, so there must be some
narrow scenarios where it is occurring. Therefore, I have not
sprinkled null checks or introduced a utility method just yet, though
that might be a good next step.

Change-Id:I80b5fe57abb153aac676e92e7020cc367051c9e7*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ExtractIncludeAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ExtractIncludeAction.java
//Synthetic comment -- index d3ff090..9566aff 100644

//Synthetic comment -- @@ -173,7 +173,8 @@
Element primaryNode = getPrimaryNode();
if (primaryNode != null) {
String id = primaryNode.getAttributeNS(ANDROID_URI, ATTR_ID);
            if (id.startsWith(ID_PREFIX) || id.startsWith(NEW_ID_PREFIX)) {
// Use everything following the id/, and make it lowercase since that is
// the convention for layouts
defaultName = id.substring(id.indexOf('/') + 1).toLowerCase();
//Synthetic comment -- @@ -340,7 +341,8 @@
Element primaryNode = getPrimaryNode();
if (primaryNode != null) {
String oldId = primaryNode.getAttributeNS(ANDROID_URI, ATTR_ID);
            if (oldId.length() > 0) {
return oldId;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java
//Synthetic comment -- index eac2228..91475d5 100644

//Synthetic comment -- @@ -176,6 +176,20 @@
if (infos != null) {
mLastValidViewInfoRoot = infos.getFirst();
mIncludedBounds = infos.getSecond();
} else {
mLastValidViewInfoRoot = null;
mIncludedBounds = null;








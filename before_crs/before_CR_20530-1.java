/*Fix error logging for null-message exceptions

Some exceptions (such as NullPointerException) can have null as their
getLocalizedMessage().  This meant that the render session could have
a result with a null message, but a non-null exception. This scenario
was not handled, for some error resulted in a failed result but no
details given.

Change-Id:I22c70cd09b3403e73e71ca971981b32026f00ad0*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 2a457e9..a857dc7 100644

//Synthetic comment -- @@ -1484,8 +1484,12 @@
if (session.getResult().isSuccess() == false) {
// An error was generated. Print it (and any other accumulated warnings)
String errorMessage = session.getResult().getErrorMessage();
            if (errorMessage != null && errorMessage.length() > 0) {
                logger.error(null, session.getResult().getErrorMessage(), null /*data*/);
} else if (!logger.hasProblems()) {
logger.error(null, "Unexpected error in rendering, no details given",
null /*data*/);








/*Change context menu label from "Clear" to "Default"

The property context menus in the layout editor lets you choose
between True and False (for booleans), and between the various
possible enum values for enum properties. You can also reset the
values back to whatever the defaults were.

The menu label for this was "Clear". This changeset changes this to
"Default" instead, since "Clear" sounds more like an action than a
persistent choice among many, and when this option is chosen you are
removing a local override of the property and picking up whatever the
default is.

Change-Id:Id3ea9618612ac44e2b965e77c9e84751364cf813*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index 983560f..516fa44 100644

//Synthetic comment -- @@ -290,7 +290,7 @@
"1t", "True",       //$NON-NLS-1$
"2f", "False",      //$NON-NLS-1$
"3sep", MenuAction.Choices.SEPARATOR, //$NON-NLS-1$
                        "4clr", "Clear"),   //$NON-NLS-1$
value,
"properties",           //$NON-NLS-1$
onChange);
//Synthetic comment -- @@ -309,7 +309,7 @@
mapify(
"~1sep", MenuAction.Choices.SEPARATOR,  //$NON-NLS-1$
"~2clr",                                //$NON-NLS-1$
                            "Clear " + (p.isFlag() ? "flag" : "enum")
)
),
current,








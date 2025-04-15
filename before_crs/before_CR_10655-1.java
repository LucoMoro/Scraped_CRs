/*Fixed Android issue #400, where the Intent documentation was inaccurate in a number of places, undoubtedly causing untold grief to innumerable masses.*/
//Synthetic comment -- diff --git a/core/java/android/content/Intent.java b/core/java/android/content/Intent.java
//Synthetic comment -- index f5397c4..7c1eccc 100644

//Synthetic comment -- @@ -75,10 +75,10 @@
* <p>Some examples of action/data pairs are:</p>
*
* <ul>
 *   <li> <p><b>{@link #ACTION_VIEW} <i>content://contacts/1</i></b> -- Display
*     information about the person whose identifier is "1".</p>
*   </li>
 *   <li> <p><b>{@link #ACTION_DIAL} <i>content://contacts/1</i></b> -- Display
*     the phone dialer with the person filled in.</p>
*   </li>
*   <li> <p><b>{@link #ACTION_VIEW} <i>tel:123</i></b> -- Display
//Synthetic comment -- @@ -89,10 +89,10 @@
*   <li> <p><b>{@link #ACTION_DIAL} <i>tel:123</i></b> -- Display
*     the phone dialer with the given number filled in.</p>
*   </li>
 *   <li> <p><b>{@link #ACTION_EDIT} <i>content://contacts/1</i></b> -- Edit
*     information about the person whose identifier is "1".</p>
*   </li>
 *   <li> <p><b>{@link #ACTION_VIEW} <i>content://contacts/</i></b> -- Display
*     a list of people, which the user can browse through.  This example is a
*     typical top-level entry into the Contacts application, showing you the
*     list of people. Selecting a particular person to view would result in a
//Synthetic comment -- @@ -156,7 +156,7 @@
* defined in the Intent class, but applications can also define their own.
* These strings use java style scoping, to ensure they are unique -- for
* example, the standard {@link #ACTION_VIEW} is called
 * "android.app.action.VIEW".</p>
*
* <p>Put together, the set of actions, data types, categories, and extra data
* defines a language for the system allowing for the expression of phrases
//Synthetic comment -- @@ -347,7 +347,7 @@
*     <li> <p><b>{ action=android.app.action.MAIN,
*         category=android.app.category.LAUNCHER }</b> is the actual intent
*         used by the Launcher to populate its top-level list.</p>
 *     <li> <p><b>{ action=android.app.action.VIEW
*          data=content://com.google.provider.NotePad/notes }</b>
*         displays a list of all the notes under
*         "content://com.google.provider.NotePad/notes", which
//Synthetic comment -- @@ -399,7 +399,7 @@
* NoteEditor activity:</p>
*
* <ul>
 *     <li> <p><b>{ action=android.app.action.VIEW
*          data=content://com.google.provider.NotePad/notes/<var>{ID}</var> }</b>
*         shows the user the content of note <var>{ID}</var>.</p>
*     <li> <p><b>{ action=android.app.action.EDIT








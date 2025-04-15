/*NotificationManager: droiddoc documentation improvements

Specifically, corrects and improves the overview and the documentation
for the NotificationManager.notify(String, int, Notification) method
to reflect the fact that the pair (tag, id) is used for notification
matching.

Change-Id:Ic088a56f457285523d90d296853685393b8c3412*/
//Synthetic comment -- diff --git a/core/java/android/app/NotificationManager.java b/core/java/android/app/NotificationManager.java
//Synthetic comment -- index 6fe12fc..2b42f8c 100644

//Synthetic comment -- @@ -38,13 +38,17 @@
* </ul>
*
* <p>
 * Each of the notify methods takes an int id parameter.  This id identifies
 * this notification from your app to the system, so that id should be unique
 * within your app.  If you call one of the notify methods with an id that is
 * currently active and a new set of notification parameters, it will be
 * updated.  For example, if you pass a new status bar icon, the old icon in
 * the status bar will be replaced with the new one.  This is also the same
 * id you pass to the {@link #cancel} method to clear this notification.
*
* <p>
* You do not instantiate this class directly; instead, retrieve it through
//Synthetic comment -- @@ -93,12 +97,11 @@
/**
* Persistent notification on the status bar,
*
     * @param tag An string identifier for this notification unique within your
     *        application.
* @param notification A {@link Notification} object describing how to
*        notify the user, other than the view you're providing. Must not be null.
     * @return the id of the notification that is associated with the string identifier that
     * can be used to cancel the notification
*/
public void notify(String tag, int id, Notification notification)
{








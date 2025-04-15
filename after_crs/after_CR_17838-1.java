/*NotificationManager: droiddoc documentation improvements

Specifically, corrects and improves the documentation for the
NotificationManager.notify(String, int, Notification) method.

Change-Id:Ic088a56f457285523d90d296853685393b8c3412*/




//Synthetic comment -- diff --git a/core/java/android/app/NotificationManager.java b/core/java/android/app/NotificationManager.java
//Synthetic comment -- index 6fe12fc..fbfbae6 100644

//Synthetic comment -- @@ -93,12 +93,11 @@
/**
* Persistent notification on the status bar,
*
     * @param tag A string identifier for this notification.
     * @param id An identifier for this notification.  The pair (tag, id) must be unique
     *        within your application.
* @param notification A {@link Notification} object describing how to
*        notify the user, other than the view you're providing. Must not be null.
*/
public void notify(String tag, int id, Notification notification)
{








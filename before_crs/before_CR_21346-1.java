/*Fixing self-assignment in cloning code.

Change-Id:I6c918c0c7345678cbb171905eccfca50e59ae41a*/
//Synthetic comment -- diff --git a/core/java/android/app/Notification.java b/core/java/android/app/Notification.java
//Synthetic comment -- index 856943d..414a2a1 100644

//Synthetic comment -- @@ -374,7 +374,7 @@
if (this.contentView != null) {
that.contentView = this.contentView.clone();
}
        that.iconLevel = that.iconLevel;
that.sound = this.sound; // android.net.Uri is immutable
that.audioStreamType = this.audioStreamType;









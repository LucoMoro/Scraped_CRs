/*Updated RGB LED test cases

The test cases for turning on the RGB LED with persistent light was corrected.
The color for blinking was updated to blue. And finally an option for turning
off the lights was added.*/




//Synthetic comment -- diff --git a/tests/StatusBar/src/com/android/statusbartest/NotificationTestList.java b/tests/StatusBar/src/com/android/statusbartest/NotificationTestList.java
//Synthetic comment -- index f2ddd0f..4fd7e65 100644

//Synthetic comment -- @@ -165,6 +165,8 @@
Notification n = new Notification();
n.flags |= Notification.FLAG_SHOW_LIGHTS;
n.ledARGB = 0xff0000ff;
                n.ledOnMS = 1;
                n.ledOffMS = 0;
mNM.notify(1, n);
}
},
//Synthetic comment -- @@ -175,6 +177,8 @@
Notification n = new Notification();
n.flags |= Notification.FLAG_SHOW_LIGHTS;
n.ledARGB = 0xffff0000;
                n.ledOnMS = 1;
                n.ledOffMS = 0;
mNM.notify(1, n);
}
},
//Synthetic comment -- @@ -185,6 +189,20 @@
Notification n = new Notification();
n.flags |= Notification.FLAG_SHOW_LIGHTS;
n.ledARGB = 0xffffff00;
                n.ledOnMS = 1;
                n.ledOffMS = 0;
                mNM.notify(1, n);
            }
        },

        new Test("Lights off") {
            public void run()
            {
                Notification n = new Notification();
                n.flags |= Notification.FLAG_SHOW_LIGHTS;
                n.ledARGB = 0x00000000;
                n.ledOnMS = 0;
                n.ledOffMS = 0;
mNM.notify(1, n);
}
},
//Synthetic comment -- @@ -194,7 +212,7 @@
{
Notification n = new Notification();
n.flags |= Notification.FLAG_SHOW_LIGHTS;
                n.ledARGB = 0xff0000ff;
n.ledOnMS = 1300;
n.ledOffMS = 1300;
mNM.notify(1, n);
//Synthetic comment -- @@ -206,7 +224,7 @@
{
Notification n = new Notification();
n.flags |= Notification.FLAG_SHOW_LIGHTS;
                n.ledARGB = 0xff0000ff;
n.ledOnMS = 300;
n.ledOffMS = 300;
mNM.notify(1, n);








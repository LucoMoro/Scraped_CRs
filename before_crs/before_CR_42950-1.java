/*Add return statements and exception handling

In the function handleEvent added additional return statements what
allows not to perform additional checks. Empty catch statements are
filled. Try/catch blocks are added where necessary.

Change-Id:I6dd13f14d97d7e75e65f56bad211889112b6cef8*/
//Synthetic comment -- diff --git a/cmds/monkey/src/com/android/commands/monkey/MonkeySourceScript.java b/cmds/monkey/src/com/android/commands/monkey/MonkeySourceScript.java
//Synthetic comment -- index b2b9b4f..4e445bc 100644

//Synthetic comment -- @@ -265,6 +265,7 @@
mQ.addLast(e);
System.out.println("Added key up \n");
} catch (NumberFormatException e) {
}
return;
}
//Synthetic comment -- @@ -302,6 +303,7 @@
.addPointer(0, x, y, pressure, size);
mQ.addLast(e);
} catch (NumberFormatException e) {
}
return;
}
//Synthetic comment -- @@ -319,6 +321,7 @@
persist != 0));
}
} catch (NumberFormatException e) {
}
return;
}
//Synthetic comment -- @@ -385,95 +388,105 @@

// Handle drag event
if ((s.indexOf(EVENT_KEYWORD_DRAG) >= 0) && args.length == 5) {
            float xStart = Float.parseFloat(args[0]);
            float yStart = Float.parseFloat(args[1]);
            float xEnd = Float.parseFloat(args[2]);
            float yEnd = Float.parseFloat(args[3]);
            int stepCount = Integer.parseInt(args[4]);

            float x = xStart;
            float y = yStart;
            long downTime = SystemClock.uptimeMillis();
            long eventTime = SystemClock.uptimeMillis();

            if (stepCount > 0) {
                float xStep = (xEnd - xStart) / stepCount;
                float yStep = (yEnd - yStart) / stepCount;

                MonkeyMotionEvent e =
                        new MonkeyTouchEvent(MotionEvent.ACTION_DOWN).setDownTime(downTime)
.setEventTime(eventTime).addPointer(0, x, y, 1, 5);
                mQ.addLast(e);

                for (int i = 0; i < stepCount; ++i) {
                    x += xStep;
                    y += yStep;
eventTime = SystemClock.uptimeMillis();
                    e = new MonkeyTouchEvent(MotionEvent.ACTION_MOVE).setDownTime(downTime)
                        .setEventTime(eventTime).addPointer(0, x, y, 1, 5);
mQ.addLast(e);
}

                eventTime = SystemClock.uptimeMillis();
                e = new MonkeyTouchEvent(MotionEvent.ACTION_UP).setDownTime(downTime)
                    .setEventTime(eventTime).addPointer(0, x, y, 1, 5);
                mQ.addLast(e);
}
}

// Handle pinch or zoom action
if ((s.indexOf(EVENT_KEYWORD_PINCH_ZOOM) >= 0) && args.length == 9) {
            //Parse the parameters
            float pt1xStart = Float.parseFloat(args[0]);
            float pt1yStart = Float.parseFloat(args[1]);
            float pt1xEnd = Float.parseFloat(args[2]);
            float pt1yEnd = Float.parseFloat(args[3]);

            float pt2xStart = Float.parseFloat(args[4]);
            float pt2yStart = Float.parseFloat(args[5]);
            float pt2xEnd = Float.parseFloat(args[6]);
            float pt2yEnd = Float.parseFloat(args[7]);

            int stepCount = Integer.parseInt(args[8]);

            float x1 = pt1xStart;
            float y1 = pt1yStart;
            float x2 = pt2xStart;
            float y2 = pt2yStart;

            long downTime = SystemClock.uptimeMillis();
            long eventTime = SystemClock.uptimeMillis();

            if (stepCount > 0) {
                float pt1xStep = (pt1xEnd - pt1xStart) / stepCount;
                float pt1yStep = (pt1yEnd - pt1yStart) / stepCount;

                float pt2xStep = (pt2xEnd - pt2xStart) / stepCount;
                float pt2yStep = (pt2yEnd - pt2yStart) / stepCount;

                mQ.addLast(new MonkeyTouchEvent(MotionEvent.ACTION_DOWN).setDownTime(downTime)
                        .setEventTime(eventTime).addPointer(0, x1, y1, 1, 5));

                mQ.addLast(new MonkeyTouchEvent(MotionEvent.ACTION_POINTER_DOWN
                        | (1 << MotionEvent.ACTION_POINTER_INDEX_SHIFT)).setDownTime(downTime)
                        .addPointer(0, x1, y1).addPointer(1, x2, y2).setIntermediateNote(true));

                for (int i = 0; i < stepCount; ++i) {
                    x1 += pt1xStep;
                    y1 += pt1yStep;
                    x2 += pt2xStep;
                    y2 += pt2yStep;

eventTime = SystemClock.uptimeMillis();
                    mQ.addLast(new MonkeyTouchEvent(MotionEvent.ACTION_MOVE).setDownTime(downTime)
                            .setEventTime(eventTime).addPointer(0, x1, y1, 1, 5).addPointer(1, x2,
                                    y2, 1, 5));
}
                eventTime = SystemClock.uptimeMillis();
                mQ.addLast(new MonkeyTouchEvent(MotionEvent.ACTION_POINTER_UP)
                        .setDownTime(downTime).setEventTime(eventTime).addPointer(0, x1, y1)
                        .addPointer(1, x2, y2));
}
}

// Handle flip events
//Synthetic comment -- @@ -481,6 +494,7 @@
boolean keyboardOpen = Boolean.parseBoolean(args[0]);
MonkeyFlipEvent e = new MonkeyFlipEvent(keyboardOpen);
mQ.addLast(e);
}

// Handle launch events
//Synthetic comment -- @@ -555,6 +569,7 @@
MonkeyWaitEvent e = new MonkeyWaitEvent(sleeptime);
mQ.addLast(e);
} catch (NumberFormatException e) {
}
return;
}
//Synthetic comment -- @@ -590,6 +605,7 @@
mQ.addLast(we);
e = new MonkeyKeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_CENTER);
mQ.addLast(e);
}

//The power log event is mainly for the automated power framework
//Synthetic comment -- @@ -605,12 +621,14 @@
MonkeyPowerEvent e = new MonkeyPowerEvent(power_log_type, test_case_status);
mQ.addLast(e);
}
}

//Write power log to sdcard
if (s.indexOf(EVENT_KEYWORD_WRITEPOWERLOG) >= 0) {
MonkeyPowerEvent e = new MonkeyPowerEvent();
mQ.addLast(e);
}

//Run the shell command
//Synthetic comment -- @@ -618,6 +636,7 @@
String cmd = args[0];
MonkeyCommandEvent e = new MonkeyCommandEvent(cmd);
mQ.addLast(e);
}

//Input the string through the shell command








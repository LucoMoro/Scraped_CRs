/*monkeyrunner: Support MOVE event for touch

monkeyrunner support only drag, not MOVE type touch event.
Because drag event just drag from one point to another point, can't test
complex user touch event case(e.g., drawing curves or move icon at
launcher's homescreen).

This commit add support for MOVE touch event.

Change-Id:I9b2d5b2617d0c13cadbcfb63cd12007b9a9c0784Signed-off-by: SeongJae Park <sj38.park@gmail.com>*/




//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/adb/AdbChimpDevice.java b/chimpchat/src/com/android/chimpchat/adb/AdbChimpDevice.java
//Synthetic comment -- index 7c4b62a..200c69e 100644

//Synthetic comment -- @@ -360,6 +360,9 @@
case DOWN_AND_UP:
manager.tap(x, y);
break;
                case MOVE:
                    manager.touchMove(x, y);
                    break;
}
} catch (IOException e) {
LOG.log(Level.SEVERE, "Error sending touch event: " + x + " " + y + " " + type, e);








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/TouchPressType.java b/chimpchat/src/com/android/chimpchat/core/TouchPressType.java
//Synthetic comment -- index e5b92b7..7e1d4b6 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
* When passed as a string, the "identifier" value is used.
*/
public enum TouchPressType {
    DOWN("down"), UP("up"), DOWN_AND_UP("downAndUp"), MOVE("move");

private static final Map<String,TouchPressType> identifierToEnum =
new HashMap<String,TouchPressType>();








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index e60d12e..e73e386 100644

//Synthetic comment -- @@ -66,6 +66,9 @@
@MonkeyRunnerExported(doc = "Sends a DOWN event, immediately followed by an UP event when used with touch() or press()")
public static final String DOWN_AND_UP = TouchPressType.DOWN_AND_UP.getIdentifier();

    @MonkeyRunnerExported(doc = "Sends an MOVE event when used with touch().")
    public static final String MOVE = TouchPressType.MOVE.getIdentifier();

private IChimpDevice impl;

public MonkeyDevice(IChimpDevice impl) {








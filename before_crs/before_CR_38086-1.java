/*Fix pattern issue in com.android.cts.monkey.MonkeyTest

The pattern is designed to match the output of logcat without
format.
But, there are 8 formats defined in /system/core/liblog/logprint.c.
If logcat is set with any format, the test cases will fail.
Since 'executeAdbCommand' ensures the tag filter of 'MonkeyActivity:I',
check if the output contains MONKEY or HUMAN should work as expected.

Change-Id:I1bec6a4717994558ee4d66eee9795bc702fffa54*/
//Synthetic comment -- diff --git a/tests/appsecurity-tests/src/com/android/cts/monkey/MonkeyTest.java b/tests/appsecurity-tests/src/com/android/cts/monkey/MonkeyTest.java
//Synthetic comment -- index 2bf27ed..17a5cf8 100644

//Synthetic comment -- @@ -24,8 +24,6 @@

public class MonkeyTest extends AbstractMonkeyTest {

    private static final Pattern LOG_PATTERN =
            Pattern.compile("I/MonkeyActivity\\([\\d ]+\\): (.*)");
private static final String MONKEY = "@(>.<)@";
private static final String HUMAN = "(^_^)";

//Synthetic comment -- @@ -49,10 +47,8 @@
try {
while (s.hasNextLine()) {
String line = s.nextLine();
                Matcher m = LOG_PATTERN.matcher(line);
                if (m.matches()) {
monkeyLogsFound = true;
                    assertEquals(isMonkey ? MONKEY : HUMAN, m.group(1));
}
}
assertTrue(monkeyLogsFound);








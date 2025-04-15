/*Added missing --pct-syskeys to processOptions in Monkey

According to the developer guidelineshttp://developer.android.com/guide/developing/tools/monkey.htmlit should be possible to run monkey with option --pct-syskeys <percent>
to adjust percentage of "system" key events. (These are keys that are
generally reserved for use by the system, such as Home, Back,
Start Call, End Call, or Volume controls.) However, when trying
to run monkey with that option it fails with:
** Error: Unknown option: --pct-syskeys
The function processOptions in file Monkey.java was missing the
option --pct-syskeys.

Change-Id:I5d328fc93dfe67ed7a905735d6762c7f91c69838*/




//Synthetic comment -- diff --git a/cmds/monkey/src/com/android/commands/monkey/Monkey.java b/cmds/monkey/src/com/android/commands/monkey/Monkey.java
//Synthetic comment -- index 79548d4..84b2d52 100644

//Synthetic comment -- @@ -562,6 +562,9 @@
} else if (opt.equals("--pct-trackball")) {
int i = MonkeySourceRandom.FACTOR_TRACKBALL;
mFactors[i] = -nextOptionLong("trackball events percentage");
                } else if (opt.equals("--pct-syskeys")) {
                    int i = MonkeySourceRandom.FACTOR_SYSOPS;
                    mFactors[i] = -nextOptionLong("system (key) operations percentage");
} else if (opt.equals("--pct-nav")) {
int i = MonkeySourceRandom.FACTOR_NAV;
mFactors[i] = -nextOptionLong("nav events percentage");








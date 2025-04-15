/*Fixed MonkeyRunner to work with PyDev.

Eclipse passes the -u option to MonkeyRunner by default. Since we don't
do anything with that option, we can just ignore it.

Bug: 4109883

Change-Id:If3624c13583f639e745742f100a0e31e1ab694ad*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerOptions.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerOptions.java
//Synthetic comment -- index db53851..0deaf16 100644

//Synthetic comment -- @@ -153,6 +153,8 @@
}

pluginListBuilder.add(plugin);
            } else if ("-u".equals(argument)){
                // This is asking for unbuffered input. We can ignore this.
} else if (argument.startsWith("-") &&
// Once we have the scriptfile, the rest of the arguments go to jython.
scriptFile == null) {








/*Fix error detection for invalid command line arguments.

This fixes 2 edge cases:
- when an unknown extra parameter is given (it was simply
  ignored before)

  e.g.: "android update sdk foo" => foo is extra.

- when a 1-argument parameter is used and no argument is actually
  given but instead the next dash-parameter is found.

  e.g.: "android update sdk --filter (missing value) --somearg"

Change-Id:Idc34f61728411aceaf1cf33f070ac275b3798019*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/CommandLineProcessor.java b/sdkmanager/app/src/com/android/sdkmanager/CommandLineProcessor.java
//Synthetic comment -- index 6536baa..7edee3a 100644

//Synthetic comment -- @@ -257,7 +257,7 @@
a);
return;
} else {
                            // It looks like a dashed parameter but it is unknown by this
// verb-object combination

errorMsg = String.format(
//Synthetic comment -- @@ -309,6 +309,12 @@
return;

}
                    } else {
                        // The argument is not a dashed parameter and we already
                        // have a verb/object. Must be some extra unknown argument.
                        errorMsg = String.format(
                                "Argument '%1$s' is not recognized.",
                                a);
}
} else if (arg != null) {
// This argument was present on the command line
//Synthetic comment -- @@ -321,8 +327,22 @@
errorMsg = String.format("Missing argument for flag %1$s.", a);
return;
}
                        String b = args[i];

                        Arg dummyArg = null;
                        if (b.startsWith("--")) {
                            dummyArg = findLongArg(verb, directObject, b.substring(2));
                        } else if (b.startsWith("-")) {
                            dummyArg = findShortArg(verb, directObject, b.substring(1));
                        }
                        if (dummyArg != null) {
                            errorMsg = String.format(
                                    "Oops, it looks like you didn't provide an argument for '%1$s'.\n'%2$s' was found instead.",
                                    a, b);
                            return;
                        }

                        error = arg.getMode().process(arg, b);
} else {
error = arg.getMode().process(arg, null);









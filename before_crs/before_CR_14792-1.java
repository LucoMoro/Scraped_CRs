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
//Synthetic comment -- index 6536baa..1c385b3 100644

//Synthetic comment -- @@ -257,7 +257,7 @@
a);
return;
} else {
                            // It looks like a dashed parameter and but it is unknown by this
// verb-object combination

errorMsg = String.format(
//Synthetic comment -- @@ -309,6 +309,12 @@
return;

}
}
} else if (arg != null) {
// This argument was present on the command line
//Synthetic comment -- @@ -321,8 +327,22 @@
errorMsg = String.format("Missing argument for flag %1$s.", a);
return;
}

                        error = arg.getMode().process(arg, args[i]);
} else {
error = arg.getMode().process(arg, null);









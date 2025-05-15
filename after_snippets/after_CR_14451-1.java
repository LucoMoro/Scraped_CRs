
//<Beginning of snippet n. 0>


* @param args The arguments typically received by a main method.
*/
public void parseArgs(String[] args) {
        String errorMsg = null;
String verb = null;
String directObject = null;

// It looks like a dashed parameter and we don't have a a verb/object
// set yet, the parameter was just given too early.

                            errorMsg = String.format(
"Flag '%1$s' is not a valid global flag. Did you mean to specify it after the verb/object name?",
a);
return;
// It looks like a dashed parameter and but it is unknown by this
// verb-object combination

                            errorMsg = String.format(
"Flag '%1$s' is not valid for '%2$s %3$s'.",
a, verb, directObject);
return;

// Error if it was not a valid verb
if (verb == null) {
                            errorMsg = String.format(
"Expected verb after global parameters but found '%1$s' instead.",
a);
return;

// Error if it was not a valid object for that verb
if (directObject == null) {
                            errorMsg = String.format(
"Expected verb after global parameters but found '%1$s' instead.",
a);
return;
String error = null;
if (arg.getMode().needsExtra()) {
if (++i >= n) {
                            errorMsg = String.format("Missing argument for flag %1$s.", a);
return;
}

} else {
error = arg.getMode().process(arg, null);

if (isHelpRequested()) {
                            // The --help flag was requested. We'll continue the usual processing
                            // so that we can find the optional verb/object words. Those will be
                            // used to print specific help.
                            // Setting a non-null error message triggers printing the help, however
                            // there is no specific error to print.
                            errorMsg = "";
}
}

if (error != null) {
                        errorMsg = String.format("Invalid usage for flag %1$s: %2$s.", a, error);
return;
}
}
}

            if (errorMsg == null) {
if (verb == null && !acceptLackOfVerb()) {
                    errorMsg = "Missing verb name.";
} else if (verb != null) {
if (directObject == null) {
// Make sure this verb has an optional direct object
}

if (directObject == null) {
                            errorMsg = String.format("Missing object name for verb '%1$s'.", verb);
return;
}
}
}

if (missing != null) {
                        errorMsg  = String.format(
"The %1$s %2$s must be defined for action '%3$s %4$s'",
plural ? "parameters" : "parameter",
missing,
}
}
} finally {
            if (errorMsg != null) {
                printHelpAndExitForAction(verb, directObject, errorMsg);
}
}
}
*/
public void printHelpAndExitForAction(String verb, String directObject,
String errorFormat, Object... args) {
        if (errorFormat != null && errorFormat.length() > 0) {
stderr(errorFormat, args);
}

*/
stdout("\n" +
"Usage:\n" +
            "  android [global options] %s [action options]\n" +
"\n" +
            "Global options:",
            verb == null ? "action" :
                verb + (directObject == null ? "" : " " + directObject));
listOptions(GLOBAL_FLAG_VERB, NO_VERB_OBJECT);

if (verb == null || directObject == null) {
stdout("\nValid actions are composed of a verb and an optional direct object:");
for (String[] action : mActions) {
                if (verb == null || verb.equals(action[ACTION_VERB_INDEX])) {
                    stdout("- %1$6s %2$-12s: %3$s",
                            action[ACTION_VERB_INDEX],
                            action[ACTION_OBJECT_INDEX],
                            action[ACTION_DESC_INDEX]);
                }
}
}

        // Only print details if a verb/object is requested
        if (verb != null) {
            for (String[] action : mActions) {
                if (verb == null || verb.equals(action[ACTION_VERB_INDEX])) {
                    if (directObject == null || directObject.equals(action[ACTION_OBJECT_INDEX])) {
                        stdout("\nAction \"%1$s %2$s\":",
                                action[ACTION_VERB_INDEX],
                                action[ACTION_OBJECT_INDEX]);
                        stdout("  %1$s", action[ACTION_DESC_INDEX]);
                        stdout("Options:");
                        listOptions(action[ACTION_VERB_INDEX], action[ACTION_OBJECT_INDEX]);
                    }
}
}
}

//<End of snippet n. 0>









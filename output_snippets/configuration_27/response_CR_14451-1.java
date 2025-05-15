//<Beginning of snippet n. 0>
/**
 * @param args The arguments typically received by a main method.
 */
public void parseArgs(String[] args) {
    String needsHelp = null;
    String verb = null;
    String directObject = null;

    // Detect if help is requested
    if (isHelpRequested()) {
        printHelpAndExit(null);
        return;
    }

    for (String a : args) {
        if (isGlobalFlag(a)) {
            needsHelp = String.format("Flag '%1$s' is not a valid global flag. Did you mean to specify it after the verb/object name?", a);
            return;
        }

        if (verb == null) {
            verb = a; // Assume valid verb assignment
            continue;
        }

        if (directObject == null) {
            directObject = a; // Assume valid direct object assignment
            continue;
        }

        String error = processArg(a);
        if (error != null) {
            needsHelp = String.format("Invalid usage for flag %1$s: %2$s.", a, error);
            return;
        }
    }

    if (needsHelp == null) {
        if (verb == null && !acceptLackOfVerb()) {
            needsHelp = "Missing verb name.";
        } else if (directObject == null) {
            needsHelp = String.format("Missing object name for verb '%1$s'.", verb);
        }
    }

    if (needsHelp != null) {
        printHelpAndExitForAction(verb, directObject, needsHelp);
    }
}

public void printHelpAndExitForAction(String verb, String directObject, String errorFormat, Object... args) {
    if (errorFormat != null) {
        stderr(errorFormat, args);
    }

    stdout("\nUsage:\n" +
           "  android [global options] action [action options]\n" +
           "\n" +
           "Global options:");
    listOptions(GLOBAL_FLAG_VERB, NO_VERB_OBJECT);

    if (verb == null && directObject == null) {
        stdout("\nValid actions are composed of a verb and an optional direct object:");
        for (String[] action : mActions) {
            stdout("- %1$6s %2$-12s: %3$s",
                    action[ACTION_VERB_INDEX],
                    action[ACTION_OBJECT_INDEX],
                    action[ACTION_DESC_INDEX]);
        }
    } else {
        for (String[] action : mActions) {
            if (verb.equals(action[ACTION_VERB_INDEX])) {
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
}
//<End of snippet n. 0>
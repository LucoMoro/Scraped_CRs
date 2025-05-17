//<Beginning of snippet n. 0>

public void parseArgs(String[] args) {
    String needsHelp = null;
    String verb = null;
    String directObject = null;

    for (String a : args) {
        if (isGlobalFlag(a)) {
            needsHelp = String.format(
                "Flag '%1$s' is not a valid global flag.", a);
            return;
        }
        
        if (isInvalidForVerb(a, verb, directObject)) {
            needsHelp = String.format(
                "Flag '%1$s' is not valid for '%2$s %3$s'.", a, verb, directObject);
            return;
        }

        if (verb == null) {
            needsHelp = String.format(
                "Expected verb after global parameters but found '%1$s' instead.", a);
            return;
        }

        if (directObject == null) {
            needsHelp = String.format(
                "Expected direct object for verb '%1$s' but found '%2$s'.", verb, a);
            return;
        }
        
        String error = handleArg(a, verb, directObject);
        
        if (isHelpRequested()) {
            printHelpAndExit(null);
            return;
        }

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
            return;
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

    stdout("\nValid actions are composed of a verb and an optional direct object:");
    for (String[] action : mActions) {
        stdout("- %1$6s %2$-12s",
               action[ACTION_VERB_INDEX],
               action[ACTION_OBJECT_INDEX]);
    }

    for (String[] action : mActions) {
        if (verb == null || verb.equals(action[ACTION_VERB_INDEX])) {
            if (directObject == null || directObject.equals(action[ACTION_OBJECT_INDEX])) {
                stdout("\nAction \"%1$s %2$s\":",
                       action[ACTION_VERB_INDEX],
                       action[ACTION_OBJECT_INDEX]);
                stdout("Options:");
                listOptions(action[ACTION_VERB_INDEX], action[ACTION_OBJECT_INDEX]);
            }
        }
    }
}

//<End of snippet n. 0>
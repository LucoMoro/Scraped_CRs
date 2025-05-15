//<Beginning of snippet n. 0>
public void parseArgs(String[] args) {
    String needsHelp = null;
    String verb = null;
    String directObject = null;

    // Error if it was not a valid verb
    if (verb == null) {
        needsHelp = "Missing verb name.";
        return;
    }

    // Error if it was not a valid object for that verb
    if (directObject == null) {
        needsHelp = String.format("Missing object name for verb '%1$s'.", verb);
        return;
    }

    String error = null;

    if (arg.getMode().needsExtra()) {
        if (++i >= n) {
            needsHelp = String.format("Missing argument for flag %1$s.", a);
            return;
        }
    } else {
        error = arg.getMode().process(arg, null);

        if (isHelpRequested()) {
            printHelpAndExit(null);
            return;
        }
    }

    if (error != null) {
        needsHelp = String.format("Invalid usage for flag %1$s: %2$s.", a, error);
        return;
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

    if (verb == null || directObject == null) {
        stdout("\nValid actions are composed of a verb and an optional direct object:");
        for (String[] action : mActions) {
            stdout("- %1$6s %2$-12s",
                    action[ACTION_VERB_INDEX],
                    action[ACTION_OBJECT_INDEX]);
        }
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
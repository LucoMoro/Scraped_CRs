//<Beginning of snippet n. 0>

public void parseArgs(String[] args) {
    String needsHelp = null;
    String verb = null;
    String directObject = null;

    for (String a : args) {
        if (isFlag(a)) {
            if (verb == null && directObject == null) {
                needsHelp = String.format(
                    "Flag '%1$s' is not a valid global flag. Did you mean to specify it after the verb/object name?", a);
                return;
            }
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
                "Expected object after verb '%1$s' but found '%2$s' instead.", verb, a);
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
    }

    if (needsHelp == null) {
        if (verb == null && !acceptLackOfVerb()) {
            needsHelp = "Missing verb name.";
        } else if (verb != null && directObject == null) {
            needsHelp = String.format("Missing object name for verb '%1$s'.", verb);
        }
    }

    if (missing != null) {
        needsHelp = String.format(
            "The %1$s %2$s must be defined for action '%3$s %4$s'",
            plural ? "parameters" : "parameter", missing);
    }

    if (needsHelp != null) {
        printHelpAndExitForAction(verb, directObject, needsHelp);
    }
}

public void printHelpAndExitForAction(String verb, String directObject,
    String errorFormat, Object... args) {
    if (errorFormat != null) {
        stderr(errorFormat, args);
    }

    stdout("\n" +
        "Usage:\n" +
        "  android [global options] action [action options]\n" +
        "\n" +
        "Global options:");
    listOptions(GLOBAL_FLAG_VERB, NO_VERB_OBJECT);

    if (verb == null || directObject == null) {
        stdout("\nValid actions are composed of a verb and an optional direct object:");
        for (String[] action : mActions) {
            stdout("- %1$6s %2$-12s: %3$s",
                action[ACTION_VERB_INDEX],
                action[ACTION_OBJECT_INDEX],
                action[ACTION_DESC_INDEX]);
        }
    }

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
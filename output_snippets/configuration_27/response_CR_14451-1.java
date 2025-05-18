//<Beginning of snippet n. 0>
public void parseArgs(String[] args) {
    String needsHelp = null;
    String verb = null;
    String directObject = null;

    for (String a : args) {
        if (isGlobalFlag(a)) {
            needsHelp = String.format("Flag '%1$s' is not a valid global flag. Did you mean to specify it after the verb/object name?", a);
            return;
        }

        if (verb == null) {
            verb = a;
            continue;
        }

        if (directObject == null) {
            directObject = a;
            continue;
        }

        String error = null; 
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
           "\nGlobal options:");
    listOptions(GLOBAL_FLAG_VERB, NO_VERB_OBJECT);

    if (verb == null && directObject == null) {
        stdout("\nValid actions are composed of a verb and an optional direct object:");
        for (String[] action : mActions) {
            stdout("- %1$6s %2$-12s: %3$s",
                    action[ACTION_VERB_INDEX],
                    action[ACTION_OBJECT_INDEX],
                    action[ACTION_DESC_INDEX]);
        }
    } else if ("create".equals(verb)) {
        stdout("\nAction \"create\":");
        stdout("Options:");
        listOptions("create", null);
        if (directObject != null) {
            for (String[] action : mActions) {
                if (verb.equals(action[ACTION_VERB_INDEX]) && directObject.equals(action[ACTION_OBJECT_INDEX])) {
                    stdout("\nAction \"%1$s %2$s\":", action[ACTION_VERB_INDEX], action[ACTION_OBJECT_INDEX]);
                    stdout("  %1$s", action[ACTION_DESC_INDEX]);
                    stdout("Options:");
                    listOptions(action[ACTION_VERB_INDEX], action[ACTION_OBJECT_INDEX]);
                }
            }
        }
    }

    for (String[] action : mActions) {
        if (verb == null || verb.equals(action[ACTION_VERB_INDEX])) {
            if (directObject == null || directObject.equals(action[ACTION_OBJECT_INDEX])) {
                stdout("\nAction \"%1$s %2$s\":", action[ACTION_VERB_INDEX], action[ACTION_OBJECT_INDEX]);
                stdout("  %1$s", action[ACTION_DESC_INDEX]);
                stdout("Options:");
                listOptions(action[ACTION_VERB_INDEX], action[ACTION_OBJECT_INDEX]);
            }
        }
    }
}
//<End of snippet n. 0>
```
//<Beginning of snippet n. 0>
        if (args.size() == 2) {
            // Handle the case for exactly 2 arguments
            processTwoArguments(args);
        } else if (args.size() == 3) {
            // Handle the case for exactly 3 arguments
            processThreeArguments(args);
        } else {
            // Handle the case for an unexpected number of arguments
            throw new IllegalArgumentException("Expected 2 or 3 arguments, but got " + args.size());
        }
//<End of snippet n. 0>
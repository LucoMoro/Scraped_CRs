/*Allow saving log files in different directory

If TestLog path is not specified, then use ConfigRoot instead to
retain old behavior.

Change-Id:Icf07fb8555192c8eb3722bc5fdc792000a764669*/




//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/HostConfig.java b/tools/host/src/com/android/cts/HostConfig.java
//Synthetic comment -- index 116a46f..fbea3a5 100644

//Synthetic comment -- @@ -63,6 +63,7 @@
"logo.gif", "newrule-green.png"};

private String mConfigRoot;
    private String mLogRoot;
private CaseRepository mCaseRepos;
private ResultRepository mResultRepos;
private PlanRepository mPlanRepos;
//Synthetic comment -- @@ -174,6 +175,13 @@
String planRoot = repositoryRoot + File.separator + planCfg;
String resRoot = repositoryRoot + File.separator + resCfg;

        String logCfg = getStringAttributeValueOpt(doc, "TestLog", "path", fileName);
        if (null == logCfg) {
            mLogRoot = mConfigRoot;
        } else {
            mLogRoot = repositoryRoot + File.separator + logCfg;
        }

boolean validCase = true;
if (!validateDirectory(caseRoot)) {
validCase = new File(caseRoot).mkdirs();
//Synthetic comment -- @@ -189,12 +197,16 @@
if (!validateDirectory(planRoot)) {
validPlan = new File(planRoot).mkdirs();
}
        boolean validLog = true;
        if (!validateDirectory(mLogRoot)) {
            validLog = new File(mLogRoot).mkdirs();
        }

mCaseRepos = new CaseRepository(caseRoot);
mResultRepos = new ResultRepository(resRoot);
mPlanRepos = new PlanRepository(planRoot);

        return validCase && validRes && validPlan && validLog;
}

/**
//Synthetic comment -- @@ -285,6 +297,15 @@
}

/**
     * Get the root directory of log files.
     *
     * @return the root directory of log files.
     */
    public String getLogRoot() {
        return mLogRoot;
    }

    /**
* Get string attribute value.
*
* @param doc The document.
//Synthetic comment -- @@ -317,6 +338,29 @@
}

/**
     * Get string attribute value if it exists.
     *
     * @param doc The document.
     * @param tagName The tag name.
     * @param attrName The attribute name.
     * @param fileName The file name.
     * @return The attribute value.
     */
    private String getStringAttributeValueOpt(final Document doc,
            final String tagName, final String attrName, final String fileName) {

        String cfgStr = null;
        try {
            cfgStr = getStringAttributeValue(doc
                    .getElementsByTagName(tagName).item(0), attrName);
        } catch (Exception e) {
            return null;
        }

        return cfgStr;
    }

    /**
* Load configuration values from config file.
*
* @param doc The document from which to load the values.








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestHost.java b/tools/host/src/com/android/cts/TestHost.java
//Synthetic comment -- index e18bc79..52a0553 100644

//Synthetic comment -- @@ -362,7 +362,7 @@
exit();
}

            Log.initLog(sConfig.getLogRoot());
sConfig.loadRepositories();
} catch (Exception e) {
Log.e("Error while parsing cts config file", e);








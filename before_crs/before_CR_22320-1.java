/*Allow saving log files in different directory

If TestLog path is not specified, then use ConfigRoot instead to
retain old behavior.

Change-Id:Icf07fb8555192c8eb3722bc5fdc792000a764669*/
//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/HostConfig.java b/tools/host/src/com/android/cts/HostConfig.java
//Synthetic comment -- index 116a46f..fbea3a5 100644

//Synthetic comment -- @@ -63,6 +63,7 @@
"logo.gif", "newrule-green.png"};

private String mConfigRoot;
private CaseRepository mCaseRepos;
private ResultRepository mResultRepos;
private PlanRepository mPlanRepos;
//Synthetic comment -- @@ -174,6 +175,13 @@
String planRoot = repositoryRoot + File.separator + planCfg;
String resRoot = repositoryRoot + File.separator + resCfg;

boolean validCase = true;
if (!validateDirectory(caseRoot)) {
validCase = new File(caseRoot).mkdirs();
//Synthetic comment -- @@ -189,12 +197,16 @@
if (!validateDirectory(planRoot)) {
validPlan = new File(planRoot).mkdirs();
}

mCaseRepos = new CaseRepository(caseRoot);
mResultRepos = new ResultRepository(resRoot);
mPlanRepos = new PlanRepository(planRoot);

        return validCase && validRes && validPlan;
}

/**
//Synthetic comment -- @@ -285,6 +297,15 @@
}

/**
* Get string attribute value.
*
* @param doc The document.
//Synthetic comment -- @@ -317,6 +338,29 @@
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

            Log.initLog(sConfig.getConfigRoot());
sConfig.loadRepositories();
} catch (Exception e) {
Log.e("Error while parsing cts config file", e);








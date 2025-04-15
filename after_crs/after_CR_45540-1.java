/*Prevent redundant "Test" postfix at creating test project.

Change-Id:I3e7c58467ecc82b308582c6dae286af19e72c2e0*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ApplicationInfoPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ApplicationInfoPage.java
//Synthetic comment -- index c832534..99d33b0 100644

//Synthetic comment -- @@ -781,6 +781,10 @@
if (applicationName == null) {
applicationName = ""; //$NON-NLS-1$
}
        if (applicationName.endsWith("Test")) { //$NON-NLS-1$
            return applicationName;
        }

if (applicationName.indexOf(' ') != -1) {
return applicationName + " Test"; //$NON-NLS-1$
} else {
//Synthetic comment -- @@ -793,9 +797,17 @@
projectName = ""; //$NON-NLS-1$
}
if (projectName.length() > 0 && Character.isUpperCase(projectName.charAt(0))) {
            if (projectName.endsWith("Test")) { //$NON-NLS-1$
                return projectName;
            } else {
                return projectName + "Test"; //$NON-NLS-1$
            }
} else {
            if (projectName.endsWith("-test")) { //$NON-NLS-1$
                return projectName;
            } else {
                return projectName + "-test"; //$NON-NLS-1$
            }
}
}

//Synthetic comment -- @@ -804,6 +816,10 @@
if (packagePath == null) {
packagePath = ""; //$NON-NLS-1$
}
        if (packagePath.endsWith(".test")) { //$NON-NLS-1$
            return packagePath;
        }

return packagePath + ".test"; //$NON-NLS-1$
}
}








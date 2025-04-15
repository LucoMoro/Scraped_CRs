/*SDK Manager: fix to suggest platform-tools install.

The way the updater currently works, it will only
generate the source.props of a new package based on the
attributes it knows from that package. That means mean
Tools r7 is updating tools, it will not add the proper
min-platform-tools-rev to the new Tools r8 package.

When "Update All" is selected, we try to do 2 new things:
- make sure to lool at local *existing* packages for
  potential missing dependencies, and suggest them for
  install.
- if a package doesn't have a min-platform-tools-rev set,
  suggest the higest revision available.

Change-Id:I76cdbc2818133429b2726d3127eedd7e65579a7e*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java
//Synthetic comment -- index 24316e4..c8f1e66 100755

//Synthetic comment -- @@ -182,6 +182,16 @@
return pkg instanceof ToolPackage;
}

/**
* The tool package executes tools/lib/post_tools_install[.bat|.sh]
* {@inheritDoc}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java
//Synthetic comment -- index 593f007..038b86a 100755

//Synthetic comment -- @@ -177,4 +177,22 @@
}
return "";
}
}








/*Add a 'removeInstrumentationArg' method to ddms testrunner.

Change-Id:I85ba17eefbc5550dc2712b28b5d870e233c85058*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/IRemoteAndroidTestRunner.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/IRemoteAndroidTestRunner.java
//Synthetic comment -- index b40f164..cd40527 100644

//Synthetic comment -- @@ -135,6 +135,13 @@
public void addInstrumentationArg(String name, String value);

/**
     * Removes a previously added argument.
     *
     * @param name the name of the instrumentation bundle argument to remove
     */
    public void removeInstrumentationArg(String name);

    /**
* Adds a boolean argument to include in instrumentation command.
* <p/>
* @see RemoteAndroidTestRunner#addInstrumentationArg








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunner.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunner.java
//Synthetic comment -- index 0d22886..c0ae309 100644

//Synthetic comment -- @@ -156,6 +156,16 @@
/**
* {@inheritDoc}
*/
    public void removeInstrumentationArg(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name argument cannot be null");
        }
        mArgMap.remove(name);
    }

    /**
     * {@inheritDoc}
     */
public void addBooleanArg(String name, boolean value) {
addInstrumentationArg(name, Boolean.toString(value));
}








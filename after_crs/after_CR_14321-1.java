/*Filter out new leak_memalign method in native heap UI

Change-Id:I39f1b7d726a0b5d26e736b404693183003ea391e*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/NativeAllocationInfo.java b/ddms/libs/ddmlib/src/com/android/ddmlib/NativeAllocationInfo.java
//Synthetic comment -- index 956b004..41d63b2 100644

//Synthetic comment -- @@ -50,6 +50,7 @@
sAllocFunctionFilter.add("chk_free"); //$NON-NLS-1$
sAllocFunctionFilter.add("chk_memalign"); //$NON-NLS-1$
sAllocFunctionFilter.add("Malloc"); //$NON-NLS-1$
        sAllocFunctionFilter.add("leak_memalign"); //$NON-NLS-1$
}

private final int mSize;
//Synthetic comment -- @@ -74,7 +75,7 @@
this.mIsZygoteChild = ((size & FLAG_ZYGOTE_CHILD) != 0);
this.mAllocations = allocations;
}

/**
* Adds a stack call address for this allocation.
* @param address The address to add.
//Synthetic comment -- @@ -82,7 +83,7 @@
void addStackCallAddress(long address) {
mStackCallAddresses.add(address);
}

/**
* Returns the total size of this allocation.
*/
//Synthetic comment -- @@ -104,7 +105,7 @@
public int getAllocationCount() {
return mAllocations;
}

/**
* Returns whether the stack call addresses have been resolved into
* {@link NativeStackCallInfo} objects.
//Synthetic comment -- @@ -120,7 +121,7 @@
public Long[] getStackCallAddresses() {
return mStackCallAddresses.toArray(new Long[mStackCallAddresses.size()]);
}

/**
* Sets the resolved stack call for this allocation.
* <p/>
//Synthetic comment -- @@ -149,7 +150,7 @@
if (mIsStackCallResolved) {
return mResolvedStackCall.toArray(new NativeStackCallInfo[mResolvedStackCall.size()]);
}

return null;
}

//Synthetic comment -- @@ -231,7 +232,7 @@
* Returns the first {@link NativeStackCallInfo} that is relevant.
* <p/>
* A relevant <code>NativeStackCallInfo</code> is a stack call that is not deep in the
     * lower level of the libc, but the actual method that performed the allocation.
* @return a <code>NativeStackCallInfo</code> or <code>null</code> if the stack call has not
* been processed from the raw addresses.
* @see #setResolvedStackCall(ArrayList)
//Synthetic comment -- @@ -260,7 +261,7 @@

return null;
}

/**
* Returns true if the method name is relevant.
* @param methodName the method name to test.
//Synthetic comment -- @@ -271,7 +272,7 @@
return false;
}
}

return true;
}
}








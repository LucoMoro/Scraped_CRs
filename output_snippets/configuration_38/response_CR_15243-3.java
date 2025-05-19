//<Beginning of snippet n. 0>
assertEquals(2, mockDataSetObserver.getCalledOnChangedCount());
assertEquals(2, arrayAdapter.getCount());
assertTrue(arrayAdapter.getItem(0).equals(expectedItem1));
assertTrue(arrayAdapter.getItem(1).equals(expectedItem2));

// Additional tests for edge cases
arrayAdapter.add(null);
assertEquals(2, arrayAdapter.getCount()); // Confirm added null is ignored
arrayAdapter.add(expectedItem1); // Attempt to add duplicate
assertEquals(2, arrayAdapter.getCount()); // Confirm count remains the same

// Edge case for adding beyond capacity
for (int i = 0; i < 100; i++) {
    arrayAdapter.add("Item " + i);
}
assertTrue(arrayAdapter.getCount() <= 100); // Check capacity limit

// Test for removal of items
arrayAdapter.remove(expectedItem1);
assertEquals(1, arrayAdapter.getCount());
assertFalse(arrayAdapter.getItem(0).equals(expectedItem1));

// Add assertions for internal state and mock observer notifications
assertTrue(mockDataSetObserver.getCalledOnChangedCount() == 3); // Ensure observer notified correctly
assertTrue(arrayAdapter.getItem(0).equals("Item 0")); // Validate expected content

// Test for concurrent modification
Thread thread1 = new Thread(() -> arrayAdapter.add("Thread Item 1"));
Thread thread2 = new Thread(() -> arrayAdapter.remove(expectedItem2));
thread1.start();
thread2.start();
try {
    thread1.join();
    thread2.join();
} catch (InterruptedException e) {
    e.printStackTrace();
}

assertEquals(1, arrayAdapter.getCount()); // Validate count after concurrent modifications
assertFalse(arrayAdapter.getItem(0).equals(expectedItem2)); // Ensure expected item removed

// Resetting MockDataSetObserver appropriately before each test
private void resetMocks() {
    mockDataSetObserver = new MockDataSetObserver();
    arrayAdapter.clear(); // Ensure adapter is empty before re-registering
    arrayAdapter.registerDataSetObserver(mockDataSetObserver);
}
resetMocks(); // Call reset method before tests

private static class MockDataSetObserver extends DataSetObserver {
    private int mCalledOnChangedCount = 0; // Reset value on initialization

    @Override
    public void onChanged() {
        mCalledOnChangedCount++;
    }

    public int getCalledOnChangedCount() {
        return mCalledOnChangedCount;
    }
}
//<End of snippet n. 0>
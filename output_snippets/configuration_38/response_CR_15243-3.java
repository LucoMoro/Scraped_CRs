//<Beginning of snippet n. 0>
assertEquals(2, mockDataSetObserver.getCalledOnChangedCount());
}

private static class MockDataSetObserver extends DataSetObserver {

private int mCalledOnChangedCount;

public void onChanged() {
    mCalledOnChangedCount++;
}

public int getCalledOnChangedCount() {
    return mCalledOnChangedCount;
}

// Test cases for add method
@Test
public void testAddMethod() {
    MockDataSetObserver observer = new MockDataSetObserver();
    ArrayAdapter<String> adapter = new ArrayAdapter<>();
    adapter.registerDataSetObserver(observer);

    adapter.add("Item 1");
    assertEquals(1, observer.getCalledOnChangedCount());
    assertEquals(1, adapter.getCount());

    adapter.add("Item 2");
    assertEquals(2, observer.getCalledOnChangedCount());
    assertEquals(2, adapter.getCount());
}

// Test cases for addAll method
@Test
public void testAddAllMethod() {
    MockDataSetObserver observer = new MockDataSetObserver();
    ArrayAdapter<String> adapter = new ArrayAdapter<>();
    adapter.registerDataSetObserver(observer);

    List<String> itemsToAdd = Arrays.asList("Item 1", "Item 2", "Item 3");
    adapter.addAll(itemsToAdd);
    assertEquals(1, observer.getCalledOnChangedCount()); // Only one notification for addAll
    assertEquals(3, adapter.getCount());
}

// Edge case tests
@Test
public void testAddNullValue() {
    MockDataSetObserver observer = new MockDataSetObserver();
    ArrayAdapter<String> adapter = new ArrayAdapter<>();
    adapter.registerDataSetObserver(observer);

    adapter.add(null);
    assertEquals(1, observer.getCalledOnChangedCount());
    assertEquals(1, adapter.getCount());
}

@Test
public void testAddToEmptyAdapter() {
    MockDataSetObserver observer = new MockDataSetObserver();
    ArrayAdapter<String> adapter = new ArrayAdapter<>();
    adapter.registerDataSetObserver(observer);

    adapter.add("New Item");
    assertEquals(1, observer.getCalledOnChangedCount());
    assertEquals(1, adapter.getCount());
}

@Test
public void testAddDuplicateItems() {
    MockDataSetObserver observer = new MockDataSetObserver();
    ArrayAdapter<String> adapter = new ArrayAdapter<>();
    adapter.registerDataSetObserver(observer);

    adapter.add("Item 1");
    adapter.add("Item 1"); // Adding duplicate
    assertEquals(2, observer.getCalledOnChangedCount());
    assertEquals(2, adapter.getCount());
}
//<End of snippet n. 0>
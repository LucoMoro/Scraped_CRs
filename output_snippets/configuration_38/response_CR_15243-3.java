<<Beginning of snippet n. 0>>
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
}

// Test cases for add method
@Test
public void testAdd() {
    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
    MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
    adapter.registerDataSetObserver(mockDataSetObserver);

    adapter.add("Item 1");
    assertEquals(1, adapter.getCount());
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());

    adapter.add("Item 2");
    assertEquals(2, adapter.getCount());
    assertEquals(2, mockDataSetObserver.getCalledOnChangedCount());

    adapter.add(null);
    assertEquals(3, adapter.getCount());
    assertEquals(3, mockDataSetObserver.getCalledOnChangedCount());

    adapter.add("Item 1");
    assertEquals(4, adapter.getCount());
    assertEquals(4, mockDataSetObserver.getCalledOnChangedCount());
}

// Test cases for addAll method
@Test
public void testAddAll() {
    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
    MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
    adapter.registerDataSetObserver(mockDataSetObserver);

    List<String> items = new ArrayList<>();
    items.add("Item 1");
    items.add("Item 2");
    adapter.addAll(items);
    assertEquals(2, adapter.getCount());
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());

    items.clear();
    adapter.addAll(Arrays.asList("Item 3", "Item 4", null));
    assertEquals(5, adapter.getCount());
    assertEquals(2, mockDataSetObserver.getCalledOnChangedCount());

    items.add("Item 1");
    items.add("Item 3");
    adapter.addAll(items);
    assertEquals(7, adapter.getCount());
    assertEquals(3, mockDataSetObserver.getCalledOnChangedCount());
}
<<End of snippet n. 0>>
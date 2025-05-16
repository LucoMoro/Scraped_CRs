//<Beginning of snippet n. 0>
assertEquals(2, mockDataSetObserver.getCalledOnChangedCount());
}

private static class MockDataSetObserver extends DataSetObserver {

private int mCalledOnChangedCount = 0;

@Override
public void onChanged() {
    super.onChanged();
    mCalledOnChangedCount++;
}

public int getCalledOnChangedCount() {
    return mCalledOnChangedCount;
}
}
// Test cases for add method
@Test
public void testAdd_NotifiesObserver() {
    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
    MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
    adapter.registerDataSetObserver(mockDataSetObserver);
    
    adapter.add("Test Item");
    
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
    assertEquals(1, adapter.getCount());
    assertEquals("Test Item", adapter.getItem(0));
}

@Test
public void testAddWithNullValue() {
    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
    MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
    adapter.registerDataSetObserver(mockDataSetObserver);
    
    adapter.add(null);
    
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
    assertEquals(1, adapter.getCount());
    assertNull(adapter.getItem(0));
}

// Test cases for addAll method
@Test
public void testAddAll_NotifiesObserver() {
    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
    MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
    adapter.registerDataSetObserver(mockDataSetObserver);
    
    List<String> items = Arrays.asList("Item 1", "Item 2");
    adapter.addAll(items);
    
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
    assertEquals(2, adapter.getCount());
    assertEquals("Item 1", adapter.getItem(0));
    assertEquals("Item 2", adapter.getItem(1));
}

@Test
public void testAddAllWithEmptyCollection() {
    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
    MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
    adapter.registerDataSetObserver(mockDataSetObserver);
    
    adapter.addAll(Collections.emptyList());
    
    assertEquals(0, mockDataSetObserver.getCalledOnChangedCount());
    assertEquals(0, adapter.getCount());
}

@Test
public void testAddAllWithNullValue() {
    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
    MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
    adapter.registerDataSetObserver(mockDataSetObserver);
    
    List<String> items = Arrays.asList("Item 1", null, "Item 2");
    adapter.addAll(items);
    
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
    assertEquals(3, adapter.getCount());
    assertEquals("Item 1", adapter.getItem(0));
    assertNull(adapter.getItem(1));
    assertEquals("Item 2", adapter.getItem(2));
}
//<End of snippet n. 0>
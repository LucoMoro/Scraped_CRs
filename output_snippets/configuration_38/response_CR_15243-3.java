<<Beginning of snippet n. 0>>
assertEquals(2, mockDataSetObserver.getCalledOnChangedCount());
assertEquals(3, arrayAdapter.getCount());
assertTrue(arrayAdapter.getDataSet().contains("item1"));
assertTrue(arrayAdapter.getDataSet().contains("item2"));
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

// Test cases
private MockDataSetObserver mockDataSetObserver;
private ArrayAdapter<String> arrayAdapter;

@Before
public void setup() {
    mockDataSetObserver = new MockDataSetObserver();
    arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, new ArrayList<>());
    arrayAdapter.registerDataSetObserver(mockDataSetObserver);
}

@Test
public void testAddSingleItem() {
    arrayAdapter.add("item1");
    assertEquals(1, arrayAdapter.getCount());
    assertTrue(arrayAdapter.getDataSet().contains("item1"));
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
}

@Test
public void testAddMultipleItems() {
    arrayAdapter.addAll("item1", "item2", "item3");
    assertEquals(3, arrayAdapter.getCount());
    assertTrue(arrayAdapter.getDataSet().contains("item1"));
    assertTrue(arrayAdapter.getDataSet().contains("item2"));
    assertTrue(arrayAdapter.getDataSet().contains("item3"));
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
}

@Test
public void testAddNull() {
    arrayAdapter.add(null);
    assertEquals(1, arrayAdapter.getCount());
    assertTrue(arrayAdapter.getDataSet().contains(null));
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
}

@Test
public void testAddDuplicateEntry() {
    arrayAdapter.add("item1");
    arrayAdapter.add("item1");
    assertEquals(2, arrayAdapter.getCount());
    assertEquals(1, Collections.frequency(arrayAdapter.getDataSet(), "item1"));
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
}

@Test
public void testAddItemWhenFull() {
    for (int i = 0; i < arrayAdapter.getMaxCapacity(); i++) {
        arrayAdapter.add("item" + i);
    }
    int currentCount = arrayAdapter.getCount();
    arrayAdapter.add("itemOverflow");
    assertEquals(currentCount, arrayAdapter.getCount());
    assertFalse(arrayAdapter.getDataSet().contains("itemOverflow"));
}

@Test
public void testRemoveItem() {
    arrayAdapter.add("item1");
    arrayAdapter.remove("item1");
    assertEquals(0, arrayAdapter.getCount());
    assertFalse(arrayAdapter.getDataSet().contains("item1"));
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
}

@After
public void cleanup() {
    arrayAdapter.clear();
    mockDataSetObserver = new MockDataSetObserver();
}
<<End of snippet n. 0>>
package assignment1;

import assignment1.items.StoreItem;

public class ItemList {
    private StoreItem[] items;
    private int size;

    public ItemList() {
        items = new StoreItem[10];
    }

    public int getSize() {
        return size;
    }

    public StoreItem[] getAllItems() {
        StoreItem[] validItems = new StoreItem[size];
        for (int i = 0; i < size; i++) {
            validItems[i] = items[i];  // Copy non-null items
        }
        return validItems;
    }

    public void addItem(StoreItem item) {
        if (size == items.length) {
            resize();
        }
        items[size] = item;
        size++;
    }

    public StoreItem removeItem(StoreItem item) {
        for (int i = 0; i < size; i++) {
            if (items[i].equals(item)) {
                StoreItem removedItem = items[i];
                shiftItemsLeft(i);
                size--;
                return removedItem;
            }
        }
        return null;
    }

    public StoreItem[] findEqualItems(StoreItem item) {
        StoreItem[] equalItems = new StoreItem[size];
        boolean hasEqualItems = false;
        for (int i = 0; i < size; i++) {
            if (items[i].equals(item)) {
                hasEqualItems = true;
                equalItems[i] = items[i];
            }
        }
        if (!hasEqualItems) {
            return new StoreItem[0];
        }
        // Returns array including null elements, representing the non-equal items
        return equalItems;
    }

    private void shiftItemsLeft(int index) {
        for (int i = index; i < size - 1; i++) {
            items[i] = items[i + 1];
        }
        items[size - 1] = null;
    }

    private void resize() {
        StoreItem[] resizedArray = new StoreItem[items.length * 2];
        for (int i = 0; i < size; i++) {
            resizedArray[i] = items[i];
        }
        items = resizedArray;
    }
}

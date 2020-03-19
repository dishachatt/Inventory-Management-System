package com.disha.restapi;

import java.util.HashMap;
import java.util.ArrayList;
import java.time.*;

class InventoryItemCollection {

    private HashMap<Integer, InventoryItem> inventoryItems = null;
    private ArrayList<InventoryItem> inventoryItemList = null;

    enum ItemActivity {
        ADDED,
        REMOVED,
        INVALID
    }

    public class ItemAndActivity {

        private int itemId = -1;
        private LocalDateTime activityDate = null;
        private ItemActivity activityType = ItemActivity.INVALID;

        public ItemAndActivity(int itemId, LocalDateTime activityDate, ItemActivity activityType) {
            this.itemId = itemId;
            this.activityDate = activityDate;
            this.activityType = activityType;
        }

        public String toString() {
            StringBuilder json = new StringBuilder();
            json.append("{ \"id\" : \"").append(this.itemId).append("\", ");
            json.append("\"date\" : \"").append(this.activityDate.toString()).append("\", ");
            json.append("\"type\" : \"").append(this.activityType.toString()).append("\"");
            json.append(" }");
            return json.toString();
        }

    }

    public InventoryItemCollection() {
        inventoryItems = new HashMap<Integer, InventoryItem>();
        inventoryItemList = new ArrayList<InventoryItem>();
    }

    public void addItem(String name, String description) {
        InventoryItem item = new InventoryItem(name, description);
        inventoryItems.put(item.getId(), item);
        inventoryItemList.add(item);
    }

    public boolean removeItem(int id) {
        if (inventoryItems.containsKey(id)) {
            inventoryItems.get(id).markAsRemoved();
            return true;
        }
        else {
            return false;
        }
    }

    public InventoryItem getItem(int id) {
        if (inventoryItems.containsKey(id)) {
            InventoryItem item = inventoryItems.get(id);
            return item.isActive() ? item : null;
        }
        else {
            return null;
        }
    }

    public ArrayList<ItemAndActivity> getItemActivity(LocalDateTime start, LocalDateTime end) {
        ArrayList<ItemAndActivity> result = new ArrayList<ItemAndActivity>();

        for (InventoryItem item : inventoryItemList) {
            LocalDateTime added = item.addedWhen();
            LocalDateTime removed = item.removedWhen();

            if (added.compareTo(start) >= 0 && added.compareTo(end) <= 0) {
                result.add(new ItemAndActivity(item.getId(), added, ItemActivity.ADDED));
            }

            if (removed != null && removed.compareTo(start) >= 0 && removed.compareTo(end) <= 0) {
                result.add(new ItemAndActivity(item.getId(), removed, ItemActivity.REMOVED));
            }
        }

        return result;
    }
}
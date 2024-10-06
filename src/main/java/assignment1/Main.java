package assignment1;

import assignment1.items.*;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ItemList itemList = new ItemList();
        FizzWiz fizzWiz1 = new FizzWiz(2.5, 10, 1);
        FizzWiz fizzWiz2 = new FizzWiz(2.5, 10, 1);
        FizzWiz fizzWiz3 = new FizzWiz(2.5, 10, 1);
        FizzWiz fizzWiz4 = new FizzWiz(2.5, 10, 1);
        FizzWiz fizzWiz5 = new FizzWiz(2.5, 10, 6);
        SnoozeJuice snooze1 = new SnoozeJuice(2.5, 10, 3, 65);
        SnoozeJuice snooze2 = new SnoozeJuice(2.5, 10, 5, 65);
        itemList.addItem(fizzWiz1);
        itemList.addItem(new Snack(2.5, 10, "Jeez", MyDate.today()));
        itemList.addItem(snooze1);
        itemList.addItem(fizzWiz2);
        itemList.addItem(fizzWiz3);
//        itemList.addItem(fizzWiz4);
//        itemList.addItem(snooze2);
//        itemList.addItem(fizzWiz5);
        Store store = new Store(itemList);
        Drink[] drinks = {};
        store.refillDrinkInventory(drinks);
        // print the store's inventory
        System.out.println(Arrays.toString(store.getItems().getAllItems()));
        ItemList customerItems = new ItemList();
        customerItems.addItem(new Snack(2.5, 10, "Jeez", MyDate.today()));
        store.completeSale(customerItems);
        System.out.println(Arrays.toString(store.getItems().getAllItems()));
    }
}
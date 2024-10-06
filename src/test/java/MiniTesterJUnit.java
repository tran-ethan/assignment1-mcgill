import assignment1.ItemList;
import assignment1.Store;
import assignment1.StoreItemTest;
import assignment1.items.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MiniTesterJUnit {

    // CALCULATE IF THE ASSIGNMENT SUBMISSION HAS PASSED THE MASTERY LEVELS

    private static int proficiencyPassed = 0;
    private static int approachMasteryPassed = 0;

    private static int fullMasteryPassed = 0;

    @Test
    @DisplayName("PROFICIENCY LEVEL PASSING STATUS")
    @Order(100)
    void testProficiencyLevelPassed() {
        System.out.println("Proficiency level passed: " + proficiencyPassed);
    }

    @Test
    @DisplayName("APPROACHING MASTERY LEVEL PASSING STATUS")
    @Order(101)
    void testApproachingMasteryLevelPassed() {
        System.out.println("Approaching Mastery level passed: " + approachMasteryPassed);
    }

    @Test
    @DisplayName("FULL MASTERY LEVEL PASSING STATUS")
    @Order(102)
    void testFullMasteryLevelPassed() {
        System.out.println("Full Mastery level passed: " + fullMasteryPassed);
    }

    // Also before at each test, you should set the static variable MAX_PACK_SIZE in Drink to 6 and BUZZY_HAPPINESS_BOOST to 1.
    // This is to ensure that across all assignment submissions the values are the same.
    @BeforeEach
    @Tag("score:1")
    @DisplayName("Checking and setting static variables to ensure consistency across submissions")
    void setUpStaticVariables() throws IllegalAccessException {
        try {
            Field maxPackSize = Drink.class.getDeclaredField("MAX_PACK_SIZE");
            Field buzzyHappinessBoost = Drink.class.getDeclaredField("BUZZY_HAPPINESS_BOOST");
            maxPackSize.setAccessible(true);
            buzzyHappinessBoost.setAccessible(true);
            maxPackSize.set(null, 6);
            buzzyHappinessBoost.set(null, 1);

            // also fix the summer and winter months in MyDate

            Field summerMonths = MyDate.class.getDeclaredField("SUMMER_MONTHS");
            Field winterMonths = MyDate.class.getDeclaredField("WINTER_MONTHS");
            summerMonths.setAccessible(true);
            winterMonths.setAccessible(true);
            summerMonths.set(null, new int[]{6, 7, 8});
            winterMonths.set(null, new int[]{1, 2, 12});

            Field hotColdBoost = SnoozeJuice.class.getDeclaredField("HOT_COLD_BOOST");
            hotColdBoost.setAccessible(true);
            hotColdBoost.set(null, 2);
        } catch (NoSuchFieldException e) {
            fail("MAX_PACK_SIZE and BUZZY_HAPPINESS_BOOST should be declared in Drink class and SUMMER_MONTHS and WINTER_MONTHS should be declared in MyDate class and HOT_COLD_BOOST should be declared in SnoozeJuice class");
        }

    }

    @BeforeEach
    @Tag("score:0")
    @DisplayName("Import tester")
    public void testImports() {
        checkForForbiddenInstance(new MyDate(1, 1, 1));
        checkForForbiddenInstance(new Snack(1, 1, "type", new MyDate(1, 1, 1)));
        checkForForbiddenInstance(new StoreItemTest(1, 1));
        checkForForbiddenInstance(new ItemList());
        checkForForbiddenInstance(new Store(new ItemList()));
        checkForForbiddenInstance(new Drink(1, 1, 1, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        });
        checkForForbiddenInstance(new FizzWiz(1, 1, 1));
        checkForForbiddenInstance(new SnoozeJuice(1, 1, 1, 25));


    }

    private void checkForForbiddenInstance(Object object) {
        Class<?> clazz = object.getClass();

        // Check fields for forbidden instances
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            checkFieldForForbiddenInstance(field);
        }

        // Check methods for forbidden instances
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (!Modifier.isStatic(method.getModifiers())) {
                // Exclude static methods
                checkMethodForForbiddenInstance(method);
            }
        }
    }

    private void checkFieldForForbiddenInstance(Field field) {
        Class<?> fieldType = field.getType();
        if (isForbiddenType(fieldType)) {
            fail(fieldType.getName() + " instance found in the class " + field.getDeclaringClass().getName());
        }
    }

    private void checkMethodForForbiddenInstance(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> paramType : parameterTypes) {
            if (isForbiddenType(paramType)) {
                fail(paramType.getName() + " instance found in the class " + method.getDeclaringClass().getName());
            }
        }
    }

    private boolean isForbiddenType(Class<?> type) {
        return type == java.util.ArrayList.class || type == java.util.LinkedList.class;
    }

    @Test
    @Tag("score:0")
    @Order(1)
    @DisplayName("Test if StoreItem and Drink are abstract classes")
    void testAbstractClasses() {
        // Test if Unit and MilitaryUnit are abstract
        assertTrue(Modifier.isAbstract(StoreItem.class.getModifiers()), "StoreItem should be an abstract class..");
        assertTrue(Modifier.isAbstract(Drink.class.getModifiers()), "Drink should be an abstract class..");
    }

    @Test
    @Tag("score:0")
    @Order(2)
    @DisplayName("Testing inheritance relationships")
    void testInheritance() {
        boolean allInheritanceCorrect = true;
        allInheritanceCorrect &= isSubclass(FizzWiz.class, Drink.class);
        allInheritanceCorrect &= isSubclass(SnoozeJuice.class, Drink.class);
        allInheritanceCorrect &= isSubclass(Drink.class, StoreItem.class);
        allInheritanceCorrect &= isSubclass(Snack.class, StoreItem.class);

        assertTrue(allInheritanceCorrect, "Not all inheritances are correct..");
    }

    private boolean isSubclass(Class<?> childClass, Class<?> parentClass) {
        if (!parentClass.isAssignableFrom(childClass)) {
            System.out.println(childClass.getSimpleName() + " should be a subclass of " + parentClass.getSimpleName());
            return false;
        }
        return true;
    }

    // PROFICIENCY LEVEL TESTS

    // STORE ITEM CLASS (USES STOREITEMTEST TEST HELPER CLASS TO RUN)

    @Test
    @Tag("score:1")
    @Order(3)
    @DisplayName("PROFICIENCY: Test StoreItem constructor")
    void proficiencyTestStoreItemConstructor() {
        StoreItemTest storeItem = new StoreItemTest(5.0, 1);

        // check that double price is 5.0
        // check that int happinessIndex is 1
        Field[] declaredFields = StoreItem.class.getDeclaredFields();
        boolean priceInitializedCorrectly = false;
        boolean happinessIndexInitializedCorrectly = false;
        for (Field field : declaredFields) {
            if (field.getType() == double.class) {
                field.setAccessible(true);
                double doubleValue = 0;
                try {
                    doubleValue = (double) field.get(storeItem);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (doubleValue == 5.0) {
                    priceInitializedCorrectly = true;
                }
            } else if (field.getType() == int.class) {
                field.setAccessible(true);
                int intValue = 0;
                try {
                    intValue = (int) field.get(storeItem);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (intValue == 1) {
                    happinessIndexInitializedCorrectly = true;
                }
            }
        }
        assertTrue(priceInitializedCorrectly, "price is not initialized correctly in the StoreItem constructor");
        assertTrue(happinessIndexInitializedCorrectly, "happinessIndex is not initialized correctly in the StoreItem constructor");
        proficiencyPassed++;

    }

    @Test
    @Tag("score:1")
    @Order(4)
    @DisplayName("PROFICIENCY: Test StoreItem getPrice method")
    void proficiencyTestStoreItemGetPrice() {

        StoreItemTest storeItem = new StoreItemTest(5.0, 1);
        assertEquals(5.0, storeItem.getPrice(), "getPrice should return 5.0");
        proficiencyPassed++;

    }

    @Test
    @Tag("score:1")
    @Order(5)
    @DisplayName("PROFICIENCY: Test StoreItem getHappinessIndex method")
    void proficiencyTestStoreItemGetHappinessIndex() {

        StoreItemTest storeItem = new StoreItemTest(5.0, 1);
        assertEquals(1, storeItem.getHappinessIndex(), "getHappinessIndex should return 1");
        proficiencyPassed++;

    }

    @Test
    @Tag("score:1")
    @Order(6)
    @DisplayName("PROFICIENCY: Test StoreItem equals method")
    void proficiencyTestStoreItemEquals() {

        StoreItemTest storeItem1 = new StoreItemTest(5.0, 1);
        StoreItemTest storeItem2 = new StoreItemTest(5.0, 1);
        StoreItemTest storeItem3 = new StoreItemTest(5.0, 2);
        StoreItemTest storeItem4 = new StoreItemTest(6.0, 1);
        assertTrue(storeItem1.equals(storeItem2), "equals method should return true");
        assertFalse(storeItem1.equals(storeItem3), "equals method should return false");
        assertFalse(storeItem1.equals(storeItem4), "equals method should return false");
        proficiencyPassed++;

    }

    // DRINK CLASS

    @Test
    @Tag("score:1")
    @Order(7)
    @DisplayName("PROFICIENCY: Test Drink constructor for initialization of numBottles and buzziness")
    void proficiencyTestDrinkConstructor1() {

        Drink drink = new Drink(5.0, 1, 4, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };

        Field[] declaredFields = Drink.class.getDeclaredFields();

        boolean numBottlesInitializedCorrectly = false;

        for (Field field : declaredFields) {
            if (field.getType() == int.class) {
                field.setAccessible(true);
                int intValue = 0;
                try {
                    intValue = (int) field.get(drink);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (intValue == 4) {
                    numBottlesInitializedCorrectly = true;
                }
            } else if (field.getType() == boolean.class) {
                field.setAccessible(true);
                boolean booleanValue = false;
                try {
                    booleanValue = (boolean) field.get(drink);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (booleanValue) {
                    assertTrue(booleanValue, "buzziness is not initialized correctly in the Drink constructor");
                }
            }
        }
        assertTrue(numBottlesInitializedCorrectly, "numBottles is not initialized correctly in the Drink constructor");
        proficiencyPassed++;

    }


    @Test
    @Tag("score:1")
    @Order(9)
    @DisplayName("PROFICIENCY: Test getNumOfBottles")
    void proficiencyTestGetNumOfBottles() {
        Drink drink = new Drink(5.0, 1, 4, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        assertTrue(drink.getNumOfBottles() == 4, "getNumOfBottles should return 4");
        proficiencyPassed++;

    }

    @Test
    @Tag("score:1")
    @Order(11)
    @DisplayName("PROFICIENCY: Test equals method should compare object type and buzziness")
    void proficiencyTestEqualsMethod() {
        Drink drink1 = new Drink(5.0, 1, 4, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drink2 = new Drink(5.0, 1, 4, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drink3 = new Drink(5.0, 1, 4, false) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Snack snack = new Snack(5.0, 1, "type", new MyDate(1, 1, 1));
        assertTrue(drink1.equals(drink2), "equals method should return true");
        assertFalse(drink1.equals(drink3), "equals method should return false");
        assertFalse(drink1.equals(snack), "equals method should return false when comparing different types");
        proficiencyPassed++;

    }


    @Test
    @Tag("score:1")
    @Order(13)
    @DisplayName("PROFICIENCY: Test equals method edgecase should not compare numBottles")
    void proficiencyTestEqualsMethodNumBottles() {
        Drink drink1 = new Drink(3.0, 1, 4, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drink2 = new Drink(3.0, 1, 4, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drink3 = new Drink(3.0, 1, 5, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        assertTrue(drink1.equals(drink2), "equals method should return true");
        assertTrue(drink1.equals(drink3), "equals method should return true when number of bottles are different");
        proficiencyPassed++;

    }

    @Test
    @Tag("score:1")
    @Order(14)
    @DisplayName("PROFICIENCY: Test Snack constructor initializes type and date correctly")
    void proficiencyTestSnackConstructor() {
        MyDate date = new MyDate(1, 1, 1);
        Snack snack = new Snack(5.0, 1, "type", date);

        Field[] declaredFields = Snack.class.getDeclaredFields();

        boolean typeInitializedCorrectly = false;
        boolean dateInitializedCorrectly = false;

        for (Field field : declaredFields) {
            if (field.getType() == String.class) {
                field.setAccessible(true);
                String type = null;
                try {
                    type = (String) field.get(snack);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (type.equals("type")) {
                    typeInitializedCorrectly = true;
                }
            } else if (field.getType() == MyDate.class) {
                field.setAccessible(true);
                MyDate myDate = null;
                try {
                    myDate = (MyDate) field.get(snack);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (myDate.equals(date)) {
                    dateInitializedCorrectly = true;
                }
            }
        }
        assertTrue(typeInitializedCorrectly, "type is not initialized correctly in the Snack constructor");
        assertTrue(dateInitializedCorrectly, "date is not initialized correctly in the Snack constructor");
        proficiencyPassed++;
    }

    @Test
    @Tag("score:1")
    @Order(15)
    @DisplayName("PROFICIENCY: Test equals method for Snack (should also check for expiry date)")
    void proficiencyTestSnackEquals() {
        MyDate date = new MyDate(1, 1, 1);
        Snack snack1 = new Snack(5.0, 1, "type", date);
        Snack snack2 = new Snack(5.0, 1, "type", date);
        Snack snack3 = new Snack(5.0, 1, "type", new MyDate(1, 1, 2));
        Snack snack4 = new Snack(5.0, 1, "type2", date);
        assertTrue(snack1.equals(snack2), "equals method should return true when all fields are the same");
        assertFalse(snack1.equals(snack3), "equals method should return false when dates are different");
        assertFalse(snack1.equals(snack4), "equals method should return false when types are different");
        proficiencyPassed++;
    }


    @Test
    @Tag("score:1")
    @Order(16)
    @DisplayName("PROFICIENCY: Test MyDate constructor")
    void proficiencyTestMyDateConstructor() {
        MyDate date = new MyDate(1, 2, 3);

        Field[] declaredFields = MyDate.class.getDeclaredFields();

        boolean dayInitializedCorrectly = false;
        boolean monthInitializedCorrectly = false;
        boolean yearInitializedCorrectly = false;

        for (Field field : declaredFields) {
            if (field.getType() == int.class) {
                field.setAccessible(true);
                int intValue = 0;
                try {
                    intValue = (int) field.get(date);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (intValue == 1) {
                    dayInitializedCorrectly = true;
                } else if (intValue == 2) {
                    monthInitializedCorrectly = true;
                } else if (intValue == 3) {
                    yearInitializedCorrectly = true;
                }
            }
        }
        assertTrue(dayInitializedCorrectly, "day is not initialized correctly in the MyDate constructor");
        assertTrue(monthInitializedCorrectly, "month is not initialized correctly in the MyDate constructor");
        assertTrue(yearInitializedCorrectly, "year is not initialized correctly in the MyDate constructor");
        proficiencyPassed++;

    }

    @Test
    @Tag("score:1")
    @Order(18)
    @DisplayName("PROFICIENCY: Test getDay, getMonth, getYear methods in MyDate class")
    void proficiencyTestGetDate() {

        MyDate date = new MyDate(1, 2, 3);
        assertEquals(1, date.getDay(), "getDay should return 1");
        assertEquals(2, date.getMonth(), "getMonth should return 2");
        assertEquals(3, date.getYear(), "getYear should return 3");
        proficiencyPassed++;

    }

    // ITEMLIST CLASS

    @Test
    @Tag("score:1")
    @Order(20)
    @DisplayName("PROFICIENCY: Test ItemList constructor initializes array")
    void proficiencyTestItemListConstructor() {

        Field[] declaredFields = ItemList.class.getDeclaredFields();
        boolean arrayInitializedCorrectly = false;

        for (Field field : declaredFields) {
            if (field.getType() == StoreItem[].class) {
                arrayInitializedCorrectly = true;
                break;
            }
        }
        assertTrue(arrayInitializedCorrectly, "items array is not initialized correctly in the ItemList constructor");
        proficiencyPassed++;

    }

    @Test
    @Tag("score:1")
    @Order(22)
    @DisplayName("PROFICIENCY: Test getAllItems() method in ItemList class")
    void proficiencyTestGetAllItemsMethod() {
        ItemList itemList = new ItemList();
        Drink drink1 = new Drink(5.0, 1, 4, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drink2 = new Drink(10.0, 1, 4, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drink3 = new Drink(5.0, 2, 4, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };

        itemList.addItem(drink1);
        itemList.addItem(drink2);
        itemList.addItem(drink3);

        StoreItem[] allItems = itemList.getAllItems();
        assertEquals(3, allItems.length, "getAllItems should return an array of size 3");
        assertEquals(drink1, allItems[0], "First element should be drink1");
        assertEquals(drink2, allItems[1], "Second element should be drink2");
        assertEquals(drink3, allItems[2], "Third element should be drink3");
        proficiencyPassed++;

    }

    @Test
    @Tag("score:1")
    @Order(23)
    @DisplayName("PROFICIENCY: Test FizzWiz constructor")
    void proficiencyTestFizzWizConstructor() throws IllegalAccessException {
        FizzWiz f = new FizzWiz(3.0, 5, 3);
        // use getters to check if the values are initialized correctly
        assertEquals(3, f.getNumOfBottles(), "numBottles should be 3");
        assertEquals(6, f.getHappinessIndex(), "happinessIndex should be 6");
        assertEquals(3.0, f.getPrice(), "price should be 3.0");
        proficiencyPassed++;
    }

    @Test
    @Tag("score:1")
    @Order(24)
    @DisplayName("PROFICIENCY: Test FizzWiz getPortion method returns null when there are not enough bottles")
    void proficiencyTestFizzWizGetPortion() {
        FizzWiz f = new FizzWiz(3.0, 5, 3);
        assertNull(f.getPortion(4), "getPortion should return null when there are not enough bottles");
        proficiencyPassed++;
    }


    @Test
    @Tag("score:1")
    @Order(26)
    @DisplayName("PROFICIENCY: Test SnoozeJuice constructor")
    void proficiencyTestSnoozeJuiceConstructor() {
        SnoozeJuice s = new SnoozeJuice(3.0, 5, 3, 25);
        // use getters to check if the values are initialized correctly
        assertEquals(3, s.getNumOfBottles(), "numBottles should be 3");
        assertEquals(5, s.getHappinessIndex(), "happinessIndex should be 5");
        assertEquals(3.0, s.getPrice(), "price should be 3.0");

        boolean temperatureInitializedCorrectly = false;
        // check that temperature is initialized correctly
        Field[] declaredFields = SnoozeJuice.class.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getType() == double.class) {
                field.setAccessible(true);
                double doubleValue = 0;
                try {
                    doubleValue = (double) field.get(s);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (doubleValue == 25) {
                    temperatureInitializedCorrectly = true;
                }
            }
        }
        assertTrue(temperatureInitializedCorrectly, "temperature is not initialized correctly in the SnoozeJuice constructor");
        proficiencyPassed++;
    }


    @Test
    @Tag("score:1")
    @Order(28)
    @DisplayName("PROFICIENCY: Test equals method in SnoozeJuice")
    void proficiencyTestSnoozeJuiceEquals1() {
        SnoozeJuice s1 = new SnoozeJuice(3.0, 5, 3, 25);
        SnoozeJuice s2 = new SnoozeJuice(3.0, 3, 3, 25);
        SnoozeJuice s3 = new SnoozeJuice(3.0, 5, 3, 20);
        FizzWiz f = new FizzWiz(3.0, 5, 3);

        assertFalse(s1.equals(s2), "equals method should return false when happinessIndex is different");
        assertFalse(s1.equals(s3), "equals method should return false when price is different");
        assertFalse(s1.equals(f), "equals method should return false when comparing different classes");
        proficiencyPassed++;

    }


    // STORE CLASS

    @Test
    @Tag("score:1")
    @Order(30)
    @DisplayName("PROFICIENCY: Test Store constructor initializes itemList correctly")
    void proficiencyTestStoreConstructor() {
        ItemList itemList = new ItemList();
        Store store = new Store(itemList);

        Field[] declaredFields = Store.class.getDeclaredFields();

        boolean itemListInitializedCorrectly1 = false; // check that it exists

        for (Field field : declaredFields) {
            if (field.getType() == ItemList.class) {
                field.setAccessible(true);
                ItemList itemListField = null;
                try {
                    itemListField = (ItemList) field.get(store);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (itemListField.equals(itemList)) {
                    itemListInitializedCorrectly1 = true;
                }
            }
        }
        assertTrue(itemListInitializedCorrectly1, "itemList is not initialized correctly in the Store constructor");
        proficiencyPassed++;
    }


    @Test
    @Tag("score:1")
    @Order(32)
    @DisplayName("PROFICIENCY: Test getItems method in Store class")
    void proficiencyTestGetItemsMethod() {
        ItemList itemList = new ItemList();
        Store store = new Store(itemList);

        Field[] declaredFields = Store.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == ItemList.class) {
                field.setAccessible(true);
                ItemList itemListField = null;
                try {
                    itemListField = (ItemList) field.get(store);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                assertEquals(itemList, itemListField, "getItems should return the ItemList");
            }
        }
        proficiencyPassed++;
    }

    // APPROACHING MASTERY LEVEL TESTS

    @Test
    @Tag("score:1")
    @Order(33)
    @DisplayName("APPROACHING MASTERY: Test getHappinessIndex in Drink class")
    void approachMasteryTestGetHappinessIndex() {
        Drink drink1 = new Drink(5.0, 1, 4, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drink2 = new Drink(5.0, 1, 4, false) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        assertEquals(2, drink1.getHappinessIndex(), "getHappinessIndex should return 2");
        assertEquals(1, drink2.getHappinessIndex(), "getHappinessIndex should return 1");
        approachMasteryPassed++;

    }

    @Test
    @Tag("score:1")
    @Order(34)
    @DisplayName("APPROACHING MASTERY: Test combine method in Drink class where drinks are not equal")
    void approachMasteryTestCombineMethod1() {
        Drink drink1 = new Drink(5.0, 1, 4, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drink2 = new Drink(5.0, 2, 3, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drink3 = new Drink(10.0, 1, 4, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };

        assertFalse(drink1.combine(drink2), "combine should return false when happiness indexes are not equal");
        assertFalse(drink1.combine(drink3), "combine should return false when prices are not equal");
        approachMasteryPassed++;
    }


    // SNOOZEJUICE CLASS

    @Test
    @Tag("score:1")
    @Order(37)
    @DisplayName("APPROACHING MASTERY: Test getHappinessIndex in SnoozeJuice class for extreme temperatures should return 0")
    void approachMasteryTestSnoozeJuiceGetHappinessIndex() {
        SnoozeJuice s1 = new SnoozeJuice(3.0, 5, 3, 3);
        SnoozeJuice s2 = new SnoozeJuice(3.0, 5, 3, 66);
        // check that getHappinessIndex returns 0 when temperature is less than 4 or greater than 65
        assertEquals(0, s1.getHappinessIndex(), "getHappinessIndex should return 0 when temperature is less than 4");
        assertEquals(0, s2.getHappinessIndex(), "getHappinessIndex should return 0 when temperature is greater than 65");
        approachMasteryPassed++;

    }


    @Test
    @Tag("score:1")
    @Order(40)
    @DisplayName("APPROACHING MASTERY: Test getPortion in SnoozeJuice class returns drink with number of bottles matching input portion and updates numBottles in class object")
    void approachMasteryTestSnoozeJuiceGetPortion1() {
        SnoozeJuice s = new SnoozeJuice(3.0, 5, 5, 25);
        Drink drink = s.getPortion(3);
        assertEquals(3, drink.getNumOfBottles(), "getNumOfBottles should return 3");
        assertEquals(2, s.getNumOfBottles(), "getNumOfBottles should return 2 in class object");
        approachMasteryPassed++;

    }

    @Test
    @Tag("score:1")
    @Order(54)
    @DisplayName("APPROACHING MASTERY: Check that isExpired() returns true when the expiration date has passed based on the current date")
    public void approachMasteryTestSnackisExpired1() {
        Snack snack =new Snack(10,6,"Chip",new MyDate(1,1,1999));
        assertTrue(snack.isExpired(),"isExpired should return true if the current date is behind expiration date");
        approachMasteryPassed++;
    }

    @Test
    @Tag("score:1")
    @Order(56)
    @DisplayName("APPROACHING MASTERY: Check that the happiness index is halved when the snack is expired")
    public void approachMasteryTestSnackgetHappinessIndex1() {
        Snack snack =new Snack(10,6,"Chip",new MyDate(1,1,1999));
        int happinesIndex=snack.getHappinessIndex();
        assertEquals(3,happinesIndex,"Happiness Index of snack should be halved if the expiration date is passed");
        approachMasteryPassed++;


    }

    private StoreItem[]  getStoreItems(ItemList itemList) throws IllegalAccessException, NoSuchFieldException {
        Field itemsField=null;
        Field[] declaredFields = ItemList.class.getDeclaredFields();

        for (Field declaredField : declaredFields) {
            Class<?> fieldType = declaredField.getType();
            String result=fieldType.getName();
            if(result.contains("[Lassignment1.items.StoreItem")){
                itemsField=declaredField;
            }
        }
        itemsField.setAccessible(true);
        StoreItem[] items = (StoreItem[]) itemsField.get(itemList);

        return items;
    }

    private boolean containsInList(StoreItem[]list, StoreItem si){
        for(StoreItem s: list){
            if(s==si)return true;

        }
        return false;
    }
    @Test
    @Tag("score:1")
    @Order(58)
    @DisplayName("APPROACHING MASTERY: Add 1 item and check that the item is stored in the StoreItem[] items")
    public void approachMasteryTestItemListaddItem1() {

        Snack snack =new Snack(10,6,"Chip",new MyDate(1,1,2999));

        ItemList il = new ItemList();
        il.addItem(snack);
        StoreItem []si;
        try {
            si = getStoreItems(il);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        assertTrue(containsInList(si,snack),"The item should be stored in the storeItems in ItemList");
        assertEquals(il.getSize(),1,"The length of the ItemList should be updated to 1");
        approachMasteryPassed++;
    }


    @Test
    @Tag("score:1")
    @Order(61)
    @DisplayName("APPROACHING MASTERY: Check that the item is removed and it is exactly the same item and drink with different numOfBottles with other same field should also be removed")
    public void approachMasteryTestItemListremoveItem1() {
        ItemList il = new ItemList();
        SnoozeJuice toRemove =new SnoozeJuice(10,10,3,30);
        SnoozeJuice toRemove_2 =new SnoozeJuice(10,10,5,30);
        il.addItem(toRemove);


        SnoozeJuice removed = (SnoozeJuice) il.removeItem(toRemove);
        assertSame(toRemove,removed,"The removeItem() should return the same object that is initially added");

        il.addItem(toRemove);
        SnoozeJuice removed_2 =  (SnoozeJuice)il.removeItem(toRemove_2);

        assertSame(toRemove,removed_2,"The removeItem() should return the same object that is initially added_2");
        approachMasteryPassed++;
    }
    @Test
    @Tag("score:1")
    @Order(64)
    @DisplayName("APPROACHING MASTERY: Check that the returned ItemList size is 0 when no items are equal to target")
    public void approachMasteryTestItemListfindEqualItems1() {
        ItemList il = new ItemList();
        SnoozeJuice toRemove =new SnoozeJuice(10,10,3,30);
        il.addItem(toRemove);

        StoreItem[]result = il.findEqualItems(new SnoozeJuice(50,10,3,9));
        SnoozeJuice toRemove_2 =new SnoozeJuice(10,10,3,20);
        assertEquals(result.length,0,"An empty array should be returned if no equal item can be found ");
        approachMasteryPassed++;

    }


    @Test
    @Tag("score:1")
    @Order(66)
    @DisplayName("APPROACHING MASTERY: Check to remove one snack that is expired")
    public void approachMasteryTestStorecleanUp1() {
        ItemList il = new ItemList();
        Snack toRemove =new Snack(10,6,"Chip",new MyDate(1,1,1999));

        il.addItem(toRemove);
        Store store=new Store(il);
        store.cleanUp();

        assertEquals(0, store.getItems().getSize(),"The expired snack should be removed after cleanUp");
        approachMasteryPassed++;
    }

    @Test
    @Tag("score:1")
    @Order(49)
    @DisplayName("FULL MASTERY: Test refillDrinkInventory in Store class where it adds new drinks but does not combine because they are not compatible")
    void fullMasteryTestRefillInventory1() {
        ItemList itemList = new ItemList();
        // add some snacks and drinks
        Drink drinkStore1 = new Drink(5.0, 1, 4, false) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drinkStore2 = new Drink(6.0, 1, 3, false) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Snack snackStore1 = new Snack(5.0, 1, "type1", MyDate.today());
        Snack snackStore2 = new Snack(5.0, 1, "type2", MyDate.today());

        itemList.addItem(drinkStore1);
        itemList.addItem(drinkStore2);
        itemList.addItem(snackStore1);
        itemList.addItem(snackStore2);


        Store store = new Store(itemList);
        Drink drink1 = new Drink(5.0, 1, 4, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drink2 = new Drink(7.0, 1, 3, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drink3 = new Drink(10.0, 1, 4, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };

        Drink[] drinks = {drink1, drink2, drink3};
        store.refillDrinkInventory(drinks);

        boolean drink1Found = false;
        boolean drink2Found = false;
        boolean drink3Found = false;
        ItemList storeItemsAfter = store.getItems();
        assertEquals(7, storeItemsAfter.getSize(), "getSize should return 7");
        StoreItem[] allItems = storeItemsAfter.getAllItems();
        for (StoreItem item : allItems) {
            if (item instanceof Drink) {
                Drink drink = (Drink) item;
                if (drink.equals(drink1)) {
                    drink1Found = true;
                } else if (drink.equals(drink2)) {
                    drink2Found = true;
                } else if (drink.equals(drink3)) {
                    drink3Found = true;
                }
            }
        }
        assertTrue(drink1Found, "Drink 1 should be found in the store, since refillDrinkInventory should add it");
        assertTrue(drink2Found, "Drink 2 should be found in the store, since refillDrinkInventory should add it");
        assertTrue(drink3Found, "Drink 3 should be found in the store, since refillDrinkInventory should add it");
        fullMasteryPassed++;
    }

    @Test
    @Tag("score:1")
    @Order(53)
    @DisplayName("FULL MASTERY: Test refillDrinkInventory in Store class where drinks are combined correctly for multiple drinks input and multiple drinks in store but one drink is not max size")
    void fullMasteryTestRefillInventory5(){
        ItemList itemList = new ItemList();
        // add some snacks and drinks
        Drink drinkStore1 = new Drink(5.0, 1, 6, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drinkStore2 = new Drink(5.0, 1, 1, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drinkStore3 = new Drink(5.0, 1, 1, false) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Snack snackStore1 = new Snack(5.0, 1, "type1", MyDate.today());
        Snack snackStore2 = new Snack(5.0, 1, "type2", MyDate.today());

        itemList.addItem(drinkStore1);
        itemList.addItem(drinkStore2);
        itemList.addItem(drinkStore3);
        itemList.addItem(snackStore1);
        itemList.addItem(snackStore2);

        Store store = new Store(itemList);
        Drink drink1 = new Drink(5.0, 1, 1, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drink2 = new Drink(7.0, 1, 3, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drink3 = new Drink(5.0, 1, 2, true) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };
        Drink drink4 = new Drink(5.0, 1, 1, false) {
            @Override
            public Drink getPortion(int portionSize) {
                return null;
            }
        };

        Drink[] drinks = {drink1, drink2, drink3, drink4};
        store.refillDrinkInventory(drinks);

        ItemList storeItemsAfter = store.getItems();
        StoreItem[] allItems = storeItemsAfter.getAllItems();
        assertEquals(6, storeItemsAfter.getSize(), "getSize should return 6 as one drink is added after refillDrinkInventory");

        boolean drinksCorrectlyRefilled1 = false;
        boolean drinksCorrectlyRefilled2 = false;
        boolean drinksCorrectlyRefilled3 = false;
        boolean drinksCorrectlyRefilled4 = false;
        for (StoreItem item : allItems) {
            if (item instanceof Drink) {
                Drink drink = (Drink) item;
                if (drink.equals(drinkStore1)) {
                    if(!drinksCorrectlyRefilled1 && drink.getNumOfBottles() == 6){
                        drinksCorrectlyRefilled1 = true;
                    } else if (!drinksCorrectlyRefilled2 && drink.getNumOfBottles() == 4) {
                        drinksCorrectlyRefilled2 = true;
                    }
                } else if (drink.equals(drinkStore3)) {
                    if(!drinksCorrectlyRefilled3 && drink.getNumOfBottles() == 2) {
                        drinksCorrectlyRefilled3 = true;
                    }
                } else if (drink.equals(drink2)) {
                    if (!drinksCorrectlyRefilled4 && drink.getNumOfBottles() == 3) {
                        drinksCorrectlyRefilled4 = true;
                    }
                }

            }
        }
        assertTrue(drinksCorrectlyRefilled1 && drinksCorrectlyRefilled2 && drinksCorrectlyRefilled3 && drinksCorrectlyRefilled4, "Drinks were not correctly refiled after refillDrinkInventory");
        fullMasteryPassed++;
        }

}

package assignment1.items;

import java.time.LocalDate;

public class MyDate {
    private int day, month, year;
    public static int[] SUMMER_MONTHS = {6, 7, 8};
    public static int[] WINTER_MONTHS = {1, 2, 12};

    public MyDate(int day, int month, int year) throws IllegalArgumentException {
        // Day validation
        int maxDays = switch (month) {
            case 2 -> 28; // February
            case 4, 6, 9, 11 -> 30; // April, june, september, november
            default -> 31; // All other months
        };
        if (day < 1 || day > maxDays) {
            throw new IllegalArgumentException("Invalid day.");
        }
        // Month validation
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month.");
        }
        // Year validation
        if (year < 0) {
            throw new IllegalArgumentException("Invalid year.");
        }

        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int compare(MyDate date) {
        // Returns 1 if the date is after given date, 0 if equal, -1 if the date is before given date
        if (year > date.year) {
            return 1;
        } else if (year < date.year) {
            return -1;
        } else {
            if (month > date.month) {
                return 1;
            } else if (month < date.month) {
                return -1;
            } else {
                return Integer.compare(day, date.day);
            }
        }
    }

    public boolean isSummer() {
        for (int summerMonth : SUMMER_MONTHS) {
            if (month == summerMonth) {
                return true;
            }
        }
        return false;
    }

    public boolean isWinter() {
        for (int winterMonth : WINTER_MONTHS) {
            if (month == winterMonth) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyDate date)) return false;
        return (day == date.day) && (month == date.month) && (year == date.year);
    }

    public static MyDate today() {
        LocalDate today = LocalDate.now();
        return new MyDate(today.getDayOfMonth(), today.getMonthValue(), today.getYear());
    }

    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }
}

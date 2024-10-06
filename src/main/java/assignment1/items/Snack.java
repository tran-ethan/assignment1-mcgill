package assignment1.items;

public class Snack extends StoreItem {
    private String snackType;
    private MyDate expirationDate;

    public Snack(double price, int happiness, String snackType, MyDate expirationDate) throws IllegalArgumentException {
        super(price, happiness);
        this.snackType = snackType;
        this.expirationDate = expirationDate;
    }

    public boolean isExpired() {
        MyDate today = MyDate.today();
        return today.compare(expirationDate) == 1;
    }

    @Override
    public int getHappinessIndex() {
        if (isExpired()) {
            // If the snack is expired, the happiness index is halved. Since integer, it will truncate decimal value
            return super.getHappinessIndex() / 2;
        }
        return super.getHappinessIndex();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        if (!(o instanceof Snack snack)) return false;
        return snackType.equals(snack.snackType) && expirationDate.equals(snack.expirationDate);
    }
}

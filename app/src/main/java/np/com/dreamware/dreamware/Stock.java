package np.com.dreamware.dreamware;

public class Stock {
    private String name;
    private long stock = Long.MIN_VALUE;

    public Stock(String name) {

        this.name = name;
    }

    public Stock(String name, long stock) {

        this.name = name;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public boolean isStockSet() {
        if (stock == Long.MIN_VALUE) {
            return false;
        }
        return true;
    }
}

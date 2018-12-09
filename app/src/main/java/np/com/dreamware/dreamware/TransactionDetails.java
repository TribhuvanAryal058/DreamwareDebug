package np.com.dreamware.dreamware;

public class TransactionDetails {

    private Long t;
    private Long q;
    private String id;
    private String n;

    public TransactionDetails(Long time, Long quantity, String id, String number) {
        this.t = time;
        this.q = quantity;
        this.id = id;
        this.n = number;

    }

    public TransactionDetails() {

    }

    public String getNumber() {
        return n;
    }

    public void setNumber(String number) {
        this.n = number;
    }

    public String getId() {
        return id;
    }

    public Long getTime() {
        return t;

    }

    public void setTime(Long time) {
        this.t = time;
    }

    @Override
    public String toString() {
        return "TransactionDetails{" +
                "t=" + t +
                ", q=" + q +
                ", i='" + id + '\'' +
                ", n='" + n + '\'' +
                '}';
    }

    public long getQuantity() {
        return this.q;
    }

    public void setQuantity(Long quantity) {
        this.q = quantity;
    }
}

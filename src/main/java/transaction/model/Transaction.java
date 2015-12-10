package transaction.model;

/**
 * Created by sb on 09.12.2015.
 */
public class Transaction {

    private double amount;
    private String type;
    private long parent_id;

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public long getParent_id() {
        return parent_id;
    }
    public void setParent_id(long parent_id) {
        this.parent_id = parent_id;
    }
}

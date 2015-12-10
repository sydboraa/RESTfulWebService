package transaction.model;

/**
 * Created by sb on 09.12.2015.
 */
public class Transaction {

   // private long transactionId;

    private double amount;

    private String type;

    private long parent_id;

//    @JsonCreator
//    public Transaction(@JsonProperty("amount") double amount, @JsonProperty("type") String type) {
//        this.amount = amount;
//        this.type = type;
//    }
//
//    @JsonCreator
//    public Transaction(@JsonProperty("amount") double amount, @JsonProperty("type") String type,
//                       @JsonProperty("parent_id") long parent_id) {
//        this.amount = amount;
//        this.type = type;
//        this.parent_id = parent_id;
//    }

//    @JsonCreator
//    public Transaction(@JsonProperty("amount") double amount, @JsonProperty("type") String type, @JsonProperty("amount") long parentId) {
//        this.amount = amount;
//        this.type = type;
//        this.parentId = parentId;
//    }

//    public long getTransactionId() {
//        return transactionId;
//    }
//
//    public void setTransactionId(long transactionId) {
//        this.transactionId = transactionId;
//    }

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

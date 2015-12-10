package transaction.utils;

/**
 * Created by sb on 10.12.2015.
 */
public final class Roots {

    /* GET */
    public static final String GET_ALL_TRANSACTIONS = "/transactionservice/transaction";
    public static final String GET_SPECIFIC_TRANSACTION = "/transactionservice/transaction/{transaction_id}";
    public static final String GET_TYPE = "/transactionservice/types/{type}";
    public static final String GET_TOTAL_AMOUNT = "/transactionservice/sum/{transaction_id}";

    /* PUT */
    public static final String PUT_TRANSACTION = "/transactionservice/transaction/{transaction_id}";

}

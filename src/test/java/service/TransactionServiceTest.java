package service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import transaction.model.Transaction;
import transaction.service.TransactionService;
import java.util.*;

/**
 * Created by sb on 10.12.2015.
 */
public class TransactionServiceTest {

    @Autowired
    TransactionService transactionService = new TransactionService();

    private Map<Long, Transaction> testTransactionMap = new HashMap<>();
    private Map<Long, Set<Transaction>> testTransactionsLinkedWithParentIdMap = new HashMap<Long, Set<Transaction>>();

    @Before
    public void setup() throws Exception {

        Transaction firstTransaction = new Transaction();
        firstTransaction.setAmount(1000.0);
        firstTransaction.setParent_id(0);
        firstTransaction.setType("cars");

        Transaction secondTransaction = new Transaction();
        secondTransaction.setAmount(500.0);
        secondTransaction.setParent_id(10);
        secondTransaction.setType("cars");

        this.testTransactionMap.put(10L, firstTransaction);
        this.testTransactionMap.put(11L, secondTransaction);

        Set<Transaction> testTransactionSet = new HashSet<Transaction>();
        testTransactionSet.add(secondTransaction);

        this.testTransactionsLinkedWithParentIdMap.put(10L, testTransactionSet);
    }

    /* * Unit tests for findAmount(long transactionId,
                              Map<Long, Transaction> transactionMap,
                              Map<Long, Set<Transaction>> transactionsLinkedWithParentIdMap) method
    * */

    @Test(expected=NullPointerException.class)
    public void nullPointerOnAmountTest() throws Exception {
        transactionService.findAmount(1, null, null);
    }

    @Test
    public void findAmountTest() {
        double amount = transactionService.findAmount(10L, this.testTransactionMap, this.testTransactionsLinkedWithParentIdMap);
        assert(amount == 1500.0);
    }

    @Test(expected=NullPointerException.class)
    public void findAmountTestWithoutNoParentId() {
        double amount = transactionService.findAmount(10L, new HashMap<Long, Transaction>(), new HashMap<Long, Set<Transaction>>());
        assert(amount == 0.0);
    }

    @Test
    public void findAmountTestWithEmptyMaps() {
        double amount = transactionService.findAmount(10L, this.testTransactionMap, new HashMap<Long, Set<Transaction>>());
        assert(amount == 1000.0);
    }

    /* * Unit tests for addToTypeIndex(long transaction_id, String type,
                                      Map<String, Set<Long>> typeIndexMap) method
    * */
    @Test //not expecting throw nullPointerException
    public void noExceptionExpectedOnAddToTypeIndex() throws Exception {
        transactionService.addToTypeIndex(1, null, null);
    }

    /* * Unit tests for checkAndAddForParentTransactions(Transaction transaction,
                                                         Map<Long, Set<Transaction>> transactionsLinkedWithParentIdMap)
    * */
    @Test
    public void noExceptionOnCheckAndAddForParentTransactions() throws Exception {
        transactionService.checkAndAddForParentTransactions(null, null);
    }

    @Test
    public void nullPointerOnCheckAndAddForParentTransactions() throws Exception {
        transactionService.checkAndAddForParentTransactions(testTransactionMap.get(10L), null);
    }

}

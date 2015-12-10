package transaction.controller;

/**
 * Created by sb on 09.12.2015.
 */
import java.util.*;
import org.springframework.web.bind.annotation.*;
import transaction.model.Transaction;
import transaction.service.ResponseService;

@RestController
@RequestMapping("/transactionservice")
public class TransactionController {

    private Map<Long, Transaction> transactionMap = new HashMap<>();
    private Map<String, Set<Long>> typeIndexMap = new HashMap<String, Set<Long>>();
    private Map<Long, Set<Transaction>> transactionsLinkedWithParentIdMap = new HashMap<Long, Set<Transaction>>();

    @RequestMapping(method=RequestMethod.GET, value="/transaction")
    public Collection<Transaction> getAll() {
        return transactionMap.values();
    }

    @RequestMapping(method=RequestMethod.GET, value="/transaction/{transaction_id}")
    public @ResponseBody Transaction getTransaction(@PathVariable("transaction_id") long transaction_id) {
        return transactionMap.get(transaction_id);
    }

    @RequestMapping(method=RequestMethod.PUT, value="/transaction/{transaction_id}", consumes="application/json", produces = "application/json")
    public ResponseService addTransaction(@PathVariable("transaction_id") long transaction_id, @RequestBody Transaction transaction) {
        Transaction storedTransaction = transactionMap.get(transaction_id);
        ResponseService status = new ResponseService();
        if(storedTransaction == null) {
            transactionMap.put(transaction_id, transaction);
            checkAndAddForParentTransactions(transaction_id, transaction);
            addToTypeIndex(transaction_id, transaction.getType());
            status.setStatus("ok");
            return status;

        } else {
            status.setStatus("NOK. There is another transaction with the same id.");
            return status;
        }
    }

    @RequestMapping(method=RequestMethod.GET, value="/types/{type}")
    public Set<Long> getTransactionByType(@PathVariable("type") String type) {
        Set<Long> transIdsByType =  typeIndexMap.get(type);
        if(transIdsByType != null) {
            return transIdsByType;
        } else {
            return Collections.emptySet();
        }
    }

    public void checkAndAddForParentTransactions(long transactionId, Transaction transaction) {
        if(transaction != null) {
            long parentId = transaction.getParent_id();
            if(parentId != 0) {
                Set<Transaction> transactionIdsSet = transactionsLinkedWithParentIdMap.get(parentId);
                if(transactionIdsSet == null) {
                    Set<Transaction> transactionSet = new HashSet<Transaction>();
                    transactionSet.add(transaction);
                    transactionsLinkedWithParentIdMap.put(parentId, transactionSet);
                } else {
                    // var olana eklenecek
                    transactionIdsSet.add(transaction);
                }
            } //otherwise, there is no parent_id for this transaction
        }
    }

    private void addToTypeIndex(long transaction_id, String type) {
        if(type != null){
           Set<Long> transactionIdSetFromMap = typeIndexMap.get(type);
            if(transactionIdSetFromMap == null) {
                Set<Long> transactionIdSet = new HashSet<Long>();
                transactionIdSet.add(transaction_id);
                typeIndexMap.put(type,transactionIdSet);
            } else {
                transactionIdSetFromMap.add(transaction_id);
            }
        }
    }

    @RequestMapping(method=RequestMethod.GET, value="/sum/{transaction_id}")
    public double getSum(@PathVariable("transaction_id") long transaction_id) {
        return findAmount(transaction_id);
    }

    private double findAmount(long transactionId) {
        Transaction currentTransaction = transactionMap.get(transactionId);
        Set<Transaction> transactionSetBOParent = transactionsLinkedWithParentIdMap.get(transactionId);
        if(transactionSetBOParent != null) {
            double sum = 0;
            for (Transaction transaction : transactionSetBOParent) {
                sum += transaction.getAmount();
            }
            return currentTransaction.getAmount() + sum;
        } else {
            return currentTransaction.getAmount();
        }
    }



}

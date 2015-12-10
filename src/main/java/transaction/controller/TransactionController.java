package transaction.controller;

/**
 * Created by sb on 09.12.2015.
 */
import java.util.*;
import org.springframework.web.bind.annotation.*;
import transaction.model.Transaction;
import transaction.service.ResponseService;
import transaction.service.TransactionService;
import transaction.utils.Roots;

@RestController
public class TransactionController {

    private Map<Long, Transaction> transactionMap = new HashMap<>();
    private Map<String, Set<Long>> typeIndexMap = new HashMap<String, Set<Long>>();
    private Map<Long, Set<Transaction>> transactionsLinkedWithParentIdMap = new HashMap<Long, Set<Transaction>>();

    TransactionService transactionService = new TransactionService();

    /* put a transaction */
    @RequestMapping(method=RequestMethod.PUT, value= Roots.PUT_TRANSACTION, consumes="application/json", produces = "application/json")
    public ResponseService addTransaction(@PathVariable("transaction_id") long transaction_id, @RequestBody Transaction transaction) {
        Transaction storedTransaction = transactionMap.get(transaction_id);
        ResponseService status = new ResponseService();
        if(storedTransaction == null) {
            transactionMap.put(transaction_id, transaction);
            transactionService.checkAndAddForParentTransactions(transaction_id, transaction, transactionsLinkedWithParentIdMap);
            transactionService.addToTypeIndex(transaction_id, transaction.getType(), typeIndexMap);
            status.setStatus("ok");
            return status;

        } else {
            status.setStatus("NOK. There is another transaction with the same id.");
            return status;
        }
    }

    /* get all transactions */
    @RequestMapping(method=RequestMethod.GET, value= Roots.GET_ALL_TRANSACTIONS)
    public Collection<Transaction> getAll() {
        return transactionMap.values();
    }

    /* get a specific transaction using transaction_id */
    @RequestMapping(method=RequestMethod.GET, value= Roots.GET_SPECIFIC_TRANSACTION)
    public @ResponseBody Transaction getTransaction(@PathVariable("transaction_id") long transaction_id) {
        return transactionMap.get(transaction_id);
    }


    /* get transactions' ids that share the same types */
    @RequestMapping(method=RequestMethod.GET, value= Roots.GET_TYPE)
    public Set<Long> getTransactionByType(@PathVariable("type") String type) {
        Set<Long> transIdsByType =  typeIndexMap.get(type);
        if(transIdsByType != null) {
            return transIdsByType;
        } else {
            return Collections.emptySet();
        }
    }

    /* A sum of all transactions that are transitively linked by their parent_id to $transaction_id */
    @RequestMapping(method=RequestMethod.GET, value= Roots.GET_TOTAL_AMOUNT, produces = "application/json")
    public double getSum(@PathVariable("transaction_id") long transaction_id) {
        return transactionService.findAmount(transaction_id, transactionMap, transactionsLinkedWithParentIdMap);
    }

}

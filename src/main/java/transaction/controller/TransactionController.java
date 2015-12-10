package transaction.controller;

/**
 * Created by sb on 09.12.2015.
 */
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import transaction.model.Transaction;
import transaction.model.Amount;
import transaction.model.Response;
import transaction.model.Status;
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
    @ResponseStatus( HttpStatus.OK )
    public Response addTransaction(@PathVariable("transaction_id") long transaction_id, @RequestBody Transaction transaction) {
        Transaction storedTransaction = transactionMap.get(transaction_id);
        Status status = new Status();
        if(storedTransaction == null) {
            transactionMap.put(transaction_id, transaction);
            transactionService.checkAndAddForParentTransactions(transaction, transactionsLinkedWithParentIdMap);
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
    @ResponseStatus( HttpStatus.OK )
    public List<Transaction> getAll() {
        return new ArrayList(transactionMap.values());
    }

    /* get a specific transaction using transaction_id */
    @RequestMapping(method=RequestMethod.GET, value= Roots.GET_SPECIFIC_TRANSACTION)
    @ResponseStatus( HttpStatus.OK )
    public @ResponseBody Transaction getTransaction(@PathVariable("transaction_id") long transaction_id) {
        return transactionMap.get(transaction_id);
    }

    /* get all unique types */
    @RequestMapping(method=RequestMethod.GET, value= Roots.GET_ALL_UNIQE_TYPES)
    @ResponseStatus( HttpStatus.OK )
    public Set<String> getAllTypes() {
        return typeIndexMap.keySet();
    }

    /* get transactions' ids that share the same types */
    @RequestMapping(method=RequestMethod.GET, value= Roots.GET_TYPE)
    @ResponseStatus( HttpStatus.OK )
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
    @ResponseStatus( HttpStatus.OK )
    public Response getSum(@PathVariable("transaction_id") long transaction_id) {
        try {
            Amount amount = new Amount();
            double totalAmount = transactionService.findAmount(transaction_id, transactionMap, transactionsLinkedWithParentIdMap);
            amount.setSum(totalAmount);
            return amount;

        } catch (NullPointerException ex) {
            Status status = new Status();
            status.setStatus("NOK. There is no transaction with id: " + transaction_id);
            return status;
        }
    }

}

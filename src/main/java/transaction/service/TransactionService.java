package transaction.service;

import transaction.model.Transaction;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by sb on 10.12.2015.
 */
public class TransactionService {

    public double findAmount(long transactionId,
                              Map<Long, Transaction> transactionMap,
                              Map<Long, Set<Transaction>> transactionsLinkedWithParentIdMap) {

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

    //this method inserts a record to typeIndexMap. I need this map because of /types/something api
    public void addToTypeIndex(long transaction_id, String type, Map<String, Set<Long>> typeIndexMap) {
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

    public void checkAndAddForParentTransactions(Transaction transaction,
                                                 Map<Long, Set<Transaction>> transactionsLinkedWithParentIdMap) {

        if(transaction != null) {
            long parentId = transaction.getParent_id();
            if(parentId != 0) {
                Set<Transaction> transactionIdsSet = transactionsLinkedWithParentIdMap.get(parentId);
                if(transactionIdsSet == null) {
                    Set<Transaction> transactionSet = new HashSet<Transaction>();
                    transactionSet.add(transaction);
                    transactionsLinkedWithParentIdMap.put(parentId, transactionSet);
                } else {
                    // if exist a set, add on it
                    transactionIdsSet.add(transaction);
                }
            } //otherwise, there is no parent_id for this transaction
        }
    }
}

package cafe.caption.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

interface TransactionManager {
    fun <T> transaction(block: () -> T): T
}

class TransactionManagerImpl(val db: Database) : TransactionManager {
    override fun <T> transaction(block: () -> T): T =
        transaction(db) {
            block()
        }
}
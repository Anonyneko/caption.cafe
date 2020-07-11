package cafe.caption.db.repositories

import cafe.caption.db.Users
import cafe.caption.model.domain.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

interface UserRepository {
    fun create(user: User): User
    fun update(user: User): User
    fun get(id: String): User?
    fun count(): Long
}

class UserRepositoryImpl(private val db: Database) : UserRepository {
    override fun create(user: User): User {
        return transaction(db) {
            Users.insert { fromUser(it, user) }.resultedValues?.first()?.toUser()!!
        }
    }

    override fun update(user: User): User {
        return transaction(db) {
            Users.update({ Users.id eq UUID.fromString(user.id) }) { fromUser(it, user) }
            Users.select { Users.id eq UUID.fromString(user.id) }.first().toUser()
        }
    }

    override fun get(id: String): User? {
        return transaction(db) {
            Users.select { Users.id eq UUID.fromString(id) }.firstOrNull()?.toUser()
        }
    }

    override fun count(): Long {
        return transaction(db) {
            Users.selectAll().count()
        }
    }
}

fun ResultRow.toUser() = User(
    id = this[Users.id].toString(),
    username = this[Users.username],
    email = this[Users.email],
    password = this[Users.password],
    deleted = this[Users.deleted]
)

fun Users.fromUser(it: UpdateBuilder<Number>, user: User) {
    it[username] = user.username
    it[email] = user.email
    it[password] = user.password
    it[deleted] = user.deleted
}
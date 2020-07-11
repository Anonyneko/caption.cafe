package cafe.caption.db

import org.jetbrains.exposed.sql.*

object Users : Table("users") {
    // "autoIncrement" here only means that Exposed will return the value auto-generated in the database
    // when the row is inserted; "autoGenerate" would have created this value on the client instead
    val id = uuid("id").index(isUnique = true).autoIncrement()
    val username = varchar("username", 1024).index(isUnique = true)
    val email = varchar("email", 1024).nullable().index(isUnique = true)
    val password = varchar("password", 1024).nullable()
    val deleted = bool("deleted")
}
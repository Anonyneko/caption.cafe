package cafe.caption

import cafe.caption.db.TransactionManager
import cafe.caption.db.TransactionManagerImpl
import cafe.caption.db.repositories.UserRepository
import cafe.caption.db.repositories.UserRepositoryImpl
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

interface ApplicationModule {
    val db: Database
    val flyway: Flyway
    val dbUrl: String
    val transactionManager: TransactionManager
    val userRepository: UserRepository

    class Impl : ApplicationModule {
        override val dbUrl = "jdbc:postgresql://db:5432/" + System.getenv("POSTGRES_DB")
        override val db: Database by lazy {
            Database.connect(
                dbUrl,
                driver = "org.postgresql.Driver",
                user = System.getenv("POSTGRES_USER"),
                password = System.getenv("POSTGRES_PASSWORD")
            )
        }
        override val flyway: Flyway by lazy {
            Flyway.configure()
                .locations("classpath:db/migrations")
                .dataSource(dbUrl, System.getenv("POSTGRES_USER"), System.getenv("POSTGRES_PASSWORD"))
                .load()
        }
        override val transactionManager: TransactionManager by lazy { TransactionManagerImpl(db) }
        override val userRepository: UserRepository by lazy { UserRepositoryImpl(db) }
    }
}

interface Context : ApplicationModule {

    class Impl(applicationModule: ApplicationModule) : Context,
        ApplicationModule by applicationModule
}

fun createContext(): Context {
    val applicationModule = ApplicationModule.Impl()

    return Context.Impl(applicationModule)
}
package cafe.caption

import cafe.caption.db.Users
import cafe.caption.model.domain.User
import cafe.caption.utils.encryptPassword
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("Global")

fun main() {
    val context = createContext()
    val db = context.db

    val flyway = context.flyway
    flyway.migrate()

    createDefaultAdminIfNecessary(context)

    val routes = routes(
        "/" bind GET to { request -> Response(OK).body("Main page")},
        "/{name}" bind GET to { request -> Response(OK).body("Hello, ${request.path("name")}")}
    )

    logger.debug("Start server")
    routes.asServer(Undertow(8080)).start().block()
}

fun createDefaultAdminIfNecessary(context: Context) {
    context.transactionManager.transaction {
        if (context.userRepository.count() == 0L) {
            val user = context.userRepository.create(
                User(
                    username = "admin",
                    password = encryptPassword(System.getenv("DEFAULT_ADMIN_PASSWORD")),
                    email = System.getenv("DEFAULT_ADMIN_EMAIL")
                )
            )
            logger.debug("Default admin user created: $user")
        }
    }
}
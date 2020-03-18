package cafe.caption

import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
    val context = createContext()
    val db = context.db

    val flyway = context.flyway
    flyway.migrate()

    val routes = routes(
        "/" bind GET to { request -> Response(OK).body("Main page")},
        "/{name}" bind GET to { request -> Response(OK).body("Hello, ${request.path("name")}")}
    )

    routes.asServer(Undertow(8080)).start().block()
}

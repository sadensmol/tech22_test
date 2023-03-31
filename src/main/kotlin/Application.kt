import freemarker.cache.ClassTemplateLoader
import freemarker.core.HTMLOutputFormat
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.util.getOrFail
import java.math.BigDecimal
import kotlin.Error

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureRouting()
    configureTemplating()
}

fun Application.configureTemplating() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        outputFormat = HTMLOutputFormat.INSTANCE
    }
}

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("currencyRates" to currencyRates)))
        }
        post("/convert") {
            try{
                val formParameters = call.receiveParameters()
                val from = formParameters.getOrFail("from")
                val to = formParameters.getOrFail("to")
                val amount = formParameters.getOrFail("amount")

                val result = ConversionResult(
                    from = from,
                    to = to,
                    fromAmount = amount.toBigDecimal(),
                    toAmount = BigDecimal.ONE //todo
                )
                call.respond(FreeMarkerContent("converted.ftl", mapOf("result" to result)))

            }catch (e:Exception) {
                call.respond(FreeMarkerContent("error.ftl", mapOf("error" to Error(title = "Conversion failed",
                    description=e.message?:"Unknown error"))))
            }
        }
    }
}

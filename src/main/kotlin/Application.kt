import freemarker.cache.ClassTemplateLoader
import freemarker.core.HTMLOutputFormat
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.util.getOrFail
import java.math.BigDecimal
import java.math.RoundingMode





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
            call.respond(FreeMarkerContent("index.ftl", mapOf("currencies" to currencies)))
        }
        post("/convert") {
            try {
                val formParameters = call.receiveParameters()
                val from = formParameters.getOrFail("from")
                val to = formParameters.getOrFail("to")
                val amount = formParameters.getOrFail("amount")
                val bdAmount = BigDecimal(amount)

                if (bdAmount <= BigDecimal.ZERO) {
                    throw Error("Amount must be positive!")
                }

                val response = currencyHttpClient.get(RUB_RATE_API)
                if (response.status != HttpStatusCode.OK) {
                    throw Error("Cannot contact conversion service")
                }

                val res = response.body<ConverterCurrencies>()
                val conversionPairs = res.values.filter { cur -> listOf(from, to).contains(cur.label) }
                val curA = conversionPairs.find { cur -> cur.label == from } ?: throw Error("Currency $from not found")
                val curB = conversionPairs.find { cur -> cur.label == to } ?: throw Error("Currency $to not found")
                val convertedAmount = calcCrossRate(curA, curB)*bdAmount

                val result = ConversionResult(
                    from = from,
                    to = to,
                    fromAmount = bdAmount,
                    toAmount = convertedAmount.setScale(2,RoundingMode.HALF_DOWN)
                )

                call.respond(FreeMarkerContent("converted.ftl", mapOf("result" to result)))

            } catch (e: Throwable) {
                call.respond(
                    FreeMarkerContent(
                        "error.ftl", mapOf(
                            "error" to Error(
                                title = "Conversion failed",
                                description = e.message ?: "Unknown error"
                            )
                        )
                    )
                )
            }
        }
    }
}

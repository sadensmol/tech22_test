import freemarker.cache.ClassTemplateLoader
import freemarker.core.HTMLOutputFormat
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
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
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.math.BigDecimal
import kotlin.Error

private const val CONVERT_API = "https://api.fastforex.io/convert?api_key=c2ebafec83-41eb29415b-rsdlka"
private val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 3000
    }
}


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
            try{
                val formParameters = call.receiveParameters()
                val from = formParameters.getOrFail("from")
                val to = formParameters.getOrFail("to")
                val amount = formParameters.getOrFail("amount")
                val bdAmount = BigDecimal(amount)

                if (bdAmount <= BigDecimal.ZERO) {
                    throw Error("Amount must be positive!")
                }

                val response = httpClient.get(String.format("$CONVERT_API&from=%s&to=%s&amount=%s", from, to, amount))

                if (response.status != HttpStatusCode.OK) {
                    throw Error("Cannot contact conversion service")
                }

                val res = response.body<JsonObject>()

                val convertedAmount = BigDecimal((res["result"] as JsonObject)[to].toString())

                val result = ConversionResult(
                    from = from,
                    to = to,
                    fromAmount = bdAmount,
                    toAmount = convertedAmount
                )

                call.respond(FreeMarkerContent("converted.ftl", mapOf("result" to result)))

            }catch (e:Error) {
                call.respond(FreeMarkerContent("error.ftl", mapOf("error" to Error(title = "Conversion failed",
                    description=e.message?:"Unknown error"))))
            }
        }
    }
}

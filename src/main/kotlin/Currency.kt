import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.Charsets
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.xml.xml
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

// list of supported currencies
val currencies = listOf("USD", "EUR", "GEL", "RUB","INR")

// cbr.ru specific implementation
@Serializable
@SerialName("ValCurs")
data class ConverterCurrencies(
    @SerialName("Date")
    val date: String,
    @SerialName("name")
    val name: String,
    val values: List<ConverterCurrency>,
)

@Serializable
@SerialName("Valute")
data class ConverterCurrency(
    @SerialName("ID")
    val id: String?=null,
    @SerialName("CharCode")
    @XmlElement(true)
    val label: String,
    @SerialName("NumCode")
    @XmlElement(true)
    val code: String?=null,
    @SerialName("Nominal")
    @XmlElement(true)
    val multiplier: Int,
    @SerialName("Value")
    @XmlElement(true)
    val value: String,
    @SerialName("Name")
    @XmlElement(true)
    val name: String?=null,
)

const val RUB_RATE_API = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=01/04/2023"
val currencyHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        xml(format = XML())
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 3000
    }

    Charsets {
        responseCharsetFallback = Charset.forName("windows-1251")
    }

}

val df = (NumberFormat.getNumberInstance(Locale("ru","RU")) as DecimalFormat).apply{
    isParseBigDecimal = true
}

fun calcCrossRate (curA:ConverterCurrency, curB:ConverterCurrency) : BigDecimal {
    val rateA = df.parse (curA.value) as BigDecimal
    val multA = curA.multiplier.toBigDecimal()
    val rateB =df.parse (curB.value) as BigDecimal
    val multB = curB.multiplier.toBigDecimal()
    return rateA*multB/rateB*multA
}

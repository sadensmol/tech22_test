import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.formUrlEncode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals


private fun bodyWrapper(content: String) = """
<!DOCTYPE html>
<html lang="en">
<head>
<title>Currency converter</title>
</head>
<body style="text-align: center; font-family: sans-serif">
${content.trimIndent()}
</body>
</html>
""".trimIndent()

class ApplicationTest {
    @Test
    fun `should return error on zero amount`() = testApplication {
        val response = client.post("/convert") {
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("from" to "RUB", "to" to "RUB", "amount" to "0").formUrlEncode())
        }
        assertEquals(HttpStatusCode.OK, response.status)

        assertEquals(
            bodyWrapper(
                """
Some error occurred:
<br>
<b>Conversion failed</b>
<br>
Amount must be positive!
"""),
            response.bodyAsText().trim()
        )

    }
    @Test
    fun `should return proper value for one to one conversion`() = testApplication {
        val response = client.post("/convert") {
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("from" to "RUB", "to" to "RUB", "amount" to "1").formUrlEncode())
        }
        assertEquals(HttpStatusCode.OK, response.status)

        assertEquals(
            bodyWrapper(
                """
1 RUB equals to 1 RUB
"""),
            response.bodyAsText().trim()
        )

    }
}

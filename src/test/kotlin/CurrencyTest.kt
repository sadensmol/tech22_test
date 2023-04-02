import org.junit.Assert
import java.math.BigDecimal
import kotlin.test.Test


class CurrencyTest  {
    @Test
    fun `should calculate cross rate for same multiplier`() {
        val curA = ConverterCurrency(label = "USD", value = "77,3233", multiplier = 1)
        val curB = ConverterCurrency(label = "EUR", value = "84,1116", multiplier = 1)
        Assert.assertEquals(BigDecimal("0.9193"), calcCrossRate(curA, curB))
    }
    @Test
    fun `should calculate cross rate with different multipliers`() {
        val curA = ConverterCurrency(label = "USD", value = "77,3233", multiplier = 1)
        val curB = ConverterCurrency(label = "INR", value = "94,0240", multiplier = 100)
        Assert.assertEquals(BigDecimal("82.2378"), calcCrossRate(curA, curB))
    }

}

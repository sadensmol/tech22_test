import java.math.BigDecimal


class CurrencyRate
private constructor(val label: String, val rate: BigDecimal) {
    companion object {

        fun newEntry(label: String, rate: BigDecimal) = CurrencyRate(label, rate)
    }
}

val currencyRates = mutableListOf(
    CurrencyRate.newEntry(
        "USD", BigDecimal.valueOf(1)
    )
)

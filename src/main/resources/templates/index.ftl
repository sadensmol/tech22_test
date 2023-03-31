

<#-- @ftlvariable name="currencyRates" type="kotlin.collections.List<CurrencyRate>" -->
<#import "_layout.ftl" as layout />
<@layout.header>

    <form action="/convert" method="post">
        <label for="from-currency">From:</label>
        <select id="from-currency" name="from">
            <#list currencyRates?reverse as currencyRate>
                <option value="${currencyRate.label}">${currencyRate.label}</option>
            </#list>
        </select>

        <label for="to-currency">To:</label>
        <select id="to-currency" name="to">
            <#list currencyRates?reverse as currencyRate>
                <option value="${currencyRate.label}">${currencyRate.label}</option>
            </#list>
        </select>
        <br><br>

        <label for="amount">Amount:</label>
        <input id="amount" name="amount" type="number" step="0.01"/>

        <input type="submit" value="Convert it!"/>
    </form>

</@layout.header>
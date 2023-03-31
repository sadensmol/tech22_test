

<#-- @ftlvariable name="currencies" type="kotlin.collections.List<String>" -->
<#import "_layout.ftl" as layout />
<@layout.header>

    <form action="/convert" method="post">
        <label for="from-currency">From:</label>
        <select id="from-currency" name="from">
            <#list currencies?reverse as currency>
                <option value="${currency}">${currency}</option>
            </#list>
        </select>

        <label for="to-currency">To:</label>
        <select id="to-currency" name="to">
            <#list currencies?reverse as currency>
                ?reverse as currencyRate>
                <option value="${currency}">${currency}</option>
            </#list>
        </select>
        <br><br>

        <label for="amount">Amount:</label>
        <input id="amount" name="amount" type="number" step="0.01" min="0.1"/>

        <input type="submit" value="Convert it!"/>
    </form>

</@layout.header>
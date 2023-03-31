<#-- @ftlvariable name="result" type="ConversionResult" -->
<#import "_layout.ftl" as layout />
<@layout.header>
You converted ${result.fromAmount} ${result.from} -> ${result.toAmount} ${result.to}
</@layout.header>
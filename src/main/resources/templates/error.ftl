<#-- @ftlvariable name="error" type="Error" -->
<#import "_layout.ftl" as layout />
<@layout.header>
Some error occurred:
    <br>
    <b>${error.title}</b>
    <br>
    ${error.description}
</@layout.header>
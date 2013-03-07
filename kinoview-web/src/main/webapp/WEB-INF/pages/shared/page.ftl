<#assign c=JspTaglibs["http://java.sun.com/jstl/core"]>
<#assign s=JspTaglibs["http://www.springframework.org/tags"]>
<#import "base.ftl" as layout/>

<#macro page title head="" onloadjs="">
    <@layout.base title=title head=head onloadjs=onloadjs>
        <#nested/>
    </@layout.base>
</#macro>
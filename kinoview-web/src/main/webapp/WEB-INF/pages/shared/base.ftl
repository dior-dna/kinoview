<#assign c=JspTaglibs["http://java.sun.com/jstl/core"]>
<#macro base title head="" onloadjs="">
<!DOCTYPE HTML>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title!}</title>
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <script type="text/javascript">
        var contextPath = '<@c.url value="/"></@c.url>';
    </script>
${head}
</head>
<body>
    <#nested/>
</body>
</html>
</#macro>
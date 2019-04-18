<#import "macro/mainTemplate.ftl" as wrapper>
<#include "macro/security.ftl">

<@wrapper.page>
    <div align="center">
        <h1>Увы, у вас нету доступа к этой странице</h1>
        ${errorText}
    </div>
</@wrapper.page>

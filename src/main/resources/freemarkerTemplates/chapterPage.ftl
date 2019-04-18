<#import "macro/mainTemplate.ftl" as wrapper>

<@wrapper.page>
    <div>
        <div>
            <h1 style="text-align: left">
                ${current.name}
            </h1>
        </div>
        <div style="white-space: nowrap">
            <#if previous??>
            <div class="btn-group btn-group-lg" role="group" align="left" aria-label="Basic example">
                <a href="/book/${current.book.id}/${previous.id}" type="button" class="btn btn-secondary">Previous</a>
            </div>
            </#if>
            <#if next??>
            <div class="btn-group btn-group-lg" role="group" align="right" aria-label="Basic example">
                <a href="/book/${current.book.id}/${next.id}" type="button" class="btn btn-secondary">Next</a>
            </div>
            </#if>
        </div>
        <main class="ml-mr-4">
        <#if current.content??>
            ${current.content?html?replace("\\r?\\n", "<br>", 'r')}
        <#else>
            Содержания нету
        </#if>
        </main>
    </div>
</@wrapper.page>

<#import "macro/mainTemplate.ftl" as wrapper>
<#import "macro/pager.ftl" as pager>

<@wrapper.page>
    <div class="card-columns">
        <#if tagsPage??>
            <@pager.main "tag" tagsPage>
            <#list tagsPage.content as tag>
                <div class="card my-2 mx-3" style="width: auto; height: auto; min-width: 4rem; background: rgba(255,195,109,0.69)">
                    <a href="/tag/${tag.name}">${tag.name}</a>
                </div>
            </#list>
            </@pager.main>
        <#else>
            Тегов нету
        </#if>
    </div>
</@wrapper.page>

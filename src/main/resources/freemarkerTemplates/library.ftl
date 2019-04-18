<#import "macro/mainTemplate.ftl" as wrapper>
<#include "macro/security.ftl">
<#import "/spring.ftl" as spring>

<@wrapper.page>
    <div align="center">
        <h2><@spring.message "user.library.label" /></h2>
    </div>

    <div class="card-deck">
        <#list spectatingBooks as book, lastChapter>
            <div class="card mx-my-2" style="max-width: 165px">
                <#if book.headerImage??>
                    <img src="/images/${book.headerImage}" class="card-img-top" style="max-height: 270px;">
                <#else>
                    <img src="/images/Not_found.png" class="card-img-top" style="max-height: 270px;">
                </#if>
                <div class="card-body">
                    <h5 class="card-title" id="title"
                        style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis"><a href="/book/${book.id}/${lastChapter}" class="text-decoration-none" style="color: #000000"> ${book.title}</a></h5>
                    <p class="card-text"><small class="text-muted"><@spring.message "user.library.progress" />: ${lastChapter}/${book.chapters?size}</small> </p>
                </div>
            </div>
        </#list>
    </div>
</@wrapper.page>

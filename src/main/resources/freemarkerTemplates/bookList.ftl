<#import "macro/mainTemplate.ftl" as wrapper>
<#import "macro/pager.ftl" as pager>
<#import "/spring.ftl" as spring>


<@wrapper.page>
    <div class="btn-group">
        <a href="/book/add" class="btn my-4 mx-3" style="color: #a46d06; background: rgba(146,255,37,0.36)">Добавить
            книгу</a>
    </div>
    <@pager.main "book" booksPage>
        <div class="card-columns">
            <#list booksPage.content as book>
                <div class="card">
                    <#if book.headerImage??>
                        <img src="/images/${book.headerImage}" class="card-img-top"
                             style="max-width: 300rem; max-height: 400rem"/>
                    </#if>
                    <div class="card-header" style="background: #FFFFFF;">
                        <h4><@spring.message "bookFields.author" />: <a href="/user/profile/${book.author.id}"
                                      style="color: black">${book.author.username}</a></h4>
                        <h2>${book.title}</h2>
                    </div>
                    <div class="card-body" style="background: #FFFFFF">
                        <#if book.genre??><h5 class="card-title"><a href="/search?genre=${book.genre}">${book.genre}</a>
                            </h5></#if>

                        <#if book.bookTags??>
                            <#list book.bookTags as tag>
                                <div class="card my-2 mx-3" style="width: auto">
                                    <a href="/tag/${tag.name}">${tag.name}</a>
                                </div>
                            </#list>
                        </#if>

                        <div>
                            <a href="/book/${book.id}" class="btn"
                               style="background: rgba(255,138,126,0.36)"><@spring.message "buttonName.more" /></a>
                        </div>
                    </div>
                </div>
            <#else>
                Нету книг
            </#list>
        </div>
    </@pager.main>
</@wrapper.page>

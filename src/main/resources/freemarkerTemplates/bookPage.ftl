<#import "macro/mainTemplate.ftl" as wrapper>
<#import "macro/recursiveList.ftl" as recursive>
<#include "macro/security.ftl">
<#import "/spring.ftl" as spring>
<#macro star active>
    <img src="/images/rate_star${active?string("-active","")}.svg" class="text-decoration-none"
         style="width: 45px; height: 45px">
</#macro>

<@wrapper.page>
    <div>
        <div>
            <h1 style="text-align: center">
                ${book.title}
            </h1>
        </div>
        <div>
            <#if book.headerImage??>
                <img src="/images/${book.headerImage}" align="left" width="300" height="400"
                     class="img-thumbnail mx-5 my-2"/>
            <#else>
                <img src="/images/Not_found.png" align="left" width="300" height="400" class="img-thumbnail mx-5 my-2"/>
            </#if>
            <div style="width: auto">
                <h4><@spring.message "bookFields.author" />: <a href="/user/profile/${book.author.id}" style="color: black">${book.author.username}</a>
                </h4>
                <div class="card-columns">
                    <#if book.genre??>
                        <card class="card my-2 mx-2" style="width: auto">
                            <h3><a href="/search?genre=${book.genre}">${book.genre}</a></h3>
                        </card>
                    </#if>
                </div>
                <div class="card-columns">
                    <#list bookTags as tag>
                        <span class="card my-2 mx-2"
                              style="width: auto; display: inline-block; background: rgba(202,231,255,0.36)">
                            <a href="/tag/${tag.name}"> ${tag.name}</a>
                        </span>
                    </#list>
                </div>
            </div>
            <div>

                <div class="mr-2">
                    <a href="/book/${book.id}/rate?rating=1"
                       class="text-decoration-none"><@star active=(book.resolveRating() >= 1)></@star></a>
                    <a href="/book/${book.id}/rate?rating=2"
                       class="text-decoration-none"><@star active=(book.resolveRating() >= 2)></@star></a>
                    <a href="/book/${book.id}/rate?rating=3"
                       class="text-decoration-none"><@star active=(book.resolveRating() >= 3)></@star></a>
                    <a href="/book/${book.id}/rate?rating=4"
                       class="text-decoration-none"><@star active=(book.resolveRating() >= 4)></@star></a>
                    <a href="/book/${book.id}/rate?rating=5"
                       class="text-decoration-none"><@star active=(book.resolveRating() >= 5)></@star></a>
                </div>
            </div>
            <#if ratingError??>
                <div class="badge badge-danger text-wrap" style="width: 6rem;">
                    ${ratingError}
                </div>
            </#if>
            <div>
                <h3><@spring.message "bookFields.voted_count" />: ${votedCount}</h3>

            </div>
            <#if known>
                <div>
                    <a href="/user/addBookmark/${book.id}" class="btn" style="background: #ffe95c"><@spring.message "bookFields.bookMark_add" /></a>
                    <#if isAuthor || user.roles?seq_contains("MODERATOR")>
                        <a href="/book/addChapter/${book.id}" class="btn mx-2 my-2" style="background: #ffe95c"><@spring.message "bookFields.chapter_add" /></a>
                        <a href="/book/${book.id}/edit" class="btn mx-2 my-2" style="background: #ffe95c"><@spring.message "bookFields.book_edit" /></a>
                        <a href="/book/${book.id}/remove" class="btn mx-2 my-2" style="background: #ffe95c"><@spring.message "bookFields.book_remove" /></a>
                    </#if>
                </div>
            </#if>
            <br clear="all"><br>

        </div>
        <div class="btn-group btn-group-lg" role="group" aria-label="Basic example">
            <button type="button" data-target="#carouselOptions" data-slide-to="0" class="btn btn-secondary">
                <@spring.message "bookFields.description" />
            </button>
            <button type="button" data-target="#carouselOptions" data-slide-to="1" class="btn btn-secondary"><@spring.message "bookFields.chapters" />
            </button>
            <button type="button" data-target="#carouselOptions" data-slide-to="2" class="btn btn-secondary"><@spring.message "bookFields.comments" />
            </button>
        </div>
        <div id="carouselOptions" class="carousel slide mb-5 mt-2" data-ride="carousel" data-interval="false">
            <div class="carousel-inner">
                <div class="carousel-item active">
                    <div class="border" style="word-wrap: break-word; min-height: 6rem">
                        <#if book.description??>
                            ${book.description?html?replace("\\r?\\n", "<br>", 'r')}
                        <#else>
                            <@spring.message "bookFields.description_missing" />
                        </#if>
                    </div>
                </div>
                <div class="carousel-item">
                    <div class="border" style="word-wrap: break-word">
                        <#if book.chapters??>
                            <#list book.chapters as chapter>
                                <ul class="list-group">
                                    <a href="/book/${book.id}/${chapter.id}" type="button"
                                       class="list-group-item list-group-item-action">${chapter.name}</a>
                                    <#if isAuthor || user.roles?seq_contains("MODERATOR")>
                                        <a href="/book/${book.id}/${chapter.id}/ch_remove" class="text-decoration-none"><img
                                                    src="/images/Delete.svg" style="width: 45px; height: 45px"></a>
                                        <a href="/book/${book.id}/${chapter.id}/ch_edit"
                                           class="text-decoration-none"><img src="/images/Edit.svg"
                                                                             style="width: 45px; height: 45px"></a>
                                    </#if>
                                </ul>
                            </#list>
                        <#else>
                            <h2>
                                <@spring.message "bookFields.chapter_missing" />
                            </h2>
                        </#if>
                    </div>
                </div>
                <div class="carousel-item">
                    <form action="/book/<#if comment??>${book.id}/${comment.id}/cm_edit<#else>${book.id}/comments</#if>"
                          method="post">
                        <div class="input-group">
                            <input type="text" class="form-control" name="content" placeholder="Комментарий"
                                   value="<#if comment??>${comment.content}</#if>" required>
                        </div>
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <button type="submit" class="btn" style="background: #ffe95c"><@spring.message "bookFields.comment_submit" /></button>
                    </form>
                    <#if comments??>
                        <@recursive.recursiveComment comments book.id user>
                        </@recursive.recursiveComment>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</@wrapper.page>

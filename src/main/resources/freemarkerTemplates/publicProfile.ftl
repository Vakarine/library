<#import "macro/mainTemplate.ftl" as wrapper>
<#include "macro/security.ftl">

<@wrapper.page>
    <div>
        <div>
            <div align="center" style="width: auto">
                <h1>${user.username}</h1>
            </div>
            <#if user.profilePicture??>
                <img src="/images/profiles/${user.profilePicture}" align="left" width="300" height="400"
                     class="img-thumbnail mx-5 my-2"/>
            <#else>
                <img src="/images/Not_found.png" align="left" width="300" height="400"
                     class="img-thumbnail mx-5 my-2"/>
            </#if>

            <div class="my-2">
                Количество созданных книг: ${books?size}
            </div>

            <br clear="all"><br>

            <div class="btn-group btn-group-lg" role="group" aria-label="Basic example">
                <button type="button" data-target="#carouselOptions" data-slide-to="0" class="btn btn-secondary">
                    Comments
                </button>
                <button type="button" data-target="#carouselOptions" data-slide-to="1" class="btn btn-secondary">Books
                </button>
            </div>
            <div id="carouselOptions" class="carousel slide mb-5 mt-2" data-ride="carousel" data-interval="false">
                <div class="carousel-inner">
                    <div class="carousel-item active">
                        <#list comments as comment>
                            <div class="border my-2">
                                ${comment.content}
                                <#if comment.targetBook??><br> Под книгой: <a href="/book/${comment.targetBook.id}"
                                                                              style="color: #000000; white-space: nowrap; overflow: hidden; text-overflow: ellipsis">${comment.targetBook.title}</a>
                                <#else>
                                    <br>
                                    В ответ на:
                                    <div class="border my-1">
                                        ${comment.targetComment.content}
                                    </div>
                                </#if>
                            </div>
                        </#list>
                    </div>
                    <div class="carousel-item">
                        <div>
                            <h1>Созданные книги:</h1>
                        </div>
                        <div class="card-deck">
                            <#list books as book>
                                <div class="card mx-my-2" style="max-width: 165px">
                                    <#if book.headerImage??>
                                        <img src="/images/${book.headerImage}" class="card-img-top"
                                             style="max-height: 270px;">
                                    <#else>
                                        <img src="/images/Not_found.png" class="card-img-top"
                                             style="max-height: 270px;">
                                    </#if>
                                    <div class="card-body">
                                        <h5 class="card-title" id="title"
                                            style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis"><a
                                                    href="/book/${book.id}" class="text-decoration-none"
                                                    style="color: #000000"> ${book.title}</a></h5>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</@wrapper.page>

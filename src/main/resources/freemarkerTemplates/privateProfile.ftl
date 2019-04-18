<#import "macro/mainTemplate.ftl" as wrapper>
<#include "macro/security.ftl">
<#import "/spring.ftl" as spring>

<@wrapper.page>
    <div>
        <div>
            <form action="/user/profile" enctype="multipart/form-data" method="post">
                <fieldset <#if !enabled>disabled</#if>>
                    <#if user.profilePicture??>
                        <img src="/images/profiles/${user.profilePicture}" align="left" width="300" height="400"
                             class="img-thumbnail mx-5 my-2"/>
                    <#else>
                        <img src="/images/Not_found.png" align="left" width="300" height="400"
                             class="img-thumbnail mx-5 my-2"/>
                    </#if>
                    <div class="my-2">
                        <@spring.message "user.profile.books_count" />: ${books?size}
                    </div>
                    <div class="custom-file">
                        <input type="file" name="profilePic" class="custom-file-input" id="profilePicInput" aria-describedby="profilePicInputArea">
                        <label class="custom-file-label" for="profilePicInput"><@spring.message "user.profile.image_change" /></label>
                    </div>
                    <div style="width: auto">
                        <div class="input-group my-2">
                            <input type="text" name="username" class="form-control" placeholder="<@spring.message "user.profile.username_change" />"
                                   value="${user.username}" required/>
                        </div>
                        <div class="input-group my-2">
                            <input type="text" name="password" class="form-control" placeholder="<@spring.message "user.profile.password_change" />" required/>
                        </div>
                    </div>
                </fieldset>
                <#if enabled>
                    <button type="submit" class="btn btn-block"><@spring.message "user.profile.save" /></button>
                </#if>
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <a href="/user/profile?enabled=true" style="background: #ffe95c" class="btn btn-sm btn-block"><@spring.message "user.profile.change" /></a>
            </form>

            <br clear="all"><br>

            <div>
                <h1><@spring.message "user.profile.books_list" />:</h1>
            </div>
            <div class="card-deck">
                <#list books as book>
                    <div class="card mx-my-2" style="max-width: 165px">
                        <#if book.headerImage??>
                            <img src="/images/${book.headerImage}" class="card-img-top" style="max-height: 270px;">
                        <#else>
                            <img src="/images/Not_found.png" class="card-img-top" style="max-height: 270px;">
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
</@wrapper.page>

<#import "macro/mainTemplate.ftl" as wrapper>

<@wrapper.page>
    <div>
        <div>
            <form action="/admin/user/${selectedUser.id}" enctype="multipart/form-data" method="post">
                <fieldset>
                    <#if selectedUser.profilePicture??>
                        <img src="/images/profiles/${selectedUser.profilePicture}" align="left" width="300" height="400"
                             class="img-thumbnail mx-5 my-2"/>
                    <#else>
                        <img src="/images/Not_found.png" align="left" width="300" height="400"
                             class="img-thumbnail mx-5 my-2"/>
                    </#if>

                    <div class="my-2">
                        Количество созданных книг: ${books?size}
                    </div>

                    <div>
                        <#list roles as role>
                        <label><input type="checkbox" ${selectedUser.roles?seq_contains(role)?string("checked","")} class="mx-my-2" name="${role}">${role}</label>
                        </#list>
                    </div>

                    <div class="custom-file">
                        <input type="file" name="profilePic" class="custom-file-input" id="profilePicInput" value="<#if selectedUser.profilePicture??>${selectedUser.profilePicture}</#if>" aria-describedby="profilePicInputArea">
                        <label class="custom-file-label" for="profilePicInput">Сменить картинку</label>
                    </div>

                    <div style="width: auto">
                        <div class="input-group my-2">
                            <input type="text" name="username" class="form-control" placeholder="Никнейм"
                                   value="${selectedUser.username}" required/>
                        </div>
                        <div class="input-group my-2">
                            <input type="text" name="password" class="form-control" placeholder="Пароль" required/>
                        </div>
                    </div>

                </fieldset>

                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <button type="submit" class="btn btn-block">Изменить профиль</button>
            </form>

            <br clear="all"><br>

            <div>
                <h1>Созданные книги:</h1>
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

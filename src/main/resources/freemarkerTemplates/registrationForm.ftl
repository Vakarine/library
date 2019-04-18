<#import "macro/mainTemplate.ftl" as wrapper>
<#import "/spring.ftl" as spring>

<@wrapper.page>
    <div>
        <form action="/registration" method="post" enctype="multipart/form-data">
            <div class="input-group my-1">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="profilePicInputArea">Картинка профиля</span>
                </div>
                <div class="custom-file">
                    <input type="file" name="profilePic" class="custom-file-input" id="profilePicInput" aria-describedby="profilePicInputArea">
                    <label class="custom-file-label" for="profilePicInput">Выберите картинку</label>
                </div>
            </div>
            <div class="input-group my-1">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="usernameField" >
                        Логин
                    </span>
                </div>
                <input type="text" class="form-control ${(errorName??)?string("is-invalid", "")}" name="username" aria-describedby="usernameField" placeholder="Логин" required>
                <#if errorName??>
                    <div class="invalid-feedback">
                        ${errorName}
                    </div>
                </#if>
            </div>
            <div class="input-group my-1">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="passwordField" >
                        Пароль
                    </span>
                </div>
                <input type="password" class="form-control ${(errorPassword??)?string("is-invalid", "")}" name="password" aria-describedby="passwordField" placeholder="Пароль" required>
                <#if errorPassword??>
                    <div class="invalid-feedback">
                        ${errorPassword}
                    </div>
                </#if>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit" class="btn mx-1" style="background: #cfff85">Зарегистрироваться</button>
        </form>
        <a href="/login" class="btn mx-1 my-2" style="background: #faffef">Отмена</a>
    </div>
</@wrapper.page>

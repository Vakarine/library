<#import "macro/mainTemplate.ftl" as wrapper>
<#import "/spring.ftl" as spring>

<@wrapper.page>
    <div>
        <form action="/login" method="post">
            <div class="input-group my-1">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="usernameField" >
                        Логин
                    </span>
                </div>
                <input type="text" class="form-control" name="username" aria-describedby="usernameField" placeholder="Логин">
            </div>
            <div class="input-group my-1">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="passwordField" >
                        Пароль
                    </span>
                </div>
                <input type="password" class="form-control" name="password" aria-describedby="passwordField" placeholder="Пароль">
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit" class="btn mx-1" style="background: #cfff85">Войти</button>
        </form>
        <a href="/registration" class="btn mx-1 my-2" style="background: #faffef">Регистрация</a>
    </div>
</@wrapper.page>

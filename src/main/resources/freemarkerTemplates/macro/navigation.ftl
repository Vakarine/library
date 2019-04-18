<#include "security.ftl">
<#import "/spring.ftl" as spring>


<nav class="navbar navbar-expand-lg navbar-light" style="background-color: #FFCC73;">
    <div class="navbar-nav mr-auto">
        <a class="nav-link" href="/" style="background-color: #FFCC73;"><@spring.message "buttonName.main_page" /></a>
        <a class="nav-link" href="/book" style="background-color: #FFCC73;"><@spring.message "buttonName.books" /></a>
        <div class="nav-item dropdown">
            <button class="btn nav-link" data-toggle="dropdown" data-target="#genres" aria-expanded="false" style="background-color: #FFCC73;">
                <@spring.message "buttonName.genres" />
            </button>
            <div class="dropdown-menu mr-2" aria-labelledby="genres">
                <a class="dropdown-item" href="/search?genre=Научная фантастика"><@spring.message "genreName.sciFi" /></a>
                <a class="dropdown-item" href="/search?genre=Фентези"><@spring.message "genreName.Fantasy" /></a>
                <a class="dropdown-item" href="/search?genre=Магия"><@spring.message "genreName.Magic" /></a>
                <a class="dropdown-item" href="/search?genre=Романтика"><@spring.message "genreName.Romance" /></a>
                <a class="dropdown-item" href="/search?genre=Историческое"><@spring.message "genreName.Historical" /></a>
            </div>
        </div>
        <a class="nav-link" href="/tag" style="background-color: #FFCC73;"><@spring.message "buttonName.tags" /></a>
    </div>

    <div class="nav-item dropdown mr-2">
        <div class="dropdown-menu" aria-labelledby="account">
            <#if !known>
                <a class="dropdown-item" href="/login"><@spring.message "user.login" /></a>
            </#if>
            <#if known>
                <a class="dropdown-item" href="/user/profile"><@spring.message "user.profile" /></a>
                <div class="dropdown-divider"></div>
                <a class="dropdown-item" href="/user/library"><@spring.message "user.library" /></a>
                <#if user.roles?seq_contains("MODERATOR")>
                <div class="dropdown-divider"></div>
                <a class="dropdown-item" href="/admin/user"><@spring.message "user.moderator_panel" /></a>
                </#if>
                <div class="dropdown-divider"></div>
                <a class="dropdown-item" href="#"><@spring.message "user.options" /></a>
                <div class="dropdown-divider"></div>
                <form action="/logout" method="post">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <button type="submit" class="btn dropdown-item" style="background: #ffffff"><@spring.message "user.logout" /></button>
                </form>
            </#if>
        </div>
        <button class="btn nav-link dropdown-toggle" data-toggle="dropdown" data-target="#account" aria-expanded="false" style="background-color: #FFCC73;">
            <#if !known>
                <@spring.message "unkownUser" />
            <#else>
                ${name}
            </#if>
        </button>
    </div>

    <form class="form-inline my-2 my-lg-0" action="/search" method="get">
        <input class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search" name="name">
        <button class="btn btn-outline-success my-2 my-sm-0" type="submit"><@spring.message "buttonName.search" /></button>
    </form>
</nav>
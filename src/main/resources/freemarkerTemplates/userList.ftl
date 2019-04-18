<#import "macro/mainTemplate.ftl" as wrapper>

<@wrapper.page>
    <table class="table">
        <thead>
        <th scope="col">#</th>
        <th scope="col">Никнейм</th>
        <th scope="col">Роли</th>
        <th scope="col">Редактировать</th>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td><#list user.roles as role>${role}<#sep>, </#list></td>
                <td><a href="/admin/user/${user.id}" class="btn">Редактировать</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@wrapper.page>

<#import "/spring.ftl" as spring>

<#macro recursiveComment comments bookId user>
    <#list comments as comment>
        <div>
            <div class="my-2 border" style="width: 95%; margin-left: 2em">
                ${comment.content}<br>
                ${comment.author.username}<br>

                <#if user.id == comment.author.id || user.roles?seq_contains("MODERATOR")>
                    <a href="/book/${book.id}/${comment.id}/cm_remove" class="text-decoration-none"><img
                                src="/images/Delete.svg" style="width: 45px; height: 45px"></a>
                    <a href="/book/${book.id}/${comment.id}/cm_edit" class="text-decoration-none"><img src="/images/Edit.svg" style="width: 45px; height: 45px"></a>

                </#if>
                <button class="btn" data-toggle="collapse" href="#answerCollapse${comment.id}" aria-expanded="false">
                    <@spring.message "bookFields.comment_answer" />
                </button>
                <div class="collapse" id="answerCollapse${comment.id}">
                    <form action="/book/${bookId}/${comment.id}/answer" method="post">
                        <div class="input-group">
                            <input type="text" class="form-control" name="content" placeholder="Комментарий" required>
                        </div>
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <button type="submit" class="btn" style="background: #ffe95c"><@spring.message "bookFields.comment_answer" /></button>
                    </form>
                </div>
                <#if comment.answers??>
                    <@recursiveComment comment.answers bookId user>
                    </@recursiveComment>
                </#if>
            </div>
        </div>
    </#list>
</#macro>
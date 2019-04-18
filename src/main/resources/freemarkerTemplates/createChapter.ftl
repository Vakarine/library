<#import "macro/mainTemplate.ftl" as wrapper>
<#import "/spring.ftl" as spring>

<@wrapper.page>
    <div>
        <form action="/book/<#if chapter??>${bookId}/${chapter.id}/ch_edit<#else>addChapter/${bookId}</#if>" method="post">
            <button class="btn my-2 btn-block" style="background: rgba(255,138,126,0.36)" type="submit">Добавить главу
            </button>
            <div class="input-group mb-my-3">
                <div class="input-group input-group-lg mb-3">
                    <input type="text" name="name" class="form-control ${(nameError??)?string("is-invalid","")}"
                           placeholder="Название главы" value="<#if chapter??>${chapter.name}</#if>" required/>
                    <#if nameError??>
                        <div class="invalid-feedback">
                            ${nameError}
                        </div>
                    </#if>
                </div>

                <div class="input-group mb-3">
                    <body onload="init('chapterContent')">
                    <textarea class="form-control ${(contentError??)?string("is-invalid", "")}" id="chapterContent"
                              name="content" rows="7" placeholder="Введите главу" required><#if chapter??>${chapter.content}</#if></textarea>
                    </body>
                    <#if contentError??>
                        <div class="invalid-feedback">
                            ${contentError}
                        </div>
                    </#if>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
        </form>
    </div>
</@wrapper.page>

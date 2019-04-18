<#import "macro/mainTemplate.ftl" as wrapper>
<#import "/spring.ftl" as spring>

<@wrapper.page>
    <div>
        <form action="/book/<#if book??>${book.id}/edit<#else>add</#if>" method="post" enctype="multipart/form-data">
            <button class="btn my-2 btn-block" style="background: rgba(255,138,126,0.36)" type="submit">Создать книгу</button>
            <div class="input-group mb-3 my-3">
                <div class="input-group input-group-lg mb-3">
                    <input type="text mx-my-1" name="title" class="form-control ${(titleError??)?string("is-invalid", "")}" value="<#if book??>${book.title}</#if>" placeholder="Название книги" required/>
                    <#if titleError??>
                        <div class="invalid-feedback">
                            ${titleError}
                        </div>
                    </#if>
                </div>

                <div class="custom-file">
                    <input type="file" name="image" class="custom-file-input" id="imageInput" aria-describedby="inputGroupFileAddon01">
                    <label class="custom-file-label" for="imageInput">Choose file</label>
                </div>

                <input type="text mx-1 my-1" name="tags" class="form-control" placeholder="Теги через запятую" value="<#if book??><#list bookTags as tag>${tag.name}<#sep>, </#list></#if>" multiple/>

                <div class="input-group my-2 mx-2">
                    <div class="input-group-pretend">
                        <label class="input-group-text" for="genreSelect">Жанр</label>
                    </div>
                    <select class="custom-select ${(genreError??)?string("is-invalid", "")}" id="genreSelect" name="genre" required>
                        <option selected disabled>Выбери жанр</option>
                        <option value="Научная фантастика">Научная фантастика</option>
                        <option value="Романтика">Романтика</option>
                        <option value="Фэнтези">Фэнтези</option>
                        <option value="Историческое">Историческое</option>
                        <option value="Магия">Магия</option>
                    </select>
                    <#if genreError??>
                        <div class="invalid-feedback">
                            ${genreError}
                        </div>
                    </#if>
                </div>

                <div class="input-group input-group-sm mb-3">
                    <div class="input-group mb-3" style="height: 25rem">
                    <body onload="init('description')">
                    <textarea name="description" id="description" class="form-control ${(descriptionError??)?string("is-invalid", "")}" rows="7" placeholder="Описание" required><#if book??>${book.description}</#if></textarea>
                    <#if descriptionError??>
                        <div class="invalid-feedback">
                            ${descriptionError}
                        </div>
                    </#if>
                    </body>
                </div>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}">
            </div>
        </form>
    </div>
</@wrapper.page>

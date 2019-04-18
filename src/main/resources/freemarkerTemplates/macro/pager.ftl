<#macro main ref pages>
    <#if pages??>
        <#assign total = pages.totalPages
        current = pages.number
        before = 3
        after = 3
        start = (current > 3)?then(current - before, 1)
        end = (current < total - 3)?then(current + after, total)
        pagesAvaible = start..end
        >
        <nav aria-label="Page navigation example">
            <ul class="pagination">
                <li class="page-item disabled">
                    <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Страницы</a>
                </li>

                <#if start gt 1>
                    <li class="page-item"><a class="page-link" href="/${ref}?page=0">1</a></li>
                    <li class="page-item disabled"><a class="page-link" href="/${ref}?page=0">...</a></li>
                </#if>

                <#list pagesAvaible as p>
                    <#if pages.getNumber() + 1 == p>
                        <li class="page-item active"><a class="page-link" href="/${ref}?page=${p}">${p}</a></li>
                    <#else>
                        <li class="page-item"><a class="page-link" href="/${ref}?page=${p - 1}">${p}</a></li>
                    </#if>
                </#list>

                <#if end < total-1>
                    <li class="page-item disabled"><a class="page-link" href="/${ref}?page=0">...</a></li>
                    <li class="page-item"><a class="page-link"
                                             href="/${ref}?page=${pages.totalPages - 1}">${pages.totalPages}</a>
                    </li>
                </#if>
            </ul>
        </nav>
    </#if>
    <#nested>
</#macro>

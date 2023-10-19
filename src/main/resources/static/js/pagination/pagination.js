import {ATag, LiTag} from "../common/tagUtil.js";

export function createPagination(response, space) {
    const currentPageNo = response.pageable.pageNumber + 1;
    const totalPageCount = response.totalPages;

    const pageSection = Math.floor(currentPageNo / 5);
    const pageBtnClassName = `page-btn`;
    let lastPageNo = 0;
    let firstPageNo = pageSection * 5 + 1;

    const pageBtnSpace = document.querySelector("#pagination-space");

    for (let pageNo = pageSection * 5 + 1; isTurnedTotalPage(pageNo, totalPageCount, pageSection); pageNo++) {
        createPageBtn(pageNo, currentPageNo, pageBtnClassName, pageBtnSpace);
        lastPageNo = pageNo;
    }

    createNextPageBtn(lastPageNo, totalPageCount, pageBtnClassName, pageBtnSpace);
    createPreviousBtn(pageSection, firstPageNo, pageBtnClassName, pageBtnSpace);
}

function isTurnedTotalPage(pageNo, totalPageCount, pageSection) {
    return pageNo <= totalPageCount && pageNo < pageSection * 5 + 6;
}

function createPageBtn(pageNo, currentPageNo, pageBtnClassName, pageBtnSpace) {
    let selectedPage = pageNo === currentPageNo ? " active" : "";

    const li = new LiTag()
        .setClassName("page-item")
        .setInnerHTML([new ATag()
            .setClassName(pageBtnClassName + selectedPage + " " + "page-link")
            .setDataset([{pageNo: pageNo}])
            .setHref("#")
            .setInnerHTML(pageNo)
        ]);

    pageBtnSpace.appendChild(li.getTag());
}

function createNextPageBtn(lastPageNo, totalPageCount, pageBtnClassName, pageBtnSpace) {
    // 즉 현재 페이지 섹션의 마지막 페이지 번호가 페이지 토탈보다 작으면 넥스트 버튼 출력
    if (lastPageNo < totalPageCount) {
        const li = new LiTag()
            .setClassName("page-item")
            .setInnerHTML([new ATag()
                .setClassName(pageBtnClassName + " next-btn page-link")
                .setDataset([{pageNo: lastPageNo + 1}])
                .setHref("#")
                .setInnerHTML(">")
            ]);

        pageBtnSpace.appendChild(li.getTag());
    }
}

function createPreviousBtn(pageSection, firstPageNo, pageBtnClassName, pageBtnSpace) {
    // 5보다 큰 섹션이라면 이전 버튼 추가
    if (pageSection > 0) {
        const li = new LiTag()
            .setClassName("page-item")
            .setInnerHTML([new ATag()
                .setClassName(pageBtnClassName + " before-btn page-link")
                .setDataset([{pageNo: firstPageNo - 5}])
                .setHref("#")
                .setInnerHTML("<")
            ]);

        pageBtnSpace.insertBefore(li.getTag(), pageBtnSpace.firstChild);
    }
}
import {ButtonTag, DivTag} from "../common/tagUtil.js";


export function createPagination(response, space) {
    const currentPageNo = response.pageable.pageNumber + 1;
    const totalPageCount = response.totalPages;

    const pageSection = Math.floor(currentPageNo / 5);
    const pageBtnClassName = `page-btn`;
    let lastPageNo = 0;
    let firstPageNo = pageSection * 5 + 1;

    const pageBtnSpace = new DivTag()
        .setId("page-btn-space")
        .getTag();

    for (let pageNo = pageSection * 5 + 1; isTurnedTotalPage(pageNo, totalPageCount, pageSection); pageNo++) {
        createPageBtn(pageNo, currentPageNo, pageBtnClassName, pageBtnSpace);
        lastPageNo = pageNo;
    }

    createNextPageBtn(lastPageNo, totalPageCount, pageBtnClassName, pageBtnSpace);
    createPreviousBtn(pageSection, firstPageNo, pageBtnClassName, pageBtnSpace);

    space.appendChild(pageBtnSpace);
}

function isTurnedTotalPage(pageNo, totalPageCount, pageSection) {
    return pageNo <= totalPageCount && pageNo < pageSection * 5 + 6;
}

function createPageBtn(pageNo, currentPageNo, pageBtnClassName, pageBtnSpace) {
    let selectedPage = pageNo === currentPageNo ? " btn-on" : "";

    const pageBtn = new ButtonTag()
        .setClassName(pageBtnClassName + selectedPage)
        .setDataset([{pageNo: pageNo}])
        .setInnerHTML(pageNo)
        .getTag();

    pageBtnSpace.appendChild(pageBtn);
}

function createNextPageBtn(lastPageNo, totalPageCount, pageBtnClassName, pageBtnSpace) {
    // 즉 현재 페이지 섹션의 마지막 페이지 번호가 페이지 토탈보다 작으면 넥스트 버튼 출력
    if (lastPageNo < totalPageCount) {
        const nextPageBtn = new ButtonTag()
            .setClassName(pageBtnClassName + " next-btn")
            .setDataset([{pageNo: lastPageNo + 1}])
            .setInnerHTML(">")
            .getTag();
        pageBtnSpace.appendChild(nextPageBtn);
    }
}

function createPreviousBtn(pageSection, firstPageNo, pageBtnClassName, pageBtnSpace) {
    // 5보다 큰 섹션이라면 이전 버튼 추가
    if (pageSection > 0) {
        const nextPageBtn = new ButtonTag()
            .setClassName(pageBtnClassName + " before-btn")
            .setDataset([{pageNo: firstPageNo - 5}])
            .setInnerHTML("<")
            .getTag();
        pageBtnSpace.insertBefore(nextPageBtn, pageBtnSpace.firstChild);
    }
}
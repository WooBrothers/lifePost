import {createPagination} from "./pagination.js";

export function bindPaginationBtnEvent(parentSpaceId, childClassName, renderFunc, eventFunc) {

    const paginationLiList = document.querySelectorAll(".page-item");
    paginationLiList.forEach(btn => {
        btn.addEventListener("click", (event) => {
            clickPaginationBtn.call(event, parentSpaceId, childClassName, renderFunc, eventFunc);
        });
    });
}

async function clickPaginationBtn(parentSpaceId, childClassName, renderFunc, eventFunc) {
    // 클릭한 페이지 현재 페이지면 무시
    const clickedBtn = this.target;

    if (isSamePageButtonClick(clickedBtn)) {
        return;
    }

    const parentSpace = document.getElementById(parentSpaceId);

    removeListElementFromParent(parentSpace, childClassName);

    // 현재 페이지 번호 조회
    const pageNo = clickedBtn.dataset.pageNo;
    // 페이지 새로 그리기
    const response = await renderFunc(parentSpace, pageNo, eventFunc);

    // 이전 페이지 버튼 온 끄기
    setPageBtnOff();
    // 클릭한 현재 페이지 버튼 켜기
    setClickPageBtnOn(response, clickedBtn);

    // 만약 넥스트 버튼이나 이전 버튼을 클릭한 것이라면
    if (clickedBtn.classList.contains("next-btn") || clickedBtn.classList.contains("before-btn")) {
        // 현재 페이지네이션 영역 삭제
        const cardListPaginationSpace = document.getElementById("pagination-space");

        cardListPaginationSpace.replaceChildren();
        // 패이지 네이션 새로 그리기
        createPagination(response, cardListPaginationSpace);
        bindPaginationBtnEvent(parentSpaceId, childClassName, renderFunc, eventFunc);
    }
}

function isSamePageButtonClick(clickedBtn) {
    return clickedBtn === document.querySelector(".page-btn.active");
}

function removeListElementFromParent(parentSpace, childClassName) {
    const children = document.querySelectorAll(`.${childClassName}`);

    children.forEach(child => {
        parentSpace.removeChild(child);
    })
}

function setPageBtnOff() {
    const currentOnPageBtn = document.querySelector(".page-btn.active");
    currentOnPageBtn.classList.remove("active");
}

function setClickPageBtnOn(response, clickedBtn) {
    // 클릭된 페이지 버튼의 페이지 번호 설정
    clickedBtn.dataset.pageNo = response.pageable.pageNumber + 1;
    // 현재 클릭한 버튼 활성화
    clickedBtn.classList.add("active");
}
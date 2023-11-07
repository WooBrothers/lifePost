import {animateCSS, BottomNavigationManager} from "../common/utilTool.js";

bindEventToBottomNavigation();

function renderBottomNavigationBtn() {
    // 맨처음 유저 접근
    // 저장소 정보 없음 -> 기본값 설정
    // 네브바 레터로 세팅

    // 다음 유저 접근
    // 카드 페이지 클릭
    // 저장소 -> 카드 페이지 저장
    // 카드 페이지 로딩 + 바텀 네브바 로딩
    // 네브바 색칠 처리
}

function bindEventToBottomNavigation() {
    const bottomNavBtnList = document.querySelectorAll(".bottom-nav-btn");

    bottomNavBtnList.forEach(btn => {
        btn.addEventListener("click", clickBottomNavBtn);
    });
}

function clickBottomNavBtn() {
    const btnList = document.querySelectorAll(".bottom-nav-btn");

    // 모든 버튼 끄기
    btnList.forEach(btn => {
        const btnOnImg = btn.querySelector(".nav-btn-img-on");
        const btnOffImg = btn.querySelector(".nav-btn-img-off");

        setOffImg(btnOnImg, btnOffImg);
    });

    // 어떤 네비게이션 버튼이 클릭됐는지 로컬 스토리지에 저장
    // -> 페이지 이동 후에도 네비게이션 바의 상태 유지를 위해
    setPageOnInfoToLocalStorage();

    // 현재 버튼만 켜기
    const currentBtnOnImg = this.querySelector(".nav-btn-img-on");
    const currentBtnOffImg = this.querySelector(".nav-btn-img-off");

    setOnImg(currentBtnOnImg, currentBtnOffImg);
    animateCSS(`#${currentBtnOnImg.id}`, "rubberBand").then();
}

function setOnImg(btnOnImg, btnOffImg) {
    btnOnImg.classList.remove("d-none");
    btnOffImg.classList.add("d-none");
}

function setOffImg(btnOnImg, btnOffImg) {
    btnOnImg.classList.add("d-none");
    btnOffImg.classList.remove("d-none");
}

function setPageOnInfoToLocalStorage() {
    const btnManager = new BottomNavigationManager();

    switch (this.dataset.type) {
        case "letter":
            btnManager.setLetterPageOn();
            break;
        case "card":
            btnManager.setCardPageOn();
            break;
        case "motive":
            btnManager.setMotivePageOn();
            break;
    }
}
import {getLetterContentById, getLimitedLetterContentToLogoutMember} from "../letterApi.js";
import {isTokenExpired} from "../../common/utilTool.js";

setLetterInfo();

function setLetterInfo() {
    const letterId = localStorage.getItem("letterId");

    if (!isTokenExpired()) {
        getLetterContentById(letterId).then(res => {
            setLetterPageByResponse(res);
        });
    } else {
        getLimitedLetterContentToLogoutMember(letterId).then(res => {
            setLetterPageByResponse(res);
            // 로그인 페이지 이동 안내 div 출력
            renderLoginInduce();
        });

        // 로그인 페이지 이동 버튼 이벤트
        bindEventToLetterReadPage();
    }
}

function setLetterPageByResponse(res) {
    const letterImage = res.letterImage;
    const title = res.title;
    const postDate = res.postDate;
    const writer = res.writer;
    const contents = res.contents;

    const letterImageDiv = document.getElementById("letter-img");
    letterImageDiv.style.backgroundImage = `url(${letterImage})`;

    const titleDiv = document.getElementById("letter-title");
    titleDiv.innerHTML = title;

    const PostDateDiv = document.getElementById("letter-post-date");
    const dates = postDate.split("-");
    PostDateDiv.innerHTML = dates[0] + "년 " + dates[1] + "월 " + dates[2] + "일";

    const writerDiv = document.getElementById("author-name");
    writerDiv.innerHTML = "<b>Author</b> " + writer;

    const contentsDiv = document.getElementById("letter-content");
    contentsDiv.innerHTML = contents;

    wrapImgTagInContentsDiv(contentsDiv);
}

function renderLoginInduce() {
    const loginInduceDiv = document.querySelector("#login-page-div");
    loginInduceDiv.style.display = "block";
}

function wrapImgTagInContentsDiv(contentsDiv) {
    const images = contentsDiv.querySelectorAll("img");

    images.forEach(image => {
        image.classList.add("img-fluid");
        image.style.objectFit = "cover";
    });
}

function bindEventToLetterReadPage() {
    const MoveLoginPageBtn = document.querySelector("#go-to-login-page-btn");
    MoveLoginPageBtn.addEventListener("click", () => {
        window.location = "/login/page";
    });
}
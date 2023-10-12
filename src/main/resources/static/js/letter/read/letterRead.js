import {getLetterContentById, getLimitedLetterContentToLogoutMember} from "../letterApi.js";
import {copyToClipboard, isTokenExpired} from "../../common/utilTool.js";
import {createCardBtnClick} from "../../card/list/cardListEvent.js";

setLetterInfo();

function setLetterInfo() {
    // const letterId = localStorage.getItem("letterId");
    const letterId = document.querySelector("#read-letter-space").dataset.letterId;
    if (!isTokenExpired()) {
        getLetterContentById(letterId).then(res => {
            setLetterPageByResponse(res);
            setJsonLdInfo(res);
            setOGMetaTag(res, letterId);
        });
        bindEventIfLogin();
    } else {
        getLimitedLetterContentToLogoutMember(letterId).then(res => {
            setLetterPageByResponse(res);
            renderLoginInduce(); // 로그인 페이지 이동 안내 div 출력
            setJsonLdInfo(res);
            setOGMetaTag(res, letterId);
        });
        // 로그아웃 시 로그인 페이지 이동 버튼 이벤트
        bindEventIfLogout();
    }
}

function setLetterPageByResponse(res) {
    const letterImage = res.letterImage;
    document.title = res.title + " - Life Post";
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

function bindEventIfLogout() {
    const MoveLoginPageBtn = document.querySelector("#go-to-login-page-btn");
    MoveLoginPageBtn.addEventListener("click", () => {
        window.location = "/login/page";
    });

    const copyLinkBtn = document.querySelector("#copy-link-btn-mb");
    copyLinkBtn.addEventListener("click", clickCopyLinkBtn);

    const writeCardBtn = document.querySelector("#card-write-btn-mb");
    writeCardBtn.addEventListener("click", () => {
        window.location = "/login/page";
    });
}

function setJsonLdInfo(res) {
    const description = res.description ? res.description : "당신의 생각을 바꿀 편지.";
    const jsonLdData = {
        "@context": "http://schema.org", // Schema.org 컨텍스트 URL
        "@type": "Article", // 아티클 유형
        "headline": res.title, // 아티클 제목
        "image": res.letterImage, // 이미지 URL
        "datePublished": res.postDate, // 게시 날짜
        "description": description, // 아티클 설명
        "author": {
            "@type": "Person",
            "name": res.writer // 작성자 이름
        },
        "publisher": {
            "@type": "Organization",
            "name": "Life Post", // 웹사이트 이름
            "logo": {
                "@type": "ImageObject",
                "url": `https://${window.location.hostname}/img/full-logo` // 웹사이트 로고 URL
            }
        },
        "mainEntityOfPage": {
            "@type": "WebPage",
            "@id": `https://${window.location.hostname}` // 웹사이트 URL
        }
    };

    const script = document.createElement('script');
    script.type = 'application/ld+json';
    script.innerHTML = JSON.stringify(jsonLdData);
    document.head.appendChild(script);
}


function bindEventIfLogin() {
    const copyLinkBtn = document.querySelector("#copy-link-btn-mb");
    copyLinkBtn.addEventListener("click", clickCopyLinkBtn);

    const writeCardBtn = document.querySelector("#card-write-btn-mb");
    writeCardBtn.addEventListener("click", clickWriteCardBtn);
}

function clickCopyLinkBtn() {
    const currentURL = window.location.href;
    copyToClipboard(currentURL);

    const toastLiveExample = document.getElementById('liveToast')

    const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample)
    toastBootstrap.show()
}

function clickWriteCardBtn() {
    createCardBtnClick();
}

function setOGMetaTag(response, letterId) {
    const title = response.title;
    const content = response.description;
    const image = response.letterImage;
    const url = `https://${window.location.hostname}/letter/raed/page/${letterId}`;

    const head = document.head;

    // og:title 메타 태그 생성
    const ogTitleTag = document.createElement("meta");
    ogTitleTag.setAttribute("property", "og:title");
    ogTitleTag.setAttribute("content", title);
    head.appendChild(ogTitleTag);

    // og:description 메타 태그 생성
    const ogDescriptionTag = document.createElement("meta");
    ogDescriptionTag.setAttribute("property", "og:description");
    const description = content ? content : "당신의 생각을 바꿀 편지.";
    ogDescriptionTag.setAttribute("content", description);
    head.appendChild(ogDescriptionTag);

    // og:url 메타 태그 생성
    const ogUrlTag = document.createElement("meta");
    ogUrlTag.setAttribute("property", "og:url");
    ogUrlTag.setAttribute("content", url);
    head.appendChild(ogUrlTag);

    // og:image 메타 태그 생성
    const ogImageTag = document.createElement("meta");
    ogImageTag.setAttribute("property", "og:image");
    ogImageTag.setAttribute("content", image);
    head.appendChild(ogImageTag);
}
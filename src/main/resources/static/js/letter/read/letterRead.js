import {LetterApi} from "../letterApi.js";

setLetterInfo();

function setLetterInfo() {
    const letterId = localStorage.getItem("letterId");

    LetterApi.getLetterContentById(letterId).then(res => {
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
    })
}


function wrapImgTagInContentsDiv(contentsDiv) {
    const images = contentsDiv.querySelectorAll("img");

    images.forEach(image => {
        image.classList.add("img-fluid");
        image.style.objectFit = "cover";
    });
}
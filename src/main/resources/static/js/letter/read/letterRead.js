import {LetterApi} from "../letterApi.js";

function setLetterInfo() {
    const letterId = localStorage.getItem("letterId");

    LetterApi.getLetterContentById(letterId).then(res => {
        const letterImage = res.letterImage;
        const title = res.title;
        const createdDate = res.createdDate;
        const writer = res.writer;
        const contents = res.contents;

        const letterImageDiv = document.getElementById("letter-img");
        letterImageDiv.style.backgroundImage = `url(${letterImage})`;

        const titleDiv = document.getElementById("letter-title");
        titleDiv.innerHTML = title;

        const createdDateDiv = document.getElementById("letter-post-date");
        const dates = createdDate.split("-");
        createdDateDiv.innerHTML = dates[0] + "년 " + dates[1] + "월 " + dates[2] + "일";

        const writerDiv = document.getElementById("author-name");
        writerDiv.innerHTML = "<b>Author</b> " + writer;

        const contentsDiv = document.getElementById("letter-content");
        contentsDiv.innerHTML = contents;
    })
}

setLetterInfo();
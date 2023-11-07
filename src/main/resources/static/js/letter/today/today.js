import {getLatestLetter} from "../letterApi.js";
import {readLetterPage} from "../../common/utilTool.js";

await renderTodayComp();

async function renderTodayComp() {

    await getLatestLetter().then(res => {
        setLetterInfoByResponse(res);
    });

    bindTodayLetterEvent();
}

function setLetterInfoByResponse(response) {
    const todaySpace = document.querySelector("#index-letter-space");
    todaySpace.dataset.letterId = response.id;

    const img = document.querySelector("#today-img");
    img.src = response.letterImage;

    const title = document.querySelector("#today-letter-title");
    title.innerHTML = response.title;

    const contents = document.querySelector("#today-letter-contents");
    contents.innerHTML = response.contents.slice(0, 150) + "...";
}

function bindTodayLetterEvent() {
    const todaySpace = document.querySelector("#index-letter-space");
    todaySpace.addEventListener("click", clickTodaySpace);
}

function clickTodaySpace() {
    const letterId = document.querySelector("#index-letter-space").dataset.letterId;
    readLetterPage(letterId);
}
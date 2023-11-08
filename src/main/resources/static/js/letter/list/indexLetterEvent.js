import {clickLetter} from "./letterListEvent.js";
import {createOpenLetterListSpace} from "./letterList.js";

export function bindEventToIndexLetterPage() {
    // 편지들
    const letterSpaceList = document.querySelectorAll(".letter-space");
    letterSpaceList.forEach(space => {
        space.addEventListener("click", clickLetter);
    });

    // 편지를 담는 공간
    window.addEventListener("scroll", scrollIndexLetters);
}

async function scrollIndexLetters() {
    if (window.innerHeight + window.scrollY >= document.body.offsetHeight) {

        const indexLetterSpace = document.querySelector("#letter-list-space");

        const postDate = getLastLetterPostDateFromLetterSpace(indexLetterSpace);

        await createOpenLetterListSpace(indexLetterSpace, postDate, bindEventToIndexLetterPage);
    }
}

function getLastLetterPostDateFromLetterSpace(indexLetterSpace) {

    const lastLetter = indexLetterSpace.lastChild;

    return lastLetter.dataset.postDate;
}

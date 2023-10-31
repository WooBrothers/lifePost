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

        const letterId = getLastLetterIdFromLetterSpace(indexLetterSpace);
        await createOpenLetterListSpace(indexLetterSpace, letterId, bindEventToIndexLetterPage);
    }
}

function getLastLetterIdFromLetterSpace(indexLetterSpace) {

    const lastLetter = indexLetterSpace.lastChild;

    return lastLetter.dataset.letterId;
}

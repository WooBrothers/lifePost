import {LetterApi} from "../letterApi.js";

await renderTodayComp();

async function renderTodayComp() {

    await LetterApi.getLatestLetter().then(res => {
        setLetterInfoByResponse(res);
    });

    setInterval(setTime, 1000);

    bindTodayLetterEvent();
}

function setLetterInfoByResponse(response) {
    const todaySpace = document.querySelector("#today-space");
    todaySpace.dataset.letterId = response.id;

    const img = document.querySelector("#today-img");
    img.src = response.letterImage;

    const hourglass = document.querySelector("#hourglass");
    hourglass.src = '/img/sand-time.png';

    const timer = document.querySelector("#today-timer");
    timer.innerHTML = getTime();

    const title = document.querySelector("#today-letter-title");
    title.innerHTML = response.title;

    const contents = document.querySelector("#today-letter-contents");
    contents.innerHTML = response.contents.slice(0, 75) + "...";
}

function getTime() {

    const time = new Date();

    let hours = 23 - time.getHours();
    let minutes = 59 - time.getMinutes();
    let seconds = 59 - time.getSeconds();

    return `${hours < 10 ? `0${hours}` : hours} : ${minutes < 10 ? `0${minutes}` : minutes} : ${seconds < 10 ? `0${seconds}` : seconds}`;
}

function setTime() {
    const timeDiv = document.getElementById("today-timer");

    timeDiv.innerHTML = getTime();
}

function bindTodayLetterEvent() {
    const todaySpace = document.querySelector("#today-space");
    todaySpace.addEventListener("click", clickTodaySpace);
}

function clickTodaySpace() {
    const todaySpace = document.querySelector("#today-space");
    localStorage.setItem("letterId", todaySpace.dataset.letterId);
    window.location = "/letter/read/page";
}
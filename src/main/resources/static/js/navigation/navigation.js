import {getAccessToken} from "../common/apiUtil.js";
import {bindNavigationEvent} from "./navigationEvent.js";

await setMembership();
bindNavigationEvent();

export async function setMembership() {
    if (!getAccessToken()) {
        setLogoutNavMenu();
    } else {
        setLoginNavMenu();
    }
}

function setLogoutNavMenu() {
    const nameLinkDict = {
        "navLetter": ["/letter/list/page", "편지"],
        "navIntroduction": ["/introduction", "서비스 소개"],
        "navLogin": ["/login/page", "로그인/회원가입"],
    };

    createLiByDict(nameLinkDict);
}

function setLoginNavMenu() {
    const nameLinkDict = {
        "navLetter": ["/letter/list/page", "편지"],
        "navCard": ["/card/list/page", "카드"],
        "navMypage": ["/mypage/page", "마이페이지"],
        "navLogout": ["", "로그아웃"],
    };

    createLiByDict(nameLinkDict);
}

function createLiByDict(nameLinkDict) {
    for (const key in nameLinkDict) {
        const li = document.createElement("li");
        li.classList.add("nav-item");
        li.classList.add("ms-auto");

        const a = document.createElement("a");
        a.classList.add("nav-link");
        a.classList.add("active");

        if (nameLinkDict.hasOwnProperty(key)) {
            a.href = nameLinkDict[key][0];
            a.innerHTML = nameLinkDict[key][1]
            a.id = key;
        }

        li.append(a);
        document.querySelector("#navbarContentUl").append(li);
    }
}
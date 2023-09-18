import {getAccessToken} from "./apiUtil.js";

const randomRgb = ["#FF6D60", "#F7D060", "#F3E99F", "#98D8AA", "#FF9B9B", "#FFD6A5", "#FFFEC4", "#CBFFA9", "#FFB84C", "#FFBFA9", "#C0EEE4", "#F8F988", "#FFCAC8", "#FF9E9E", "#78C1F3", "#9BE8D8", "#9BE8D8", "#E2F6CA", "#F8FDCF"]

export function getColorHexInFiveTypeList() {
    const result = randomRgb[Math.floor(Math.random() * randomRgb.length)];
    return result;
}

export function findParentWithClass(element, className) {
    let parent = element.parentNode;

    while (parent) {
        if (parent.classList.contains(className)) {
            return parent;
        }

        parent = parent.parentNode;
    }

    return null; // 부모 요소를 찾지 못한 경우 null 반환
}

export function isScreenWide() {
    return window.innerWidth >= 991;
}

export function isTokenExpired() {
    try {
        const token = getAccessToken();
        const base64Payload = token.split('.')[1];
        const payload = JSON.parse(atob(base64Payload));

        const expTimestamp = payload.exp;

        if (!expTimestamp) {
            return true;  // 만료 시간이 없으면 만료된 것으로 간주합니다.
        }

        return Math.floor(Date.now() / 1000) > expTimestamp;
    } catch (error) {
        return true;  // 에러 발생 시 만료된 것으로 간주합니다.
    }
}

export function deleteCookie(cookieName) {
    document.cookie = `${cookieName}=; expires=${getCurrentUtcTime()}; path=/;`;
}

export function getCurrentUtcTime() {
    return new Date().toUTCString();
}

export function removeHTMLAndWhitespace(input) {
    return input.replace(/<\/?[^>]+(>|$)|&nbsp;|\s+/g, ' ').trim();
}

export class TodayCardWriteHistory {
    constructor() {
        this.goal = 10;
        if (this.isNeedToInitiate()) {
            this.date = getTodayDate();
            this.totalCount = 0;
            this.memberCards = {};
            this.isGetTodayStamp = false;
        } else {
            this.date = this.getTodayCardWriteHistory()["date"];
            this.totalCount = this.getTodayCardWriteHistory()["totalCount"];
            this.memberCards = this.getTodayCardWriteHistory()["memberCards"];
            this.isGetTodayStamp = this.getTodayCardWriteHistory()["isGetTodayStamp"];
        }
    }

    isNeedToInitiate() {
        return (!this.getTodayCardWriteHistory()
            || !this.getTodayCardWriteHistory()["date"]
            || this.getTodayCardWriteHistory()["date"] !== getTodayDate()
        )
    }

    getMemberCardCount(memberCardId) {
        if (!this.memberCards[memberCardId]) {
            this.memberCards[memberCardId] = 0;
        }
        return this.memberCards[memberCardId];
    }

    increaseWriteCount(memberCardId) {

        if (this.memberCards[memberCardId]) {
            this.memberCards[memberCardId]++;
        } else {
            this.memberCards[memberCardId] = 1;
        }

        this.totalCount++;
        return this;
    }

    achieveTodayStamp() {
        this.isGetTodayStamp = true;
    }

    isTotalCountMoreThanHundred() {
        return this.totalCount >= 100;
    }

    isTotalCountMoreThanGoal() {
        return this.totalCount >= this.goal;
    }

    save() {
        this.setTodayCardWriteHistory({
            "date": this.date,
            "totalCount": this.totalCount,
            "memberCards": this.memberCards,
            "isGetTodayStamp": this.isGetTodayStamp
        });
        return this;
    }

    getTodayCardWriteHistory() {
        return getLocalStorageJson("todayCardWriteHistory");
    }

    setTodayCardWriteHistory(data) {
        return setLocalStorageJson("todayCardWriteHistory", data);
    }
}


export function getLocalStorageJson(key) {
    return JSON.parse(localStorage.getItem(key)) || null;
}

export function setLocalStorageJson(key, data) {
    localStorage.setItem(key, JSON.stringify(data));
}

export function getTodayDate() {
    const today = new Date();

    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더함
    const day = String(today.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
}

export function convertHyphenToCamelCase(target) {
    return target.replace(/-([a-z])/g, function (match, letter) {
        return letter.toUpperCase();
    });
}

export const animateCSS = (element, animation, prefix = 'animate__') =>
    // We create a Promise and return it
    new Promise((resolve, reject) => {
        const animationName = `${prefix}${animation}`;
        const node = document.querySelector(element);

        node.classList.add(`${prefix}animated`, animationName);

        // When the animation ends, we clean the classes and resolve the Promise
        function handleAnimationEnd(event) {
            event.stopPropagation();
            node.classList.remove(`${prefix}animated`, animationName);
            resolve('Animation ended');
        }

        node.addEventListener('animationend', handleAnimationEnd, {once: true});
    });

export function bootstrapPopover() {
    const popoverTriggerList = document.querySelectorAll('[data-bs-toggle="popover"]')
    const popoverList = [...popoverTriggerList].map(popoverTriggerEl => new bootstrap.Popover(popoverTriggerEl))
}

export function isImageUrlValid(imageUrl, callback) {
    const image = new Image();

    image.onload = function () {
        // 이미지가 성공적으로 로드되면 URL이 유효합니다.
        callback(true);
    };

    image.onerror = function () {
        // 이미지 로딩에 실패하면 URL이 유효하지 않거나 이미지가 존재하지 않습니다.
        callback(false);
    };

    image.src = imageUrl;
}

export function hideModal(modalElement) {
    $(modalElement).modal("hide");
    // const modal = new bootstrap.Modal(modalElement);
    // modal.hide();
}

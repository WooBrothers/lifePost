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

function getCurrentUtcTime() {
    return new Date().toUTCString();
}
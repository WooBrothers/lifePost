import {getAccessToken} from "./apiUtil.js";
import {DivTag} from "./tagUtil.js";

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

/* time start */
export function getCurrentUtcTime() {
    return new Date().toUTCString();
}

export function calculateDateOffset(baseDate, offset) {
    let date = new Date(baseDate);
    date.setDate(baseDate.getDate() + offset);

    // 내일 날짜를 ISO 8601 형식(yyyy-MM-dd)의 문자열로 변환
    return date.toISOString().split('T')[0];
}

export function getTodayDate() {
    const today = new Date();

    return today.toISOString().split('T')[0];
}

/* time end */

export function removeHTMLAndWhitespace(input) {
    // 태그 및 공백 제거
    return input.replace(/<\/?[^>]+(>|$)/g, "");
}

export function removeImageTags(html) {
    // 이미지 태그를 제거하는 정규식 패턴
    const newRegex = /<p[^>]*>[\s\S]*?(<br\s*\/?>)[\s\S]*?<\/p>/gi;
    const oldRegex = /<img[^>]*>/g;
    return html.replace(newRegex, '');
}

export class TodayCardWriteHistory {
    // local storage 로 관리되는 카드 쓰기 관련 정보를 다루는 클래스
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


export function convertHyphenToCamelCase(target) {
    return target.replace(/-([a-z])/g, function (match, letter) {
        return letter.toUpperCase();
    });
}

export const animateCSS = (element, animation, prefix = 'animate__') =>
    // 애니메이트 css를 쉽게 사용하기 위한 함수 객체
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
    // 부트스트랩 팝오버를 쓰기 위한 함수
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

export function copyToClipboard(text) {
    const dummy = document.createElement("textarea");
    document.body.appendChild(dummy);

    dummy.value = text;
    dummy.select();

    document.execCommand("copy");
    document.body.removeChild(dummy);
}

export function readLetterPage(letterId) {
    window.location = `/letter/read/page/${letterId}`;
}

export function setFilterBtnOnOff(target) {
    const className = target.id.split("-btn")[0];
    const btnList = document.querySelectorAll(`.${className}`);

    if (target.dataset.onOff === "true") {
        btnList.forEach((btn) => {
            btn.dataset.onOff = "false";
            btn.classList.remove("active");
        });
    } else {
        btnList.forEach((btn) => {
            btn.dataset.onOff = "true"
            btn.classList.add("active");
        });
    }
}

export class OnboardingManager {
    constructor() {
        // 로컬 스토리지 키
        this.storageKey = "onboardingData";
        this.firstTimeVisit = "firstTimeVisit";
        this.firstLetterRead = "firstLetterRead";
        this.firstCardPageVisit = "firstCardPageVisit";

        // 기본 데이터 구조 (예: 페이지별로 방문 여부)
        this.data = {
            firstTimeVisit: false,
            firstLetterRead: false,
            firstCardPageVisit: false,
        };

        // 로컬 스토리지에서 데이터 불러오기
        this.loadData();
    }

    // 데이터 불러오기
    loadData() {
        const storedData = localStorage.getItem(this.storageKey);
        if (storedData) {
            this.data = JSON.parse(storedData);
        }
    }

    // 데이터 저장
    saveData() {
        localStorage.setItem(this.storageKey, JSON.stringify(this.data));
    }

    // 페이지 방문 여부 설정
    setVisited(pageName) {
        if (this.data.hasOwnProperty(pageName)) {
            this.data[pageName] = true;
            this.saveData();
        }
    }

    // 페이지 방문 여부 확인
    hasVisited(pageName) {
        return this.data[pageName];
    }

    // 각 페이지 방문 여부 설정 메서드
    setFirstTimeVisited() {
        this.setVisited(this.firstTimeVisit);
    }

    setFirstReadVisited() {
        this.setVisited(this.firstLetterRead);
    }

    setFirstCardPageVisited() {
        this.setVisited(this.firstCardPageVisit);
    }

    isFirstTimeVisit() {
        return !this.data.firstTimeVisit;
    }

    isFirstLetterReadVisit() {
        return !this.data.firstLetterRead;
    }

    isFirstCardPageVisit() {
        return !this.data.firstCardPageVisit;
    }
}

export class BottomNavigationManager {
    constructor() {
        // 로컬 스토리지 키
        this.storageKey = "bottomNavigationData";
        this.letterPageOn = "letterPageOn";
        this.cardPageOn = "cardPageOn";
        this.motivePageOn = "motivePageOn";

        // 기본 데이터 구조 (기본은 편지 페이지 보도록)
        this.pageOnInfo = {
            letterPageOn: true,
            cardPageOn: false,
            motivePageOn: false,
        };

        // 로컬 스토리지에서 데이터 불러오기
        this.loadData();
    }

    loadData() {
        const storedData = localStorage.getItem(this.storageKey);
        if (storedData) {
            this.pageOnInfo = JSON.parse(storedData);
        }
    }

    // 데이터 저장
    saveData() {
        localStorage.setItem(this.storageKey, JSON.stringify(this.pageOnInfo));
    }

    offAllPage() {
        this.pageOnInfo = {
            letterPageOn: false,
            cardPageOn: false,
            motivePageOn: false,
        }
    }

    setLetterPageOn() {
        this.offAllPage();
        this.pageOnInfo.letterPageOn = true;
        this.saveData();
    }

    setCardPageOn() {
        this.offAllPage();
        this.pageOnInfo.cardPageOn = true;
        this.saveData();
    }

    setMotivePageOn() {
        this.offAllPage();
        this.pageOnInfo.motivePageOn = true;
        this.saveData();
    }
}

export function createCoupangAdBannerInFeed(targetFeed) {
    /*
    피드성 게시물에 쿠팡 광고 삽입하는 함수 - 주의점 targetFeed는 피드 객체중 하나여야 하며,
    해당 피드의 offset 크기 데이터를 사용한다.
    함수는 targetFeed 객체가 dom에 추가된 이후에 사용할 것
    */

    const coupangPartnerAdSpace = new DivTag()
        .setClassName("coupang-partner-ad-space card h-100")
        .getTag();
    const paddingDiv = new DivTag()
        .setClassName("card-body d-flex flex-column align-items-center")
        .getTag();
    const coupangAdIframe = document.createElement("iframe");

    coupangAdIframe.src = "https://ads-partners.coupang.com/widgets.html?id=727012&template=carousel&trackingCode=AF0473134&subId=&width=300&height=440&tsource=";
    coupangAdIframe.height = `${targetFeed.offsetHeight}`;
    coupangAdIframe.frameBorder = "0";
    coupangAdIframe.scrolling = "no";
    coupangAdIframe.referrerPolicy = "unsafe-url";

    paddingDiv.appendChild(coupangAdIframe);
    coupangPartnerAdSpace.appendChild(paddingDiv);

    return coupangPartnerAdSpace;
}

export function isScrolledToBottom() {
    // 맨 아래로 스크롤되었는지 여부를 확인하는 로직
    const windowHeight = window.innerHeight;
    const documentHeight = document.documentElement.scrollHeight;
    const scrollPosition = window.scrollY;
    return windowHeight + scrollPosition >= documentHeight;
}

let isScrolling = false;

export function getScrolling() {
    return isScrolling;
}

export function setScrollingUse() {
    isScrolling = true;
}

export function setScrollingNotUse() {
    isScrolling = false;
}
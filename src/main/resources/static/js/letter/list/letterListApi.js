import {authFetch} from "../../common/apiUtil.js";

export async function getLetterList(pageNo, size, type, focusTypeList) {
    // 로그인한 유저가 볼 편지 리스트 조회
    const url = `/api/v1/letter/auth/member/${pageNo}/${size}?type=${type}&focusType=${focusTypeList}`;
    let option = {
        method: "GET",
    }

    return await authFetch(url, option);
}

export async function getOpenLetterList(pageNo, size) {
    // 로그인을 안한 유저가 볼 편지 리스트 조회
    const url = `/api/v1/letter/open/${pageNo}/${size}`;

    let option = {
        method: "GET",
    }

    return await fetch(url, option).then(res => {
        return res.json();
    });
}

export async function getIndexLetterList(letterId, size) {
    const url = `/api/v1/letter/open/index/${letterId}/${size}`;

    let option = {
        method: "GET"
    }

    return await fetch(url, option).then(res => {
        return res.json();
    })
}
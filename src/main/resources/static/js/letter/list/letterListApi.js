import {authFetch} from "../../common/apiUtil.js";

export async function getLetterList(postDate, size, focusTypeList) {
    // 로그인한 유저가 볼 편지 리스트 조회
    const url = `/api/v1/letter/auth/member/${postDate}/${size}?focusType=${focusTypeList}`;
    let option = {
        method: "GET",
    }

    return await authFetch(url, option);
}

export async function getIndexLetterList(postDate, size) {
    // index 페이지에 출력할 편지
    const url = `/api/v1/letter/open/index/${postDate}/${size}`;

    let option = {
        method: "GET"
    }

    return await fetch(url, option).then(res => {
        return res.json();
    })
}
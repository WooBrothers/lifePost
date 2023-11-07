import {authFetch} from "../common/apiUtil.js";

const LETTER_OPEN_API_URL = "/api/v1/letter/open";
const LETTER_AUTH_API_URL = "/api/v1/letter/auth";

export async function getLatestLetter() {
    /* 가장 최신의 편지 조회 */

    const url = LETTER_OPEN_API_URL + "/latest";

    return await fetch(url).then(response => {
        if (response.ok) {
            return response.json();
        }
        return response.text().then(text => {
            throw new Error(text);
        });
    });
}

export async function getLetterContentById(letterId) {
    const url = `${LETTER_AUTH_API_URL}/stamp/${letterId}`;
    let option = {
        method: "GET"
    }

    return await authFetch(url, option);
}

export async function getOpenLetterContents(letterId) {
    const url = `${LETTER_OPEN_API_URL}/${letterId}`;
    let option = {
        method: "GET"
    }

    return await authFetch(url, option);
}
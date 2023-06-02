export class LetterApi {
    /* letter 관련 API class */

    // letter api url 상수
    static LETTER_API_URL = "/api/v1/letter";
    // 스크롤 시 letter-grid-containers 에 추가할 편지 수
    static LETTER_LOAD_SIZE = 3;

    static async getLatestLetter() {
        /* 가장 최신의 편지 조회 */

        const url = LetterApi.LETTER_API_URL + "/latest";

        // return await fetch(url).then(response => {
        return await fetch(url).then(response => {
            if (response.ok) {
                return response.json();
            }
            return response.text().then(text => {
                throw new Error(text);
            });
        });
    }

    static async getNextLettersByPageId(pageId) {
        /* 입력받은 page id 이후의 편지 리스트 조회 */

        const url = LetterApi.LETTER_API_URL + "/page/"
            + pageId + "/" + LetterApi.LETTER_LOAD_SIZE;

        return await fetch(url).then(response => {
            if (response.ok) {
                return response.json();
            }
            return response.text().then(text => {
                throw new Error(text);
            })
        });
    }
}
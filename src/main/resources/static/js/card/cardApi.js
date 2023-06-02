export class CardApi {
    /* card 관련 API class */

    // card api url 상수
    static CARD_API_URL = "/api/v1/card";
    static CARD_LOAD_SIZE = 3;

    static async getLatestCard() {
        /* 가장 최신의 카드 조회 */

        const url = CardApi.CARD_API_URL + "/latest";

        return await fetch(url).then(response => {
            if (response.ok) {
                return response.json();
            }
            return response.text().then(text => {
                throw new Error(text);
            });
        });
    }

    static async getNextCardsByPageId(pageId) {
        /* 입력받은 page id 이후의 편지 리스트 조회 */

        const url = CardApi.CARD_API_URL + "/page/"
            + pageId + "/" + CardApi.CARD_LOAD_SIZE;

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
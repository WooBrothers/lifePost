import {authFetch} from "../../common/apiUtil.js";

export class CardListApi {
    static schema = "/api/v1/card";

    static async getCardList(pageNo) {
        const url = CardListApi.schema + `/auth/member/${pageNo}/10`;
        let option = {method: "GET"};

        return await authFetch(url, option);
    }
}
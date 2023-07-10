import {authFetch} from "../../common/apiUtil.js";

export class CardListApi {
    static schema = "/api/v1/card";
    static memberCardUrl = CardListApi.schema + "/auth/member/3";

    static async getCardList() {
        const url = CardListApi.memberCardUrl
        let option = {method: "GET"};

        return await authFetch(url, option);
    }
}
import {authFetch} from "../../common/apiUtil.js";

export class CardListApi {
    static schema = "/api/v1/card";

    static async getCardList(pageNo, types, focus) {
        let url = CardListApi.schema + `/auth/member/${pageNo}/10`;

        const queryParams = [];
        if (types) {
            types.forEach(type => {
                queryParams.push(`type=${type}`);
            })
        }
        if (focus) {
            queryParams.push(`focus=${focus}`);
        }

        url += queryParams.length > 0 ? `?${queryParams.join('&')}` : '';

        let option = {method: "GET"};

        return await authFetch(url, option);
    }

    static async postFocusLetter() {
        
    }
}
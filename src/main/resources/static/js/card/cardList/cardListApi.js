import {authFetch} from "../../common/apiUtil.js";


export async function getCardList(pageNo, size, types, focus) {
    let url = `/api/v1/card/auth/member/${pageNo}/${size}`;

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

export async function getInfinityCardList(cardId, size, types, focus) {
    let url = ``;
}
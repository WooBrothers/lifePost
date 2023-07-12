import {CardListGrid} from "./card/cardList/cardListGrid.js";
import {checkAndRefreshToken} from "./common/apiUtil.js";

window.onload = async () => {
    /* view 생성 */
    checkAndRefreshToken();

    const cardListSpace = document.getElementById("card-list-space");
    const response = await CardListGrid.createCardListSpace(cardListSpace);

    const cardListPaginationSpace = document.getElementById("card-list-pagination-space");
    await CardListGrid.setCardListPagination(response, cardListPaginationSpace);

}
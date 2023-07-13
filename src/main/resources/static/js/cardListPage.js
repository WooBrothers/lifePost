import {CardListGrid} from "./card/cardList/cardListGrid.js";
import {checkAndRefreshToken} from "./common/apiUtil.js";
import {bindEventToCardListGrid, bindPaginationBtnEvent} from "./card/cardList/cardListEvent.js";

window.onload = async () => {
    /* view 생성 */
    checkAndRefreshToken();

    const cardListSpace = document.getElementById("card-list-space");
    const response = await CardListGrid.createCardListSpace(cardListSpace, 1, bindEventToCardListGrid);

    const cardListPaginationSpace = document.getElementById("card-list-pagination-space");
    await CardListGrid.setCardListPagination(response, cardListPaginationSpace, bindPaginationBtnEvent);
}
import {CardListGrid} from "./card/cardList/cardListGrid.js";
import {bindEventToCardListGrid} from "./card/cardList/cardListEvent.js";
import {checkAndRefreshToken} from "./common/apiUtil.js";
import {createPagination} from "./pagination/pagination.js";
import {bindPaginationBtnEvent} from "./pagination/paginationEvent.js";

window.onload = async () => {
    /* view 생성 */
    await checkAndRefreshToken();

    const cardListSpace = document.getElementById("card-list-space");
    const response = await CardListGrid.createCardListSpace(cardListSpace, 1, bindEventToCardListGrid);

    const cardListPaginationSpace = document.getElementById("pagination-space");
    await createPagination(response, cardListPaginationSpace);
    bindPaginationBtnEvent("card-list-space", "card-space", CardListGrid.createCardListSpace, bindEventToCardListGrid);
}
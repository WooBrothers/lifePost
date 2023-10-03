import {createPagination} from "./pagination/pagination.js";
import {bindPaginationBtnEvent} from "./pagination/paginationEvent.js";
import {bindEventToCardListGrid} from "./card/list/cardListEvent.js";
import {createCardListSpace} from "./card/list/cardList.js";

window.onload = async () => {
    const cardListSpace = document.getElementById("card-list-space");
    const response = await createCardListSpace(cardListSpace, 1, bindEventToCardListGrid);
    await createPagination(response);
    bindPaginationBtnEvent("card-list-space", "card-space", createCardListSpace, bindEventToCardListGrid);
}
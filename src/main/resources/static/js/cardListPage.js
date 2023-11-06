import {bindEventToCardListGrid} from "./card/list/cardListEvent.js";
import {createCardListSpace} from "./card/list/cardList.js";

window.onload = async () => {
    const cardListSpace = document.getElementById("card-list-space");
    await createCardListSpace(cardListSpace, 0, bindEventToCardListGrid);
}
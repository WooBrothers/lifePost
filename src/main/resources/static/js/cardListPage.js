import {CardListGrid} from "./card/cardList/cardListGrid.js";

window.onload = async () => {
    /* view 생성 */
    // const todayLetterGrid = document.getElementById("today-letter-grid");
    // await LetterGrid.createTodayLetterGrid(todayLetterGrid);
    console.log("asd")
    const cardListSpace = document.getElementById("card-list-space");
    await CardListGrid.createCardListSpace(cardListSpace);

    // const cardListPaginationSpace = document.getElementById("card-list-pagination-space");
    // await CardListGrid.createCardListPaginationSpace(cardListPaginationSpace);


    // const todayCardGrid = document.getElementById("today-card-grid");
    // CardGrid.createCardGridByPageIndex(todayCardGrid);
    // await CardGrid.createFocusCardGrid(todayCardGrid);
}
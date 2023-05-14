import {LetterGrid} from "./letter/grid/letterGrid.js";
import {CardGrid} from "./card/grid/cardGrid.js";

/* page load 시 이벤트 바인딩 + html 생성 */
window.onload = () => {

    /* 이벤트 바인딩 */
    LetterGrid.BindEventToLetterGrid();
    CardGrid.BindEventToCardGrid();

    /* view 생성 */
    const todayLetterGrid = document.getElementById("today-letter-grid");
    LetterGrid.createGrid(todayLetterGrid);

    const letterGridContainers = document.getElementById("letter-grid-containers");
    LetterGrid.createGridByPageIndex(1, letterGridContainers);

    const todayCardGrid = document.getElementById("today-card-grid");
    CardGrid.createCardGridByPageIndex(1, todayCardGrid);
}




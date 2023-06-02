import {LetterGrid} from "./letter/letterGrid.js";
import {CardGrid} from "./card/grid/cardGrid.js";
import {bindEventToLetterGrid} from "./letter/letterEvent.js";

/* page load 시 이벤트 바인딩 + html 생성 */
window.onload = async () => {
    // 그리드 생성 관리 클래스 객체 생성

    /* 이벤트 바인딩 */
    bindEventToLetterGrid();

    CardGrid.BindEventToCardGrid();

    /* view 생성 */
    const todayLetterGrid = document.getElementById("today-letter-grid");
    await LetterGrid.createTodayLetterGrid(todayLetterGrid);

    // todayLetterGrid 생성 히우에 id 참조
    const todayLetterId = todayLetterGrid.children[0].dataset.id;
    const letterGridContainers = document.getElementById("letter-grid-containers");
    await LetterGrid.createGridByLetterId(todayLetterId, letterGridContainers);

    const todayCardGrid = document.getElementById("today-card-grid");
    CardGrid.createCardGridByPageIndex(1, todayCardGrid);
}

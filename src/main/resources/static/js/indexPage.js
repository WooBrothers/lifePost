import {LetterGrid} from "./letter/grid/letterGrid.js";
import {CardGrid} from "./card/grid/cardGrid.js";
import {getLatestLetter} from "./letter/api/letterApi.js"


/* page load 시 이벤트 바인딩 + html 생성 */
window.onload = async () => {

    /* 이벤트 바인딩 */
    LetterGrid.BindEventToLetterGrid();
    CardGrid.BindEventToCardGrid();

    /* view 생성 */
    const todayLetterGrid = document.getElementById("today-letter-grid");

    let latestLetterId = 0;
    await getLatestLetter().then(res => {
        latestLetterId = res.id;
        LetterGrid.createGrid(todayLetterGrid, res);
    })

    const letterGridContainers = document.getElementById("letter-grid-containers");
    await LetterGrid.createGridByLetterId(latestLetterId, letterGridContainers);

    const todayCardGrid = document.getElementById("today-card-grid");
    CardGrid.createCardGridByPageIndex(1, todayCardGrid);
}




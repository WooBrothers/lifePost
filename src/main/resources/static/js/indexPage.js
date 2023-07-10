import {LetterGrid} from "./letter/letterGrid.js";
import {CardGrid} from "./card/cardGrid.js";
import {checkAndRefreshToken} from "./common/apiUtil.js";


/* page load 시 이벤트 바인딩 + html 생성 */
window.onload = async () => {
    // 그리드 생성 관리 클래스 객체 생성

    /* cookie - token 확인 -> 토큰 있을 시 검증하고 만료 됬다면 refreshToken으로 재발급 */
    // server에서 쿠키에 새 토큰을 삽입하기 전까지 동기 처리가 필요함
    checkAndRefreshToken();

    /* view 생성 */
    const todayLetterGrid = document.getElementById("today-letter-grid");
    await LetterGrid.createTodayLetterGrid(todayLetterGrid);

    const todayCardGrid = document.getElementById("today-card-grid");
    // CardGrid.createCardGridByPageIndex(todayCardGrid);
    await CardGrid.createFocusCardGrid(todayCardGrid);

    // todayLetterGrid 생성후에 letter id 참조
    const todayLetterId = todayLetterGrid.children[0].dataset.id;
    const letterGridContainers = document.getElementById("letter-grid-containers");
    await LetterGrid.createGridByLetterId(todayLetterId, letterGridContainers);
}

import {CardGrid} from "./card/cardGrid.js";
import {isTokenExpired} from "./common/utilTool.js";
import {checkAndRefreshToken} from "./common/apiUtil.js";


/* page load 시 이벤트 바인딩 + html 생성 */
window.onload = async () => {
    // 그리드 생성 관리 클래스 객체 생성

    // server에서 쿠키에 새 토큰을 삽입하기 전까지 동기 처리가 필요함
    if (isTokenExpired()) {
        await checkAndRefreshToken();
    }

    const todayCardGrid = document.getElementById("today-card-grid");
    // CardGrid.createCardGridByPageIndex(todayCardGrid);
    await CardGrid.createFocusCardGrid(todayCardGrid);

}

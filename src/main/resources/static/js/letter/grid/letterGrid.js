/* later grid를 만드는 함수 */

export class LetterGrid {

    static letterIncrease = 5;

    static createGrid(div) {
        /* 전달하는 div 객체에 자식으로 letter grid를 추가 */
        // TODO innerHTML은 서버에서 받은 값으로 변경

        const divInfos = [
            {className: "letter-item-title bg-primary", innerHTML: "title"},
            {className: "letter-item-date bg-success", innerHTML: "date"},
            {className: "letter-item-img1 bg-info", innerHTML: "img1"},
            {className: "letter-item-img2 bg-warning", innerHTML: "img2"}
        ];

        const letterGridContainer = document.createElement("div");
        letterGridContainer.className = "letter-grid-container";

        divInfos.forEach(divInfo => {
            const div = document.createElement("div");
            div.className = divInfo.className;
            div.innerHTML = divInfo.innerHTML;
            letterGridContainer.appendChild(div);
        });

        div.appendChild(letterGridContainer);
    }

    static createGridByPageIndex(pageIndex, letterGridContainers) {
        /* 입력받은 pageIndex로  */

        // // TODO API call 이후에 출력할 정보들 셋업 필요
        fetch("http://localhost:8080/v1/latter", {
            method: 'GET',
            mode: "same-origin",
            headers: {
                'ContentType': 'application/json',
            }
        })
            .then((response) => response.json())
            .then((data) => console.log(data));

        const startRange = (pageIndex - 1) * LetterGrid.letterIncrease;
        // TODO 토탈 그리드 값 보다 작게 설정해야함
        const endRange = pageIndex * LetterGrid.letterIncrease;


        for (let index = startRange + 1; index <= endRange; index++) {
            // createLetterGrid(index);
            LetterGrid.createGrid(letterGridContainers);
        }
    }

    static addGridScrollEvent() {
        /* scroll event 함수
        *
        * this : event가 발생한 엘리멘트 != LetterGrid() 클래스 및 인스턴스가 아님
        * */

        // 그리드가 출력되는 뷰의 실제 길이 (변하지 않는 화면 상의 높이)
        const letterGridViewHeight = this.clientHeight;
        // 스크롤을 내렸을 때 0부터 증가하는 현재의 그리드 높이 (점차 증가하는 높이)
        const currentGridHeight = this.scrollTop;
        // 그리드 내의 모든 컨텐츠의 높이를 더한 총 높이 (컨텐츠 끝까지의 높이)
        const letterGridTotalHeight = this.scrollHeight;
        // 그리드 내 mairgin
        const margin = 5;

        // 마지막 페이지에 도달했다면 letter 추가
        if (letterGridViewHeight + currentGridHeight + margin >= letterGridTotalHeight) {
            LetterGrid.createGridByPageIndex(currentPage + 1, this);
        }
    }

    static ClickTodayLetter() {
        // 오늘의 편지 클릭 이벤트
        console.log("click today letter.");
    }

    /* 그리드 이벤트 바인딩 함수 */
    static BindEventToLetterGrid() {
        // 오늘의 편지 그리드 컨테이너를 담을 div > 편지 보기 클릭 이벤트 바인딩
        const todayLetterGrid = document.getElementById("today-letter-grid");
        todayLetterGrid.addEventListener("click", LetterGrid.ClickTodayLetter);

        // 편지 그리드 컨네이너를 담을 div > 무한 스크롤 이벤트 바인딩
        const letterGridContainers = document.getElementById("letter-grid-containers");
        letterGridContainers.addEventListener("scroll", LetterGrid.addGridScrollEvent);
    }
}

const currentPage = 1;

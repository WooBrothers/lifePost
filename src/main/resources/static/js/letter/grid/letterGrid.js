/* later grid를 만드는 함수 */

export class LetterGrid {

    static LETTER_LOAD_SIZE = 3;

    static createGrid(letterGridContainers, letter) {
        /* 전달하는 div 객체에 자식으로 letter grid를 추가 */
        // TODO innerHTML은 서버에서 받은 값으로 변경

        const divInfos = [
            {className: "letter-item-title bg-primary", innerHTML: letter.title},
            {className: "letter-item-date bg-success", innerHTML: letter.createdDate},
            {className: "letter-item-img1 bg-info", innerHTML: letter.letterImage},
            {className: "letter-item-img2 bg-warning", innerHTML: letter.postStampImage},
        ];

        const letterGridContainer = document.createElement("div");
        letterGridContainer.className = "letter-grid-container";
        console.log("letter id :" + letter.id);
        letterGridContainer.dataset.id = letter.id;

        divInfos.forEach(divInfo => {
            const div = document.createElement("div");
            div.className = divInfo.className;
            div.innerHTML = divInfo.innerHTML;
            letterGridContainer.appendChild(div);
        });

        letterGridContainers.appendChild(letterGridContainer);
    }

    static async createGridByLetterId(latestLetterId, letterGridContainers) {
        /* 입력받은 pageIndex로  */

        const url = "/api/v1/letter/page/" + latestLetterId + "/" + LetterGrid.LETTER_LOAD_SIZE;

        await fetch(url)
            .then((response) => {
                if (response.ok) {
                    response.json().then((data) =>
                        data.content.forEach((letter) =>
                            LetterGrid.createGrid(letterGridContainers, letter)
                        )
                    );
                }
            })
            .catch((error) => console.log(error));


        // const startRange = (pageIndex - 1) * LetterGrid.LETTER_LOAD_SIZE;
        // // TODO 토탈 그리드 값 보다 작게 설정해야함
        // const endRange = pageIndex * LetterGrid.LETTER_LOAD_SIZE;
        //
        // for (let index = startRange + 1; index <= endRange; index++) {
        //     // createLetterGrid(index);
        //     LetterGrid.createGrid(letterGridContainers);
        // }
    }

    static async addGridScrollEvent() {
        /* scroll event 함수
        *
        * this : event가 발생한(클릭된) 엘리멘트
        * */

        // 클릭된 그리드가 출력되는 뷰의 실제 길이 (변하지 않는 화면 상의 높이)
        const letterGridViewHeight = this.clientHeight;
        // 클릭된 스크롤을 내렸을 때 0부터 증가하는 현재의 그리드 높이 (점차 증가하는 높이)
        const currentGridHeight = this.scrollTop;
        // 클릭된 그리드 내의 모든 컨텐츠의 높이를 더한 총 높이 (컨텐츠 끝까지의 높이)
        const letterGridTotalHeight = this.scrollHeight;
        // 클릭된 그리드 내 mairgin
        const margin = 5;

        const latestLetterId = document.getElementById("letter-grid-containers")
            .lastElementChild
            .dataset.id;

        console.log(latestLetterId);

        // 마지막 페이지에 도달했다면 letter 추가
        if (letterGridViewHeight + currentGridHeight + margin >= letterGridTotalHeight) {
            await LetterGrid.createGridByLetterId(latestLetterId, this);
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


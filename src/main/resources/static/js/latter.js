const letterIncrease = 5;
//const pageCount = Math.ceil(letterLimit / letterIncrease);

let currentPage = 1;

const letterGridContainers = document.getElementById("letter-grid-containers");

/* later grid를 만드는 함수 */
const createLetterGrid = () => {

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
    })
    const letterGridContainers = document.getElementById("letter-grid-containers");
    letterGridContainers.appendChild(letterGridContainer);
};

/* leter list에 letter 추가하는 함수 */
const addLetterGrids = (pageIndex) => {
    currentPage = pageIndex;

    // TODO API call 이후에 출력할 정보들 셋업 필요
    fetch("http://localhost:8080/v1/latter", {
        method: 'GET',
        mode: "same-origin",
        headers: {
            'ContentType': 'application/json',
        }
    })

        .then((response) => response.json())
        .then((data) => console.log(data));

    const startRange = (pageIndex - 1) * letterIncrease;
    // TODO 토탈 그리드 값 보다 작게 설정해야함
    const endRange = pageIndex * letterIncrease;

    for (let index = startRange + 1; index <= endRange; index++) {
        createLetterGrid(index);
    }
};

/* 스크롤 마지막 페이지 감지 -> 추가 페이지 로딩 */
const handleInfiniteScroll = () => {

    // 그리드가 출력되는 뷰의 실제 길이 (변하지 않는 화면 상의 높이)
    const letterGridViewHeight = letterGridContainers.clientHeight;
    // 스크롤을 내렸을 때 0부터 증가하는 현재의 그리드 높이 (점차 증가하는 높이)
    const currentGridHeight = letterGridContainers.scrollTop;
    // 그리드 내의 모든 컨텐츠의 높이를 더한 총 높이 (컨텐츠 끝까지의 높이)
    const letterGridTotalHeight = letterGridContainers.scrollHeight;
    // 그리드 내 mairgin
    const margin = 5;

    // 마지막 페이지에 도달했다면 letter 추가
    if (letterGridViewHeight + currentGridHeight + margin >= letterGridTotalHeight) {
        addLetterGrids(currentPage + 1);
    }
}

letterGridContainers.addEventListener("scroll", handleInfiniteScroll);
window.onload = () => addLetterGrids(1);


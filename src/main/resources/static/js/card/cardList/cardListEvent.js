export function bindEventToCardListGrid() {
    const filerBtn = document.getElementById("filter-btn");
    filerBtn.addEventListener("click", clickFilterBtn);
}

function clickFilterBtn() {
    const filterClassBtnList = document.querySelectorAll(".card-list-btn.filter");
    const filterBtnSpace = document.querySelector("#filter-btn-space");

    filterClassBtnList.forEach(filterBtn => {
        if (filterBtn.style.display === "none") {
            filterBtn.style.display = "block";
            filterBtnSpace.style.border = "1px solid black";
        } else {
            filterBtn.style.display = "none";
            filterBtnSpace.style.border = "none";
        }
    });
}
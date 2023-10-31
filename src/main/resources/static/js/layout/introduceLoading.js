document.addEventListener("DOMContentLoaded", function () {

    const todayFirstVisit = localStorage.getItem("todayFirstVisit");
    const currentDate = new Date().toDateString();
    const fadeOutDiv = document.getElementById("introduce-loading-div");

    if (todayFirstVisit === null || todayFirstVisit !== currentDate) {
        fadeOutDiv.classList.remove("d-none");

        // 로딩 화면을 보여줍니다.
        localStorage.setItem("todayFirstVisit", currentDate);

        setTimeout(function () {
            fadeOutDiv.classList.add("fade-out"); // 애니메이션 클래스 추가
        }, 1500); // 3초 후에 애니메이션 클래스 추가

        // 4초 후에 DOM 객체를 완전히 제거
        setTimeout(function () {
            fadeOutDiv.remove();
        }, 2000);

    } else {
        fadeOutDiv.classList.add("d-none");
    }
});



import {deleteCookie} from "../common/utilTool.js";

export function bindNavigationEvent() {
    const logoutBtn = document.querySelector("#navLogout");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", clickLogoutBtn);
    }
}

function clickLogoutBtn() {
    deleteCookie("accessToken");
    deleteCookie("refreshToken");

    location.reload();
}


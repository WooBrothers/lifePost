import {setMembership} from "./navigation.js";
import {deleteCookie} from "../common/utilTool.js";

export function bindNavigationEvent() {
    window.addEventListener("resize", resizeEvent);
    document.getElementById("nav-mobile-menu-btn").addEventListener("click", clickDropMenuNavigationBtn);

    document.querySelectorAll(".logout-btn").forEach(btn => btn.addEventListener("click", clickLogoutBtn));
}

async function resizeEvent() {
    await setMembership();
}

function clickDropMenuNavigationBtn() {
    document.querySelectorAll(".drop-menu").forEach(tag => {
        if (tag.classList.contains("navi-on")) {
            tag.classList.remove("navi-on");
            tag.classList.add("off");
        } else {
            tag.classList.remove("off");
            tag.classList.add("navi-on");
        }
    });
}

function clickLogoutBtn() {
    deleteCookie("accessToken");
    deleteCookie("refreshToken");

    location.reload();
}


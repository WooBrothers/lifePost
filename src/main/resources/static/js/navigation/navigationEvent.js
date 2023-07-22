import {setMembership} from "./navigation.js";

export function bindNavigationEvent() {
    window.addEventListener("resize", resizeEvent);
    document.getElementById("nav-mobile-menu-btn").addEventListener("click", clickDropMenuNavigationBtn);
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


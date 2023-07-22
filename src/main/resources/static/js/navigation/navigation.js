import {checkAndRefreshToken, getAccessToken} from "../common/apiUtil.js";

function setLoginNavigation() {
    const loginNavs = document.querySelectorAll(".login");
    const logoutNavs = document.querySelectorAll(".logout");
    loginNavs.forEach(nav => {
        nav.style.display = "flex";
    });

    logoutNavs.forEach(nav => {
        nav.style.display = "none";
    })
}

function setLogoutNavigation() {
    const loginNavs = document.querySelectorAll(".login");
    const logoutNavs = document.querySelectorAll(".logout");
    loginNavs.forEach(nav => {
        nav.style.display = "none";
    });

    logoutNavs.forEach(nav => {
        nav.style.display = "flex";
    })
}

async function setNavigation() {
    if (!getAccessToken()) {
        setLogoutNavigation();
    } else {
        await checkAndRefreshToken().then(res => {
            if (res.result === "ok") {
                setLoginNavigation();
            }
        });
    }
}

await setNavigation();
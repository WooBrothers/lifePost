import {checkAndRefreshToken, getAccessToken} from "../common/apiUtil.js";
import {isScreenWide} from "../common/utilTool.js";
import {bindNavigationEvent} from "./navigationEvent.js";

await setMembership();
bindNavigationEvent();

export async function setMembership() {
    if (!getAccessToken()) {
        setLogoutMembership();
        setLogoutNaviMenu();
    } else {
        await checkAndRefreshToken().then(res => {
            if (res.result === "ok") {
                setLoginMembership();
                setLoginNaviMenu();
            } else {
                setLogoutMembership();
                setLogoutNaviMenu();
            }
        });
    }
}

function setLogoutMembership() {
    const logoutMembership = document.querySelector(".logout.membership");
    const loginMembership = document.querySelector(".login.membership");

    logoutMembership.classList.remove("off");
    logoutMembership.classList.add("on");
    loginMembership.classList.remove("navi-on");
    loginMembership.classList.add("off");
}

function setLoginMembership() {
    const logoutMembership = document.querySelector("#logout-membership");
    const loginMembership = document.querySelector("#login-membership");

    loginMembership.classList.remove("off");
    loginMembership.classList.add("navi-on");
    logoutMembership.classList.remove("navi-on");
    logoutMembership.classList.add("off");
}

function setLoginNaviMenu() {

    setLogoutMenuOff();
    setLoginDropMenuSpace()

    if (isScreenWide()) {
        setLoginMenuOn();
        setDropMenuSpaceOff()
    } else {
        setLoginMenuOff();
        setDropMenuSpaceOn();
    }
}

function setLogoutNaviMenu() {
    setLoginMenuOff();
    setLogoutDropMenuSpace();

    if (isScreenWide()) {
        setLogoutMenuOn();
        setDropMenuSpaceOff();
    } else {
        setLogoutMenuOff();
        setDropMenuSpaceOn();
    }
}

function setLoginMenuOn() {
    document.querySelectorAll(".login.wide.nav-menu").forEach(tag => {
        tag.classList.remove("off");
        tag.classList.add("navi-on");
    });
}

function setLoginMenuOff() {
    document.querySelectorAll(".login.wide.nav-menu").forEach(tag => {
        tag.classList.remove("navi-on");
        tag.classList.add("off");
    });
}

function setLogoutMenuOn() {
    document.querySelectorAll(".logout.wide.nav-menu").forEach(tag => {
        tag.classList.remove("off");
        tag.classList.add("navi-on");
    });
}

function setLogoutMenuOff() {
    document.querySelectorAll(".logout.wide.nav-menu").forEach(tag => {
        tag.classList.remove("navi-on");
        tag.classList.add("off");
    });
}

function setDropMenuSpaceOff() {
    const dropMenuSpace = document.getElementById("nav-mobile-menu-btn-space");
    dropMenuSpace.classList.remove("navi-on");
    dropMenuSpace.classList.add("off");
}

function setDropMenuSpaceOn() {
    const dropMenuSpace = document.getElementById("nav-mobile-menu-btn-space");
    dropMenuSpace.classList.remove("off");
    dropMenuSpace.classList.add("navi-on");
}

function setLoginDropMenuSpace() {
    const loginMobileMenuSpace = document.querySelectorAll(".mobile.login");
    const logoutMobileMenuSpace = document.querySelectorAll(".mobile.logout");
    logoutMobileMenuSpace.forEach(tag => {
        tag.classList.remove("drop-menu");
    });
    loginMobileMenuSpace.forEach(tag => {
        tag.classList.add("drop-menu");
    });
}

function setLogoutDropMenuSpace() {
    const logoutMobileMenuSpace = document.querySelectorAll(".mobile.logout");
    const loginMobileMenuSpace = document.querySelectorAll(".mobile.login");
    loginMobileMenuSpace.forEach(tag => {
        tag.classList.remove("drop-menu");
    });
    logoutMobileMenuSpace.forEach(tag => {
        tag.classList.add("drop-menu");
    });
}

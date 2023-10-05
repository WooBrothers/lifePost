import {createCardApiCall} from "./cardModalApi.js";
import {isImageUrlValid} from "../../common/utilTool.js";
import {DivTag} from "../../common/tagUtil.js";
import {deleteCardModalBackgroundImg} from "../list/cardListEvent.js";

bindEventToCreateCardModal();

export function bindEventToCreateCardModal() {
    const cardImageInput = document.querySelector("#card-img");
    cardImageInput.addEventListener("input", inputImageUrl);

    const cardCreateBtn = document.getElementById("card-submit-btn");
    cardCreateBtn.addEventListener("click", createCardClick);
}

async function createCardClick() {
    const dataset = this.dataset;
    const cardImgUrl = document.querySelector("#card-img").value;
    const cardTitle = document.querySelector("#card-title").value;
    const cardContent = document.querySelector("#card-content").value;

    if (!cardTitle) {
        alert("card title 값은 필수입니다.");
        return;
    }

    if (!cardContent) {
        alert("card content 값은 필수입니다.");
        return;
    }

    if (cardContent.length <= 250) {
        await createCardApiCall(cardTitle, cardImgUrl, cardContent, dataset.memberCardId, dataset.type);
        location.reload();
        alert("카드 수정/생성 성공했습니다.");

    } else {
        alert("card content는 길이가 250 이하로 작성이 가능합니다.")
    }
}

function inputImageUrl() {

    // 기존에 추가한 이미지 삭제
    deleteCardModalBackgroundImg();

    const imgUrl = this.value;
    isImageUrlValid(imgUrl, function (isValid) {
        if (isValid) {
            const virtualImageBox = new DivTag()
                .setClassName("card-modal-background");

            virtualImageBox.setStyle([{
                backgroundImage: `url(${imgUrl})`,
                objectFit: "cover",
                position: "absolute",
                top: "0px",
                left: "0px",
                right: "0px",
                bottom: "0px",
                opacity: 0.3
            }]);

            document.querySelector("#modal-img-content")
                .appendChild(virtualImageBox.getTag());

        } else if (!imgUrl) {

        } else {
            alert('이미지 URL이 유효하지 않거나 이미지가 존재하지 않습니다.');
        }
    });
}
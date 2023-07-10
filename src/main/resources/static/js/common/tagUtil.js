export function addDivByDivInfosToParent(divInfos, parentDiv) {
    divInfos.forEach(divInfo => {

        const div = new DivTag()
            .setClassName(divInfo.className)
            .setId(divInfo.id)
            .setInnerHTML(divInfo.innerHTML)

        parentDiv.appendChild(div.getTag());
    });
}

export class Tag {
    constructor(tagName) {
        this.className = null;
        this.id = null;
        this.style = null;
        this.dataset = null;
        this.tag = this.createTagByName(tagName);
    }

    createTagByName(tagName) {
        return document.createElement(tagName);
    }

    setClassName(className) {
        if (className && this.tag) {
            this.className = className;
            this.tag.className = className;
        }
        return this;
    }

    setId(id) {
        if (id && this.tag) {
            this.id = id;
            this.tag.id = id;
        }
        return this;
    }

    setStyle(style) {
        if (style && this.tag) {
            if (Array.isArray(style)) {
                style.forEach(styleDict => {
                    for (const [key, value] of Object.entries(styleDict)) {
                        this.tag.style[key] = value;
                    }
                });
            } else {
                throw new Error("style 적용 시 데이터 타입은 array이어야 합니다.");
            }
        }
        return this;
    }

    setDataset(dataset) {
        if (dataset && this.tag) {
            if (Array.isArray(dataset)) {
                dataset.forEach(datasetDict => {
                    for (const [key, value] of Object.entries(datasetDict)) {
                        this.tag.dataset[key] = value;
                    }
                });
            } else {
                throw new Error("dataset 적용 시 데이터 타입은 array 이어야 합니다.");
            }
        }
        return this;
    }

    getTag() {
        return this.tag;
    }
}

export class DivTag extends Tag {
    constructor() {
        super("div");
        this.innerHTML = null;
    }

    setInnerHTML(innerHTML) {
        if (innerHTML && this.tag) {
            if (Array.isArray(innerHTML)) {
                innerHTML.forEach(innerHTMLEle => {
                    this.tag.appendChild(innerHTMLEle.getTag());
                })
            } else {
                this.innerHTML = innerHTML;
                this.tag.innerHTML = innerHTML;
            }
        }
        return this;
    }

    setBackground(backgroundImg) {
        this.tag.style.backgroundImage = `url(${backgroundImg})`;
        this.tag.style.backgroundSize = "cover";
        return this;
    }
}

export class PTag extends Tag {
    constructor() {
        super("p");
        this.innerHTML = null;
    }

    setInnerHTML(innerHTML) {
        this.innerHTML = innerHTML;
        this.tag.innerHTML = innerHTML;
        return this;
    }
}

export class ImgTag extends Tag {
    constructor() {
        super("img");
        this.src = null;
        this.alt = null;
    }

    setSrc(imgPath) {

        if (imgPath && this.tag) {
            this.src = imgPath;
            this.tag.src = imgPath;
        } else {
            throw new Error("imgPath가 올바르지 않습니다.");
        }

        return this;
    }

    setAlt(alternativeImgPath) {

        if (alternativeImgPath) {
            this.alt = alternativeImgPath;
            this.tag.alt = alternativeImgPath;
        } else {
            throw new Error("alternativeImgPath가 올바르지 않습니다.");
        }
        return this;
    }
}

export class HrTag extends Tag {
    constructor() {
        super("hr");
    }
}

export class ButtonTag extends Tag {
    constructor() {
        super("button");
        this.innerHTML = null;
    }

    setInnerHTML(innerHTML) {
        this.innerHTML = innerHTML;
        this.tag.innerHTML = innerHTML;
        return this;
    }

    setBackground(backgroundImg) {
        this.tag.style.backgroundImage = `url(${backgroundImg})`;
        this.tag.style.backgroundSize = "cover";
        return this;
    }
}

export class UlTag extends Tag {
    constructor() {
        super("ul");
    }

    setLiList(liList) {
        liList.forEach(li => {
            debugger
            this.tag.appendChild(li.tag);
        });
        return this;
    }
}

export class LiTag extends Tag {
    constructor() {
        super("li");
        this.innerHTML = null;
    }

    setInnerHTML(innerHTML) {
        this.innerHTML = innerHTML;
        this.tag.innerHTML = innerHTML;
        return this;
    }
}
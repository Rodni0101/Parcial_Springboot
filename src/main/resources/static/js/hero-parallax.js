const heroArt = document.querySelector("#heroArt");
const motionPreference = window.matchMedia("(prefers-reduced-motion: reduce)");

if (heroArt && !motionPreference.matches) {
    heroArt.addEventListener("pointermove", (event) => {
        const bounds = heroArt.getBoundingClientRect();
        const horizontal = (event.clientX - bounds.left) / bounds.width - 0.5;
        const vertical = (event.clientY - bounds.top) / bounds.height - 0.5;
        heroArt.style.setProperty("--parallax-x", `${horizontal * 12}px`);
        heroArt.style.setProperty("--parallax-y", `${vertical * 10}px`);
    });

    heroArt.addEventListener("pointerleave", () => {
        heroArt.style.setProperty("--parallax-x", "0px");
        heroArt.style.setProperty("--parallax-y", "0px");
    });
}

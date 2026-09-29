const themeStorageKey = "springstore-theme";
const savedTheme = window.localStorage.getItem(themeStorageKey);
document.documentElement.dataset.theme = savedTheme === "dark" ? "dark" : "light";

document.addEventListener("DOMContentLoaded", () => {
    const toggle = document.querySelector("#themeToggle");
    if (!toggle) {
        return;
    }

    const updateLabel = () => {
        const darkThemeActive = document.documentElement.dataset.theme === "dark";
        const label = darkThemeActive ? "Tema claro" : "Tema oscuro";
        toggle.setAttribute("aria-label", `Cambiar a ${label.toLowerCase()}`);
        toggle.title = `Cambiar a ${label.toLowerCase()}`;
        toggle.querySelector(".theme-label").textContent = label;
    };

    updateLabel();
    toggle.addEventListener("click", () => {
        const nextTheme = document.documentElement.dataset.theme === "dark" ? "light" : "dark";
        document.documentElement.dataset.theme = nextTheme;
        window.localStorage.setItem(themeStorageKey, nextTheme);
        updateLabel();
    });
});

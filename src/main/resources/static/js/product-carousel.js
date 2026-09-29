const carousel = document.querySelector("#productCarousel");

if (carousel) {
    const previousButton = document.querySelector('[data-carousel-direction="-1"]');
    const nextButton = document.querySelector('[data-carousel-direction="1"]');
    const cards = carousel.querySelectorAll(".product-card");

    const updateButtons = () => {
        const maxScroll = carousel.scrollWidth - carousel.clientWidth;
        previousButton.disabled = carousel.scrollLeft <= 1;
        nextButton.disabled = carousel.scrollLeft >= maxScroll - 1;
    };

    const moveCarousel = (direction) => {
        const firstCard = cards[0];
        const styles = window.getComputedStyle(carousel);
        const gap = Number.parseFloat(styles.columnGap) || 0;
        const distance = firstCard.getBoundingClientRect().width + gap;
        carousel.scrollBy({ left: direction * distance, behavior: "smooth" });
    };

    previousButton.addEventListener("click", () => moveCarousel(-1));
    nextButton.addEventListener("click", () => moveCarousel(1));
    carousel.addEventListener("scroll", updateButtons, { passive: true });
    window.addEventListener("resize", updateButtons);
    updateButtons();
}

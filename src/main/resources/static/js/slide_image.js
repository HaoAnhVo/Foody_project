const slides = document.querySelectorAll('.slide');
const nextButton = document.querySelector('.next-image');
const prevButton = document.querySelector('.prev-image');
const slide_image = document.querySelector('.slide-image');
const nameProduct = document.querySelector('.name-product');
const itemNames = document.querySelectorAll('.item-name');

let currentSlide = 0;

// Hàm hiển thị slide
function showSlide(index) {
    slide_image.style.transform = `translateX(-${index * 100}%)`;
    itemNames.forEach((item, i) => {
        item.classList.remove('active'); // Xóa lớp active
        if (i === index) {
            item.classList.add('active'); // Thêm lớp active cho tên sản phẩm tương ứng
        }
    });
}

showSlide(currentSlide);

nextButton.addEventListener('click', () => {
    currentSlide++;
    if (currentSlide >= slides.length) {
        currentSlide = 0; // Reset về slide đầu tiên
    }
    showSlide(currentSlide);
});

prevButton.addEventListener('click', () => {
    currentSlide--;
    if (currentSlide < 0) {
        currentSlide = slides.length - 1; // Đến slide cuối cùng
    }
    showSlide(currentSlide);
});

// Tự động chuyển slide mỗi 5 giây
setInterval(() => {
    currentSlide++;
    if (currentSlide >= slides.length) {
        currentSlide = 0; // Reset về slide đầu tiên
    }
    showSlide(currentSlide);
}, 5000);

// Hiển thị slide khi nhấp vào tên sản phẩm
itemNames.forEach((item, index) => {
    item.addEventListener('click', () => {
        currentSlide = index; // Cập nhật slide hiện tại
        showSlide(currentSlide);
    });
});
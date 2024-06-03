// script.js

// Xử lý sự kiện xóa sản phẩm khỏi giỏ hàng
function deleteCartItem(itemId) {
    if (confirm("Are you sure you want to remove this item from your cart?")) {
        // Gửi request để xóa sản phẩm
        fetch("/cart/removeItem/" + itemId, {
            method: "GET"
        })
        .then(response => {
            if (response.redirected) {
                // Nếu request thành công, chuyển hướng người dùng về trang giỏ hàng
                window.location.href = response.url;
            } else {
                console.error("Error deleting item from cart.");
            }
        })
        .catch(error => {
            console.error("Error deleting item from cart:", error);
        });
    }
}

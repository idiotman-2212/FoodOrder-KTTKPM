/*
// Khi nội dung file html đã được hiển thị trên browser thì sẽ kích hoạt
$(document).ready(function(){

    // Đăng ký sự kiện click cho thẻ tag được chỉ định bên HTML
    $("#btn-login").click(function(){
        // .val() : Lấy giá trị của thẻ input được chỉ định
        var email = $("#email").val()
        var password = $("#password").val()

        // Xuất giá trị ra trên tab console trên trình duyệt
        console.log("email : ",email, " password : ",password);

        //ajax : Dùng để call ngầm API mà không cần trình duyệt
        //axios, fetch
        //data : chỉ có khi tham số truyền ngầm
        $.ajax({
            url: "http://localhost:9999/login",
            method: "post",
            data: {
                email: email,
                password: password
            }
        }).done(function(data){

            if(data && data.statusCode == 200) {
                localStorage.setItem("token", data.data)

                 window.location='shop.html'

            } else{
                alert("Sai email hoặc mật khẩu.")
            }
            console.log("server tra ve ", data)
        })
    })

    //  Xử lý đăng kí

    $("#btn-register").click(function(){

        var username = $("#username").val()
        var password = $("#password").val()
        var repassword = $("#re-password").val()
        var email = $("#email").val()


        console.log("username : ",username, " password : ",password,
               " repassword ", repassword, " email ", email );

        $.ajax({
            url: "http://localhost:9999/login",
            method: "post",
            contentType: "application/json",
            data: JSON.stringify({
                userName: username,
                password: password,
                email: email
            })


        }).done(function(data){

            console.log("server tra ve ", data)

            window.location.href = 'product';
        })
    })

})
*/

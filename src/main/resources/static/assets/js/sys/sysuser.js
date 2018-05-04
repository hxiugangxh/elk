$(function () {


    function submit(url, submitData) {
        $.ajax({
            url: url,
            method: "post",
            dataType: "json",
            contentType:"application/json;charset=UTF-8",
            data: JSON.stringify(submitData),
            success: function(data){
                layer.alert(data.message);
            }
        });
    }


    $("#userAddForm").on('submit', function(e) {
        e.preventDefault();
        var formData = $(this).serializeArray();

        var submitData = {};

        $.map(formData, function(n, i){
            submitData[n['name']] = n['value'];
        });

        var url = "/sysuser/add";
        submit(url, submitData);
    });

});
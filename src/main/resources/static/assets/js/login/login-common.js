$(".form-custom .input").focus(function(){
    var $obj=$(this).siblings(".close-block");
    $obj.find(".close-default").removeClass("hide");
    $obj.find(".close-hover").addClass("hide");
}).blur(function () {
    var $obj=$(this).siblings(".close-block");
    $obj.find(".close-default").addClass("hide");
    $obj.find(".close-hover").addClass("hide");
});
$(".form-custom .close-block").mouseover(function(){
    $(this).find(".close-default").addClass("hide");
    $(this).find(".close-hover").removeClass("hide");
}).mouseout(function(){
    $(this).find(".close-default").removeClass("hide");
    $(this).find(".close-hover").addClass("hide");
})
$(".form-custom .close-hover").on("click",function(){
    $(this).parents(".close-block").siblings(".input").val("");
    $(this).parents(".close-block").siblings(".input").trigger('blur');
})
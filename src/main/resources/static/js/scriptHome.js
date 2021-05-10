
(function($) { "use strict";

$(function() {
    var header = $(".start-style");
    $(window).scroll(function() {    
        var scroll = $(window).scrollTop();
    
        // sticky navbar on scroll script
        if (scroll >= 10) {
            header.removeClass('start-style').addClass("scroll-on");
        } else {
            header.removeClass("scroll-on").addClass('start-style');
        }

        // scroll-up button show/hide script
        if(this.scrollY > 500){
            $('.scroll-up-btn').addClass("show");
        }else{
            $('.scroll-up-btn').removeClass("show");
        }
    });

    // slide-up script
    $('.scroll-up-btn').click(function(){
        $('html').animate({scrollTop: 0});
        // removing smooth scroll on slide-up button click
        $('html').css("scrollBehavior", "auto");
    });

});		
    
//Animation

$(document).ready(function() {
    $('body.hero-anime').removeClass('hero-anime');
        
    
});


//Menu On Hover
    
$('body').on('mouseenter mouseleave','.nav-item',function(e){
        if ($(window).width() > 750) {
            var _d=$(e.target).closest('.nav-item');_d.addClass('show');
            setTimeout(function(){
            _d[_d.is(':hover')?'addClass':'removeClass']('show');
            },1);
        }
});	

//Switch light/dark
$("#switch").on('click', function () {
    if ($("body").hasClass("dark")) {
        $("body").removeClass("dark");
        $("#switch").removeClass("switched");
    }
    else {
        $("body").addClass("dark");
        $("#switch").addClass("switched");
    }
});  

 /* Add product to cart */
 $('form #add_btn').on('click',function(e){
		 
		 var id = $(this).parent().find('#product_id').val();
		 //console.log('======>'+id);
		 $.ajax({
			url: '/api/addProductToCart/'+id,
			type: 'GET',
			success: function(countItems){
				//alert("nbr items :"+countItems);
				$('#itemCount').html(countItems);
				$('#itemCounts').attr('data-count',countItems);
			},
			error: function(){
				alert("error!");
			}
			
		 });
		 e.preventDefault();
	 });
	 
	 /* Search Product */
	 $('#search_btn').click(function(e){
		var name = $('#productName').val();
		$.ajax({
			url: '/api/searchProduct/'+name,
			type: 'GET',
			success: function(product){
				//alert("this is my product: "+product.id);
				$('#searchProductModal .vegetablePart #productImg').attr('src','/product/display/'+product.id);	
				$('#searchProductModal .vegetablePart #name').html(product.name);
				$('#searchProductModal .vegetablePart #price').html(product.price);
				$('#searchProductModal .vegetablePart #desc').html(product.product_desc);
				$('#searchProductModal .vegetablePart #product_id').attr('value',product.id);
				},
			error: function(){
				alert("Something wrong!");
			}
		});
		   $("#searchProductModal").modal("show");
		e.preventDefault();
	 });
	 

})(jQuery); 


var check = false;
var subtotal = '';
var totalcost = '' ;
function changeVal(el) {
  var qt = parseFloat(el.parent().children(".qt").html());
  var price = parseFloat(el.parent().children(".price").html());
  var eq = Math.round(price * qt * 100) / 100;
  el.parent().children(".full-price").html( eq + "DH" );
  
  changeTotal();			
}

function changeTotal() {
  
  var price = 0;
  
  $(".full-price").each(function(index){
    price += parseFloat($(".full-price").eq(index).html());
  });
  
  price = Math.round(price * 100) / 100;
  var shipping = parseFloat($(".shipping span").html());
  var fullPrice = Math.round((price + shipping) *100) / 100;
  
  if(price == 0) {
    fullPrice = 0;
  }
  
  $(".subtotal span").html(price);
  $(".total span").html(fullPrice);

  subtotal = price.toString();
  totalcost = fullPrice.toString();

}

$(document).ready(function(){
	
  changeTotal();

  /* increase quantity */
  $(".qt-plus").click(function(){
    $(this).parent().children(".qt").html(parseInt($(this).parent().children(".qt").html()) + 1);
    
    $(this).parent().children(".full-price").addClass("added");
    
    var el = $(this);
    window.setTimeout(function(){el.parent().children(".full-price").removeClass("added"); changeVal(el);}, 150);

    var quantity = parseFloat($(this).parent().children(".qt").html());
    var cart_id = $(this).parent().find('#id').val();
    
    updateQuantity(cart_id, quantity);
  });
  
  /* decrease quantity */
  $(".qt-minus").click(function(){
    
    child = $(this).parent().children(".qt");
    
    if(parseInt(child.html()) > 1) {
        child.html(parseInt(child.html()) - 1);
    }
    
    $(this).parent().children(".full-price").addClass("minused");
    
    var el = $(this);
    window.setTimeout(function(){el.parent().children(".full-price").removeClass("minused"); changeVal(el);}, 150);

    var quantity = parseFloat($(this).parent().children(".qt").html());
    var cart_id = $(this).parent().find('#id').val();
    updateQuantity(cart_id, quantity);
  });
  
  window.setTimeout(function(){$(".is-open").removeClass("is-open")}, 1200);
  

//Delete Cart function
  $('article #delete').on('click',function(){
		 var id = $(this).attr('value');
	 	 //alert(id);
		 //console.log(id);
	     check = true;
		 var el = $(this).closest('.product');
		 $.ajax({
			url: '/api/deleteProduct/'+id,
			type: 'GET',
			success: function(){
				cuteHide(el);	
				setTimeout(function () {
                    location.reload(true);
                 }, 500);
			},
				error: function(){
					alert("error");
			}
	 });

//
    
		
 });

//Animated element removal
 function cuteHide(el) {
   el.animate({opacity: '0'}, 250, function(){
     el.animate({width: '0px'}, 500, function(){
       el.remove();
     });
   });
 }
  
});

//Upadte Quantity 
function updateQuantity(cart_id, quantity){
	
		//alert('quantity: '+quantity+' and cart_id '+cart_id);
		$.ajax({
			
			url: '/api/updateQuantity/' + cart_id + '/quantity/' + quantity,
			type: 'GET',
			success: function () {
	              //alert('Updated successfully.');
	        },
	        error: function () {
	              alert('error handing here');
	         }
		});
}
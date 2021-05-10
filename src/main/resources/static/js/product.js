$(document).ready(function() {
    $('#loader').hide();
    $("#submit").on("click", function() {
    	var name = $("#name").val();
        var file = $("#product_image").val(); 
        var price = $("#price").val();
        var product_type = $("#product_type").val();
        
        $('#loader').show();
        if (name === "" || file === "" || price === "" || product_type === "") {
        	$("#submit").prop("disabled", false);
            $('#loader').hide();
            $("#name").css("border-color", "red");
            $("#product_image").css("border-color", "red");
            $("#price").css("border-color", "red");
            $("#product_type").css("border-color", "red");
            $("#error_name").html("Please fill the required field.");
            $("#error_file").html("Please fill the required field.");
            $("#error_price").html("Please fill the required field.");
            $("#error_producttype").html("Please fill the required field.");
        } else {
            $("#name").css("border-color", "");
            $("#product_image").css("border-color", "");
            $("#price").css("border-color", "");
            $("#product_type").css("border-color", "");
            $('#error_name').css('opacity', 0);
            $('#error_file').css('opacity', 0);
            $('#error_price').css('opacity', 0);
            $('#error_producttype').css('opacity', 0);
                    
        }
            });
        });



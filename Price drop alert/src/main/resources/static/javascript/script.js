const search = () => {
    console.log("searching...");
    let query = $("#search-input").val();
    console.log(query);
    if (query == '') {
        $(".search-result").hide();
    } else {
        //search
        console.log(query);
        $(".search-result").show();

        // Get username and password
        let username = "shubham@gmail.com";
        let password = "jain";

        // Construct URL with username and password as query parameters
        let url = `http://localhost:8080/admin/getproductbylikename/${query}?username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`;
        fetch(url, {
            headers: {
                'Authorization': 'Basic ' + btoa(username + ':' + password),
                'Content-Type': 'application/json'
            }
        })
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                console.log(data);
                let text = `<div class='list-group'>`;
                data.forEach((productApi) => {
                    text += `<a href='#' class='list-group-item list-group-item-info list-group-item-action product-link' data-product-url='${productApi.product_url}' data-product-name='${productApi.product_name}'>${productApi.product_name} (${productApi.product_price})</a>`;
                });
                text += `</div>`;
                $(".search-result").html(text);
                $(".search-result .list-group-item").addClass("list-group-item");
                $(".search-result").show();

                // Add event listener to each product link
                $(".product-link").click(function(e) {
                    e.preventDefault();
                    console.log("Product link clicked");
                    let productName = $(this).data("product-name");
                    let productUrl = $(this).data("product-url");
                    console.log("Product name:", productName);
                    $("input[name='p_url']").val(productUrl);
                    $("input[name='p_name']").val(productName);
                    console.log("Value set for p_url input field:", productName);
                    $(".search-result").hide();
                });

            });
    }
}

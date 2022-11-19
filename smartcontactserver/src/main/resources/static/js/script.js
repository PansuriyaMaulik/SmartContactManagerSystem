const toggleSidebar=() => {

    if($(".sidebar").is(":visible"))
    {
        // Closed sidebar
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
    } else {
        // Show Sidebar
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
    }
};

const search=()=>{
    let query = $("#search-input").val()

    if(query=='')
    {
        $(".search-result").hide();
    } else
    {
        // Search contact in DB
        console.log(query);

        //Sending request to server
        let url = `http://localhost:8282/search/${query}`;

        fetch(url).then((response)=> {
            return response.json();
    }).then((data)=>{
        //data...

            let text =`<div class='list-group'>`;
            data.forEach((contact)=>{
                text += `<a href='/user/${contact.cId}/contact' class="list-group-item list-group-action"> ${contact.name}</a>`
            });
            text += `</div>`

            $(".search-result").html(text);
            $(".search-result").show();
        });
    }
};
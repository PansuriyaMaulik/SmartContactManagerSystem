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
}
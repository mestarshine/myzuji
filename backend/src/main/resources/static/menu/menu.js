function changeMenu(menuUrl) {

}
function siMenu(pid,sid,menuName,menuUrl){
    $("#menu").siblings().removeClass("active");
    $("#"+pid).attr("class","active");
    $("#"+sid).attr("class","active");
    top.mainFrame.tabAddHandler(id,MENU_NAME,MENU_URL);
}
/**
 * Created by sirmonkey on 4/3/15.
 */
function SidePanelBuilder() {
    console.log(Date.now()+" Sidepanel started.");
    var Component = function() {
        this.sendMessage = null;
        this.socket = null;
        this.endpoint = "sidepanel";
    }
    Component.prototype.receive = function(msg) {
        console.log("-------SidePanel---------")
        console.log(msg)
        var ping = {};
        ping["receiver"] = "SidePanelController";
        ping["action"] = "Ping";
        this.sendMessage(ping);
        console.log("-----SidePanel-END-------")
    }
    return new Component();
}
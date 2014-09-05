package weixin.inteface;

import weixin.message.LocationMessage;
import weixin.message.MessageResponse;

public interface LocationMessageHandler {
    public void handle(LocationMessage msg, MessageResponse res);
}

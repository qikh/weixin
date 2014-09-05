package weixin.inteface;

import weixin.event.SubscribeEvent;
import weixin.message.MessageResponse;

public interface SubscribeEventHandler {
    public void handle(SubscribeEvent msg, MessageResponse res);
}

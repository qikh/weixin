package weixin.inteface;

import weixin.message.ImageMessage;
import weixin.message.MessageResponse;

public interface ImageMessageHandler {
    public void handle(ImageMessage msg, MessageResponse res);
}

package weixin.inteface;

import weixin.message.MessageResponse;
import weixin.message.VideoMessage;

public interface VideoMessageHandler {
    public void handle(VideoMessage msg, MessageResponse res);
}

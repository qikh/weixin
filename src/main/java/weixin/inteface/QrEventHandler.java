package weixin.inteface;

import weixin.event.QrEvent;
import weixin.message.MessageResponse;

public interface QrEventHandler {
    public void handle(QrEvent msg, MessageResponse res);
}

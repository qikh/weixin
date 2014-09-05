package weixin.inteface;

import weixin.event.LocationEvent;
import weixin.message.MessageResponse;

public interface LocationEventHandler {
    public void handle(LocationEvent msg, MessageResponse res);
}

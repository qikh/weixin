package weixin.inteface;

import weixin.event.MenuEvent;
import weixin.message.MessageResponse;

public interface MenuEventHandler {
	public void handle(MenuEvent msg, MessageResponse res);
}

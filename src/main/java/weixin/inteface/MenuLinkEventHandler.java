package weixin.inteface;

import weixin.event.MenuLinkEvent;
import weixin.message.MessageResponse;

public interface MenuLinkEventHandler {
	public void handle(MenuLinkEvent msg, MessageResponse res);
}

package io.github.gawdserver.noswear;

import io.github.gawdserver.api.Server;
import io.github.gawdserver.api.events.ChatEvent;
import io.github.gawdserver.api.utils.Chat;

/**
 * Created by Vinnie on 6/21/2015.
 */
public class ChatListener implements ChatEvent {
	@Override
	public void playerChat(String player, String chat) {
		//System.out.println("[NoSwear] " + chat);
		chat = chat.toLowerCase();
		for (String word : NoSwear.badWords) {
			if (chat.contains(word)) {
				Chat.sendMessage(player, NoSwear.warnMessage);
				NoSwear.logger.warning("Player " + player + " said bad word: " + word);
				if (NoSwear.offences.containsKey(player)) {
					int playerOffences = NoSwear.offences.get(player);
					playerOffences++;
					NoSwear.offences.put(player, playerOffences);
					NoSwear.logger.warning("Player " + player + " Offence # " + playerOffences);
					if (NoSwear.banPlayer && playerOffences >= NoSwear.tolerance) {
						Server.sendCommand("ban " + player + " " + NoSwear.banMessage);
						NoSwear.logger.info("Player " + player + " banned for swearing.");
					}
				} else {
					NoSwear.offences.put(player, 1);
					NoSwear.logger.warning("Player " + player + " first offence.");
				}
				return;
			}
		}
	}

	@Override
	public void serverChat(String chat) {
	}
}

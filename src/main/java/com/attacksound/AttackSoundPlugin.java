package com.attacksound;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.StatChanged;
import net.runelite.api.Skill;
import net.runelite.client.callback.ClientThread;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.events.FocusChanged;

@Slf4j
@PluginDescriptor(
	name = "Attack Sound"
)
public class AttackSoundPlugin extends Plugin
{
	private long previousSoundTime = 0;
	private boolean newAttackPossible = true;
	private boolean isFocused = true;
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private AttackSoundConfig config;

	@Override
	protected void startUp()
	{
		if (client.getGameState() == GameState.LOGGED_IN)
		{
			clientThread.invokeLater(() ->
			{
				previousSoundTime = System.currentTimeMillis()+1000;
			});
		}
	}

	@Subscribe
	protected void onConfigChanged(ConfigChanged configChanged)
	{
		if ("AttackSound".equals(configChanged.getGroup()))
		{
			if ("soundID".equals(configChanged.getKey()))
			{
				clientThread.invoke(() -> client.playSoundEffect(config.soundID(), config.volume()));
			}
		}
	}

	@Subscribe
	protected void onStatChanged(StatChanged event)
	{
		if (event.getSkill() == Skill.HITPOINTS)
		{
			playSound();
		}
	}

	@Subscribe
	public void onFocusChanged(FocusChanged event)
	{
		isFocused = event.isFocused();
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		if (newAttackPossible && AnimationData.fromId(client.getLocalPlayer().getAnimation()) != null)
		{
				playSound();
				newAttackPossible = false;
		}

		if (!newAttackPossible && AnimationData.fromId(client.getLocalPlayer().getAnimation()) == null)
		{
			newAttackPossible = true;
		}
	}

	public void playSound()
	{
		if (config.soundsOnlyWhileFocused() && !isFocused) return;
		if (System.currentTimeMillis() - previousSoundTime > config.muteDuration())
		{
			client.playSoundEffect(config.soundID(), config.volume());
			previousSoundTime = System.currentTimeMillis();
		}
	}

	@Provides
	AttackSoundConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AttackSoundConfig.class);
	}
}

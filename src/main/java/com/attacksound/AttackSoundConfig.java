package com.attacksound;

import net.runelite.api.SoundEffectID;
import net.runelite.api.SoundEffectVolume;
import net.runelite.client.config.Range;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("AttackSound")
public interface AttackSoundConfig extends Config
{
	@ConfigItem(
		keyName = "soundID",
		name = "Sound ID",
		description = "the ID of a sound you want to be played",
		position = 1
	)
	default int soundID()
	{
		return SoundEffectID.TOWN_CRIER_BELL_DING;
	}

	@Range(
			max = SoundEffectVolume.HIGH
	)
	@ConfigItem(
			keyName = "volume",
			name = "Volume",
			description = "The volume used for the sound played",
			position = 2
	)
	default int volume() {
		return SoundEffectVolume.MEDIUM_LOW;
	}

	@ConfigItem(
			keyName = "muteDuration",
			name = "Mute Duration",
			description = "The time that is waited until the sound can be played again",
			position = 3
	)
	default int muteDuration() {
		return 0;
	}

	@ConfigItem(
			keyName = "soundsOnlyWhileFocused",
			name = "Play sounds only when focused",
			description = "Enabling this will disable sounds while the window is not in focus",
			position = 4
	)
	default boolean soundsOnlyWhileFocused()	{ return false;	}
}

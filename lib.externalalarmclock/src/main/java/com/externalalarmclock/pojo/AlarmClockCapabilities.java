package com.externalalarmclock.pojo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Stores alarm clock capabilities.
 */
public class AlarmClockCapabilities {
	@JsonProperty
    private boolean vibrate;
	@JsonProperty
    private boolean wakeupLight;
	@JsonProperty
    private List<String> ringTone = new ArrayList<>();
	@JsonProperty
    private boolean streamAudio;

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public boolean isWakeupLight() {
        return wakeupLight;
    }

    public void setWakeupLight(boolean wakeupLight) {
        this.wakeupLight = wakeupLight;
    }

    public List<String> getRingTone() {
        return ringTone;
    }

    public void setRingTone(List<String> ringTone) {
        this.ringTone = ringTone;
    }

    public boolean isStreamAudio() {
        return streamAudio;
    }

    public void setStreamAudio(boolean streamAudio) {
        this.streamAudio = streamAudio;
    }
}

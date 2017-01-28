package com.externalalarmclock.externalalarmclock.pojo;

import java.util.List;

/**
 * Stores alarm clock capabilities.
 */
public class AlarmClockCapabilities {
    private boolean vibrate;
    private boolean wakeupLight;
    private List<String> ringTone;
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

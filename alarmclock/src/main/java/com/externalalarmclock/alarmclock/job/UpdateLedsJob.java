package com.externalalarmclock.alarmclock.job;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.InputStream;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import com.externalalarmclock.alarmclock.AlarmStore;
import com.externalalarmclock.alarmclock.resources.SetNextAlarmResource;
import com.externalalarmclock.lib.rpiws281x.IRpiWs281x;
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;
import com.externalalarmclock.lib.rpiws281x.image.EStartCorner;
import com.externalalarmclock.lib.rpiws281x.image.ImageConverter;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.DelayStart;
import de.spinscale.dropwizard.jobs.annotations.Every;

@DelayStart("5s")
@Every("1s")
public class UpdateLedsJob extends Job {
	private IRpiWs281x device;
	private AlarmStore alarmStore;
	private Logger logger;
	private ImageConverter converter;
	private int frameCount = -1;
	private int frame;

	public UpdateLedsJob(Logger logger, AlarmStore alarmStore) {
		this.logger = logger;
		this.alarmStore = alarmStore;
	}

	@Override
	public void doJob(JobExecutionContext context) throws JobExecutionException {
		Collection<RpiWs281xChannel> channels = device.getChannels();

		Map<RpiWs281xChannel, List<Color>> pixels = new HashMap<>();
		for (RpiWs281xChannel channel : channels) {
			createPixelsForChannel(pixels, channel);
		}

		if (pixels.isEmpty()) {
			return;
		}
		
		device.render(pixels);
	}

	private void createPixelsForChannel(Map<RpiWs281xChannel, List<Color>> pixels, RpiWs281xChannel channel) {
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime end = alarmStore.getNextAlarm(channel);
		ZonedDateTime start = end == null ? null : end.minusMinutes(30);

		if (start == null || now.isBefore(start) || now.isAfter(end)) {
			if (frameCount != -1) {
				pixels.put(channel, getOffPixelList(channel));
			}
			logger.debug("Before alarm start {}-{}", start, end);
			converter = null;
			frameCount = -1;
			frame = 0;
		} else if (now.isAfter(start) && now.isBefore(end)) {
			if (converter == null) {
				Rectangle r = new Rectangle(0, 0, 32, 18);
				InputStream resourceStream = SetNextAlarmResource.class.getResourceAsStream("/sunup.gif");
				converter = new ImageConverter(r, false, EStartCorner.BOTTOM_RIGHT, resourceStream);
				converter.init();
			}

			Duration afterStart = Duration.between(now, start);
			Duration totalDuration = Duration.between(end, start);
			frame = (int) (converter.getFrames() * afterStart.toNanos() / totalDuration.toNanos());
			
			if (frame != frameCount) {
				pixels.put(channel, converter.getBorderPixels(frame, channel));
				logger.info("During wakeup light {}-{}, frame {}", start, end, frame);
				frameCount = frame;
			}
		}
	}

	public void setDevice(IRpiWs281x device) {
		this.device = device;
	}

	private List<Color> getOffPixelList(RpiWs281xChannel channel) {
		return IntStream.range(0, channel.getLedCount()).mapToObj(i -> this.getNoColor()).collect(Collectors.toList());
	}

	private Color getNoColor() {
		return new Color(0x00000000, true);
	}

	public int getFrame() {
		return frame;
	}
}

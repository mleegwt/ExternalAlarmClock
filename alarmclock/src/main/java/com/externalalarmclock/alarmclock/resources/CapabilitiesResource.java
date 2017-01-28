package com.externalalarmclock.alarmclock.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.externalalarmclock.pojo.AlarmClockCapabilities;

@Path("/capabilities")
@Produces(MediaType.APPLICATION_JSON)
public class CapabilitiesResource {
    @GET
    @Timed
    public AlarmClockCapabilities getCapabilities() {
        AlarmClockCapabilities alarmClockCapabilities = new AlarmClockCapabilities();
        alarmClockCapabilities.setWakeupLight(true);
		return alarmClockCapabilities;
    }
}
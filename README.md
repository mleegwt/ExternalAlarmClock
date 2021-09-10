# ExternalAlarmClock


[![Java CI with Gradle](https://github.com/mleegwt/ExternalAlarmClock/actions/workflows/gradle.yml/badge.svg)](https://github.com/mleegwt/ExternalAlarmClock/actions/workflows/gradle.yml) [![codecov](https://codecov.io/gh/mleegwt/ExternalAlarmClock/branch/master/graph/badge.svg)](https://codecov.io/gh/mleegwt/ExternalAlarmClock)

This project contains a chain of programs that should ultimately result in a usable replacement for my wakeup light.

The replacement uses:
* SK6812 RGBW LED strip
* RPi Zero W (but that could be any RPi)
* Java 8
* Dropwizard
* JNA to interface to RPi based on https://github.com/jgarff/rpi_ws281x wrapped as JNA jar in https://github.com/mleegwt/rpi_ws281x
* Android application to get the next alarm set on the mobile device.

It's a learning experience. Development is work in progress.

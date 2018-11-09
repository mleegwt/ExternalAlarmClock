# ExternalAlarmClock


[![Known Vulnerabilities](https://snyk.io/test/github/mleegwt/ExternalAlarmClock.git/badge.svg)](https://snyk.io/test/github/mleegwt/ExternalAlarmClock.git) [![Build Status](https://travis-ci.org/mleegwt/ExternalAlarmClock.svg?branch=master)](https://travis-ci.org/mleegwt/ExternalAlarmClock)

This project contains a chain of programs that should ultimately result in a usable replacement for my wakeup light.

The replacement uses:
* SK6812 RGBW LED strip
* RPi Zero W (but that could be any RPi)
* Java 8
* Dropwizard
* JNA to interface to RPi based on https://github.com/jgarff/rpi_ws281x wrapped as JNA jar in https://github.com/mleegwt/rpi_ws281x
* Android application to get the next alarm set on the mobile device.

It's a learning experience. Development is work in progress.

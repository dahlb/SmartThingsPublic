/**
 *  Sun Lighting
 *
 *  Copyright 2018 Brendan Dahl
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Sun Lighting",
    namespace: "dahlb",
    author: "Brendan Dahl",
    description: "Change Light Temperature to 2700K at sunset, then 5000K at sunrise",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Pick Light") {
	    input "lights", "capability.colorTemperature", title: "Which Sensor?", multiple: true
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

    unschedule(checkTemp)
	initialize()
}

def initialize() {
	checkTemp()
	runEvery1Minute(checkTemp)
}

def checkTemp() {
    log.debug "currently DayTime: ${isDayTime()}"
//    log.debug "currently DayTime2: ${isDayTime2()}"

//	log.debug('hello')
//	subscribe(location, "sunset", sunsetHandler)
//    subscribe(location, "sunrise", sunriseHandler)
//    log.debug(location.currentValue("sunsetTime"))
//    log.debug(location.currentValue("sunriseTime"))
//    log.debug(location)
//	def now = new Date()
    def invalidLights = getLights(!isDayTime())
	log.debug "lights with wrong temp:"
	log.debug invalidLights
//	if (invalidLights.size() == 0) {
//    	log.debug 'already the correct temp'
//    	return;
//    }
    lights.each { setTemp(it, isDayTime()) }
}

def isLightSetToDaylight(light) {
	log.debug "${light} is at ${light.currentColorTemperature} which is daylight: ${(light.currentColorTemperature >= 4000)}"
	return  (light.currentColorTemperature >= 4000)
}
def getLights(daylight) {
    return lights.findAll { light ->
        isLightSetToDaylight(light) == daylight
    }
}

def isDayTime2() {
	def noParams = getSunriseAndSunset()
    log.debug "sunrise with no parameters: ${noParams.sunrise}"
    log.debug "sunset with no parameters: ${noParams.sunset}"
	return noParams.sunrise < noParams.sunset;
}

def isDayTime() {  
	def s = getSunriseAndSunset(zipCode: zipCode, sunriseOffset: sunriseOffset, sunsetOffset: sunsetOffset)
	def now = new Date()
    def riseTime = s.sunrise
	def setTime = s.sunset
    log.debug "current Time: ${now}"
    log.debug "sunrise: ${riseTime}"
    log.debug "sunset: ${setTime}"	
    return !(setTime.before(now) || riseTime.after(now))
}

def setTemp(light, daylight) {
	if (daylight) {
        log.debug "setting ${light} to daylight"
        light.setColorTemperature(5000)
    } else {
        log.debug "setting ${light} to nightlight"
        light.setColorTemperature(2700)
    }
}

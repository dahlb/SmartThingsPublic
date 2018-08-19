/**
 *  Bathroom Fan Humidity
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
    name: "Bathroom Fan Humidity",
    namespace: "dahlb",
    author: "Brendan Dahl",
    description: "if humidity in the bathroom is higher then rest of the house turn on the bathroom fan",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Selections") {
	    input "humidityBathroom", "capability.relativeHumidityMeasurement", title: "Bathroom Sensor?"
	    input "humidityHouse", "capability.relativeHumidityMeasurement", title: "Non-Bathroom Sensor?"
	    input "fan", "capability.switch", title: "Bathroom Fan?"
        input "threshold", "number", required: true, title: "Relative Humidity Difference To Trigger On?", defaultValue: 10
        input "offDelay", "number", required: true, title: "How Long to leave fan on?", defaultValue: 35
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

    unschedule()
	initialize()
}

def initialize() {
 	subscribe(humidityBathroom, "humidity", bathroomHumidityChanged)
    subscribe(fan, "switch.off", fanOff)
    subscribe(fan, "switch.on", fanOn)
}

def bathroomHumidityChanged(evt) {
	log.debug "bathroom humidity: ${humidityBathroom.currentHumidity}"
	log.debug "non bathroom humidity: ${humidityHouse.currentHumidity}"
    def differenceInHumidity = humidityBathroom.currentHumidity - humidityHouse.currentHumidity
    log.debug "difference is: ${differenceInHumidity}"
    if (differenceInHumidity > threshold) {
    	log.debug "turning on fan"
    	fan.on()
    }
}

def fanOn(evt) {
  log.debug "fan turned on, scheduling turning it off"
  runIn(offDelay * 60, turnFanOff, [overwrite: true])
}

def fanOff(evt) {
  log.debug "fan turned off, canceling scheduling to turn it off"
  unschedule(turnFanOff)
}

def turnFanOff() {
  log.debug "turning fan off"
  fan.off()
}

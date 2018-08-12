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
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

    unschedule(checkHumidity)
	initialize()
}

def initialize() {
	checkHumidity()
	runEvery1Minute(checkHumidity)
}

def checkHumidity() {
	log.debug "bathroom humidity: ${humidityBathroom.currentHumidity}"
	log.debug "non bathroom humidity: ${humidityBathroom.currentHumidity}"
    def differenceInHumidity = humidityBathroom.currentHumidity - humidityBathroom.currentHumidity
    if (differenceInHumidity > 10) {
    	fan.on()
    } else {
    	fan.off()
    }
}

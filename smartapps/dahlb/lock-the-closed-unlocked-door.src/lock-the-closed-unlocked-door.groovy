/**
 *  Lock the Closed Unlocked Door
 *
 *  Copyright 2017 Brendan Dahl
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
    name: "Lock the Closed Unlocked Door",
    namespace: "dahlb",
    author: "Brendan Dahl",
    description: "when door is closed and unlocked for 20 seconds relock it",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Select Door lock and sensor ...") {
	    input "door", "capability.contactSensor", title: "Which Sensor?"
        input "lock", "capability.lock", title: "Which Lock?"
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
    unschedule(checkDoor)
//	unsubscribe()
	initialize()
}

def initialize() {
	runEvery1Minute(checkDoor)
//    runIn(10, checkDoor, [overwrite: true])
//  subscribe(lock, "lock.unlocked", unlocked)
//  subscribe(lock, "lock.locked", locked)
}

//def unlocked(evt) {
//	log.debug "unlocked scheduling"
//    runIn(delay, checkDoor, [overwrite: true])
//}

//def locked(evt) {
//	log.debug "locked canceling schedule"
//	unsubscribe()
//}

def checkDoor() {
	log.debug "checkingDoor"
	if (lock.currentValue("lock") == "unlocked") {
    	if (door.currentValue("contact") == "closed") {
        	log.debug "locking door"
        	lock.lock()
//        } else {
//        	log.debug "scheduling"
//		    runIn(delay, checkDoor, [overwrite: true])
        }
    }
//    runIn(10, checkDoor, [overwrite: true])
}

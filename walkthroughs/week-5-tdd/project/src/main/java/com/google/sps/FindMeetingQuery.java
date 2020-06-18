// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<TimeRange> availableTimes = new ArrayList<TimeRange>();

    // Return no available times when the time request exceeds 24hrs or has no valid minimum
    if(request.getDuration() > TimeRange.WHOLE_DAY.duration() || request.getDuration() <= 0) {
      return availableTimes;
    }

    // Get the required attendees
    Collection<String> attendees = request.getAttendees();

    // If there's no mandatory attendees, get the optional attendees
    if(request.getAttendees().isEmpty()) {
      attendees = request.getOptionalAttendees();
    }

    // Get events where the requested people are busy 
    List<Event> busyTimes = new ArrayList<>();
    for(Event evt: events) {
      for(String attendee: attendees) {
        if(evt.getAttendees().contains(attendee)) {
          busyTimes.add(evt);
          break;
        }
      }
    }

    // Sort events by time
    Comparator<Event> compareByStartTime = (Event evt1, Event evt2) -> TimeRange.ORDER_BY_START.compare(evt1.getWhen(), evt2.getWhen());
    Collections.sort(busyTimes, compareByStartTime);

    int possibleStart = TimeRange.START_OF_DAY;

    for(Event curr:busyTimes) {
      // Check if there's available time before busy meetings
      if(possibleStart + request.getDuration() <= curr.getWhen().start()) {
        availableTimes.add(TimeRange.fromStartEnd(possibleStart, curr.getWhen().start(), false));
      
        // Update the possible start to be the end of the current busy event
        possibleStart = curr.getWhen().end();
  
      } 
      // Make sure the possible start is not in the middle of the current event
      else if(possibleStart < curr.getWhen().end()) {
        possibleStart = curr.getWhen().end();
      }
    }

    // Check end of day
    if(TimeRange.END_OF_DAY - possibleStart >= request.getDuration()) {
      availableTimes.add(TimeRange.fromStartEnd(possibleStart, TimeRange.END_OF_DAY, true));
    }

    return availableTimes;
  }
}

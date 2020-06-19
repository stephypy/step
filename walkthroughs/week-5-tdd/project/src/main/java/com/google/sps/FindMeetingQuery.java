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

    // 1. Get required attendees busy times
    // 2. Get optional busy times
    // 3. Get available times
    // 4. If available 0, get available times with no optional

    // If there are not any attendees, return all time to be available
    if(request.getAttendees().isEmpty() && request.getOptionalAttendees().isEmpty()) {
      availableTimes.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, TimeRange.END_OF_DAY, true));
      return availableTimes;
    }

    // Get mandatory attendees
    Collection<String> attendees = request.getAttendees();

    // If there are no mandatory attendees, consider the optional attendees instead
    if(attendees.isEmpty()) {
      attendees = request.getOptionalAttendees();
    }

    // Get busy events
    List<Event> busyEvents = getBusyEvents(events, attendees);
    busyEvents = sortEvents(busyEvents);

    // Get available times
    availableTimes = getAvailableTimes(busyEvents, request.getDuration());

    // Consider optional attendees if mandatory attendees were included
    if(!request.getAttendees().isEmpty() && !request.getOptionalAttendees().isEmpty()) {
      
      Collection<String> optionalAttendees = request.getOptionalAttendees();
      List<Event> busyEventsOptional = getBusyEvents(events, optionalAttendees); 
      busyEvents.addAll(busyEventsOptional);
      busyEvents = sortEvents(busyEvents);
      
      Collection<TimeRange> availableTimesWithOptional = getAvailableTimes(busyEvents, request.getDuration());

      if(!availableTimesWithOptional.isEmpty()) {
        return availableTimesWithOptional;
      }

    }    
    
    return availableTimes;
  }

  private List<Event> getBusyEvents(Collection<Event> events, Collection<String> attendees) {
    List<Event> busyEvents = new ArrayList<>();
      for(Event evt: events) {
        for(String attendee: attendees) {
          if(evt.getAttendees().contains(attendee)) {
            busyEvents.add(evt);
            break;
          }
        }
      }
    return busyEvents;
  }

  private List<Event> sortEvents(List<Event> events) {
    Comparator<Event> compareByStartTime = (Event evt1, Event evt2) -> TimeRange.ORDER_BY_START.compare(evt1.getWhen(), evt2.getWhen());
    Collections.sort(events, compareByStartTime);
    return events;
  }

  private Collection<TimeRange> getAvailableTimes(List<Event> unavailableList, long duration) {
    if(unavailableList.isEmpty()) {
      Collections.emptySet();
    }

    Collection<TimeRange> availableTimes = new ArrayList<TimeRange>();
    int possibleStart = TimeRange.START_OF_DAY;
    
    for(Event curr:unavailableList) {
      // Check if there's available time before busy meetings
      if(possibleStart + duration <= curr.getWhen().start()) {
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
    if(TimeRange.END_OF_DAY - possibleStart >= duration) {
      availableTimes.add(TimeRange.fromStartEnd(possibleStart, TimeRange.END_OF_DAY, true));
    }

    return availableTimes;
  }
}

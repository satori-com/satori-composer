package com.satori.mods.suite.gtfs;

import com.google.transit.realtime.GtfsRealtime.*;

public class GtfsProtoBufConverter {
  
  
  public static GtfsTranslatedString convert(TranslatedString value) {
    if (value == null) {
      return null;
    }
    GtfsTranslatedString result = new GtfsTranslatedString();
    do {
      GtfsTranslatedString.Translation[] array = new GtfsTranslatedString.Translation[value.getTranslationCount()];
      for (int i = 0; i < array.length; i += 1) {
        array[i] = convert(value.getTranslation(i));
      }
      result.translation = array;
    } while (false);
    return result;
  }
  
  public static GtfsTranslatedString.Translation convert(com.google.transit.realtime.GtfsRealtime.TranslatedString.Translation value) {
    if (value == null) {
      return null;
    }
    GtfsTranslatedString.Translation result = new GtfsTranslatedString.Translation();
    if (value.hasLanguage()) {
      result.language = value.getLanguage();
    }
    if (value.hasText()) {
      result.text = value.getText();
    }
    return result;
  }
  
  public static GtfsTimeRange convert(TimeRange value) {
    if (value == null) {
      return null;
    }
    GtfsTimeRange result = new GtfsTimeRange();
    if (value.hasStart()) {
      result.start = value.getStart();
    }
    if (value.hasEnd()) {
      result.end = value.getEnd();
    }
    return result;
  }
  
  public static GtfsFeedMessage convert(FeedMessage value) {
    if (value == null) {
      return null;
    }
    GtfsFeedMessage result = new GtfsFeedMessage();
    if (value.hasHeader()) {
      result.header = convert(value.getHeader());
    }
    do {
      GtfsFeedEntity[] array = new GtfsFeedEntity[value.getEntityCount()];
      for (int i = 0; i < array.length; i += 1) {
        array[i] = convert(value.getEntity(i));
      }
      result.entity = array;
    } while (false);
    return result;
  }
  
  public static GtfsVehiclePosition convert(VehiclePosition value) {
    if (value == null) {
      return null;
    }
    GtfsVehiclePosition result = new GtfsVehiclePosition();
    if (value.hasTrip()) {
      result.trip = convert(value.getTrip());
    }
    if (value.hasCurrentStopSequence()) {
      result.currentStopSequence = value.getCurrentStopSequence();
    }
    if (value.hasCongestionLevel()) {
      result.congestionLevel = convert(value.getCongestionLevel());
    }
    if (value.hasOccupancyStatus()) {
      result.occupancyStatus = convert(value.getOccupancyStatus());
    }
    if (value.hasStopId()) {
      result.stopId = value.getStopId();
    }
    if (value.hasCurrentStatus()) {
      result.currentStatus = convert(value.getCurrentStatus());
    }
    if (value.hasPosition()) {
      result.position = convert(value.getPosition());
    }
    if (value.hasVehicle()) {
      result.vehicle = convert(value.getVehicle());
    }
    if (value.hasTimestamp()) {
      result.timestamp = value.getTimestamp();
    }
    return result;
  }
  
  public static GtfsVehiclePosition.VehicleStopStatus convert(com.google.transit.realtime.GtfsRealtime.VehiclePosition.VehicleStopStatus value) {
    if (value == null) {
      return null;
    }
    return (GtfsVehiclePosition.VehicleStopStatus.fromInt(value.getNumber()));
  }
  
  public static GtfsVehiclePosition.CongestionLevel convert(com.google.transit.realtime.GtfsRealtime.VehiclePosition.CongestionLevel value) {
    if (value == null) {
      return null;
    }
    return (GtfsVehiclePosition.CongestionLevel.fromInt(value.getNumber()));
  }
  
  public static GtfsVehiclePosition.OccupancyStatus convert(com.google.transit.realtime.GtfsRealtime.VehiclePosition.OccupancyStatus value) {
    if (value == null) {
      return null;
    }
    return (GtfsVehiclePosition.OccupancyStatus.fromInt(value.getNumber()));
  }
  
  public static GtfsEntitySelector convert(EntitySelector value) {
    if (value == null) {
      return null;
    }
    GtfsEntitySelector result = new GtfsEntitySelector();
    if (value.hasRouteId()) {
      result.routeId = value.getRouteId();
    }
    if (value.hasTrip()) {
      result.trip = convert(value.getTrip());
    }
    if (value.hasRouteType()) {
      result.routeType = value.getRouteType();
    }
    if (value.hasStopId()) {
      result.stopId = value.getStopId();
    }
    if (value.hasAgencyId()) {
      result.agencyId = value.getAgencyId();
    }
    return result;
  }
  
  public static GtfsTripUpdate convert(TripUpdate value) {
    if (value == null) {
      return null;
    }
    GtfsTripUpdate result = new GtfsTripUpdate();
    if (value.hasTrip()) {
      result.trip = convert(value.getTrip());
    }
    if (value.hasDelay()) {
      result.delay = value.getDelay();
    }
    do {
      GtfsTripUpdate.StopTimeUpdate[] array = new GtfsTripUpdate.StopTimeUpdate[value.getStopTimeUpdateCount()];
      for (int i = 0; i < array.length; i += 1) {
        array[i] = convert(value.getStopTimeUpdate(i));
      }
      result.stopTimeUpdate = array;
    } while (false);
    if (value.hasVehicle()) {
      result.vehicle = convert(value.getVehicle());
    }
    if (value.hasTimestamp()) {
      result.timestamp = value.getTimestamp();
    }
    return result;
  }
  
  public static GtfsTripUpdate.StopTimeEvent convert(com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeEvent value) {
    if (value == null) {
      return null;
    }
    GtfsTripUpdate.StopTimeEvent result = new GtfsTripUpdate.StopTimeEvent();
    if (value.hasDelay()) {
      result.delay = value.getDelay();
    }
    if (value.hasTime()) {
      result.time = value.getTime();
    }
    if (value.hasUncertainty()) {
      result.uncertainty = value.getUncertainty();
    }
    return result;
  }
  
  public static GtfsTripUpdate.StopTimeUpdate convert(com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate value) {
    if (value == null) {
      return null;
    }
    GtfsTripUpdate.StopTimeUpdate result = new GtfsTripUpdate.StopTimeUpdate();
    if (value.hasStopSequence()) {
      result.stopSequence = value.getStopSequence();
    }
    if (value.hasArrival()) {
      result.arrival = convert(value.getArrival());
    }
    if (value.hasStopId()) {
      result.stopId = value.getStopId();
    }
    if (value.hasDeparture()) {
      result.departure = convert(value.getDeparture());
    }
    if (value.hasScheduleRelationship()) {
      result.scheduleRelationship = convert(value.getScheduleRelationship());
    }
    return result;
  }
  
  public static GtfsTripUpdate.StopTimeUpdate.ScheduleRelationship convert(com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate.ScheduleRelationship value) {
    if (value == null) {
      return null;
    }
    return (GtfsTripUpdate.StopTimeUpdate.ScheduleRelationship.fromInt(value.getNumber()));
  }
  
  public static GtfsTripDescriptor convert(TripDescriptor value) {
    if (value == null) {
      return null;
    }
    GtfsTripDescriptor result = new GtfsTripDescriptor();
    if (value.hasStartTime()) {
      result.startTime = value.getStartTime();
    }
    if (value.hasTripId()) {
      result.tripId = value.getTripId();
    }
    if (value.hasDirectionId()) {
      result.directionId = value.getDirectionId();
    }
    if (value.hasRouteId()) {
      result.routeId = value.getRouteId();
    }
    if (value.hasScheduleRelationship()) {
      result.scheduleRelationship = convert(value.getScheduleRelationship());
    }
    if (value.hasStartDate()) {
      result.startDate = value.getStartDate();
    }
    return result;
  }
  
  public static GtfsTripDescriptor.ScheduleRelationship convert(com.google.transit.realtime.GtfsRealtime.TripDescriptor.ScheduleRelationship value) {
    if (value == null) {
      return null;
    }
    return (GtfsTripDescriptor.ScheduleRelationship.fromInt(value.getNumber()));
  }
  
  public static GtfsPosition convert(Position value) {
    if (value == null) {
      return null;
    }
    GtfsPosition result = new GtfsPosition();
    if (value.hasOdometer()) {
      result.odometer = value.getOdometer();
    }
    if (value.hasBearing()) {
      result.bearing = value.getBearing();
    }
    if (value.hasLatitude()) {
      result.latitude = value.getLatitude();
    }
    if (value.hasSpeed()) {
      result.speed = value.getSpeed();
    }
    if (value.hasLongitude()) {
      result.longitude = value.getLongitude();
    }
    return result;
  }
  
  public static GtfsAlert convert(Alert value) {
    if (value == null) {
      return null;
    }
    GtfsAlert result = new GtfsAlert();
    do {
      GtfsTimeRange[] array = new GtfsTimeRange[value.getActivePeriodCount()];
      for (int i = 0; i < array.length; i += 1) {
        array[i] = convert(value.getActivePeriod(i));
      }
      result.activePeriod = array;
    } while (false);
    if (value.hasEffect()) {
      result.effect = convert(value.getEffect());
    }
    if (value.hasCause()) {
      result.cause = convert(value.getCause());
    }
    if (value.hasDescriptionText()) {
      result.descriptionText = convert(value.getDescriptionText());
    }
    if (value.hasHeaderText()) {
      result.headerText = convert(value.getHeaderText());
    }
    do {
      GtfsEntitySelector[] array = new GtfsEntitySelector[value.getInformedEntityCount()];
      for (int i = 0; i < array.length; i += 1) {
        array[i] = convert(value.getInformedEntity(i));
      }
      result.informedEntity = array;
    } while (false);
    if (value.hasUrl()) {
      result.url = convert(value.getUrl());
    }
    return result;
  }
  
  public static GtfsAlert.Cause convert(com.google.transit.realtime.GtfsRealtime.Alert.Cause value) {
    if (value == null) {
      return null;
    }
    return (GtfsAlert.Cause.fromInt(value.getNumber()));
  }
  
  public static GtfsAlert.Effect convert(com.google.transit.realtime.GtfsRealtime.Alert.Effect value) {
    if (value == null) {
      return null;
    }
    return (GtfsAlert.Effect.fromInt(value.getNumber()));
  }
  
  public static GtfsFeedEntity convert(FeedEntity value) {
    if (value == null) {
      return null;
    }
    GtfsFeedEntity result = new GtfsFeedEntity();
    if (value.hasIsDeleted()) {
      result.isDeleted = value.getIsDeleted();
    }
    if (value.hasAlert()) {
      result.alert = convert(value.getAlert());
    }
    if (value.hasTripUpdate()) {
      result.tripUpdate = convert(value.getTripUpdate());
    }
    if (value.hasId()) {
      result.id = value.getId();
    }
    if (value.hasVehicle()) {
      result.vehicle = convert(value.getVehicle());
    }
    return result;
  }
  
  public static GtfsVehicleDescriptor convert(VehicleDescriptor value) {
    if (value == null) {
      return null;
    }
    GtfsVehicleDescriptor result = new GtfsVehicleDescriptor();
    if (value.hasLicensePlate()) {
      result.licensePlate = value.getLicensePlate();
    }
    if (value.hasId()) {
      result.id = value.getId();
    }
    if (value.hasLabel()) {
      result.label = value.getLabel();
    }
    return result;
  }
  
  public static GtfsFeedHeader convert(FeedHeader value) {
    if (value == null) {
      return null;
    }
    GtfsFeedHeader result = new GtfsFeedHeader();
    if (value.hasGtfsRealtimeVersion()) {
      result.gtfsRealtimeVersion = value.getGtfsRealtimeVersion();
    }
    if (value.hasIncrementality()) {
      result.incrementality = convert(value.getIncrementality());
    }
    if (value.hasTimestamp()) {
      result.timestamp = value.getTimestamp();
    }
    return result;
  }
  
  public static GtfsFeedHeader.Incrementality convert(com.google.transit.realtime.GtfsRealtime.FeedHeader.Incrementality value) {
    if (value == null) {
      return null;
    }
    return (GtfsFeedHeader.Incrementality.fromInt(value.getNumber()));
  }
  
}

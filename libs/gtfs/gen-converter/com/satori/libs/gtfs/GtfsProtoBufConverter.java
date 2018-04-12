
package com.satori.libs.gtfs;

import com.satori.libs.gtfs.GtfsRealtime.Alert;
import com.satori.libs.gtfs.GtfsRealtime.EntitySelector;
import com.satori.libs.gtfs.GtfsRealtime.FeedEntity;
import com.satori.libs.gtfs.GtfsRealtime.FeedHeader;
import com.satori.libs.gtfs.GtfsRealtime.FeedMessage;
import com.satori.libs.gtfs.GtfsRealtime.Position;
import com.satori.libs.gtfs.GtfsRealtime.TimeRange;
import com.satori.libs.gtfs.GtfsRealtime.TranslatedString;
import com.satori.libs.gtfs.GtfsRealtime.TripDescriptor;
import com.satori.libs.gtfs.GtfsRealtime.TripUpdate;
import com.satori.libs.gtfs.GtfsRealtime.VehicleDescriptor;
import com.satori.libs.gtfs.GtfsRealtime.VehiclePosition;

public class GtfsProtoBufConverter {


    public static com.satori.libs.gtfs.GtfsTranslatedString convert(TranslatedString value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsTranslatedString result = new com.satori.libs.gtfs.GtfsTranslatedString();
        do{
          com.satori.libs.gtfs.GtfsTranslatedString.Translation[] array = new com.satori.libs.gtfs.GtfsTranslatedString.Translation[value.getTranslationCount()];
          for(int i=0; i<array.length; i+=1){
            array[i] = convert(value.getTranslation(i));
          }
          result.translation = array;
        }while(false);
        return result;
    }

    public static com.satori.libs.gtfs.GtfsTranslatedString.Translation convert(com.satori.libs.gtfs.GtfsRealtime.TranslatedString.Translation value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsTranslatedString.Translation result = new com.satori.libs.gtfs.GtfsTranslatedString.Translation();
        if(value.hasLanguage()){
          result.language = value.getLanguage();
        }
        if(value.hasText()){
          result.text = value.getText();
        }
        return result;
    }

    public static com.satori.libs.gtfs.GtfsTimeRange convert(TimeRange value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsTimeRange result = new com.satori.libs.gtfs.GtfsTimeRange();
        if(value.hasStart()){
          result.start = value.getStart();
        }
        if(value.hasEnd()){
          result.end = value.getEnd();
        }
        return result;
    }

    public static com.satori.libs.gtfs.GtfsFeedMessage convert(FeedMessage value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsFeedMessage result = new com.satori.libs.gtfs.GtfsFeedMessage();
        if(value.hasHeader()){
          result.header = convert(value.getHeader());
        }
        do{
          com.satori.libs.gtfs.GtfsFeedEntity[] array = new com.satori.libs.gtfs.GtfsFeedEntity[value.getEntityCount()];
          for(int i=0; i<array.length; i+=1){
            array[i] = convert(value.getEntity(i));
          }
          result.entity = array;
        }while(false);
        return result;
    }

    public static com.satori.libs.gtfs.GtfsVehiclePosition convert(VehiclePosition value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsVehiclePosition result = new com.satori.libs.gtfs.GtfsVehiclePosition();
        if(value.hasTrip()){
          result.trip = convert(value.getTrip());
        }
        if(value.hasCurrentStopSequence()){
          result.currentStopSequence = value.getCurrentStopSequence();
        }
        if(value.hasCongestionLevel()){
          result.congestionLevel = convert(value.getCongestionLevel());
        }
        if(value.hasOccupancyStatus()){
          result.occupancyStatus = convert(value.getOccupancyStatus());
        }
        if(value.hasStopId()){
          result.stopId = value.getStopId();
        }
        if(value.hasCurrentStatus()){
          result.currentStatus = convert(value.getCurrentStatus());
        }
        if(value.hasPosition()){
          result.position = convert(value.getPosition());
        }
        if(value.hasVehicle()){
          result.vehicle = convert(value.getVehicle());
        }
        if(value.hasTimestamp()){
          result.timestamp = value.getTimestamp();
        }
        return result;
    }

    public static com.satori.libs.gtfs.GtfsVehiclePosition.VehicleStopStatus convert(com.satori.libs.gtfs.GtfsRealtime.VehiclePosition.VehicleStopStatus value) {
        if(value == null){
          return null;
        }
        return (com.satori.libs.gtfs.GtfsVehiclePosition.VehicleStopStatus.fromInt(value.getNumber()));
    }

    public static com.satori.libs.gtfs.GtfsVehiclePosition.CongestionLevel convert(com.satori.libs.gtfs.GtfsRealtime.VehiclePosition.CongestionLevel value) {
        if(value == null){
          return null;
        }
        return (com.satori.libs.gtfs.GtfsVehiclePosition.CongestionLevel.fromInt(value.getNumber()));
    }

    public static com.satori.libs.gtfs.GtfsVehiclePosition.OccupancyStatus convert(com.satori.libs.gtfs.GtfsRealtime.VehiclePosition.OccupancyStatus value) {
        if(value == null){
          return null;
        }
        return (com.satori.libs.gtfs.GtfsVehiclePosition.OccupancyStatus.fromInt(value.getNumber()));
    }

    public static com.satori.libs.gtfs.GtfsEntitySelector convert(EntitySelector value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsEntitySelector result = new com.satori.libs.gtfs.GtfsEntitySelector();
        if(value.hasRouteId()){
          result.routeId = value.getRouteId();
        }
        if(value.hasTrip()){
          result.trip = convert(value.getTrip());
        }
        if(value.hasRouteType()){
          result.routeType = value.getRouteType();
        }
        if(value.hasStopId()){
          result.stopId = value.getStopId();
        }
        if(value.hasAgencyId()){
          result.agencyId = value.getAgencyId();
        }
        return result;
    }

    public static com.satori.libs.gtfs.GtfsTripUpdate convert(TripUpdate value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsTripUpdate result = new com.satori.libs.gtfs.GtfsTripUpdate();
        if(value.hasTrip()){
          result.trip = convert(value.getTrip());
        }
        if(value.hasDelay()){
          result.delay = value.getDelay();
        }
        do{
          com.satori.libs.gtfs.GtfsTripUpdate.StopTimeUpdate[] array = new com.satori.libs.gtfs.GtfsTripUpdate.StopTimeUpdate[value.getStopTimeUpdateCount()];
          for(int i=0; i<array.length; i+=1){
            array[i] = convert(value.getStopTimeUpdate(i));
          }
          result.stopTimeUpdate = array;
        }while(false);
        if(value.hasVehicle()){
          result.vehicle = convert(value.getVehicle());
        }
        if(value.hasTimestamp()){
          result.timestamp = value.getTimestamp();
        }
        return result;
    }

    public static com.satori.libs.gtfs.GtfsTripUpdate.StopTimeEvent convert(com.satori.libs.gtfs.GtfsRealtime.TripUpdate.StopTimeEvent value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsTripUpdate.StopTimeEvent result = new com.satori.libs.gtfs.GtfsTripUpdate.StopTimeEvent();
        if(value.hasDelay()){
          result.delay = value.getDelay();
        }
        if(value.hasTime()){
          result.time = value.getTime();
        }
        if(value.hasUncertainty()){
          result.uncertainty = value.getUncertainty();
        }
        return result;
    }

    public static com.satori.libs.gtfs.GtfsTripUpdate.StopTimeUpdate convert(com.satori.libs.gtfs.GtfsRealtime.TripUpdate.StopTimeUpdate value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsTripUpdate.StopTimeUpdate result = new com.satori.libs.gtfs.GtfsTripUpdate.StopTimeUpdate();
        if(value.hasStopSequence()){
          result.stopSequence = value.getStopSequence();
        }
        if(value.hasArrival()){
          result.arrival = convert(value.getArrival());
        }
        if(value.hasStopId()){
          result.stopId = value.getStopId();
        }
        if(value.hasDeparture()){
          result.departure = convert(value.getDeparture());
        }
        if(value.hasScheduleRelationship()){
          result.scheduleRelationship = convert(value.getScheduleRelationship());
        }
        return result;
    }

    public static com.satori.libs.gtfs.GtfsTripUpdate.StopTimeUpdate.ScheduleRelationship convert(com.satori.libs.gtfs.GtfsRealtime.TripUpdate.StopTimeUpdate.ScheduleRelationship value) {
        if(value == null){
          return null;
        }
        return (com.satori.libs.gtfs.GtfsTripUpdate.StopTimeUpdate.ScheduleRelationship.fromInt(value.getNumber()));
    }

    public static com.satori.libs.gtfs.GtfsTripDescriptor convert(TripDescriptor value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsTripDescriptor result = new com.satori.libs.gtfs.GtfsTripDescriptor();
        if(value.hasStartTime()){
          result.startTime = value.getStartTime();
        }
        if(value.hasTripId()){
          result.tripId = value.getTripId();
        }
        if(value.hasDirectionId()){
          result.directionId = value.getDirectionId();
        }
        if(value.hasRouteId()){
          result.routeId = value.getRouteId();
        }
        if(value.hasScheduleRelationship()){
          result.scheduleRelationship = convert(value.getScheduleRelationship());
        }
        if(value.hasStartDate()){
          result.startDate = value.getStartDate();
        }
        return result;
    }

    public static com.satori.libs.gtfs.GtfsTripDescriptor.ScheduleRelationship convert(com.satori.libs.gtfs.GtfsRealtime.TripDescriptor.ScheduleRelationship value) {
        if(value == null){
          return null;
        }
        return (com.satori.libs.gtfs.GtfsTripDescriptor.ScheduleRelationship.fromInt(value.getNumber()));
    }

    public static com.satori.libs.gtfs.GtfsPosition convert(Position value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsPosition result = new com.satori.libs.gtfs.GtfsPosition();
        if(value.hasOdometer()){
          result.odometer = value.getOdometer();
        }
        if(value.hasBearing()){
          result.bearing = value.getBearing();
        }
        if(value.hasLatitude()){
          result.latitude = value.getLatitude();
        }
        if(value.hasSpeed()){
          result.speed = value.getSpeed();
        }
        if(value.hasLongitude()){
          result.longitude = value.getLongitude();
        }
        return result;
    }

    public static com.satori.libs.gtfs.GtfsAlert convert(Alert value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsAlert result = new com.satori.libs.gtfs.GtfsAlert();
        do{
          com.satori.libs.gtfs.GtfsTimeRange[] array = new com.satori.libs.gtfs.GtfsTimeRange[value.getActivePeriodCount()];
          for(int i=0; i<array.length; i+=1){
            array[i] = convert(value.getActivePeriod(i));
          }
          result.activePeriod = array;
        }while(false);
        if(value.hasEffect()){
          result.effect = convert(value.getEffect());
        }
        if(value.hasCause()){
          result.cause = convert(value.getCause());
        }
        if(value.hasDescriptionText()){
          result.descriptionText = convert(value.getDescriptionText());
        }
        if(value.hasHeaderText()){
          result.headerText = convert(value.getHeaderText());
        }
        do{
          com.satori.libs.gtfs.GtfsEntitySelector[] array = new com.satori.libs.gtfs.GtfsEntitySelector[value.getInformedEntityCount()];
          for(int i=0; i<array.length; i+=1){
            array[i] = convert(value.getInformedEntity(i));
          }
          result.informedEntity = array;
        }while(false);
        if(value.hasUrl()){
          result.url = convert(value.getUrl());
        }
        return result;
    }

    public static com.satori.libs.gtfs.GtfsAlert.Cause convert(com.satori.libs.gtfs.GtfsRealtime.Alert.Cause value) {
        if(value == null){
          return null;
        }
        return (com.satori.libs.gtfs.GtfsAlert.Cause.fromInt(value.getNumber()));
    }

    public static com.satori.libs.gtfs.GtfsAlert.Effect convert(com.satori.libs.gtfs.GtfsRealtime.Alert.Effect value) {
        if(value == null){
          return null;
        }
        return (com.satori.libs.gtfs.GtfsAlert.Effect.fromInt(value.getNumber()));
    }

    public static com.satori.libs.gtfs.GtfsFeedEntity convert(FeedEntity value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsFeedEntity result = new com.satori.libs.gtfs.GtfsFeedEntity();
        if(value.hasIsDeleted()){
          result.isDeleted = value.getIsDeleted();
        }
        if(value.hasAlert()){
          result.alert = convert(value.getAlert());
        }
        if(value.hasTripUpdate()){
          result.tripUpdate = convert(value.getTripUpdate());
        }
        if(value.hasId()){
          result.id = value.getId();
        }
        if(value.hasVehicle()){
          result.vehicle = convert(value.getVehicle());
        }
        return result;
    }

    public static com.satori.libs.gtfs.GtfsVehicleDescriptor convert(VehicleDescriptor value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsVehicleDescriptor result = new com.satori.libs.gtfs.GtfsVehicleDescriptor();
        if(value.hasLicensePlate()){
          result.licensePlate = value.getLicensePlate();
        }
        if(value.hasId()){
          result.id = value.getId();
        }
        if(value.hasLabel()){
          result.label = value.getLabel();
        }
        return result;
    }

    public static com.satori.libs.gtfs.GtfsFeedHeader convert(FeedHeader value) {
        if(value == null){
          return null;
        }
        com.satori.libs.gtfs.GtfsFeedHeader result = new com.satori.libs.gtfs.GtfsFeedHeader();
        if(value.hasGtfsRealtimeVersion()){
          result.gtfsRealtimeVersion = value.getGtfsRealtimeVersion();
        }
        if(value.hasIncrementality()){
          result.incrementality = convert(value.getIncrementality());
        }
        if(value.hasTimestamp()){
          result.timestamp = value.getTimestamp();
        }
        return result;
    }

    public static com.satori.libs.gtfs.GtfsFeedHeader.Incrementality convert(com.satori.libs.gtfs.GtfsRealtime.FeedHeader.Incrementality value) {
        if(value == null){
          return null;
        }
        return (com.satori.libs.gtfs.GtfsFeedHeader.Incrementality.fromInt(value.getNumber()));
    }

}

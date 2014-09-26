package ar.com.globallogic.promocion.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.globallogic.promocion.commons.CommonsConstants;
import ar.com.globallogic.promocion.commons.exception.OutOffZoneException;
import ar.com.globallogic.promocion.exception.UntrackeableException;
import ar.com.globallogic.promocion.mongo.model.ChanellInfo;
import ar.com.globallogic.promocion.mongo.model.Event;
import ar.com.globallogic.promocion.mongo.model.Position;
import ar.com.globallogic.promocion.mongo.model.PositionHistory;
import ar.com.globallogic.promocion.mongo.model.PositionRecord;
import ar.com.globallogic.promocion.mongo.model.Trackeable;
import ar.com.globallogic.promocion.mongo.repository.TrackeableRepository;
import ar.com.globallogic.promocion.notifications.NotificationService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.WriteResult;

@Component
public class HistoryService {

	@Autowired
	Jongo jongo;

	@Autowired
	@Qualifier("trackeableRepository")
	TrackeableRepository trackeableRepository;

	@Autowired
	NotificationService notificationService;

	@SuppressWarnings("unchecked")
	public void addRecord(Position record, String id) {

		setCurrentPosition(record, id);

		MongoCollection collection = jongo
				.getCollection(CommonsConstants.HISTORY_COLLECTION);

		if (!insertRecord(record, id, collection)) {
			createHistory(record, id, collection);
		}

		if (trackeableRepository.isOutOfZone(id)) {
			saveOutOffZoneEvent(id);
			throw new OutOffZoneException();
		} else {
			saveBackToZoneEvent(id);
		}
		MongoCollection chanell = jongo
				.getCollection(CommonsConstants.CHANELL_COLLECTION);
		Iterator<ChanellInfo> iterator = chanell.find().as(ChanellInfo.class)
				.iterator();
		List<ChanellInfo> chanells = IteratorUtils.toList(iterator);
		for (ChanellInfo chanellInfo : chanells) {

			Trackeable byIdInBox = trackeableRepository.getByIdInBox(id,
					chanellInfo.getSouthWest().getLongitude(), chanellInfo
							.getSouthWest().getLatitude(), chanellInfo
							.getNorthEast().getLongitude(), chanellInfo
							.getNorthEast().getLatitude());

			if(byIdInBox != null){
				JSONObject message = new JSONObject();
				message.append("trackeable_id", id);
				message.append("latitude", record.getLatitude());
				message.append("longitude", record.getLongitude());
				message.append("velocity", Math.random() * 100);
				notificationService.publishPosition(message);
			}
		}
	}

	private void saveOutOffZoneEvent(String id) {
		Notification notification = new Notification();
		MongoCollection collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);
		Trackeable trackeable = collection.findOne("{ _id : #} ", id).as(
				Trackeable.class);
		if (!trackeable.getRedFlag()) {
			trackeable.setRedFlag(true);
			Event event = new Event();
			event.setDate(new Date());
			if (trackeable.getIsVehicle()) {
				event.setCode(CommonsConstants.VEHICLE_OUT_OF_ZONE_CODE);
				event.setValue(CommonsConstants.VEHICLE_OUT_OF_ZONE_MESSAGE);
				notification
						.setDescription(CommonsConstants.VEHICLE_OUT_OF_ZONE_MESSAGE);
			} else {
				event.setCode(CommonsConstants.GENDARME_OUT_OF_ZONE_CODE);
				event.setValue(CommonsConstants.GENDARME_OUT_OF_ZONE_MESSAGE);
				notification
						.setDescription(CommonsConstants.GENDARME_OUT_OF_ZONE_MESSAGE);
			}
			persistEvent(event, trackeable);
			notification.setEvent_id(event.getId());
			notification.setTrackeable_id(event.getId());
			ObjectMapper mapper = new ObjectMapper();

			try {
				JSONObject json = new JSONObject(
						mapper.writeValueAsString(notification));
				notificationService.publish(json);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveBackToZoneEvent(String id) {
		Notification notification = new Notification();
		MongoCollection collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);
		Trackeable trackeable = collection.findOne("{ _id : #} ", id).as(
				Trackeable.class);
		if (trackeable.getRedFlag()) {
			trackeable.setRedFlag(false);
			Event event = new Event();
			event.setDate(new Date());
			if (trackeable.getIsVehicle()) {
				event.setCode(CommonsConstants.VEHICLE_BACK_TO_ZONE_CODE);
				event.setValue(CommonsConstants.VEHICLE_BACK_TO_ZONE_MESSAGE);
				notification
						.setDescription(CommonsConstants.VEHICLE_BACK_TO_ZONE_MESSAGE);
			} else {
				event.setCode(CommonsConstants.GENDARME_BACK_TO_ZONE_CODE);
				event.setValue(CommonsConstants.GENDARME_BACK_TO_ZONE_MESSAGE);
				notification
						.setDescription(CommonsConstants.GENDARME_BACK_TO_ZONE_MESSAGE);
			}
			persistEvent(event, trackeable);
			notification.setEvent_id(event.getId());
			notification.setTrackeable_id(event.getId());
			ObjectMapper mapper = new ObjectMapper();

			try {
				JSONObject json = new JSONObject(
						mapper.writeValueAsString(notification));
				notificationService.publish(json);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}

	private void persistEvent(Event event, Trackeable trackeable) {
		event.setTrackeable_id(trackeable.getId());
		MongoCollection event_collection = jongo
				.getCollection(CommonsConstants.EVENT_COLLECTION);
		MongoCollection trackeable_collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);
		event_collection.save(event);
		trackeable_collection.save(trackeable);
	}

	private boolean insertRecord(Position record, String id,
			MongoCollection collection) {

		PositionRecord positionRecord = new PositionRecord();
		positionRecord.setPostion(record);
		positionRecord.setDate(new Date());

		WriteResult with = collection.update("{trackeable_id:#}", id).with(
				"{$push:{ records: # } }", positionRecord);
		return with.isUpdateOfExisting();
	}

	private PositionHistory instanceHistory(Position record, String id) {
		PositionHistory history;
		PositionRecord positionRecord = new PositionRecord();
		positionRecord.setPostion(record);
		positionRecord.setDate(new Date());
		history = new PositionHistory();
		history.setTrackeable_id(id);
		history.addRecord(positionRecord);
		return history;
	}

	private void createHistory(Position record, String id,
			MongoCollection collection) {
		PositionHistory history = instanceHistory(record, id);
		collection.save(history);
	}

	private void setCurrentPosition(Position record, String id) {

		MongoCollection collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);

		WriteResult result = collection.update("{ _id : # }", id).with(
				"{ $set :{currentPosition : #}}", record);

		if (!result.isUpdateOfExisting()) {
			throw new UntrackeableException();
		}

	}

	public List<PositionRecord> list(String id) {

		MongoCollection collection = jongo
				.getCollection(CommonsConstants.HISTORY_COLLECTION);
		return collection.findOne("{trackeable_id : # }", id)
				.as(PositionHistory.class).getRecords();

	}

}

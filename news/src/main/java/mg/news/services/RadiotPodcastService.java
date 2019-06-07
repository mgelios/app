package mg.news.services;

import mg.news.converter.RadiotPodcastDTOToEntity;
import mg.news.converter.RadiotPodcastEntityToDTO;
import mg.news.dbentities.RadiotPodcastDBEntity;
import mg.news.dbentities.RadiotPodcastTimeLabelDBEntity;
import mg.news.mapper.RadiotPodcastMapper;
import mg.news.models.RadiotPodcast;
import mg.news.repositories.RadiotPodcastRepository;
import mg.news.repositories.RadiotPodcastTimeLabelRepository;
import mg.news.utils.RadiotUrlBuilder;
import mg.utils.JSONConsumer;
import mg.utils.JSONHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RadiotPodcastService {

    @Autowired
    private JSONConsumer jsonConsumer;
    @Autowired
    private JSONHelper jsonHelper;
    @Autowired
    private RadiotUrlBuilder radiotUrlBuilder;
    @Autowired
    private RadiotPodcastRepository radiotPodcastRepository;
    @Autowired
    private RadiotPodcastTimeLabelRepository radiotPodcastTimeLabelRepository;
    @Autowired
    private RadiotPodcastDTOToEntity radiotPodcastDTOToEntity;
    @Autowired
    private RadiotPodcastEntityToDTO radiotPodcastEntityToDTO;

    public List<RadiotPodcast> getRadiotPodcasts() {
        List<RadiotPodcastDBEntity> podcasts = new ArrayList<>();
        if (radiotPodcastRepository.findAll().iterator().hasNext()) {
            radiotPodcastRepository.findAll().forEach(podcasts::add);
        } else {
            podcasts = updateRadiotPodcasts();
        }
        RadiotPodcast podcast = RadiotPodcastMapper.INSTANCE.mapToDTO(podcasts.get(0));
        return podcasts.stream()
                .map(radiotPodcastEntityToDTO::convert)
                .collect(Collectors.toList());
    }

    public List<RadiotPodcastDBEntity> updateRadiotPodcasts() {
        radiotPodcastRepository.deleteAll();
        return saveRadiotPodcasts(jsonConsumer.getJsonArray(radiotUrlBuilder.buildPodcastUrl()));
    }

    private List<RadiotPodcastDBEntity> saveRadiotPodcasts(JSONArray json) {
        List<RadiotPodcastDBEntity> result = new ArrayList<>();
        json.forEach(item -> {
            RadiotPodcastDBEntity radiotPodcast = new RadiotPodcastDBEntity();
            JSONObject jsonItem = (JSONObject) item;
            Set<RadiotPodcastTimeLabelDBEntity> timeLabels = null;
            radiotPodcast.setAudioUrl(jsonHelper.getString(jsonItem, "audio_url"));
            radiotPodcast.setBody(jsonHelper.getString(jsonItem, "body"));
            radiotPodcast.setImage(jsonHelper.getString(jsonItem, "image"));
            radiotPodcast.setShowNotes(jsonHelper.getString(jsonItem, "show_notes"));
            radiotPodcast.setTitle(jsonHelper.getString(jsonItem, "title"));
            radiotPodcast.setUrl(jsonHelper.getString(jsonItem, "url"));
            radiotPodcast.setDate(Timestamp.from(Instant.now()));
            radiotPodcast = radiotPodcastRepository.save(radiotPodcast);
            timeLabels = saveRadiotPodcastTimeLabels(jsonHelper.getJSONArray(jsonItem, "time_labels"), radiotPodcast);
            radiotPodcast.setTimeLabels(timeLabels);
            result.add(radiotPodcastRepository.save(radiotPodcast));
        });
        return result;
    }

    private Set<RadiotPodcastTimeLabelDBEntity> saveRadiotPodcastTimeLabels(JSONArray json, RadiotPodcastDBEntity podcast) {
        Set<RadiotPodcastTimeLabelDBEntity> result = new HashSet<>();
        json.forEach(item -> {
            JSONObject jsonItem = (JSONObject) item;
            RadiotPodcastTimeLabelDBEntity timeLabel = new RadiotPodcastTimeLabelDBEntity();
            timeLabel.setDuration(jsonHelper.getLong(jsonItem, "duration"));
            timeLabel.setTopic(jsonHelper.getString(jsonItem, "topic"));
            timeLabel.setTime(Timestamp.from(Instant.now()));
            timeLabel.setPodcast(podcast);
            result.add(radiotPodcastTimeLabelRepository.save(timeLabel));
        });
        return result;
    }
}

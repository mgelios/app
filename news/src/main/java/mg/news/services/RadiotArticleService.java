package mg.news.services;

import mg.news.dbentities.RadiotArticleDBEntity;
import mg.news.mapper.RadiotArticleMapper;
import mg.news.models.RadiotArticle;
import mg.news.repositories.RadiotArticleRepository;
import mg.news.utils.RadiotUrlBuilder;
import mg.utils.JSONConsumer;
import mg.utils.JSONHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RadiotArticleService {

    @Autowired
    private JSONConsumer jsonConsumer;
    @Autowired
    private JSONHelper jsonHelper;
    @Autowired
    private RadiotUrlBuilder radiotUrlBuilder;
    @Autowired
    private RadiotArticleRepository radiotArticleRepository;

    public List<RadiotArticle> getRadiotArticlesList() {
        List<RadiotArticleDBEntity> dbArticles = new ArrayList<>();
        Optional<RadiotArticleDBEntity> dbOptionalSingularArticle = radiotArticleRepository.findTopByOrderByIdDesc();
        if (dbOptionalSingularArticle.isPresent()) {
            RadiotArticleDBEntity dbSingularArticle = dbOptionalSingularArticle.get();
            if (dbSingularArticle.getLastUpdated().toLocalDateTime().getDayOfYear() != LocalDateTime.now().getDayOfYear()) {
                radiotArticleRepository.deleteAll();
                dbArticles = fillRadiotArticles();
            } else {
                radiotArticleRepository.findAll().forEach(dbArticles::add);
            }
        } else {
            dbArticles = fillRadiotArticles();
        }
        return dbArticles.stream()
                //.map(radiotArticleEntityToDTO::convert)
                .map(RadiotArticleMapper.INSTANCE::mapToDTO)
                .collect(Collectors.toList());
    }

    private List<RadiotArticleDBEntity> fillRadiotArticles() {
        List<RadiotArticleDBEntity> radiotArticleList = new ArrayList<>();
        JSONArray jsonArticles = jsonConsumer.getJsonArray(radiotUrlBuilder.buildNewsUrl(100));
        jsonArticles.forEach(article -> {
            JSONObject jsonArticle = (JSONObject) article;
            RadiotArticleDBEntity dbArticle = new RadiotArticleDBEntity();
            dbArticle.setAuthor(jsonHelper.getString(jsonArticle, "author"));
            dbArticle.setComments(jsonHelper.getLong(jsonArticle, "comments"));
            dbArticle.setContent(jsonHelper.getString(jsonArticle,"content"));
            dbArticle.setFeed(jsonHelper.getString(jsonArticle,"feed"));
            dbArticle.setLikes(jsonHelper.getLong(jsonArticle,"likes"));
            dbArticle.setLink(jsonHelper.getString(jsonArticle,"link"));
            dbArticle.setMainPicture(jsonHelper.getString(jsonArticle,"pic"));
            dbArticle.setSlug(jsonHelper.getString(jsonArticle,"slug"));
            dbArticle.setSnippet(jsonHelper.getString(jsonArticle,"snippet"));
            dbArticle.setTitle(jsonHelper.getString(jsonArticle,"title"));
            dbArticle.setOriginalTime(Timestamp.from(Instant.now()));
            dbArticle.setRadiotTime(Timestamp.from(Instant.now()));
            dbArticle.setLastUpdated(Timestamp.from(Instant.now()));
            dbArticle = radiotArticleRepository.save(dbArticle);
            radiotArticleList.add(dbArticle);
        });
        return radiotArticleList;
    }
}

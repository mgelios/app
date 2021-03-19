package mg.news.controller;

import lombok.AllArgsConstructor;
import mg.news.service.RadiotArticleService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/v1/radiot/article")
@AllArgsConstructor
public class RadiotNewsRestController {

    private final RadiotArticleService radiotArticleService;

    @GetMapping("/list")
    public Object getRadiotArticles() {
        return radiotArticleService.getRadiotArticlesList();
    }
}

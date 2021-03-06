package mg.blog.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleDto {

    private Long id;
//    private LocalUser author;
    private String title;
    private String shortDescription;
    private String content;
    private LocalDateTime lastUpdated;
//    private Set<Tag> tags;
    private SubcategoryDto subcategoryDto;
//    private List<Comment> comments;
}

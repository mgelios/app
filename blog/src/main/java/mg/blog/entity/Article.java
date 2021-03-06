package mg.blog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="mg_blog_article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
//    @Column(name = "author")
//    private UserDBEntity author;
    @Column(name = "title")
    private String title;
    @Column(name = "short_description")
    private String shortDescription;
    @Column(name = "content")
    private String content;
    @Column(name = "timestamp")
    private Timestamp lastUpdated;
    @ManyToOne
    @JoinColumn(name = "subcategory", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Subcategory subcategory;
//    @ManyToOne
//    private Set<TagDBEntity> tags;
//    private List<CommentDBEntity> comments;
}

package org.example.kakaocommunity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.kakaocommunity.common.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(length = 26)
    private String title;

    private String content;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ColumnDefault("0")
    private int likeCount;
    @ColumnDefault("0")
    private int viewCount;
    @ColumnDefault("0")
    private int commentCount;

    @OneToMany(mappedBy = "post")
    List<Comment> comments = new ArrayList<>();


    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeContent(String content) {
        this.content = content;
    }
    public void changeImage(Image image) {
        this.image = image;
    }

}

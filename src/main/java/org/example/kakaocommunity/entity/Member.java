package org.example.kakaocommunity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.kakaocommunity.common.BaseEntity;


@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer id;

    @Column(length = 10, nullable = false, unique = true)
    private String nickname;
    @Column(length = 320, nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeImage(Image image) {
        this.image = image;
    }
}

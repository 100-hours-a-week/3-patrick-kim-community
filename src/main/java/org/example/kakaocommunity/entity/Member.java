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

    @Column(length = 10)
    private String nickname;
    @Column(length = 320)
    private String email;
    private String password;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

}

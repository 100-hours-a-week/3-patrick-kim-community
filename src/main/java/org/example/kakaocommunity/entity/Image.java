package org.example.kakaocommunity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.kakaocommunity.global.common.BaseEntity;
import org.example.kakaocommunity.entity.enums.ImageStatus;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false, length = 512)
    private String s3Key;

    @Column(length = 512)
    private String url;

    @Column(nullable = false, length = 20)
    private ImageStatus status; //USED, UNUSED, DELETED
}

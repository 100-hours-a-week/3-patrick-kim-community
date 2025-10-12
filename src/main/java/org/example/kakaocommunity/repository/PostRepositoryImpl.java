package org.example.kakaocommunity.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.entity.Post;
import org.example.kakaocommunity.entity.QImage;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.example.kakaocommunity.entity.QPost.post;
import static org.example.kakaocommunity.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findPostsWithCursor(Long cursorId, int limit) {
        QImage memberImage = new QImage("memberImage");
        QImage postImage = new QImage("postImage");

        return queryFactory
                .selectFrom(post)
                .leftJoin(post.member, member).fetchJoin()
                .leftJoin(member.image, memberImage).fetchJoin()
                .leftJoin(post.image, postImage).fetchJoin()
                .where(cursorIdCondition(cursorId))
                .orderBy(post.id.desc())
                .limit(limit)
                .fetch();
    }

    private BooleanExpression cursorIdCondition(Long cursorId) {
        return cursorId != null ? post.id.lt(cursorId) : null;
    }
}
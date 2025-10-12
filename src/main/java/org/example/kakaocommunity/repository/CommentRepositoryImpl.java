package org.example.kakaocommunity.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.entity.Comment;
import org.example.kakaocommunity.entity.QImage;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.example.kakaocommunity.entity.QComment.comment;
import static org.example.kakaocommunity.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findCommentsByPostIdWithCursor(Long postId, Long cursorId, int limit) {
        QImage memberImage = new QImage("memberImage");

        return queryFactory
                .selectFrom(comment)
                .leftJoin(comment.member, member).fetchJoin()
                .leftJoin(member.image, memberImage).fetchJoin()
                .where(
                        comment.post.id.eq(postId),
                        cursorIdCondition(cursorId)
                )
                .orderBy(comment.id.desc())
                .limit(limit)
                .fetch();
    }

    private BooleanExpression cursorIdCondition(Long cursorId) {
        return cursorId != null ? comment.id.lt(cursorId) : null;
    }
}
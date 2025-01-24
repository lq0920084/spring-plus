package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.search.dto.request.SearchServiceRequest;
import org.example.expert.domain.search.dto.response.SearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(jpaQueryFactory.
                selectFrom(todo).
                leftJoin(todo.user, user).
                fetchJoin().
                where(todo.id.eq(todoId)).
                fetchFirst());
    }

    @Override
    public Page<SearchResponse> search(SearchServiceRequest searchServiceRequest, Pageable pageable){
        QManager manager = QManager.manager;
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;
        QComment comment = QComment.comment;

        List<SearchResponse> response = jpaQueryFactory.
                select(Projections.constructor(
                        SearchResponse.class,
                        todo.title,
                        manager.count(),
                        comment.count()))
                .from(todo)
                .join(manager).on(manager.todo.eq(todo))
               .innerJoin(manager.user,user)
               .leftJoin(comment).on(comment.todo.eq(todo))
                .where(
                        searchServiceRequest.getTitle(),
                        searchServiceRequest.getNickname(),
                        todo.createdAt.between(searchServiceRequest.getStartDate()
                                ,searchServiceRequest.getEndDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(todo.id)
                .fetch();

       long total =  Optional.ofNullable(jpaQueryFactory.
               select(todo.id.count())
               .from(todo)
               .innerJoin(manager).on(manager.todo.eq(todo))
               .innerJoin(manager.user,user)
               .leftJoin(comment).on(comment.todo.eq(todo))
               .where(
                       searchServiceRequest.getTitle(),
                       searchServiceRequest.getNickname(),
                       todo.createdAt.between(searchServiceRequest.getStartDate()
                               ,searchServiceRequest.getEndDate())
               )
               .fetchOne()).orElse(0L);

    return new PageImpl<>(response,pageable,total);
    }
}

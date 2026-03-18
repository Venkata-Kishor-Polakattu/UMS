package com.nk.studentservice.repo;

import com.nk.studentservice.searchDocuments.StudentSearchDocument;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class StudentSearchRepository2 {

    private final ElasticsearchOperations elasticsearchOperations;


    public List<StudentSearchDocument> findByName(String name){
        Query query= NativeQuery.builder()
                .withQuery(
                        q->q.
                                match(m->m
                                        .field("name")
                                        .query(name)
                                        .fuzziness("AUTO")
                                )
                ).build();

        SearchHits<StudentSearchDocument> hits = elasticsearchOperations.search(query, StudentSearchDocument.class);

        return hits.stream().map(
                SearchHit::getContent
        ).toList();
    }
}

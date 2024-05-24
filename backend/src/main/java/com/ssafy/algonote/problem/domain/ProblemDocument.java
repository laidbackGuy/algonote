package com.ssafy.algonote.problem.domain;


import com.ssafy.algonote.problem.dto.ConsumerProblemResDto;
import jakarta.persistence.Id;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;


@Getter
@Document(indexName="problem")
@Setter
@Mapping(mappingPath = "elastic/problem/es-mapping.json")
@Setting(settingPath = "elastic/problem/es-setting.json")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProblemDocument {
    @Id
    @Field(type = FieldType.Text,analyzer = "nori_ngram_analyzer", searchAnalyzer = "nori_analyzer")
    private String id;

    @Field(type = FieldType.Text,analyzer = "nori_ngram_analyzer", searchAnalyzer = "nori_analyzer")
    private String title;

    @Field(type = FieldType.Integer)
    private int tier;

    @Field(type = FieldType.Integer)
    private int acceptedUserCount;

    @Field(type = FieldType.Double)
    private double averageTries;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Tag> tags;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Tag{
        @Field(type = FieldType.Text,analyzer = "nori_ngram_analyzer", searchAnalyzer = "nori_analyzer")
        private String tag;
    }

    public static ProblemDocument from(ConsumerProblemResDto consumerProblemResDto){
        List<Tag> tags = consumerProblemResDto.getTags().stream()
            .map(Tag::new)
            .collect(Collectors.toList());
        return ProblemDocument.builder()
            .id(String.valueOf(consumerProblemResDto.getProblemId()))
            .title(consumerProblemResDto.getTitle())
            .tier(consumerProblemResDto.getLevel())
            .acceptedUserCount(consumerProblemResDto.getAcceptedUserCount())
            .tags(tags)
            .build();
    }

    public void addTags(Collection<String> newTags){
        newTags.stream().map(Tag::new).forEach(tags::add);
    }

}

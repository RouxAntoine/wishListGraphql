package com.main.webObject;


import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class QueryDTO {

    private String query;

    private String operationName;

    @Builder.Default
    private Collection<String> variable = new ArrayList<>();
}
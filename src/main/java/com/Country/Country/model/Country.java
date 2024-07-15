package com.Country.Country.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "country")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {

        @Id
        private String id;
        private String name;

        private String fullName;
        private String capitalCity;
        private String currency;
        private String language;
        private String region;
    }

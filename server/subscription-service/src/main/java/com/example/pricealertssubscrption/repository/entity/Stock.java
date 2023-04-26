package com.example.pricealertssubscrption.repository.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record Stock(@JsonProperty("id") int id, @JsonProperty("buy") double buy, @JsonProperty("sell") double sell) implements Serializable {

}

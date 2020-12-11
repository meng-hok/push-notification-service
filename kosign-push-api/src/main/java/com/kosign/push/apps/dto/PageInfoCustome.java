package com.kosign.push.apps.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.github.pagehelper.*;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Page;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
//@NoArgsConstructor
@Setter
@Getter
public class PageInfoCustome {
    @JsonProperty("last_page")
    private int lastPage;

    private boolean last;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("total_elements")
    private Long totalElements;

    @JsonProperty("number_elements")
    private Integer numberOfElements;

    @JsonProperty("first_page")
    private int firstPage;

    private boolean first;

    private int size;

    private int number;

//    private boolean empty;

    @JsonProperty("navigate_page")
    private Object object;

    public PageInfoCustome(PageInfo page) {

        this.lastPage = page.getNavigateLastPage();
        this.last=page.isIsLastPage();
        this.totalPages = page.getPages();
        this.totalElements = page.getTotal();
        this.first = page.isIsFirstPage();
        this.size = page.getSize();
        this.firstPage=page.getNavigateFirstPage();
        this.number = page.getPageNum();
        this.object=page.getNavigatepageNums();
        //        this.numberOfElements = page.getNumber();
        //        this.empty = page.isEmpty();
    }


}

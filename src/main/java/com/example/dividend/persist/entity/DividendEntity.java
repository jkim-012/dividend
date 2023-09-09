package com.example.dividend.persist.entity;

import com.example.dividend.model.Dividend;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name="DIVIDEND")
@Getter
@NoArgsConstructor
@ToString
@Table(
        //중복 데이터 저장을 방지하는 제약 조건
        uniqueConstraints ={
                @UniqueConstraint(
                        columnNames = {"companyId", "date"}
                )
        }
)
public class DividendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long companyId;
    private LocalDateTime date;

    private String dividend;

    public DividendEntity(Long companyId, Dividend dividend){
        this.companyId = companyId;
        this.date = dividend.getLocalDateTime();
        this.dividend = dividend.getDividend();
    }


}

package com.woobros.member.hub.model.tincase.summary.card;

import com.woobros.member.hub.model.letter.summary.card.archive.SummaryCardArchive;
import com.woobros.member.hub.model.tincase.TinCase;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Entity
public class SummaryCard {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    private TinCase tinCase;

    @ManyToOne
    @JoinColumn
    private SummaryCardArchive summaryCardArchive;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;
}

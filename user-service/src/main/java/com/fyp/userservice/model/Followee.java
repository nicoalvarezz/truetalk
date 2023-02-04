package com.fyp.userservice.model;

import com.fyp.userservice.model.composoiteId.FolloweeId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(FolloweeId.class)
@Table(name = "followees", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"follower_id", "followee_id"})
})
public class Followee {

    @Id
    @Column(name = "follower_id")
    private UUID followerId;

    @Id
    @Column(name = "followee_id")
    private UUID followeeId;
}

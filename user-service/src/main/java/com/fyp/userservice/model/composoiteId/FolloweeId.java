package com.fyp.userservice.model.composoiteId;

import java.io.Serializable;
import java.util.UUID;

public class FolloweeId implements Serializable {

    private UUID followeeId;

    private UUID followerId;
}

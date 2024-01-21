package com.driveasy.Core.Cars;

import java.io.Serializable;

public enum CarStatus implements Serializable {
    Available,
    Ordered,
    Renting,
    Repairing,
    Unavailable
}

package com.driveasy.Core.Users;

public enum UserValidationResult {
    Valid,
    InvalidEmail,
    PasswordTooShort,
    PasswordTooLong,
    NameLengthExceeded,
    InfoLengthExceeded,
    EmailAlreadyExists,
    Error
}

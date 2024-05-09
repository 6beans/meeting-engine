package ru.sixbeans.meetingengine.service.io;

import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface FileFetchingService {

    Optional<byte[]> get(@NotNull String fileUrl);
}

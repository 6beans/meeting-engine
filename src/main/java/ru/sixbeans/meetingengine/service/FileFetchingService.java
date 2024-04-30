package ru.sixbeans.meetingengine.service;

import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface FileFetchingService {

    Optional<byte[]> get(@NotNull String fileUrl);
}
